package br.com.jcv.codegen.codegenerator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeGeneratorFieldDescriptor {
    String fieldReferenceInDto() default "";
    String fieldDescription() default  "";
    String regexValidation() default "";
}
