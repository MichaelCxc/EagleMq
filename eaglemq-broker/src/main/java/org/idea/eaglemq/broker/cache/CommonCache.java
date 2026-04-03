package org.idea.eaglemq.broker.cache;

import org.idea.eaglemq.broker.config.GlobalProperties;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;

import java.util.ArrayList;
import java.util.List;

public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();
    public static List<EagleMqTopicModel> eagleMqTopicModelList = new ArrayList<>();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static List<EagleMqTopicModel> getEagleMqTopicModelList() {
        return eagleMqTopicModelList;
    }

    public static void setEagleMqTopicModelList(List<EagleMqTopicModel> eagleMqTopicModelList) {
        CommonCache.eagleMqTopicModelList = eagleMqTopicModelList;
    }
}
