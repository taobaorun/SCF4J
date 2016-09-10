package com.jiaxy.cache.helpers;

import com.jiaxy.cache.AbstractCacheFactory;
import com.jiaxy.cache.Cache;
import com.jiaxy.cache.Key;
import com.jiaxy.cache.event.CacheClearEvent;
import com.jiaxy.cache.event.CacheEvictEvent;
import com.jiaxy.cache.event.CacheGetEvent;
import com.jiaxy.cache.event.CachePutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;

/**
 * @author: wutao
 * @version: $Id:CacheProxy.java 2014/01/14 15:21 $
 *
 */
public class CacheProxy implements InvocationHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Cache cache;

    private AbstractCacheFactory cacheFactory;

    public CacheProxy(Cache cache) {
        this.cache = cache;
    }

    public CacheProxy(Cache cache, AbstractCacheFactory cacheFactory) {
        this.cache = cache;
        this.cacheFactory = cacheFactory;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object rs = null;
        try{
            if ( "put".equals(method.getName()) && args.length == 2 ){
                Key key = null;
                if ( args[0] instanceof Key){
                    key = (Key)args[0];
                } else if ( args[0] != null ){
                    key = KeyNodeGenerator.generateKey(args[0].toString(),null,null);
                }
                if ( key != null ){
                    rs = method.invoke(cache,new Object[]{key.getKey(),args[1]});
                    Date current = new Date();
                    key.setCachedTime(current.getTime());
                    key.setExpireTime(current.getTime() + cache.getExpire() * 1000);
                    logger.debug("cacheName:{} put cache key:\n{{}} ,expire:{}",cache.getName(),key,cache.getExpire());
                    logger.debug(" cached value is {}",args[1]);
                    cacheFactory.publishCacheEvent(new CachePutEvent(cacheFactory, key));
                }else {
                    logger.warn(" Key(Key) object is null ,cache put operation do noting");
                }
            } else if ("delete".equals(method.getName()) && args.length == 1){
                Key key = null;
                if ( args[0] instanceof Key){
                    key = (Key)args[0];
                } else if ( args[0] != null ){
                    key = KeyNodeGenerator.generateKey(args[0].toString(),null,null);
                }
                if ( key != null ){
                    rs = method.invoke(cache,key.getKey());
                    logger.debug("cacheName:{} delete cache key:\n{{}}",cache.getName(),key);
                    cacheFactory.publishCacheEvent(new CacheEvictEvent(cacheFactory, key));
                } else {
                    logger.warn(" Key(Key) object is null ,cache delete operation do noting");
                }
            } else if ("get".equals(method.getName())  && args.length == 1){
                Key key = null;
                if ( args[0] instanceof Key){
                    key = (Key)args[0];
                } else if ( args[0] != null ){
                    key = KeyNodeGenerator.generateKey(args[0].toString(),null,null);
                }
                if ( key != null ){
                    rs = method.invoke(cache,key.getKey());
                    logger.debug("cacheName:{} get cache key:\n{{}} ,cached result is {}}",cache.getName(),key,rs != null ? "not null":"null");
                    logger.debug("result:\n{}",rs);
                    cacheFactory.publishCacheEvent(new CacheGetEvent(cacheFactory,key));
                } else {
                    logger.warn(" Key(Key) object is null ,cache get operation do noting");
                }
            } else if ("clear".equals(method.getName())){
                rs = method.invoke(cache,args);
                cacheFactory.publishCacheEvent(new CacheClearEvent(cacheFactory));
            }else {
                rs = method.invoke(cache,args);
            }
        }catch (Throwable e){
            throw e;
        }
        return rs;
    }

    public static Cache getProxyCache(Cache sourceCache,AbstractCacheFactory cacheFactory){
        if ( sourceCache == null ){
            return null;
        }
        CacheProxy proxy = new CacheProxy(sourceCache,cacheFactory);
        Cache proxyCache = (Cache) Proxy.newProxyInstance(sourceCache.getClass().getClassLoader(), sourceCache.getClass().getInterfaces(), proxy);
        return proxyCache;
    }
}
