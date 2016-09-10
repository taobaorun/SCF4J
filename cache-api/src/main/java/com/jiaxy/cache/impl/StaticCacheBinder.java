package com.jiaxy.cache.impl;

import com.jiaxy.cache.spi.CacheFactoryBinder;
import com.jiaxy.cache.ICacheFactory;

/**
 *
 * dummy StaticCacheBinder
 *
 * @author: wutao
 * @version: $Id:StaticCacheBinder.java 2014/01/10 16:59 $
 *
 */
public class StaticCacheBinder implements CacheFactoryBinder {

    private static final StaticCacheBinder SINGLETON = new StaticCacheBinder();


    public static final StaticCacheBinder getSingleton() {
        return SINGLETON;
    }

    private StaticCacheBinder() {
        throw new UnsupportedOperationException("This code should have never made it into compass-cache-api.jar");
    }

    public ICacheFactory getCacheFactory() {
        throw new UnsupportedOperationException("This code should never make it into compass-cache-api.jar");
    }

    public String getCacheFactoryClassStr() {
        throw new UnsupportedOperationException("This code should never make it into compass-cache-api.jar");
    }

}
