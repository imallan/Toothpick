package com.imallan.toothpick;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@SuppressWarnings("Convert2streamapi")
@AutoService(Processor.class)
public class OnClickProcessor extends AbstractProcessor {

    private static final String ACTIVITY_TYPE_PACKAGE = "android.app";
    private static final String ACTIVITY_TYPE_SIMPLE_NAME = "Activity";
    private static final String VIEW_TYPE_PACKAGE = "android.view";
    private static final String VIEW_TYPE_SIMPLE_NAME = "View";

    private Elements mElementUtils;
    private Filer mFiler;
    private Messager mMessager;
    private Types mTypeUtils;
    private boolean mFirstRound = true;
    private HashMap<String, Element> mMapType = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();
        mTypeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(OnClickView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!mFirstRound) {
            return false;
        }
        mFirstRound = false;
        Set<? extends Element> annotatedElements
                = roundEnv.getElementsAnnotatedWith(OnClickView.class);
        if (annotatedElements == null) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "No annotation has been found");
            return false;
        }
        for (Element annotatedElement : annotatedElements) {
            Element element = annotatedElement.getEnclosingElement();
            mMapType.put(element.toString(), element);
        }


        for (String key : mMapType.keySet()) {
            Element parentClass = mMapType.get(key);

            ClassName activity = ClassName.get(ACTIVITY_TYPE_PACKAGE, ACTIVITY_TYPE_SIMPLE_NAME);
            ClassName view = ClassName.get(VIEW_TYPE_PACKAGE, VIEW_TYPE_SIMPLE_NAME);
            ClassName object = ClassName.get("java.lang", "Object");

            MethodSpec.Builder bindActivityBuilder = MethodSpec.methodBuilder("bindActivity")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(activity, "activity", Modifier.FINAL)
                    .returns(void.class);

            MethodSpec.Builder bindViewBuilder = MethodSpec.methodBuilder("bindView")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(object, "object", Modifier.FINAL)
                    .addParameter(view, "view", Modifier.FINAL)
                    .returns(void.class);

            List<? extends Element> enclosedElements = parentClass.getEnclosedElements();
            for (Element enclosedElement : enclosedElements) {
                if (enclosedElement.getKind() == ElementKind.METHOD) {
                    OnClickView annotation = enclosedElement.getAnnotation(OnClickView.class);
                    if (annotation != null) {
                        for (int id : annotation.value()) {
                            bindActivityBuilder.addStatement("activity.findViewById(" + id + ")" +
                                    ".setOnClickListener($L)", getTypeSpec(enclosedElement));
                            bindViewBuilder.addStatement("view.findViewById(" + id + ")" +
                                    ".setOnClickListener($L)", getTypeSpecForView(enclosedElement));
                        }
                    }
                }
            }
            MethodSpec bindActivity = bindActivityBuilder.build();
            MethodSpec bindView = bindViewBuilder.build();

            TypeSpec viewInjector =
                    TypeSpec.classBuilder(parentClass.getSimpleName() + "$$ViewInjector")
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(bindActivity)
                            .addMethod(bindView)
                            .build();


            JavaFile javaFile = JavaFile.builder(parentClass.getEnclosingElement().toString(),
                    viewInjector).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                return false;
            }

        }

        return true;
    }


    private TypeSpec getTypeSpecForView(Element annotatedElement) {
        ClassName view = ClassName.get(VIEW_TYPE_PACKAGE, VIEW_TYPE_SIMPLE_NAME);
        ClassName onClickListener = ClassName.get(
                VIEW_TYPE_PACKAGE, VIEW_TYPE_SIMPLE_NAME,
                "OnClickListener"
        );


        MethodSpec.Builder builder = MethodSpec.methodBuilder("onClick")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(view, "v");

        Element enclosingElement = annotatedElement.getEnclosingElement();
        ClassName parentName = ClassName.get(enclosingElement.getEnclosingElement().toString(),
                enclosingElement.getSimpleName().toString());
        if (((ExecutableElement) annotatedElement).getParameters().size() > 0) {
            builder.addStatement("(($T)object).$L(v)", parentName, annotatedElement.getSimpleName());
        } else {
            builder.addStatement("(($T)object).$L()", parentName, annotatedElement.getSimpleName());
        }
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(onClickListener)
                .addMethod(builder
                        .build()
                )
                .build();
    }

    private TypeSpec getTypeSpec(Element annotatedElement) {
        ClassName view = ClassName.get(VIEW_TYPE_PACKAGE, VIEW_TYPE_SIMPLE_NAME);
        ClassName onClickListener = ClassName.get(
                VIEW_TYPE_PACKAGE, VIEW_TYPE_SIMPLE_NAME,
                "OnClickListener"
        );


        MethodSpec.Builder builder = MethodSpec.methodBuilder("onClick")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .returns(void.class)
                .addParameter(view, "v");

        Element enclosingElement = annotatedElement.getEnclosingElement();
        ClassName parentName = ClassName.get(enclosingElement.getEnclosingElement().toString(),
                enclosingElement.getSimpleName().toString());
        if (((ExecutableElement) annotatedElement).getParameters().size() > 0) {
            builder.addStatement("(($T)activity).$L(v)", parentName, annotatedElement.getSimpleName());
        } else {
            builder.addStatement("(($T)activity).$L()", parentName, annotatedElement.getSimpleName());
        }
        return TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(onClickListener)
                .addMethod(builder
                        .build()
                )
                .build();
    }

}
