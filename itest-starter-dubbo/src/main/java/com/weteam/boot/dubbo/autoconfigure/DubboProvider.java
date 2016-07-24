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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "dubbo.provider")
public class DubboProvider {

    /**
     * 服务的超时时间,单位毫秒
     */
    private int timeout = 10000;

    /**
     * 调用失败重试次数
     */
    private int retries = 0;

    /**
     * 过滤器
     */
    private String filter = "providerInterfaceLogFilter";

    /**
     * 是否延迟暴露,-1表示不延迟暴露
     */
    private int delay = -1;
}
