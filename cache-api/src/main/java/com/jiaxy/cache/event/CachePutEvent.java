package com.jiaxy.cache.event;

import com.jiaxy.cache.ICacheFactory;
import com.jiaxy.cache.Key;

/**
 * @author: wutao
 * @version: $Id:CachePutEvent.java 2014/01/14 15:10 $
 *
 */
public class CachePutEvent extends CacheEvent{

    private Key key;


    public CachePutEvent(ICacheFactory cacheFactory,Key key) {
        super(cacheFactory);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

}
