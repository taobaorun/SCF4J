package com.jiaxy.cache.event;

import com.jiaxy.cache.ICacheFactory;
import com.jiaxy.cache.Key;

/**
 * @author: wutao
 * @version: $Id:CacheEvictEvent.java 2014/01/14 15:10 $
 *
 */
public class CacheEvictEvent extends CacheEvent{

    private Key key;

    public CacheEvictEvent(ICacheFactory cacheFactory,Key key) {
        super(cacheFactory);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }
}
