package org.idea.eaglemq.broker.config;

import io.netty.util.internal.StringUtil;
import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.constants.BrokerConstants;

public class GlobalPropertiesLoader {

    public void loadProperties(){
        GlobalProperties globalProperties = new GlobalProperties();
        String eagleMqHome = System.getenv(BrokerConstants.EAGLE_MQ_HOME);

        if(StringUtil.isNullOrEmpty(eagleMqHome)){
            throw new IllegalArgumentException("EAGLE_MQ_HOME is null");
        }
        globalProperties.setEagleMqHome(eagleMqHome);
        CommonCache.setGlobalProperties(globalProperties);
    }
}
