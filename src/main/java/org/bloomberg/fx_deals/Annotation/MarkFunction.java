package org.bloomberg.fx_deals.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

// this annotation to mark methods using a unique name eg: for aop etc

public @interface MarkFunction {
    String value();
}
