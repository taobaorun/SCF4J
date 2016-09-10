package com.jiaxy.cache;

import java.util.Collection;

/**
 * @author: wutao
 * @version: $Id:IKeyManager.java 2014/01/10 10:22 $
 *
 */
public interface IKeyManager {


    /**
     * 管理所有cache key的缓存key
     */
    String ALL_KEY="com.jiaxy.cache.allkey#";

    /**
     *
     */
    String VITRUAL_ROOT="VITRUAL_ROOT";


    /**
     * @return 返回cache-api.properties指定keyspace所有 Key节点
     */
    Collection<Key> getManagedIsolatedKey();


    /**
     *
     * @param namespace
     * @return 返回cache-api.properties指定keyspace 中 namespace下的Key节点
     */
    Collection<Key> getManagedIsolatedKey(String namespace);

    /**
     * build a key tree by  tree model( the param model is just tree structure )
     *
     * @param namespace namespace
     * @param model  model
     * @return 返回keystree的根节点
     */
    KeyNode buildKeyTree(String namespace, KeyNode model);


    /**
     *
     * @param namespace
     * @return
     */
    KeyNode buildKeyTree(String namespace);

    /**
     *
     * 将 key节点加入Key 缓存集合当中
     *
     * 此方法需要考虑多线程下并发问题，此问题有具体的实现去解决
     *
     * @param key 需要加入的Key 节点
     */
    void addKey(Key key);


    /**
     *
     * 将 key节点加入Key 缓存集合中删除
     *
     * 此方法需要考虑多线程下并发问题，此问题有具体的实现去解决
     *
     * @param key 需要删除的Key 节点
     */
    void removeKey(Key key);


    /**
     * 获得缓存key 对应的缓存对象
     *
     * @param realKey realKey
     * @return return cached value
     */
    Object getCachedValue( String realKey);


    Object getCachedValue( Key key);


    /**
     *
     * @param realKey realKey
     */
    boolean deleteCachedValue( String realKey );


    boolean deleteCachedValue( Key key );


    /**
     *
     * @param namespace namespace
     */
    boolean deleteCachedValues( String namespace);


    /**
     *
     * @param namespace namespace
     * @param model model
     */
    boolean deleteCachedValues( String namespace,KeyNode model);


    boolean deleteAllCachedValue();


    boolean deleteCachedKeys();


    /**
     *
     * @return return the cached data size(byte)
     */
    long dataSize();

}
