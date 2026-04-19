package org.idea.eaglemq.broker.config;

import com.alibaba.fastjson2.JSON;
import io.netty.util.internal.StringUtil;
import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.constants.BrokerConstants;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;
import org.idea.eaglemq.broker.utils.FileContentUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.alibaba.fastjson2.JSON.parseArray;

public class EagleMqTopicLoader {

    private String filePath;

    public void loadProperties(){
        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getEagleMqHome();
        if(StringUtil.isNullOrEmpty(basePath)){
            throw new IllegalArgumentException("EAGLE_MQ_HOME is invalid.");
        }
        filePath = basePath + "/broker/config/eaglemq-topic.json";
        String fileContent = FileContentUtil.readFromFile(filePath);
        List<EagleMqTopicModel> eagleMqTopicModelList = JSON.parseArray(fileContent, EagleMqTopicModel.class);
        CommonCache.setEagleMqTopicModelList(eagleMqTopicModelList);
        //CommonCache.setEagleMqTopicModelMap(eagleMqTopicModelList.stream().collect(Collectors.toMap(EagleMqTopicModel::getTopic,item->item)));

    }

    public void startRefreshEagleMqTopicInfoTask(){
        //异步线程
        //每隔15秒将内存中的配置刷新到磁盘里面
        // Redis RDB
        CommonThreadPoolConfig.refreshEagleMqTopicExecutor.execute(new Runnable() {
            @Override
            public void run() {
                do{
                    try{
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);
                        System.out.println("Refresh disk");
                        List<EagleMqTopicModel> eagleMqTopicModelList = CommonCache.getEagleMqTopicModelList();
                        FileContentUtil.overWriteToFile(filePath, JSON.toJSONString(eagleMqTopicModelList));
                    }catch (InterruptedException e){
                        throw new RuntimeException(e);
                    }
                }while(true);
            }
        });
    }
}
