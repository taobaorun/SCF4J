package com.jiaxy.cache.event;

import java.util.EventListener;

/**
 * @author: wutao
 * @version: $Id:CacheListener.java 2014/01/14 11:57 $
 *
 */
public interface CacheListener<E extends CacheEvent> extends EventListener{

    /**
     * Handle an cache event.
     * @param event the event to respond to
     */
    void onCacheEvent(E event);

}
