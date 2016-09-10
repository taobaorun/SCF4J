package com.jiaxy.cache.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author: wutao
 * @version: $Id:SpringCacheKeyGenerator.java 2014/01/22 13:45 $
 *
 */
@Component("compass-cache-spring-KeyGenerator")
public class SpringCacheKeyGenerator implements KeyGenerator ,KeyGeneratorDelegateAware{

    private Logger logger = LoggerFactory.getLogger(getClass());



    private Map<String,KeyGeneratorDelegate> keyGeneratorDelegateMap;


    @Override
    public Object generate(Object target, Method method, Object... params) {
        CacheKeyRule keyRule;
        if ( target != null && target.getClass().getAnnotation(CacheKeyRule.class) != null){
            keyRule =  target.getClass().getAnnotation(CacheKeyRule.class);
        } else {
            keyRule =  method.getAnnotation(CacheKeyRule.class);
        }
        KeyGeneratorDelegate kg = null;
        if ( keyRule != null && keyGeneratorDelegateMap != null ){
            kg = keyGeneratorDelegateMap.get(keyRule.value());
            if ( kg == null ){
                kg = keyGeneratorDelegateMap.get(KGD.MD5_KGD);
                logger.info("{} 没有对应的KeyGeneratorDelegate实现,采用默认MD5KeyGeneratorDelegate", keyRule.value());
            }
            if ( kg == null ){
                kg = new MD5KeyGeneratorDelegate();
                keyGeneratorDelegateMap.put(KGD.MD5_KGD, kg);
                return kg.generate(target, method, params);
            }
            return kg.generate(target,method,params);
        }else {
            kg = keyGeneratorDelegateMap.get(KGD.DEFAULT_KGD);
            if ( kg == null ){
                keyGeneratorDelegateMap.put(KGD.DEFAULT_KGD, kg);
            }
            return kg.generate(target,method,params);
        }
    }

    @Override
    public void setKeyGeneratorDelegates(Map<String, KeyGeneratorDelegate> keyGeneratorDelegates) {
        keyGeneratorDelegateMap = keyGeneratorDelegates;
    }
}
