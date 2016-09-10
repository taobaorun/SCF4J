package com.jiaxy.cache.event;

import com.jiaxy.cache.CacheFactory;
import com.jiaxy.cache.Config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: wutao
 * @version: $Id:SimpleCacheEventMulticaster.java 2014/01/14 14:45 $
 *
 */
public class SimpleCacheEventMulticaster extends AbstractCacheEventMulticaster{

    private  static final Executor executor = Executors.newFixedThreadPool(Integer.valueOf(CacheFactory.getConfig().getProperty(Config.KEYMANAGER_MANAGEKEY_THREAD,"10")));

    @Override
    public void multicastEvent(final CacheEvent event) {

        for (final CacheListener listener : getCacheListeners(event)) {
            executor.execute(new Runnable() {
                public void run() {
                    listener.onCacheEvent(event);
                }
            });
        }
    }
}
