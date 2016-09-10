package com.jiaxy.cache.spring;

import java.lang.annotation.*;

/**
 * @author: wutao
 * @version: $Id:Namespace.java 2014/01/22 14:43 $
 *
 */

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Namespace {

    String value();
}
