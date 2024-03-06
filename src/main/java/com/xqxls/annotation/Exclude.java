package com.xqxls.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: xqxls
 * @CreateTime: 2024/2/16 22:27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exclude {

}
