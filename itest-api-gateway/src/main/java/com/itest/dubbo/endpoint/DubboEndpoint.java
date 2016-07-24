/**
 * Copyright (c) 2009-2016, LarryKoo 老古 (gumutianqi@gmail.com)
 * Created on 16/7/24
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itest.dubbo.endpoint;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.config.spring.ReferenceBean;

public class DubboEndpoint extends AbstractEndpoint<List<Object>> implements ApplicationContextAware {

    private ApplicationContext context;

    public DubboEndpoint() {
        super("dubbo");
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Override
    public List<Object> invoke() {
        List<ProviderBean> publishedInterfaceList = new ArrayList<>();
        List<ConsumerBean> subscribedInterfaceList = new ArrayList<>();
        AnnotationBean annotationBean = context.getBean(AnnotationBean.class);
        Field serviceConfigsField = ReflectionUtils.findField(AnnotationBean.class, "serviceConfigs");
        ReflectionUtils.makeAccessible(serviceConfigsField);
        Object services = ReflectionUtils.getField(serviceConfigsField, annotationBean);
        if (services instanceof Set) {
            final Set<ServiceConfig<?>> serviceConfigs = (Set<ServiceConfig<?>>) services;
            for (ServiceConfig config : serviceConfigs) {
                ProviderBean providerBean = new ProviderBean();
                providerBean.setTarget(config.getStub());
                providerBean.setServiceInterface(config.getInterface());
                providerBean.setServiceVersion(config.getVersion());
                providerBean.setClientTimeout(config.getTimeout());
                providerBean.setMethodNames(config.getMethods());
                publishedInterfaceList.add(providerBean);
            }
        }
        Field referenceConfigsField = ReflectionUtils.findField(AnnotationBean.class, "referenceConfigs");
        ReflectionUtils.makeAccessible(referenceConfigsField);
        Object references = ReflectionUtils.getField(referenceConfigsField, annotationBean);
        if (references instanceof ConcurrentMap) {
            final ConcurrentMap<String, ReferenceBean<?>> referenceConfigs = (ConcurrentMap<String, ReferenceBean<?>>) references;
            for (Entry<String, ReferenceBean<?>> reference : referenceConfigs.entrySet()) {
                ReferenceBean referenceBean = reference.getValue();
                ConsumerBean consumerBean = new ConsumerBean();
                consumerBean.setGroup(consumerBean.getGroup());
                consumerBean.setInterfaceName(consumerBean.getInterfaceName());
                consumerBean.setMethodNames(consumerBean.getMethodNames());
                consumerBean.setVersion(consumerBean.getVersion());
                subscribedInterfaceList.add(consumerBean);
            }
        }
        List<Object> all = new ArrayList<Object>();
        Map<String, List> provider = new HashMap<String, List>();
        provider.put("provider", publishedInterfaceList);
        Map<String, List> consumer = new HashMap<String, List>();
        consumer.put("consumer", subscribedInterfaceList);
        all.add(provider);
        all.add(consumer);
        return all;
    }

}
