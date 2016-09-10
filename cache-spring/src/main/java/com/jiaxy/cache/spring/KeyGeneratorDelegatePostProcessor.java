package com.jiaxy.cache.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wutao
 * @version $Id: KeyGeneratorDelegatePostProcessor.java, v1.0 2013/08/05 14:22 $
 *
 */

@Component("cache-keyGeneratorDelegatePostProcessor")
public class KeyGeneratorDelegatePostProcessor implements BeanPostProcessor,PriorityOrdered {

    private Map<String,KeyGeneratorDelegate> keyGenertorDelegateMap = new HashMap<String, KeyGeneratorDelegate>();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ( bean instanceof KeyGeneratorDelegate){
            KGD component = bean.getClass().getAnnotation(KGD.class);
            if ( component != null ){
                keyGenertorDelegateMap.put(component.name(), (KeyGeneratorDelegate) bean);
            }
        }
        if ( bean instanceof KeyGeneratorDelegateAware ){
            ((KeyGeneratorDelegateAware) bean).setKeyGeneratorDelegates(Collections.unmodifiableMap(keyGenertorDelegateMap));
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
