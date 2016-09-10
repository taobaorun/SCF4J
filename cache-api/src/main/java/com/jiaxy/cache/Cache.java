package com.jiaxy.cache;

/**
 * @author: wutao
 * @version: $Id:Cache.java 2014/01/09 18:20 $
 *
 */
public interface Cache {

    // default cache name
    String DEFAULT_CACHE="default_cache";


    /**
     *
     * @return return the wrapped cache entry (ie:XMemcachedClient,RedisClient etc)
     */
    Object getNativeCache();

    void setNativeCache(Object nativeCache);

    String getName();

    Object get(Object key);


    /**
     * the key will not be managed by IKeyManager,in other words ,this method will not be intercepted by the CacheProxy
     *
     * @param key cache key
     * @return return the cached value
     */
    Object getDirect(Object key);

    boolean put(Object key,Object value);

    /**
     * cache the value with the key(as the real cache key),the key will not be managed by IKeyManager .
     * in other words ,this method will not be intercepted by the CacheProxy
     * @param key cache key
     * @param value be cached value
     * @return return true if cached successfully
     */
    boolean putDirect(Object key,Object value);

    boolean delete(Object key);

    /**
     * delete the cached value with the key(as the real cache key),the key will not be managed by IKeyManager .
     * in other words ,this method will not be intercepted by the CacheProxy
     * @param key cache key
     * @return return true if delete successfully
     */
    boolean deleteDirect(Object key);


    void clear();

    /**
     *
     * @return return the cache expire time
     */
    int getExpire();


    public void setExpire(int expire);

    public void setName(String name);

    public void setOpTimeout(long opTimeout);

    long getOpTimeout();





}
