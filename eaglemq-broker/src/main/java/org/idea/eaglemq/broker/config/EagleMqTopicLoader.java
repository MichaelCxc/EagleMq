package org.idea.eaglemq.broker.config;

import com.alibaba.fastjson2.JSON;
import io.netty.util.internal.StringUtil;
import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;
import org.idea.eaglemq.broker.utils.FileContentReaderUtil;

import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSON.parseArray;

public class EagleMqTopicLoader {



    public void loadProperties(){
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid.");
        }
        String topicJsonFilePath = basePath + "/broker/config/eaglemq-topic.json";

        String fileContent = FileContentReaderUtil.readFromFile(topicJsonFilePath);
        List<EagleMqTopicModel> eagleMqTopicModelList = JSON.parseArray(fileContent, EagleMqTopicModel.class);
        CommonCache.setEagleMqTopicModelList(eagleMqTopicModelList);
        CommonCache.setEagleMqTopicModelMap(eagleMqTopicModelList.stream().collect(Collectors.toMap(EagleMqTopicModel::getTopic,item->item)));

    }
}
