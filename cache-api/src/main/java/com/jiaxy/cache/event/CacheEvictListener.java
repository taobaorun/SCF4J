package com.jiaxy.cache.event;

import com.jiaxy.cache.IKeyManager;
import com.jiaxy.cache.KeyManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: wutao
 * @version: $Id:CacheEvictListener.java 2014/01/14 15:14 $
 *
 */
public class CacheEvictListener implements CacheListener<CacheEvictEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public synchronized void onCacheEvent(CacheEvictEvent event) {
        logger.debug(" event:[{}] ",event.getClass().getName());
        IKeyManager keyManager = KeyManagerFactory.getIKeyManager();
        if ( event.getKey() != null ){
            logger.info("remove key in event :{}",event.getKey());
            keyManager.removeKey(event.getKey());
        } else {
            logger.info(" event get key is empty");
        }
    }
}
