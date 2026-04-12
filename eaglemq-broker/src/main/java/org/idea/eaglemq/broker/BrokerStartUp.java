package org.idea.eaglemq.broker;

import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.config.EagleMqTopicLoader;
import org.idea.eaglemq.broker.config.GlobalPropertiesLoader;
import org.idea.eaglemq.broker.constants.BrokerConstants;
import org.idea.eaglemq.broker.core.CommitLogAppendHandler;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;

import java.io.IOException;
import java.util.List;

public class BrokerStartUp {

    private static GlobalPropertiesLoader globalPropertiesLoader;
    private static EagleMqTopicLoader eagleMqTopicLoader;
    private static CommitLogAppendHandler commitLogAppendHandler;

    private static void initProperties() throws IOException {
        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        eagleMqTopicLoader = new EagleMqTopicLoader();
        eagleMqTopicLoader.loadProperties();
        commitLogAppendHandler = new CommitLogAppendHandler();
        List<EagleMqTopicModel> eagleMqTopicModelList = CommonCache.getEagleMqTopicModelList();
        for (EagleMqTopicModel eagleMqTopicModel : eagleMqTopicModelList){
            String topicName = eagleMqTopicModel.getTopic();
            commitLogAppendHandler.prepareMMapLoading(topicName);
        }


    }

    public static void main(String[] args) throws IOException {
        initProperties();
        // Load property
        String topic = "order_cancel_topic";
        commitLogAppendHandler.appendMsg(topic, "This is a test content.");
        commitLogAppendHandler.readMsg(topic);
        //Initializa
    }
}
