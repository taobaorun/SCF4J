package com.jiaxy.cache.impl;


import com.jiaxy.cache.ICacheFactory;
import com.jiaxy.cache.spi.CacheFactoryBinder;

/**
 * @author: wutao
 * @version: $Id:StaticCacheBinder.java 2014/02/14 13:25 $
 *
 */
public class StaticCacheBinder implements CacheFactoryBinder {

    private static final StaticCacheBinder SINGLETON = new StaticCacheBinder();

    private ICacheFactory cacheFactory;

    private StaticCacheBinder(){
        cacheFactory = new MemcachedCacheFactory();
        cacheFactory.init();
    }

    public static final StaticCacheBinder getSingleton() {
        return SINGLETON;
    }

    public ICacheFactory getCacheFactory() {
        return cacheFactory;
    }

    public String getCacheFactoryClassStr() {
        return MemcachedCacheFactory.class.getName();
    }
}
