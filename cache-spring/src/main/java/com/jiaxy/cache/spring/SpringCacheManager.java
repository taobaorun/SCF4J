package com.jiaxy.cache.spring;

import com.jiaxy.cache.CacheFactory;
import com.jiaxy.cache.ICacheFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: wutao
 * @version: $Id:SpringCacheManager.java 2014/01/22 11:46 $
 *
 */
public class SpringCacheManager extends AbstractCacheManager {

    @Override
    protected Collection<? extends Cache> loadCaches() {
        ICacheFactory cacheFactory = CacheFactory.getICacheFactory();
        Collection<String> cacheNames = cacheFactory.getCacheNames();
        List<Cache> caches = new ArrayList<Cache>();
        if ( cacheNames != null){
            for (String cacheName : cacheNames ){
                caches.add(new SpringCache(cacheFactory.getCache(cacheName)));
            }
        }
        return caches;
    }

}
