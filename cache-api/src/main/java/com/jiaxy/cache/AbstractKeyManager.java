package com.jiaxy.cache;

import com.jiaxy.cache.helpers.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: wutao
 * @version: $Id:AbstractKeyManager.java 2014/01/17 15:46 $
 *
 */
public abstract class AbstractKeyManager implements IKeyManager {


    private Logger logger = LoggerFactory.getLogger(getClass());

    private ScheduledExecutorService scheduledExecutorService;

    public AbstractKeyManager() {
        clearExpiredKeyWork();
    }

    public KeyNode buildKeyTree(String namespace, KeyNode model){
        if ( model == null ){
            return null;
        }
        KeyNode root = findRootByNamespace(model,namespace);
        if ( root == null ){
            return model;
        }
        Collection<Key> keys = getManagedIsolatedKey();
        if ( keys != null ){
            for (Key isolatedKey : keys ){
                fillKeyTreeNode(root,isolatedKey);
            }
        }
        return root;
    }


    public String getKeyspace(){
        return IKeyManager.ALL_KEY+CacheFactory.getCacheConfigValue("keyspace","DEFAULT");
    }

    @Override
    public KeyNode buildKeyTree(String namespace) {
        Collection<Key> keys = getManagedIsolatedKey();
        KeyNode root = new KeyNode();
        if (StringUtil.isEmpty(namespace)){
            root.setNamespace(VITRUAL_ROOT);
            for (Key key : keys ){
                if ( key.getKeyNode() != null ){
                    KeyNode keyNode = findKeyNode(root.getChildren(),key.getKeyNode().getNamespace());
                    if ( keyNode != null ){
                        if ( findKey(keyNode.getKeyList(),key.getKey()) == null){
                           keyNode.getKeyList().add(key);
                        }
                    }else {
                        if ( key.getKeyNode().getKeyList() == null ){
                            key.getKeyNode().setKeyList(new ArrayList<Key>());
                        }
                        if ( !key.getKeyNode().getKeyList().contains(key)){
                            key.getKeyNode().getKeyList().add(key);
                        }
                        root.getChildren().add(key.getKeyNode());
                    }
                }
            }
            return root;
        }else {
            root.setNamespace(namespace);
            if ( keys != null ){
                for (Key key : keys ){
                    if ( key.getKeyNode() != null && key.getKeyNode().getNamespace().equals(namespace)){
                        root.getKeyList().add(key);
                    }
                }
            }
            return root;
        }
    }

    private KeyNode findKeyNode(List<KeyNode> keyNodes ,String namespace){
        if ( keyNodes != null ){
            for (KeyNode keyNode : keyNodes ){
                if ( keyNode.getNamespace().equals(namespace )){
                    return keyNode;
                }
            }
        }
        return null;
    }

    private Key findKey(List<Key> keys ,String realKey){
        if ( keys != null ){
            for (Key key : keys ){
                if ( realKey.equals(key.getKey())){
                    return key;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Key> getManagedIsolatedKey(String namespace) {
        Collection<Key> keys = getManagedIsolatedKey();
        List<Key> keyList = new ArrayList<Key>();
        if ( keys != null && keys.size() > 0 ){
            for (Key key : keys ){
                if ( key.getKeyNode() != null && key.getKeyNode().getNamespace().equalsIgnoreCase(namespace)){
                    keyList.add(key);
                }
            }
        }
        return keyList;
    }

    @Override
    public boolean deleteCachedValue(String realKey) {
        Cache cache = CacheFactory.getCache(Cache.DEFAULT_CACHE);
        return cache.deleteDirect(realKey);
    }

    @Override
    public boolean deleteCachedValues(String namespace) {
        Collection<Key> keys = getManagedIsolatedKey();
        boolean rs = false;
        if ( keys != null && keys.size() > 0 ){
            for (Key key : keys ){
                if ( key.getKeyNode() != null && key.getKeyNode().getNamespace().equalsIgnoreCase(namespace)){
                    rs = deleteCachedValue(key);
                    if ( !rs ){
                        logger.debug("{} key is delete failed ",key);
                    }
                }
            }
        }
        return rs;
    }

    @Override
    public boolean deleteCachedValues(String namespace, KeyNode model) {
        KeyNode keyNode = buildKeyTree(namespace, model);
        if ( keyNode != null ){
            return deleteCachedValue(keyNode);
        } else {
            return false;
        }
    }

    private boolean deleteCachedValue(KeyNode keyNode ){
        boolean rs = false;
        if ( keyNode.getKeyList() != null ){
            for (Key key : keyNode.getKeyList() ){
                rs = deleteCachedValue(key);
            }
        }
        if ( keyNode.getChildren() != null && keyNode.getChildren().size() > 0 ){
            for ( KeyNode child : keyNode.getChildren() ){
                rs =deleteCachedValue(child);
                if ( !rs ){
                    logger.debug("{} key is delete failed ",child);
                }
            }
        }
        return rs;
    }

    @Override
    public boolean deleteAllCachedValue() {
        Collection<Key> keys = getManagedIsolatedKey();
        boolean rs = false;
        if ( keys != null && keys.size() > 0 ){
            for (Key key : keys ){
                rs = deleteCachedValue(key);
                if ( !rs ){
                    logger.debug("{} key is delete failed ",key);
                }
            }
        }
        return rs;
    }

    @Override
    public Object getCachedValue(String realKey) {
        Cache cache = CacheFactory.getCache(Cache.DEFAULT_CACHE);
        return cache.getDirect(realKey);
    }

    @Override
    public boolean deleteCachedValue(Key key) {
        Cache cache = CacheFactory.getCache(Cache.DEFAULT_CACHE);
        return cache.delete(key);
    }

    @Override
    public Object getCachedValue(Key key) {
        Cache cache = CacheFactory.getCache(Cache.DEFAULT_CACHE);
        return cache.get(key);
    }


    @Override
    public long dataSize() {
        Collection<Key> keys = getManagedIsolatedKey();
        if ( keys != null && keys.size() > 0 ){
            long size = 0;
            for (Key key : keys ){
                Object cachedValue = getCachedValue(key);
                if ( cachedValue != null && cachedValue instanceof String){
                    size += ((String) cachedValue).toCharArray().length * 2;
                }
            }
            return size;
        }
        return 0;
    }

    private KeyNode findRootByNamespace(KeyNode model,String namespace){
        if ( model.getNamespace().equalsIgnoreCase(namespace)){
            return model;
        }else {
            for (KeyNode child : model.getChildren() ){
                KeyNode root = findRootByNamespace(child,namespace);
                if ( root != null ){
                    return root;
                }
            }
            return null;
        }
    }

    private void fillKeyTreeNode(KeyNode keyNode,Key isolatedKey){
        if ( keyNode != null ){
            if (isolatedKey.getKeyNode() != null && keyNode.getNamespace().equalsIgnoreCase(isolatedKey.getKeyNode().getNamespace())){
                keyNode.getKeyList().add(isolatedKey);
                return;
            }else {
                if ( keyNode.getChildren() != null ){
                    for (KeyNode child : keyNode.getChildren() ){
                        fillKeyTreeNode(child,isolatedKey);
                    }
                }
            }

        }
    }

    protected  void filterExpiredKey( Collection<Key> isolatedKeys){
        Date current = new Date();
        int size = 0;
        if ( isolatedKeys != null ){
            for (Key key : isolatedKeys ){
               if ( current.getTime() > key.getExpireTime() ){
                   logger.debug(" key [{}] is expired will be removed by key manager from keyspace:{}", key,getKeyspace());
                   removeKey(key);
                   size++;
               }
            }
        }
        logger.info(" removed {} expired key",size);
    }


    protected  boolean filterExpiredKey( Key key){
       Date current = new Date();
       if ( key != null && current.getTime()  > key.getExpireTime() ){
           logger.debug(" key [{}] is expired will be removed by key manager", key);
           return true;
       } else {
           return false;
       }
    }

    protected void clearExpiredKeyWork(){
        if ( Boolean.valueOf(CacheFactory.getConfig().getProperty("isCache","true")) ){
            scheduledExecutorService= Executors.newScheduledThreadPool(Integer.valueOf(CacheFactory.getConfig().getProperty(Config.KEYMANAGER_CLEARKEY_THREAD,"1")));
            scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    try {
                        getManagedIsolatedKey();
                    }catch (Exception e){
                        logger.error("clearExpiredKeyWork failed",e);
                    }
                }
            },0,Integer.valueOf(CacheFactory.getConfig().getProperty(Config.KEYMANAGER_CLEARKEY_PERIOD,"2")), TimeUnit.SECONDS);
        }
    }
}
