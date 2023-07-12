package io.agistep.annotation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
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

import static java.lang.reflect.Modifier.PUBLIC;

@SupportedAnnotationTypes("io.agistep.annotation.EventSourcingAggregate")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AggregateEventStoreProcessor  extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;
    private Trees trees;
    private TreeMaker treeMaker;
    private Names names;
    private Context context;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        JavacProcessingEnvironment javacProcessingEnvironment = (JavacProcessingEnvironment)processingEnv;
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
            treePathScanner.scan(path, path.getCompilationUnit());
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
                            JCTree.JCMethodDecl init = getInitDefaultMethod();
                            jcClassDecl.defs = jcClassDecl.defs.append(init);
                        }

                        if (!hasReorganizeMethod) {

                        }



                        // TODO Create Xxxx
                    }
                });
            }
            return trees;

        }

    };

    private JCTree.JCMethodDecl getInitDefaultMethod() {
        return treeMaker.MethodDef(
                treeMaker.Modifiers(1),
                names.init,
                treeMaker.TypeIdent(TypeTag.VOID),
//                                    treeMaker.Type(new Type.JCVoidType()),
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

    private JCTree.JCClassDecl creatInitClassDef(String string) {
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(PUBLIC);
        String upperVar = string.substring(0, 1).toUpperCase() + string.substring(1);

        Name className = names.fromString(upperVar + "created");
        List<JCTree.JCTypeParameter> typeParameters = List.nil(); // 제네릭 타입 매개변수가 없는 경우

        JCTree.JCExpression superClass = null; // 슈퍼클래스가 없는 경우
// 클래스 정의를 나타내는 JCClassDecl 생성
        JCTree.JCClassDecl classDecl = treeMaker.ClassDef(
                modifiers, // 클래스 접근 제어자 및 기타 선언
                className, // 클래스 이름
                typeParameters, // 제네릭 타입 매개변수
                superClass, // 슈퍼클래스 또는 인터페이스
                List.nil(), // 구현하는 인터페이스 (빈 목록)
                List.nil() // 클래스 멤버 (빈 목록)
        );
        return classDecl;
    }
}
