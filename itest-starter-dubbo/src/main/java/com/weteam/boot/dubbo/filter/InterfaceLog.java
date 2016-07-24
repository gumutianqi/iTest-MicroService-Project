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
package com.weteam.boot.dubbo.filter;

import com.alibaba.dubbo.common.json.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceLog implements Serializable {
    private String invokeTime = String.valueOf(System.currentTimeMillis());
    private String methodName;
    private String senderName;
    private String senderHost;
    private String receiverName;
    private String receiverHost;
    private String srvGroup;
    private String version;
    private String[] paramTypes;
    private Object[] paramValues;
    private Object resultValue;
    private long costTime = 0L;
    private String exceptionMsg;

    @Override
    public String toString() {
        String slfStr;
        try {
            slfStr = JSON.json(this);
        } catch (Exception e) {
            slfStr = toSimpleString();
        }
        return slfStr;
    }

    public String toSimpleString() {
        return String.format("{\"invokeTime\": \"%s\" , \"methodName\": \"%s\", \"senderName\": \"%s\", \"senderHost\": \"%s\", \"receiverName\": \"%s\", \"receiverHost\": \"%s\", \"srvGroup\": \"%s\", \"version\": \"%s\", \"paramTypes\": [], \"paramValues\": [], \"resultValue\": {}, \"costTime\": %s, \"exceptionMsg\": \"%s\"}", this.invokeTime, this.methodName, this.senderName, this.senderHost, this.receiverName, this.receiverHost, this.srvGroup, this.version, this.costTime, this.exceptionMsg);
    }
}
