package io.agistep.annotation;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("io.agistep.annotation.AggregateEventStore")
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
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "initxxxxx");
        this.processingEnvironment = processingEnv;
        this.trees = Trees.instance(processingEnv);
        this.context = javacProcessingEnvironment.getContext();
        this.treeMaker = TreeMaker.instance(this.context);
        this.names = Names.instance(this.context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "process");
        return false;
    }
}
