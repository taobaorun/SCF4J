package com.jiaxy.cache.impl;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.jiaxy.cache.helpers.CacheUtil;
import org.slf4j.Logger;


/**
 * Hello world!
 *
 */
public class App 
{
	
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
    	
    	ScheduledExecutorService exe = Executors.newScheduledThreadPool(10);
    	exe.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try{
					int len = new Random().nextInt(100);
					len = len + 1;
					for( int i = 0 ;i < len; i++ ){
						LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
						params.put("req", JSONObjectBean.build().toJSONString());
						CacheUtil.put("compass_dev", "com.jiaxy.cache", "main", params, JSONObjectBean.buildeResult());
//						System.out.println("-------"+CacheUtil.get("compass_dev", "com.jiaxy.cache", "main", params));
					}
					
				}catch( Exception e){
					logger.error(" put error",e);
				}
				
			}
		}, 0, 1, TimeUnit.SECONDS);
    }
}
