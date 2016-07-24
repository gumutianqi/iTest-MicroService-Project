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

import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ProviderInterfaceLogFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("interfacelog");

    public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
        long startTimeMills = System.currentTimeMillis();
        InterfaceLog interfaceLog = new InterfaceLog();

        Throwable exception = null;

        Result result = null;
        try {
            RpcContext context = RpcContext.getContext();
            Map requestParams = invoker.getUrl().getParameters();

            interfaceLog.setMethodName(invoker.getInterface().getName() + "." + inv.getMethodName());
            String senderName = context.getAttachment("SENDER_APP_NAME");
            interfaceLog.setSenderName(senderName);
            interfaceLog.setReceiverName((String) requestParams.get("application"));
            interfaceLog.setSenderHost(context.getRemoteHost() + ":" + context.getRemotePort());
            interfaceLog.setReceiverHost(context.getLocalHost() + ":" + context.getLocalPort());
            interfaceLog.setSrvGroup((String) requestParams.get("group"));
            interfaceLog.setVersion((String) requestParams.get("version"));

            Class[] types = inv.getParameterTypes();
            if ((types != null) && (types.length > 0)) {
                String[] paramTypes = new String[types.length];
                int i = 0;
                for (Class type : types) {
                    paramTypes[i] = type.getName();
                    i++;
                }
                interfaceLog.setParamTypes(paramTypes);
            }

            interfaceLog.setParamValues(inv.getArguments());

            result = invoker.invoke(inv);

            if (result != null) {
                exception = result.getException();
                interfaceLog.setResultValue(result.getValue());
            }
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            interfaceLog.setCostTime(System.currentTimeMillis() - startTimeMills);
            if (exception != null) {
                interfaceLog.setExceptionMsg(exception.getMessage());
            }
            logger.info(interfaceLog.toString());
        }

        return result;
    }
}
