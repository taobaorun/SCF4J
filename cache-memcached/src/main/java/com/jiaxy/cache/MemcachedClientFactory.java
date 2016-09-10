package com.jiaxy.cache;

import com.jiaxy.cache.helpers.StringUtil;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.TextCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: wutao
 * @version: $Id:MemcachedClientFactory.java 2014/02/08 17:07 $
 *
 */
public class MemcachedClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(MemcachedClientFactory.class);

    private static MemcachedClient client;


    static {

        List<InetSocketAddress> addressList = new ArrayList<InetSocketAddress>();
        List<Integer> weightList = new ArrayList<Integer>();
        Map<String,String> xmemcachedProperties = readMemcachedProperties();
        String regxIp = "xmemcached.server[0-9]*.ip";
        String regxPort= "xmemcached.server[0-9]*.port";
        String weightPort= "xmemcached.server[0-9]*.weight";
        for (Iterator<Map.Entry<String,String>> it = xmemcachedProperties.entrySet().iterator();it.hasNext();){
            Map.Entry<String,String> entryIp = it.next();
            String key = String.valueOf(entryIp.getKey());
            String ipValue = String.valueOf(entryIp.getValue());
            Matcher mip = Pattern.compile(regxIp).matcher(key);
            if ( !mip.find() ){
                continue;
            }
            if ( it.hasNext() ){
                Map.Entry<String,String> entryPort = it.next();
                if ( entryPort == null ){
                    throw new IllegalArgumentException("xmemcached.properties 指定ip应指定该ip的端口");
                }
                Matcher mport = Pattern.compile(regxPort).matcher(entryPort.getKey());
                if (  !mport.find() ){
                    throw new IllegalArgumentException("xmemcached.properties 指定ip应指定该ip的端口");
                }
                int portValue = Integer.valueOf(entryPort.getValue());
                addressList.add(new InetSocketAddress(ipValue, portValue));
                Map.Entry<String,String> entryWeight = it.next();
                if ( entryWeight == null ){
                    throw new IllegalArgumentException("xmemcached.properties 指定ip应指定该ip的权重");
                }
                Matcher mweight = Pattern.compile(weightPort).matcher(entryWeight.getKey());
                if (  !mweight.find() ){
                    throw new IllegalArgumentException("xmemcached.properties 指定ip应指定该ip的权重");
                }
                weightList.add(Integer.valueOf(entryWeight.getValue()));
            }else {
                throw new IllegalArgumentException("xmemcached.properties 指定ip应指定该ip的端口");
            }
        }

        int[] weights = new int[weightList.size()];
        for ( int i = 0 ;i < weightList.size() ;i++ ){
            weights[i] = weightList.get(i);
        }
        XMemcachedClientBuilder builder = new XMemcachedClientBuilder(addressList,weights);
        if ( xmemcachedProperties.get("connectionPoolSize") != null ){
            builder.setConnectionPoolSize(Integer.valueOf(xmemcachedProperties.get("connectionPoolSize")));
        } else {
            builder.setConnectionPoolSize(1);
        }
        if ( xmemcachedProperties.get("opTimeout") != null ){
            builder.setOpTimeout(Integer.valueOf(xmemcachedProperties.get("opTimeout")));
        } else {
            builder.setOpTimeout(5000);
        }
        builder.setCommandFactory(new TextCommandFactory());
        builder.setSessionLocator(new KetamaMemcachedSessionLocator(true));
        builder.setTranscoder(new SerializingTranscoder());
        try {
            client = builder.build();
            client.setEnableHeartBeat(false);
            logger.info("关闭XMemcachedClient heart beat ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static MemcachedClient build(){
        return client;
    }

    private static Map<String,String> readMemcachedProperties(){
        Map<String,String> redisProperties = new LinkedHashMap<String, String>();
        InputStream in = MemcachedClientFactory.class.getClassLoader().getResourceAsStream("cache/xmemcached.properties");
        if ( in == null ){
            in = Thread.currentThread().getClass().getClassLoader().getResourceAsStream("cache/xmemcached.properties");
        }
        BufferedReader reader = null;
        try {
            String readedLine = null;
            reader = new BufferedReader(new InputStreamReader(in));
            while ( (readedLine = reader.readLine()) != null ){
                if (StringUtil.isNotEmpty(readedLine)){
                    if ( readedLine.startsWith("#")){
                        continue;
                    }
                    String[] tmp = readedLine.split("=");
                    if ( tmp.length > 1 ){
                        redisProperties.put(tmp[0],tmp[1]);
                    }else {
                        redisProperties.put(tmp[0],"");
                    }
                }
            }
        } catch (IOException e) {
            logger.error("获取cache/xmemcached.properties文件出错",e);
            throw new IllegalArgumentException(e);
        }finally {
            if ( reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return redisProperties;

    }

}
