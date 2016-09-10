package com.jiaxy.cache.impl;

import com.jiaxy.cache.IKeyManager;
import com.jiaxy.cache.spi.KeyManagerBinder;

/**
 * @author: wutao
 * @version: $Id:StaticKeyManagerBinder.java 2014/01/20 10:02 $
 *
 */
public class StaticKeyManagerBinder implements KeyManagerBinder {

    private static final StaticKeyManagerBinder SINGLETON = new StaticKeyManagerBinder();

    private IKeyManager keyManager;

    private StaticKeyManagerBinder(){
        keyManager = new KeyManager();
    }

    public static final StaticKeyManagerBinder getSingleton() {
        return SINGLETON;
    }


    @Override
    public IKeyManager getKeyManager() {
        return keyManager;
    }

    @Override
    public String getKeyManagerClassStr() {
        return KeyManager.class.getName();
    }
}
