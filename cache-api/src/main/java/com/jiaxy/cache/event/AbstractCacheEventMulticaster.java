package com.jiaxy.cache.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: wutao
 * @version: $Id:AbstractCacheEventMulticaster.java 2014/01/14 13:39 $
 *
 */
public abstract class AbstractCacheEventMulticaster implements CacheEventMulticaster {

    private final ListenerRetriever defaultRetriever = new ListenerRetriever();

    private final Map<ListenerCacheKey,ListenerRetriever> retrieverCache = new ConcurrentHashMap<ListenerCacheKey, ListenerRetriever>();

    private ReentrantLock lock = new ReentrantLock();


    @Override
    public void addCacheListener(CacheListener listener) {
        try{
            lock.lock();
            defaultRetriever.cacheListeners.add(listener);
            retrieverCache.clear();
        }finally {
            lock.unlock();
        }

    }

    @Override
    public void removeCacheListener(CacheListener listener) {
        try{
            lock.lock();
            defaultRetriever.cacheListeners.remove(listener);
            retrieverCache.clear();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public void removeAllListeners() {
        try{
            lock.lock();
            defaultRetriever.cacheListeners.clear();
            retrieverCache.clear();
        }finally {
            lock.unlock();
        }
    }


    /**
     * 返回指定事件的相关的所有监听器
     *
     * @param event
     * @return
     */
    protected Collection<CacheListener> getCacheListeners(CacheEvent event){
        Class<? extends CacheEvent> eventType = event.getClass();
        Class sourceType = event.getSource().getClass();
        ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
        ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
        if (retriever != null) {
            return retriever.getCacheListeners();
        }
        else {
            retriever = new ListenerRetriever();
            LinkedList<CacheListener> allListeners = new LinkedList<CacheListener>();
           try {
               lock.lock();
                for (CacheListener listener : this.defaultRetriever.cacheListeners) {
                    if (supportsEvent(listener, eventType, sourceType)) {
                        retriever.cacheListeners.add(listener);
                        allListeners.add(listener);
                    }
                }
                this.retrieverCache.put(cacheKey, retriever);
            }finally {
               lock.unlock();
           }
            return allListeners;
        }
    }

    protected boolean supportsEvent(
            CacheListener listener, Class<? extends CacheEvent> eventType, Class sourceType) {

            return isSupportedEvent(listener,eventType);
    }


    private boolean isSupportedEvent(CacheListener listener, Class<? extends CacheEvent> eventType){
        Type[] types = listener.getClass().getGenericInterfaces();
        if ( types != null && types.length > 0 ){
            Type type = types[0];//  CacheListener<E extends CacheEvent>
            if ( type instanceof ParameterizedType){
                ParameterizedType pt = (ParameterizedType) type;
                Type[] tmpArr = pt.getActualTypeArguments();
                if ( tmpArr != null && tmpArr.length > 0 && tmpArr[0] instanceof Class ){
                    Class clz = (Class) tmpArr[0];
                    if ( eventType.isAssignableFrom( clz )){
                        return true;
                    }else{
                        return false;
                    }
                }
            }
            return false;
        }else {
            return false;
        }
    }


    /**
     * 根据事件类型和sourceType组成缓存ListenerRetriever对象的key
     */
    private static class ListenerCacheKey {


        private final Class eventType;

        private final Class sourceType;

        public ListenerCacheKey(Class eventType, Class sourceType) {
            this.eventType = eventType;
            this.sourceType = sourceType;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            ListenerCacheKey otherKey = (ListenerCacheKey) other;
            return (this.eventType.equals(otherKey.eventType) && this.sourceType.equals(otherKey.sourceType));
        }

        @Override
        public int hashCode() {
            return this.eventType.hashCode() * 29 + this.sourceType.hashCode();
        }
    }


    /**
     * CacheListener 容器
     *
     */
    private class ListenerRetriever {

        public final Set<CacheListener> cacheListeners;


        public ListenerRetriever() {
            this.cacheListeners = new LinkedHashSet<CacheListener>();
        }


        public Collection<CacheListener> getCacheListeners() {
            List<CacheListener> listeners = new LinkedList<CacheListener>();
            for ( CacheListener listener : this.cacheListeners ){
                listeners.add(listener);
            }
            return listeners;
        }
    }





    }
