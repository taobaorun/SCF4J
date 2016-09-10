package com.jiaxy.cache;

import com.jiaxy.cache.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: wutao
 * @version: $Id:AbstractCacheFactory.java 2014/01/13 11:56 $
 *
 */
public abstract class AbstractCacheFactory implements ICacheFactory {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCacheFactory.class);

    private static final String CACHE_CONFIG_NAME = "cache/cachemeta.xml";

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();

    private Set<String> cacheNames = new LinkedHashSet<String>();

    private static List<CacheMeta> cacheMetas;

    private CacheEventMulticaster cacheEventMulticaster;

    static {
        InputStream in = AbstractCacheFactory.class.getClassLoader().getResourceAsStream(CACHE_CONFIG_NAME);
        if ( in == null ){
            in = Thread.currentThread().getClass().getClassLoader().getResourceAsStream(CACHE_CONFIG_NAME);
        }
        cacheMetas = CacheMeta.parseCacheMetas(in);


    }

    @Override
    public void init(){
        Collection<? extends Cache> caches = loadCaches();
        this.cacheMap.clear();
        if ( caches != null){
            for (Cache cache : caches) {
                this.cacheMap.put(cache.getName(), cache);
                this.cacheNames.add(cache.getName());
            }
        }
        cacheEventMulticaster = new SimpleCacheEventMulticaster();

        addCacheListener(new CachePutListener());
        addCacheListener(new CacheEvictListener());
        addCacheListener(new CacheClearListener());
        addCacheListener(new CacheGetListener());
    }



    public void addCacheListener(CacheListener<? extends CacheEvent> listener) {
        if ( cacheEventMulticaster != null ){
            cacheEventMulticaster.addCacheListener(listener);
        }
    }

    protected List<CacheMeta> getCacheMetas(){
        return Collections.unmodifiableList(cacheMetas);
    }

    public Cache getCache(String name) {
        return this.cacheMap.get(name);
    }

    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheNames);
    }


    @Override
    public void publishCacheEvent(CacheEvent event) {
        cacheEventMulticaster.multicastEvent(event);
    }


    protected <T extends SmartCache>Cache instantiateCustomCache(Object nativeCache,CacheMeta meta){
        try {
            Class<T> customCache = (Class<T>) Class.forName(meta.getClassName());
            T cache = customCache.newInstance();
            cache.setNativeCache(nativeCache);
            cache.fillCacheMetaValue(meta);
            return cache;
        } catch (Exception e) {
            logger.error(" instantiate {} failed", meta.getClassName(), e);
        }
        return null;
    }

    protected abstract Collection<? extends Cache> loadCaches();


}
