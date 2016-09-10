package com.jiaxy.cache.impl;

import com.jiaxy.cache.spi.CacheFactoryBinder;
import com.jiaxy.cache.ICacheFactory;

/**
 * @author: wutao
 * @version: $Id:StaticCacheBinder.java 2014/01/10 16:05 $
 *
 */
public class StaticCacheBinder implements CacheFactoryBinder {

    private static final StaticCacheBinder SINGLETON = new StaticCacheBinder();

    private ICacheFactory cacheFactory;

    private StaticCacheBinder(){
        cacheFactory = new JedisCacheFactory();
        cacheFactory.init();
    }

    public static final StaticCacheBinder getSingleton() {
        return SINGLETON;
    }

    public ICacheFactory getCacheFactory() {
        return cacheFactory;
    }

    public String getCacheFactoryClassStr() {
        return JedisCacheFactory.class.getName();
    }
}
