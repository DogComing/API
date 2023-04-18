package com.platform.annotation;

import java.lang.annotation.*;

/**
 * @program: platform
 * @description: 忽略Token验证
 * @author: Yuan
 * @create: 2020-09-12 15:28
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreAuth {

}
