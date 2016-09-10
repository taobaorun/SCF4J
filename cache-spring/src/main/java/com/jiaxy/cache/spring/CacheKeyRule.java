package com.jiaxy.cache.spring;

import java.lang.annotation.*;

/**
 * 指定keyGenerator 产生 key的规则
 *
 * @author wutao
 * @version $Id: CacheKeyRule.java, v1.0 2013/08/05 13:12 $
 *
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface CacheKeyRule {

    String value();
}
