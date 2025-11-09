package org.idea.eaglemq.broker.config;

import io.netty.util.internal.StringUtil;
import org.idea.eaglemq.broker.cache.CommonCache;

public class TopicInfoLoader {

    private TopicInfo topicInfo;

    public void loadProperties(){
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throws new IllegalArgumentException("EAGLE_MQ_HOME is invalid.");
        }
        String topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";
        topicInfo = new TopicInfo();

    }
}
