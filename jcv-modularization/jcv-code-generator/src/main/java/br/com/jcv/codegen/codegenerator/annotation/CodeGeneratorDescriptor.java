package br.com.jcv.codegen.codegenerator.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeGeneratorDescriptor {
    String basePackage() default "";
    String project() default "";
    String fullDescription() default "";
    String author() default "";
}
