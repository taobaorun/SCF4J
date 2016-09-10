package com.jiaxy.cache;

import com.jiaxy.cache.impl.StaticKeyManagerBinder;

/**
 * @author: wutao
 * @version: $Id:KeyManagerFactory.java 2014/01/17 15:45 $
 *
 */
public class KeyManagerFactory {


    static IKeyManager keyManager;

    static {
        keyManager = StaticKeyManagerBinder.getSingleton().getKeyManager();
    }

    private KeyManagerFactory() {
    }


    public static IKeyManager getIKeyManager(){
        return keyManager;
    }


}
