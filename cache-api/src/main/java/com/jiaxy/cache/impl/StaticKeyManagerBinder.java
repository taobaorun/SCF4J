package com.jiaxy.cache.impl;

import com.jiaxy.cache.IKeyManager;
import com.jiaxy.cache.spi.KeyManagerBinder;

/**
 * @author: wutao
 * @version: $Id:StaticKeyManagerBinder.java 2014/01/17 16:08 $
 *
 */
public class StaticKeyManagerBinder implements KeyManagerBinder {

    public static final StaticKeyManagerBinder SINGLETON = new StaticKeyManagerBinder();

    public static final StaticKeyManagerBinder getSingleton() {
        return SINGLETON;
    }

    private StaticKeyManagerBinder() {
    }

    @Override
    public IKeyManager getKeyManager() {
        throw new UnsupportedOperationException("This code should have never made it into compass-cache-api.jar");
    }

    @Override
    public String getKeyManagerClassStr() {
        throw new UnsupportedOperationException("This code should have never made it into compass-cache-api.jar");
    }
}
