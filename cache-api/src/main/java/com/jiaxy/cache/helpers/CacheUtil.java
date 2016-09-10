package com.jiaxy.cache.helpers;

import com.jiaxy.cache.Cache;
import com.jiaxy.cache.CacheFactory;
import com.jiaxy.cache.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

/**
 * @author: wutao
 * @version: $Id:CacheUtil.java 2014/01/10 11:14 $
 *
 */
public class CacheUtil {

    private static Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    public static boolean put(String key,Object val){
        return put(Cache.DEFAULT_CACHE,key,val);
    }

    /**
     * put with namespace
     * @param namespace namespace
     * @param key key
     * @param val be cached value
     * @return
     */
    public static boolean putWithNamespace(String namespace,String key,Object val){
        return putWithNamespace(namespace, Cache.DEFAULT_CACHE, key, val);
    }

    public static boolean putWithNamespace(String namespace,String cacheName,String key,Object val){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
            params.put("keyStr",key);
            return cache.put(KeyNodeGenerator.generateKey(namespace,null, params), val);
        } else {
            logger.warn(" named {} cache is not exists",cacheName);
            return false;
        }
    }

    /**
     *
     * @param cacheName cacheName is the same as the name in the cachemeta.xml
     * @param key key
     * @param val be cached value
     * @return
     */
    public static boolean put(String cacheName,String key,Object val){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            return cache.put(key,val);
        }else {
            logger.warn(" named {} cache is not exists",cacheName);
            return false;
        }
    }

    /**
     *
     * @param clz the package name will be used as namespace
     * @param methodName method name
     * @param params params
     * @param val be cached value
     * @return return true if put success,if not return false
     */
    public static boolean put(Class clz,String methodName,LinkedHashMap<String,?> params,Object val){
        return put(Cache.DEFAULT_CACHE,clz.getPackage().getName(),methodName,params,val);
    }

    public static boolean put(String cacheName,Class clz,String methodName,LinkedHashMap<String,?> params,Object val){
        return put(cacheName,clz.getPackage().getName(),methodName,params,val);
    }

    public static boolean put(String cacheName,String namespace,String methodName,LinkedHashMap<String,?> params,Object val){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            return cache.put(KeyNodeGenerator.generateKey(namespace,methodName, params), val);
        } else {
            logger.warn(" named {} cache is not exists",cacheName);
            return false;
        }
    }



    public static Object get(String key){
        return get(Cache.DEFAULT_CACHE,key);
    }
    public static Object getWithNamespace(String namespace,String key){
        return getWithNamespace(namespace,Cache.DEFAULT_CACHE,key);
    }

    public static Object get(String cacheName,String key){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            return cache.get(key);
        }else {
            logger.warn(" named {} cache is not exists",cacheName);
            return null;
        }
    }
    public static Object getWithNamespace(String namespace,String cacheName,String key){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
            params.put("keyStr",key);
            return cache.get(KeyNodeGenerator.generateKey(namespace,null, params));
        }else {
            logger.warn(" named {} cache is not exists",cacheName);
            return null;
        }
    }

    public static Object get(String cacheName,Class clz,String methodName,LinkedHashMap<String,?> params){
        return get(cacheName,clz.getPackage().getName(),methodName,params);
    }
    public static Object get(Class clz,String methodName,LinkedHashMap<String,?> params){
        return get(Cache.DEFAULT_CACHE,clz,methodName,params);
    }

    public static Object get(String cacheName,String namespace,String methodName,LinkedHashMap<String,?> params){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            Key key = KeyNodeGenerator.generateKey(namespace,methodName, params);
            return cache.get(key);
        }else {
            logger.warn(" named {} cache is not exists",cacheName);
            return null;
        }

    }

    public static boolean delete(String key){
        return delete(Cache.DEFAULT_CACHE,key);
    }
    public static boolean deleteWithNamespace(String namespace,String key){
        return deleteWithNamespace( namespace,Cache.DEFAULT_CACHE, key);
    }

    public static boolean delete(String cacheName,String key){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            return cache.delete(key);
        }else{
            logger.warn(" named {} cache is not exists",cacheName);
            return false;
        }
    }
    public static boolean deleteWithNamespace(String namespace,String cacheName,String key){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
            params.put("keyStr",key);
            return cache.delete(KeyNodeGenerator.generateKey(namespace, null, params));
        }else{
            logger.warn(" named {} cache is not exists",cacheName);
            return false;
        }
    }

    public static boolean delete(Class clz,String methodName,LinkedHashMap<String,?> params){
        return delete(Cache.DEFAULT_CACHE,clz.getPackage().getName(),methodName,params);
    }

    public static boolean delete(String cacheName,Class clz,String methodName,LinkedHashMap<String,?> params){
        return delete(cacheName,clz.getPackage().getName(),methodName,params);
    }

    public static boolean delete(String cacheName,String namespace,String methodName,LinkedHashMap<String,?> params){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            Key key = KeyNodeGenerator.generateKey(namespace,methodName, params);
            return cache.delete(key);
        }else {
            logger.warn(" named {} cache is not exists",cacheName);
            return false;
        }
    }

    public static void clear(){
        clear(Cache.DEFAULT_CACHE);
    }
    public static void clear(String cacheName){
        Cache cache = CacheFactory.getCache(cacheName);
        if ( cache != null ){
            cache.clear();
        } else {
            logger.warn(" named {} cache is not exists",Cache.DEFAULT_CACHE);
        }

    }






}
