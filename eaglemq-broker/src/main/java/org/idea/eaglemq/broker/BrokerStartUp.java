package org.idea.eaglemq.broker;

import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.config.EagleMqTopicLoader;
import org.idea.eaglemq.broker.config.GlobalPropertiesLoader;
import org.idea.eaglemq.broker.constants.BrokerConstants;
import org.idea.eaglemq.broker.core.CommitLogAppendHandler;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BrokerStartUp {

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static EagleMqTopicLoader eagleMqTopicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;

    private static void initProperties() throws IOException {
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        eagleMqTopicLoader = new EagleMqTopicLoader();
        eagleMqTopicLoader.loadProperties();
        eagleMqTopicLoader.startRefreshEagleMqTopicInfoTask();
        commitLogAppendHandler = new CommitLogAppendHandler();
        List<EagleMqTopicModel> eagleMqTopicModelList = CommonCache.getEagleMqTopicModelList();
        for (EagleMqTopicModel eagleMqTopicModel : eagleMqTopicModelList){
            String topicName = eagleMqTopicModel.getTopic();
            commitLogAppendHandler.prepareMMapLoading(topicName);
        }


    }

    public static void main(String[] args) throws IOException, InterruptedException {
        initProperties();
        // Load property
        String topic = "order_cancel_topic";
        for(int i = 0; i < 10; i++){
            commitLogAppendHandler.appendMsg(topic, ("This is content" + i).getBytes());
            System.out.println("Write data to disk");
            TimeUnit.SECONDS.sleep(5);
        }
        //commitLogAppendHandler.appendMsg(topic, "This is a test content.".getBytes());
        commitLogAppendHandler.readMsg(topic);
        //Initializa
    }
}
