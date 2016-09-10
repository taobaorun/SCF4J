package com.jiaxy.cache.substitute;

import com.jiaxy.cache.AbstractCache;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.CacheFactory;
import com.jiaxy.cache.ICacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wutao
 * @version: $Id:SubstituteCache.java 2014/02/10 10:02 $
 *
 */
public class SubstituteCache extends AbstractCache implements Cache {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Cache substituteCache;

    private MemcachedSubstituteCacheFactory substituteCacheFactory;


    public SubstituteCache(Cache substituteCache, MemcachedSubstituteCacheFactory substituteCacheFactory) {
        this.substituteCache = substituteCache;
        this.substituteCacheFactory = substituteCacheFactory;
    }

    public SubstituteCache() {
    }

    @Override
    public Object getNativeCache() {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("getNativeCache from substitute cache");
            return substituteCache.getNativeCache();
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
            return cache.getNativeCache();
        }
    }

    @Override
    public String getName() {
        return substituteCache.getName();
    }

    @Override
    public Object getDirect(Object key) {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("get cache value from substitute cache");
            return substituteCache.getDirect(key);
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
            return cache.getDirect(key);
        }
    }

    @Override
    public boolean putDirect(Object key, Object value) {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("put cache value from substitute cache");
            return substituteCache.putDirect(key,value);
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
            return cache.putDirect(key, value);
        }
    }

    @Override
    public boolean deleteDirect(Object key) {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("delete cache value from substitute cache");
            return substituteCache.deleteDirect(key);
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
            return cache.deleteDirect(key);
        }
    }

    @Override
    public void clear() {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("clear cache  from substitute cache");
            substituteCache.clear();
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
            cache.clear();
        }
    }

    @Override
    public int getExpire() {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("getExpire  from substitute cache");
            return substituteCache.getExpire();
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
           return cache.getExpire();
        }
    }


    @Override
    public void setNativeCache(Object nativeCache) {
        if ( substituteCacheFactory.isOpen() ){
            logger.info("setNativeCache  from substitute cache");
            substituteCache.setNativeCache(nativeCache);
        } else {
            ICacheFactory cacheFactory = CacheFactory.getICacheFactory(false);
            Cache cache = cacheFactory.getCache(getName());
            cache.setNativeCache(nativeCache);
        }
    }
}
