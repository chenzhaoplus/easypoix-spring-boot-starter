package com.itguoguo.easypoix.starter.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHandleDataType {
    String[] value() default {};

    Class<?> clazz();
}
