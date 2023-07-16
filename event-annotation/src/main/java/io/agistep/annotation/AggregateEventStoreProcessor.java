package io.agistep.annotation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.Set;

@SupportedAnnotationTypes("io.agistep.annotation.EventSourcingAggregate")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AggregateEventStoreProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;
    private Trees trees;
    private TreeMaker treeMaker;
    private Names names;
    private Context context;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        JavacProcessingEnvironment javacProcessingEnvironment = (JavacProcessingEnvironment) processingEnv;
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "init");
        this.processingEnvironment = processingEnv;
        this.trees = Trees.instance(processingEnv);
        this.context = javacProcessingEnvironment.getContext();
        this.treeMaker = TreeMaker.instance(this.context);
        this.names = Names.instance(this.context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "process");

        for (final Element element : roundEnv.getElementsAnnotatedWith(EventSourcingAggregate.class)) {

            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "@AggregateEventStore annotation cant be used on" + element.getSimpleName());
                return true;
            }
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "@AggregateEventStore annotation Processing " + element.getSimpleName());
            final TreePath path = trees.getPath(element);

            CompilationUnitTree compilationUnit = path.getCompilationUnit();
            treePathScanner.scan(path, compilationUnit);
        }

        return true;
    }

    private final TreePathScanner<Object, CompilationUnitTree> treePathScanner = new TreePathScanner<>() {
        /**
         * CompillationUnitTree 는 소스파일에서 패키지 선언에서 부터 abstract syntax tree 를 정의함
         * ClassTree -> 클래스 , 인터페이스, enum 어노테이션을 트리노드로 선언
         * class 정의 위에 어노테이션 작성시 내부적으로 메소드 실행
         * CompilationUnitTree AST(Abstract Syntax Tree 의 최상단)
         */
        @Override
        public Trees visitClass(ClassTree classTree, CompilationUnitTree unitTree) {

            JCTree.JCCompilationUnit compilationUnit = (JCTree.JCCompilationUnit) unitTree;

            if (compilationUnit.sourcefile.getKind() == JavaFileObject.Kind.SOURCE) {

                compilationUnit.accept(new TreeTranslator() {


                    @Override
                    public void visitTopLevel(JCTree.JCCompilationUnit tree) {
                        super.visitTopLevel(tree);
                        JCTree packageExpression = tree.defs.get(0);

                        List<JCTree> from = List.from(tree.defs.subList(1, tree.defs.length()));

                        JCTree.JCImport googleProtobufImport = getGoogleProtobufImport();
                        JCTree.JCImport eventApplierImport = getEventApplierImport();
                        JCTree.JCImport eventReorganizerImport = getEventReorganizerImport();
                        JCTree.JCImport listImport = getListImport();

                        tree.defs = List.of(packageExpression, googleProtobufImport, eventReorganizerImport, eventApplierImport, listImport).appendList(from);

                    }

                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                        super.visitClassDef(jcClassDecl);

                        List<JCTree> members = jcClassDecl.getMembers();

                        List<String> strings = List.of("reorganize", "apply");

                        boolean hasReorganizeMethod = false;
                        boolean hasApplyMethod = false;
                        boolean hasNoArgsConstructor = false;

                        for (JCTree member : members) {
                            if (!(member instanceof JCTree.JCMethodDecl)) {
                                continue;
                            }

                            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) member;
                            if (hasNoArgsPublicConstructorMethod(jcMethodDecl)) {
                                hasNoArgsConstructor = true;
                                // TODO throw Error
                            }

                            if (hasReorganizedMethod(jcMethodDecl)) {
                                hasReorganizeMethod = true;
                            }

                            if (hasApplyMethod(jcMethodDecl)) {
                                hasApplyMethod = true;
                            }
                        }

                        if (!hasNoArgsConstructor) {
                            JCTree.JCMethodDecl init = getInitDefaultConstructorMethod();
                            jcClassDecl.defs = jcClassDecl.defs.append(init);
                        }

                        if (!hasReorganizeMethod) {
                            JCTree.JCMethodDecl reorganizeWithEventsParams = createReorganizeWithEventsParams();
                            jcClassDecl.defs = jcClassDecl.defs.prepend(reorganizeWithEventsParams);


                            JCTree.JCMethodDecl reorganizeWithEventsParams2 = createReorganizeWithEventsParams2();
                            jcClassDecl.defs = jcClassDecl.defs.prepend(reorganizeWithEventsParams2);

                        }

                        if (!hasApplyMethod) {
                            JCTree.JCMethodDecl aa = createApplyMethod();
                            jcClassDecl.defs = jcClassDecl.defs.prepend(aa);
                        }
                    }
                });
            }
            return trees;

        }

        private JCTree.JCImport getJcImport() {
            JCTree.JCImport importStatement = treeMaker.Import(
                    treeMaker.Select(
                            treeMaker.Ident(names.fromString("com")),
                            names.fromString("example")
                    ),
                    false
            );
            return importStatement;
        }

    };

    private JCTree.JCImport getListImport() {
        return treeMaker.Import(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("java.util")),
                        names.fromString("List")
                ),
                false
        );
    }

    private JCTree.JCImport getEventReorganizerImport() {
        return treeMaker.Import(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("io.agistep.event")),
                        names.fromString("EventReorganizer")
                ),
                false
        );
    }

    private JCTree.JCImport getEventApplierImport() {
        return treeMaker.Import(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("io.agistep.event")),
                        names.fromString("EventApplier")
                ),
                false
        );
    }

    private JCTree.JCImport getGoogleProtobufImport() {
        return treeMaker.Import(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("com.google.protobuf")),
                        names.fromString("Message")
                ),
                false
        );
    }

    private JCTree.JCMethodDecl createApplyMethod() {
        // 접근 제어자 및 기타 선언을 나타내는 JCModifiers 생성
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE);

        // 메소드의 이름을 나타내는 Name 생성
        Name methodName = names.fromString("apply");

        // 메소드의 매개변수를 나타내는 JCVariableDecl 리스트 생성
        List<JCTree.JCVariableDecl> parameters = List.of(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        names.fromString("message"),
                        treeMaker.Ident(names.fromString("Message")),
                        null
                )
        );

        // EventApplier.instance().apply(this, message)를 나타내는 JCExpressionStatement 생성
        JCTree.JCExpression instanceIdent = treeMaker.Select(
                treeMaker.Ident(names.fromString("EventApplier")),
                names.fromString("instance")
        );

        JCTree.JCMethodInvocation instanceMethod = treeMaker.Apply(
                List.nil(),
                instanceIdent,
                List.nil()
        );

        JCTree.JCExpression applyMethodIdent = treeMaker.Select(
                instanceMethod,
                names.fromString("apply")
        );
        JCTree.JCExpression thisIdent = treeMaker.Ident(names._this);
        JCTree.JCExpression messageIdent = treeMaker.Ident(names.fromString("message"));
        JCTree.JCExpressionStatement applyStatement = treeMaker.Exec(
                treeMaker.Apply(
                        List.nil(),
                        applyMethodIdent,
                        List.of(thisIdent, messageIdent)
                )
        );

        // 메소드 몸체를 나타내는 JCBlock 생성
        JCTree.JCBlock body = treeMaker.Block(0, List.of(applyStatement));

        // 메소드 정의를 나타내는 JCMethodDecl 생성
        return treeMaker.MethodDef(
                modifiers,
                methodName,
                treeMaker.TypeIdent(TypeTag.VOID),
                List.nil(),
                parameters,
                List.nil(),
                body,
                null
        );
    }

    private JCTree.JCMethodDecl createReorganizeWithEventsParams2() {
        // 접근 제어자 및 기타 선언을 나타내는 JCModifiers 생성
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC);

        // 메소드의 이름을 나타내는 Name 생성
        Name methodName = names.fromString("reorganize");

        // 메소드의 매개변수를 나타내는 JCVariableDecl 리스트 생성
        List<JCTree.JCVariableDecl> parameters = List.of(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        names.fromString("events"),
                        treeMaker.TypeApply(
                                treeMaker.Ident(names.fromString("List")),
                                List.of(treeMaker.Ident(names.fromString("Event")))
                        ),
                        null
                )
        );

// events == null || events.isEmpty()를 나타내는 JCExpression 생성
        JCTree.JCExpression eventsIdent = treeMaker.Ident(names.fromString("events"));
        JCTree.JCExpression condition = treeMaker.Binary(
                JCTree.Tag.OR,
                treeMaker.Binary(
                        JCTree.Tag.EQ,
                        eventsIdent,
                        treeMaker.Literal(TypeTag.BOT, null)
                ),
                treeMaker.Apply(
                        List.nil(),
                        treeMaker.Select(eventsIdent, names.fromString("isEmpty")),
                        List.nil()
                )
        );

        // 조건문을 나타내는 JCIf 생성
        JCTree.JCIf ifStatement = treeMaker.If(
                condition,
                treeMaker.Return(treeMaker.Literal(TypeTag.BOT, null)),
                null
        );

        // events.toArray(new Event[0])를 나타내는 JCMethodInvocation 생성
        JCTree.JCExpression toArrayMethod = treeMaker.Select(
                eventsIdent,
                names.fromString("toArray")
        );
        JCTree.JCNewArray newArray = treeMaker.NewArray(
                treeMaker.Ident(names.fromString("Event")),
                List.of(treeMaker.Literal(TypeTag.INT, 0)),
                null
        );
        JCTree.JCMethodInvocation toArrayInvocation = treeMaker.Apply(
                List.nil(),
                toArrayMethod,
                List.of(newArray)
        );

        // return reorganize(events.toArray(new Event[0]))를 나타내는 JCReturn 생성
        JCTree.JCExpression reorganizeIdent = treeMaker.Ident(names.fromString("reorganize"));
        JCTree.JCReturn returnStatement = treeMaker.Return(
                treeMaker.Apply(
                        List.nil(),
                        reorganizeIdent,
                        List.of(toArrayInvocation)
                )
        );

        // 메소드 정의를 나타내는 JCMethodDecl 생성
        JCTree.JCBlock body = treeMaker.Block(0, List.of(ifStatement, returnStatement));
        return treeMaker.MethodDef(
                modifiers,
                methodName,
                treeMaker.Ident(names.fromString("Todo")),
                List.nil(),
                parameters,
                List.nil(),
                body,
                null
        );
    }

    private JCTree.JCMethodDecl createReorganizeWithEventsParams() {

// 접근 제어자 및 기타 선언을 나타내는 JCModifiers 생성
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC);

// 메소드의 이름을 나타내는 Name 생성
        Name methodName = names.fromString("reorganize");

        // 메소드의 매개변수를 나타내는 JCVariableDecl 리스트 생성
        List<JCTree.JCVariableDecl> parameters = List.of(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        names.fromString("events"),
                        treeMaker.TypeArray(treeMaker.Ident(names.fromString("Event"))),
                        null
                )
        );


        JCTree.JCExpression todoIdent = treeMaker.Ident(names.fromString("Todo"));
        JCTree.JCExpression newClass = treeMaker.NewClass(
                null,
                null,
                todoIdent,
                List.nil(),
                null
        );

        JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.FINAL),
                names.fromString("aggregate"),
                treeMaker.Ident(names.fromString("Todo")),
                newClass
        );


// events == null || events.length == 0를 나타내는 JCExpression 생성
        JCTree.JCExpression eventsIdent = treeMaker.Ident(names.fromString("events"));
        JCTree.JCExpression condition = treeMaker.Binary(
                JCTree.Tag.OR,
                treeMaker.Binary(
                        JCTree.Tag.EQ,
                        eventsIdent,
                        treeMaker.Literal(TypeTag.BOT, null)
                ),
                treeMaker.Binary(
                        JCTree.Tag.EQ,
                        treeMaker.Select(eventsIdent, names.fromString("length")),
                        treeMaker.Literal(TypeTag.INT, 0)
                )
        );

        // 조건문을 나타내는 JCIf 생성
        JCTree.JCIf ifStatement = treeMaker.If(
                condition,
                treeMaker.Return(treeMaker.Literal(TypeTag.BOT, null)),
                null
        );

        // EventReorganizer.reorganize(aggregate, events)를 나타내는 JCExpressionStatement 생성
        JCTree.JCExpression reorganizeMethod = treeMaker.Select(
                treeMaker.Ident(names.fromString("EventReorganizer")),
                names.fromString("reorganize")
        );
        JCTree.JCExpression aggregateIdent = treeMaker.Ident(names.fromString("aggregate"));
        JCTree.JCExpressionStatement reorganizeStatement = treeMaker.Exec(
                treeMaker.Apply(
                        List.nil(),
                        reorganizeMethod,
                        List.of(aggregateIdent, eventsIdent)
                )
        );

        // return aggregate를 나타내는 JCReturn 생성
        JCTree.JCReturn returnStatement = treeMaker.Return(aggregateIdent);

        // 모든 구성 요소를 포함하는 JCBlock 생성
        JCTree.JCBlock body = treeMaker.Block(0, List.of(jcVariableDecl, ifStatement, reorganizeStatement, returnStatement));

        // 메소드 정의를 나타내는 JCMethodDecl 생성
        return treeMaker.MethodDef(
                modifiers,
                methodName,
                treeMaker.Ident(names.fromString("Todo")),
                List.nil(),
                parameters,
                List.nil(),
                body,
                null
        );
    }

    private JCTree.JCMethodDecl getInitDefaultConstructorMethod() {
        return treeMaker.MethodDef(
                treeMaker.Modifiers(1),
                names.init,
                treeMaker.TypeIdent(TypeTag.VOID),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, List.nil()),
                null);
    }

    private static boolean hasApplyMethod(JCTree.JCMethodDecl jcMethodDecl) {
        return "apply".equals(jcMethodDecl.name.toString());
    }

    private static boolean hasReorganizedMethod(JCTree.JCMethodDecl jcMethodDecl) {
        return "reorganize".equals(jcMethodDecl.name.toString());
    }


    //    String string = jcClassDecl.name.toString();
//    String upperVar = string.substring(0, 1).toUpperCase() + string.substring(1);
//    JCTree.JCExpression thisExpression = treeMaker.Ident(names._this);
//
//    // class를 나타내는 JCExpression 생성
//    JCTree.JCExpression classExpression = treeMaker.Select(
//            treeMaker.Ident(names._class),
//            names.fromString(upperVar + "Created")
//    );
//
//
//    /**
//     * 식 생성 함수 - select
//     */
//    JCTree.JCExpression eventApplierInstanceExpression = treeMaker.Select(
//            treeMaker.Ident(names.fromString("io.agistep.event")),
//            names.fromString("EventApplier")
//    );
//
//    JCTree.JCExpression methodExpression = treeMaker.Select(
//            eventApplierInstanceExpression,
//            names.fromString("instance")
//    );
//
//    /**
//     * Method 변환 함수 - apply
//     */
//    JCTree.JCMethodInvocation instanceMethod = treeMaker.Apply(
//            List.nil(),
//            methodExpression,
//            List.nil()
//    );
//
//    JCTree.JCVariableDecl createdClassInstance = createCreatedEventClassInstance();
////                                    JCTree.JCVariableDecl variableDecl = createCreatedClassInstance();
//    jcMethodDecl.body.stats = jcMethodDecl.body.getStatements().append(createdClassInstance);
//    JCTree.JCIdent ident = treeMaker.Ident(names.fromString("obj"));
//
//
//    JCTree.JCMethodInvocation applied = treeMaker.Apply(
//            List.nil(),
//            treeMaker.Select(
//                    instanceMethod,
//                    names.fromString("apply")
//            ),
//            List.of(thisExpression, ident)
//    );
//    JCTree.JCExpressionStatement printStatement = treeMaker.Exec(applied);
//    jcMethodDecl.body.stats = jcMethodDecl.body.getStatements().append(printStatement);
    private static boolean hasNoArgsPublicConstructorMethod(JCTree.JCMethodDecl jcMethodDecl) {
        JCTree.JCModifiers mods = jcMethodDecl.mods;
        List<JCTree.JCVariableDecl> params = jcMethodDecl.params;
        return hasNoParams(params)
                && isConstructor(jcMethodDecl)
                && isPublicModifier(mods);
    }

    private static boolean isConstructor(JCTree.JCMethodDecl jcMethodDecl) {
        return jcMethodDecl.getName().toString().startsWith("<init>");
    }

    private static boolean hasNoParams(List<JCTree.JCVariableDecl> params) {
        return params.isEmpty();
    }

    private static boolean isPublicModifier(JCTree.JCModifiers mods) {
        return mods.flags == 1;
    }


    private TreePathScanner<Object, CompilationUnitTree> getObjectCompilationUnitTreeTreePathScanner() {

        return treePathScanner;
    }

    private JCTree.JCVariableDecl createCreatedEventClassInstance() {

        /**
         * ex) TodoCreated.
         */
        JCTree.JCIdent todoCreatedIdent = treeMaker.Ident(names.fromString("TodoCreated"));

        /**
         * ex) TodoCreated.newBuilder
         */
        JCTree.JCExpression newBuilderIdent = treeMaker.Select(
                todoCreatedIdent,
                names.fromString("newBuilder")
        );

        JCTree.JCMethodInvocation newBuilderMethod = treeMaker.Apply(
                List.nil(),
                newBuilderIdent,
                List.nil()
        );

        JCTree.JCExpression setTestMethod = treeMaker.Select(
                newBuilderMethod,
                names.fromString("setText")
        );
        JCTree.JCLiteral helloWorld = treeMaker.Literal("Some Text");
        JCTree.JCMethodInvocation newBuilderInvocation = treeMaker.Apply(
                List.nil(),
                setTestMethod,
                List.of(helloWorld)
        );


        JCTree.JCExpression buildMethodIdent = treeMaker.Select(
                newBuilderInvocation,
                names.fromString("build")
        );
        JCTree.JCMethodInvocation buildInvocation = treeMaker.Apply(
                List.nil(),
                buildMethodIdent,
                List.nil()
        );

// 할당식을 나타내는 JCExpressionStatement 생성
//        JCTree.JCExpressionStatement assignmentStatement = treeMaker.Exec(
//                treeMaker.Assign(
//                        treeMaker.Ident(names.fromString("created")),
//                        buildInvocation
//                )
//        );
//        return assignmentStatement;

        return treeMaker.VarDef(
                treeMaker.Modifiers(Flags.FINAL),
                names.fromString("obj"),
                treeMaker.Ident(names.fromString("TodoCreated")),
                buildInvocation
        );
    }

//    private JCTree.JCClassDecl creatInitClassDef(String string) {
//        JCTree.JCModifiers modifiers = treeMaker.Modifiers(PUBLIC);
//        String upperVar = string.substring(0, 1).toUpperCase() + string.substring(1);
//
//        Name className = names.fromString(upperVar + "created");
//        List<JCTree.JCTypeParameter> typeParameters = List.nil(); // 제네릭 타입 매개변수가 없는 경우
//
//        JCTree.JCExpression superClass = null; // 슈퍼클래스가 없는 경우
//// 클래스 정의를 나타내는 JCClassDecl 생성
//        JCTree.JCClassDecl classDecl = treeMaker.ClassDef(
//                modifiers, // 클래스 접근 제어자 및 기타 선언
//                className, // 클래스 이름
//                typeParameters, // 제네릭 타입 매개변수
//                superClass, // 슈퍼클래스 또는 인터페이스
//                List.nil(), // 구현하는 인터페이스 (빈 목록)
//                List.nil() // 클래스 멤버 (빈 목록)
//        );
//        return classDecl;
//    }
}
