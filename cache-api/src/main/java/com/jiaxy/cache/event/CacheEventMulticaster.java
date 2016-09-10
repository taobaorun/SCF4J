package com.jiaxy.cache.event;

/**
 * @author: wutao
 * @version: $Id:CacheEventMulticaster.java 2014/01/14 13:21 $
 *
 */
public interface CacheEventMulticaster {


    void addCacheListener(CacheListener listener);

    void removeCacheListener(CacheListener listener);

    void removeAllListeners();

    /**
     * Multicast the given application event to appropriate listeners.
     * @param event the event to multicast
     */
    void multicastEvent(CacheEvent event);


}
