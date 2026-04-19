package org.idea.eaglemq.broker.cache;

import org.idea.eaglemq.broker.config.GlobalProperties;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();
    public static List<EagleMqTopicModel> eagleMqTopicModelList = new ArrayList<>();
    public static Map<String, EagleMqTopicModel> eagleMqTopicModelMap = new HashMap<>();
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

    public static Map<String, EagleMqTopicModel> getEagleMqTopicModelMap() {
        return eagleMqTopicModelList.stream().collect(Collectors.toMap(EagleMqTopicModel::getTopic, item->item));
    }

    public static void setEagleMqTopicModelMap(Map<String, EagleMqTopicModel> eagleMqTopicModelMap) {
        CommonCache.eagleMqTopicModelMap = eagleMqTopicModelMap;
    }
}
