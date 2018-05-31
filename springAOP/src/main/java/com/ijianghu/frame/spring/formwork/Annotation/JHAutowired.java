package com.ijianghu.frame.spring.formwork.Annotation;

import java.lang.annotation.*;

/**
 * @author kai on
 * @date 2018/5/24 20:55
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JHAutowired {

    String value() default  "";
}
