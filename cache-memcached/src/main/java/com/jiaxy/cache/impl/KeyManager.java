package com.jiaxy.cache.impl;

import com.jiaxy.cache.AbstractKeyManager;
import com.jiaxy.cache.Key;
import com.jiaxy.cache.MemcachedClientFactory;
import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * @author: wutao
 * @version: $Id:KeyManager.java 2014/02/14 13:28 $
 *
 */
public class KeyManager extends AbstractKeyManager {

    private MemcachedClient client = MemcachedClientFactory.build();

    private static final int EXP_TIME = 24 * 60 * 60;

    @Override
    public Collection<Key> getManagedIsolatedKey() {
        Set<Key> keySet = null;
        try {
            keySet = client.get(getKeyspace());
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return keySet;
    }

    @Override
    public void addKey(final Key key) {
        try {
            GetsResponse<Integer> result = client.gets(getKeyspace());
            if ( result != null ){
                client.cas(getKeyspace(),EXP_TIME,new CASOperation<Set<Key>>() {
                    @Override
                    public int getMaxTries() {
                        return 3;
                    }

                    @Override
                    public Set<Key> getNewValue(long currentCAS, Set<Key> currentValue) {
                        if ( currentValue == null ){
                            currentValue = new HashSet<Key>();
                        }
                        currentValue.add(key);
                        return currentValue;
                    }
                });
            }else {
                Set<Key> keySet = new HashSet<Key>();
                keySet.add(key);
                client.set(getKeyspace(), EXP_TIME, keySet);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeKey(final Key key) {
        try {
            client.cas(getKeyspace(),EXP_TIME,new CASOperation<Set<Key>>() {
                @Override
                public int getMaxTries() {
                    return 3;
                }

                @Override
                public Set<Key> getNewValue(long currentCAS, Set<Key> currentValue) {
                    currentValue.remove(key);
                    return currentValue;
                }
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteCachedKeys() {
        try {
            return client.cas(getKeyspace(),EXP_TIME,new CASOperation<Set<Key>>() {
                @Override
                public int getMaxTries() {
                    return 3;
                }

                @Override
                public Set<Key> getNewValue(long currentCAS, Set<Key> currentValue) {
                    currentValue.clear();
                    return currentValue;
                }
            });
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MemcachedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
