package com.jiaxy.cache.event;

import com.jiaxy.cache.IKeyManager;
import com.jiaxy.cache.KeyManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wutao
 * @version: $Id:CachePutListener.java 2014/01/14 15:12 $
 *
 */
public class CachePutListener implements CacheListener<CachePutEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public  void onCacheEvent(CachePutEvent event) {
        logger.debug(" event:[{}] ",event.getClass().getName());
        IKeyManager keyManager = KeyManagerFactory.getIKeyManager();
        if ( event.getKey() != null ){
            logger.debug("add key in event :{}",event.getKey());
            keyManager.addKey(event.getKey());
        }
    }
}
