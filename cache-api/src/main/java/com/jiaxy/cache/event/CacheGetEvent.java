package com.jiaxy.cache.event;

import com.jiaxy.cache.ICacheFactory;
import com.jiaxy.cache.Key;

/**
 * @author: wutao
 * @version: $Id:CacheGetEvent.java 2014/01/14 15:10 $
 *
 */
public class CacheGetEvent extends CacheEvent{

    private Key key;

    private String namespace;

    public CacheGetEvent(ICacheFactory cacheFactory) {
        super(cacheFactory);
    }

    public CacheGetEvent(ICacheFactory cacheFactory, Key key) {
        super(cacheFactory);
        this.key = key;
    }

    public CacheGetEvent(ICacheFactory cacheFactory, String namespace) {
        super(cacheFactory);
        this.namespace = namespace;
    }

    public Key getKey() {
        return key;
    }

    public String getNamespace() {
        return namespace;
    }
}
