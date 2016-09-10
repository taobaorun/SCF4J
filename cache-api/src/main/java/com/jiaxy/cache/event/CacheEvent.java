package com.jiaxy.cache.event;

import com.jiaxy.cache.ICacheFactory;

import java.util.EventObject;

/**
 * @author: wutao
 * @version: $Id:CacheEvent.java 2014/01/14 11:58 $
 *
 */
public class CacheEvent extends EventObject{


    public CacheEvent(ICacheFactory cacheFactory) {
        super(cacheFactory);
    }
}
