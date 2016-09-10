package com.jiaxy.cache.spring;

import java.lang.reflect.Method;

/**
 * Cache key 具体产生规则
 *
 * @author wutao
 * @version $Id: KeyGeneratorDelegate.java, v1.0 2013/08/05 13:33 $
 *
 */


public interface KeyGeneratorDelegate {

    Object generate(Object target, Method method, Object... params);
}
