package com.ijianghu.frame.spring.formwork.Annotation;

import java.lang.annotation.*;

/**
 * @author kai on
 * @date 2018/5/24 20:55
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JHRequestParam {

    String value() default  "";
}
