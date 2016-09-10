package com.jiaxy.cache;

import com.jiaxy.cache.helpers.KeyNodeGenerator;
import com.jiaxy.cache.helpers.StringUtil;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: wutao
 * @version: $Id:Key.java 2014/01/10 10:51 $
 *
 */
public class Key implements Serializable{

    // method argument as key ,argument value as value

    private transient LinkedHashMap<String,?> paramsMap;

    private Map<String, String> paramsStrMap;

    //real key
    private String key;

    private String methodName;

    private KeyNode keyNode;

    private long cachedTime;

    private long expireTime;


    public LinkedHashMap<String, ?> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(LinkedHashMap<String, ?> paramsMap) {
        this.paramsMap = paramsMap;
        if (paramsMap != null) {
            this.paramsStrMap = new LinkedHashMap<String,String>();
            for (Map.Entry<String,?> entry : paramsMap.entrySet()){
                paramsStrMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
    }

    public Map<String, String> getParamsStrMap() {
        return paramsStrMap;
    }

    public void setKey(String key){
        this.key = key;
    }
    public String getKey() {
        if (StringUtil.isEmpty(key)){
            key = KeyNodeGenerator.createKey(this);
        }
        return key;
    }

    public KeyNode getKeyNode() {
        return keyNode;
    }

    public void setKeyNode(KeyNode keyNode) {
        this.keyNode = keyNode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getCachedTime() {
        return cachedTime;
    }

    public void setCachedTime(long cachedTime) {
        this.cachedTime = cachedTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key1 = (Key) o;

        if (key != null ? !key.equals(key1.key) : key1.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ( keyNode != null ){
            sb.append("namespace:").append(keyNode.getNamespace());
        }
        sb.append("methodName:").append(methodName);
        if ( cachedTime != 0 ){
            sb.append("cacheTime:").append(dateFormat(cachedTime));
        }
        if ( expireTime != 0 ){
            sb.append("expireTime:").append(dateFormat(expireTime));
        }
       /* if ( getParamsMap() != null ){
            for (Map.Entry<String,?> entry : getParamsMap().entrySet() ){
                sb.append("argument:").append(entry.getKey());
                if ( entry.getValue().getClass().isArray()){
                    sb.append("value:");
                    sb.append("[");
                    int length = Array.getLength(entry.getValue());
                    for ( int i = 0 ;i < length ;i++ ){
                        sb.append(Array.get(entry.getValue(),i));
                        if ( i != length - 1 ){
                            sb.append(",");
                        }
                    }
                    sb.append("]");
                }else {
                    sb.append("value:").append(KeyNodeGenerator.parseParams(entry.getValue()));
                }
            }
        }*/
        if ( getParamsStrMap() != null ){
            for (Map.Entry<String,String> entry : getParamsStrMap().entrySet() ){
                sb.append("argument:").append(entry.getKey());
                sb.append("value:");
                sb.append("[");
                sb.append(entry.getValue());
                sb.append("]");
            }
        }
        sb.append("realkey:[").append(key).append("]");
        return sb.toString();
    }

    private String dateFormat(long timeInMillis){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(c.getTime());
    }
}
