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
package com.weteam.boot.dubbo.autoconfigure;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import com.alibaba.dubbo.rpc.Exporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(Exporter.class)
@EnableConfigurationProperties({DubboAnnotation.class, DubboApplication.class, DubboProtocol.class, DubboRegistry.class, DubboProvider.class})
public class DubboAutoConfiguration {

    @Autowired
    private DubboApplication dubboApplication;

    @Autowired
    private DubboProtocol dubboProtocol;

    @Autowired
    private DubboProvider dubboProvider;

    @Autowired
    private DubboRegistry dubboRegistry;

    @Bean
    public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package-name}") String packageName) {
        log.debug("AnnotationBean:{}", packageName);

        if (StringUtils.isBlank(packageName)) {
            throw new RuntimeException("annotation package is null");
        }

        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(packageName);

        return annotationBean;
    }

    @Bean
    public ApplicationConfig applicationConfig() {
        log.debug("ApplicationConfig:{}", dubboApplication);
        if (StringUtils.isBlank(dubboApplication.getName())) {
            throw new RuntimeException("application name is null");
        }

        ApplicationConfig applicationConfig = new ApplicationConfig();

        applicationConfig.setName(dubboApplication.getName());
        applicationConfig.setLogger(dubboApplication.getLogger());

        return applicationConfig;
    }

    @Bean
    public ProtocolConfig protocolConfig() {
        log.debug("ProtocolConfig:{}", dubboProtocol);

        ProtocolConfig protocolConfig = new ProtocolConfig();

        protocolConfig.setName(dubboProtocol.getName());
        protocolConfig.setPort(dubboProtocol.getPort());
        protocolConfig.setAccesslog(String.valueOf(dubboProtocol.isAccessLog()));

        return protocolConfig;
    }

    @Bean
    public ProviderConfig providerConfig(ApplicationConfig applicationConfig,
                                         RegistryConfig registryConfig,
                                         ProtocolConfig protocolConfig) {
        log.debug("ProviderConfig:{}", dubboProvider);

        ProviderConfig providerConfig = new ProviderConfig();

        providerConfig.setTimeout(dubboProvider.getTimeout());
        providerConfig.setRetries(dubboProvider.getRetries());
        providerConfig.setFilter(dubboProvider.getFilter());
        providerConfig.setDelay(dubboProvider.getDelay());

        providerConfig.setApplication(applicationConfig);
        providerConfig.setRegistry(registryConfig);
        providerConfig.setProtocol(protocolConfig);

        return providerConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        log.debug("RegistryConfig:{}", dubboRegistry);

        RegistryConfig registryConfig = new RegistryConfig();

        registryConfig.setProtocol(dubboRegistry.getProtocol());
        registryConfig.setAddress(dubboRegistry.getAddress());
        registryConfig.setRegister(dubboRegistry.isRegister());
        registryConfig.setSubscribe(dubboRegistry.isSubscribe());

        return registryConfig;
    }
}
