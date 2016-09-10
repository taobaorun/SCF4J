package com.jiaxy.cache.spring;

import org.springframework.beans.factory.Aware;

import java.util.Map;

/**
 * @author wutao
 * @version $Id: KeyGeneratorDelegateAware.java, v1.0 2013/08/05 14:16 $
 *
 */


public interface KeyGeneratorDelegateAware extends Aware {

    void setKeyGeneratorDelegates(Map<String, KeyGeneratorDelegate> keyGenertorDelegates);
}
