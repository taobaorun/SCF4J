package com.jiaxy.cache.spi;

import com.jiaxy.cache.IKeyManager;

/**
 * @author: wutao
 * @version: $Id:KeyManagerBinder.java 2014/01/17 15:59 $
 *
 */
public interface KeyManagerBinder {

    IKeyManager getKeyManager();

    String getKeyManagerClassStr();
}
