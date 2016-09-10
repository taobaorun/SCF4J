package com.jiaxy.cache.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jiaxy.cache.Key;
import com.jiaxy.cache.KeyNode;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: wutao
 * @version: $Id:KeyNodeGenerator.java 2014/01/21 10:53 $
 *
 */
public class KeyNodeGenerator {

//    private static final ConcurrentHashMap<String,WeakReference<Key>> cacheKeyNode = new ConcurrentHashMap<String, WeakReference<Key>>();

    private static Cache<String,Key> cacheKey = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).maximumSize(1000).build();


/*    *//**
     *
     * @param namespace namespace
     * @param methodName method name
     * @param params the method params map .the argument name as key ,the value as map value
     * @return KeyNode
     *//*
    public static Key generateKey(String namespace,String methodName,LinkedHashMap<String,?> params){
        String keyStr = createKey(namespace,methodName,params);
        WeakReference<Key> item = cacheKeyNode.get(keyStr);
        WeakReference<Key> oldItem ;
        Key key;
        if ( item == null ){
            item = new WeakReference<Key>(null);
            oldItem = cacheKeyNode.putIfAbsent(keyStr,item);
            if ( oldItem != null ){
                item = oldItem;
            }
        }
        key = item.get();
        if ( key == null ){
            synchronized (item){
                key = item.get();
                if ( key == null ){
                    key = generate(namespace,methodName,params);
                    key.setKey(keyStr);
                    item = new WeakReference<Key>(key);
                    cacheKeyNode.put(keyStr,item);
                }
            }
        }
        return key;
    }*/
    /**
     *
     * @param namespace namespace
     * @param methodName method name
     * @param params the method params map .the argument name as key ,the value as map value
     * @return KeyNode
     */
    public static Key generateKey(final String namespace,final String methodName,final LinkedHashMap<String,?> params){
        String keyStr = createKey(namespace,methodName,params);
        try {
            return cacheKey.get(keyStr,new Callable<Key>() {
                @Override
                public Key call() throws Exception {
                    return generate(namespace,methodName,params);
                }
            });
        } catch (ExecutionException e) {
            return generate(namespace,methodName,params);
        }
    }

    public static String createKey(String namespace,String methodName,LinkedHashMap<String,?> params){
        StringBuffer kb = new StringBuffer();
        kb.append(namespace);
        kb.append(methodName);
        if ( params != null ){
            for (Map.Entry<String,?> entry : params.entrySet()){
                kb.append(entry.getKey()).append(parseParams(entry.getValue()));
            }
        }
        return StringUtil.md5(kb.toString());

    }


    /**
     * handle some special param type .ie array , set and map
     *
     * @param value
     * @return
     */
    public static String parseParams(Object value){
        if ( value == null ){
            return "";
        }
        StringBuilder b = new StringBuilder();
        if ( value.getClass().isArray() ){
            if ( Array.getLength(value ) > 0 ){
                for ( int i = 0 ;i < Array.getLength(value);i++){
                    b.append( Array.get(value,i));
                    if ( i != Array.getLength(value) - 1 ){
                        b.append(",");
                    }
                }
            }
        } else if ( value instanceof Set){
            List<Object> keyList = new ArrayList<Object>();
            keyList.addAll((Set<Object>) value);
            Collections.sort(keyList,new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
           b.append(keyList);
        } else if ( value instanceof Map){
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            List<Object> keyList = new ArrayList<Object>();
            Map map = (Map) value;
            keyList.addAll(map.keySet());
            Collections.sort(keyList,new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            for (int i = 0 ;i < keyList.size() ;i++ ){
                linkedHashMap.put(keyList.get(i),map.get(keyList.get(i)));
            }
            b.append(linkedHashMap);
        }else {
            b.append(String.valueOf(value));
        }
        return b.toString();
    }


    public static String createKey(Key key){
        if ( key == null ){
            return "";
        }
        String namespace = "";
        if ( key.getKeyNode() != null ){
            namespace = key.getKeyNode().getNamespace();
        }
        return createKey(namespace, key.getMethodName(), key.getParamsMap());

    }

    private static Key generate(String namespace,String methodName,LinkedHashMap<String,?> params){
        Key key = new Key();
        key.setParamsMap(params);
        key.setMethodName(methodName);
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace(namespace);
        key.setKeyNode(keyNode);
        return key;
    }


    static class Item<T>{
        volatile T value;

        T getValue() {
            return value;
        }

        void setValue(T value) {
            this.value = value;
        }
    }

}
