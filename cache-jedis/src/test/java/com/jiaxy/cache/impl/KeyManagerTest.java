package com.jiaxy.cache.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiaxy.cache.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * @author: wutao
 * @version: $Id:KeyManagerTest.java 2014/01/20 11:53 $
 *
 */
public class KeyManagerTest {

    private KeyNode keyNodeModel ;

    @Before
    public void setUp() throws Exception {
        keyNodeModel = new KeyNode();
        keyNodeModel.setNamespace("ROOT");
        KeyNode a = new KeyNode();
        a.setNamespace("A");
        KeyNode a1 = new KeyNode();
        a1.setNamespace("A-1");
        KeyNode a2 = new KeyNode();
        a2.setNamespace("A-2");
        KeyNode a11 = new KeyNode();
        a11.setNamespace("A-1-1");
        KeyNode a22 = new KeyNode();
        a22.setNamespace("A-2-2");
        a.setChildren(Arrays.asList(a1,a2));
        a1.setChildren(Arrays.asList(a11));
        a2.setChildren(Arrays.asList(a22));

        KeyNode b = new KeyNode();
        b.setNamespace("B");
        KeyNode b1 = new KeyNode();
        b1.setNamespace("B-1");
        KeyNode b2 = new KeyNode();
        b2.setNamespace("B-2");
        KeyNode b11 = new KeyNode();
        b11.setNamespace("B-1-1");
        KeyNode b12 = new KeyNode();
        b12.setNamespace("B-1-2");
        KeyNode b21 = new KeyNode();
        b21.setNamespace("B-2-1");
        KeyNode b22 = new KeyNode();
        b22.setNamespace("B-2-2");
        b.setChildren(Arrays.asList(b1, b2));
        b1.setChildren(Arrays.asList(b11, b12));
        b2.setChildren(Arrays.asList(b21, b22));
        keyNodeModel.setChildren(Arrays.asList(a,b));
    }


    @Test
    public void testDeleteAllCachedKeys() throws Exception {
        KeyManagerFactory.getIKeyManager().deleteCachedKeys();
        Thread.sleep(1000);
        Assert.assertEquals(0,KeyManagerFactory.getIKeyManager().getManagedIsolatedKey().size());

    }

    @Test
    public void testRemoveAllKey() throws Exception {
        KeyManagerFactory.getIKeyManager().deleteCachedKeys();

    }

    @Test
    public void testGetAllIsolatedKey() throws Exception {
        Collection<Key> tmp =  KeyManagerFactory.getIKeyManager().getManagedIsolatedKey();
        System.out.println(tmp.size());
        for (Key key : tmp){
            if ( key != null ){
                System.out.println(key.getKey());
                if ( key.getKeyNode() != null ){
                    System.out.println(key.getKeyNode().getNamespace());
                }
                System.out.println(key.getParamsMap());
                System.out.println("=====================");
            }
        }


    }

    @Test
    public void testAddKeyNode() throws Exception {

        Cache cache  = CacheFactory.getCache("compass_test");
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","1");
        params.put("arg2","2");

        key.setParamsMap(params);
        keyNode.getKeyList().add(key);
        key.setKeyNode(keyNode);
        cache.put(key, "test");
        Key key2 = new Key();
        params = new LinkedHashMap<String, String>();
        params.put("arg1","3");
        params.put("arg2","4");
        key2.setParamsMap(params);
        key2.setKeyNode(keyNode);
        cache.put(key2,"test2");
        Thread.sleep(1000);
        Collection<Key> tmp =  KeyManagerFactory.getIKeyManager().getManagedIsolatedKey();
        for (Key k : tmp){
                System.out.println(k.getKeyNode().getNamespace());
                System.out.println(k.getParamsMap());
        }

       KeyNode filledKeyTree =  KeyManagerFactory.getIKeyManager().buildKeyTree("A", keyNodeModel);
//        KeyNode.print(filledKeyTree);
       filledKeyTree.printTree();



    }

    @Test
    public void testGetCachedValue() throws Exception {
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","1");
        params.put("arg2","2");
        key.setParamsMap(params);
        key.setKeyNode(keyNode);
        Object cachedValue = KeyManagerFactory.getIKeyManager().getCachedValue(key);
        Assert.assertEquals("test",cachedValue);

    }



    @Test
    public void testRemoveKeyNode() throws Exception {

        Cache cache  = CacheFactory.getCache("compass_test");
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","1");
        params.put("arg2","2");

        key.setParamsMap(params);
        keyNode.getKeyList().add(key);
        key.setKeyNode(keyNode);
        cache.delete(key);
        Thread.sleep(1000);
    }

    @Test
    public void testKeyTree() throws Exception {
        KeyNode filledKeyTree =  KeyManagerFactory.getIKeyManager().buildKeyTree("A", keyNodeModel);
        filledKeyTree.printTree();
    }





    @Test
    public void testDeleteCachedValue() throws Exception {
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","1");
        params.put("arg2","2");
        key.setParamsMap(params);
        key.setKeyNode(keyNode);
        keyNode.getKeyList().add(key);
        KeyManagerFactory.getIKeyManager().deleteCachedValue(key);
        Thread.sleep(1000);
        Assert.assertNull(KeyManagerFactory.getIKeyManager().getCachedValue(key));

    }

    @Test
    public void testDeleteAllCachedValue() throws Exception {
//        KeyManagerFactory.getIKeyManager().deleteAllCachedValue();
        Key key = KeyManagerFactory.getIKeyManager().getManagedIsolatedKey().iterator().next();
        System.out.println(KeyManagerFactory.getIKeyManager().getManagedIsolatedKey().size());
        KeyManagerFactory.getIKeyManager().removeKey(key);
        Thread.sleep(1000);
        System.out.println(KeyManagerFactory.getIKeyManager().getManagedIsolatedKey().size());
        //Assert.assertEquals(0,KeyManagerFactory.getIKeyManager().getManagedIsolatedKey().size());

    }

    @Test
    public void testDeleteCachedValueByNamespace() throws Exception {
        KeyManagerFactory.getIKeyManager().deleteCachedValues("A-1-1");
        Thread.sleep(100);
        Assert.assertEquals(0,KeyManagerFactory.getIKeyManager().getManagedIsolatedKey("A-1-1").size());
    }

    @Test
    public void testBuildKeyTree() throws Exception {
        KeyNode keyNode = KeyManagerFactory.getIKeyManager().buildKeyTree("A-1-1");
        keyNode.printTree();

        keyNode = KeyManagerFactory.getIKeyManager().buildKeyTree(null);
        keyNode.printTree();

    }

    @Test
    public void testGetDeleteCachedValueDirect() throws Exception {
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","Direct");
        params.put("arg2","Direct");
        key.setParamsMap(params);
        key.setKeyNode(keyNode);
        Cache cache  = CacheFactory.getCache("compass_test");
        cache.put(key,"testDirectValue");
        Object cachedValue = KeyManagerFactory.getIKeyManager().getCachedValue(key.getKey());
        Assert.assertEquals("testDirectValue",cachedValue);
        KeyManagerFactory.getIKeyManager().deleteCachedValue(key.getKey());
        Assert.assertNull( KeyManagerFactory.getIKeyManager().getCachedValue(key.getKey()));
    }

    @Test
    public void testDataSize() throws Exception {
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","Direct");
        params.put("arg2","Direct");
        key.setParamsMap(params);
        key.setKeyNode(keyNode);
        Cache cache  = CacheFactory.getCache("compass_test");
        cache.put(key,"testDirectValue");
        Thread.sleep(1000);
        System.out.println(KeyManagerFactory.getIKeyManager().dataSize());

    }

    @Test
    public void testMap() throws Exception {
        KeyNode keyNode = new KeyNode();
        keyNode.setNamespace("A-1-1-jsonobject");
        Key key = new Key();
        LinkedHashMap<String,String> params = new LinkedHashMap<String, String>();
        params.put("arg1","Direct");
        params.put("arg2","Direct");
        key.setParamsMap(params);
        key.setKeyNode(keyNode);
        Cache cache  = CacheFactory.getCache("compass_test");
        JSONObject data = new JSONObject();
        data.put("data",new JSONArray());
        cache.put(key,data);
        System.out.println(cache.get(key));
        Thread.sleep(1000);

    }
}
