package com.jiaxy.cache.spring;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * @author: wutao
 * @version: $Id:SpringCache.java 2014/01/22 11:50 $
 *
 */
public class SpringCache implements Cache {

    private com.jiaxy.cache.Cache cache;

    public SpringCache(com.jiaxy.cache.Cache cache) {
        this.cache = cache;
    }

    @Override
    public String getName() {
        return cache.getName();
    }

    @Override
    public com.jiaxy.cache.Cache getNativeCache() {
        return cache;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = cache.get(key);
        if ( value != null ){
            return new SimpleValueWrapper(value);
        }else {
            return null;
        }
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key,value);
    }

    @Override
    public void evict(Object key) {
        cache.delete(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
