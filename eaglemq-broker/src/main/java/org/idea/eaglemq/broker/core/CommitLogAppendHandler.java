package org.idea.eaglemq.broker.core;

import org.idea.eaglemq.broker.model.CommitLogMessageModel;

import java.io.IOException;

public class CommitLogAppendHandler {

    private MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();

    public void prepareMMapLoading(String topicName) throws IOException {
        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(topicName,0, 1 *1024*1024);
        mMapFileModelManager.put(topicName,mMapFileModel);
    }

    public void appendMsg(String topic, byte[] content){
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);
        if (mMapFileModel == null){
            throw new RuntimeException("topic is invalid!");
        }
        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setSize(content.length);
        commitLogMessageModel.setContent(content);
        mMapFileModel.writeContent(commitLogMessageModel);
    }

    public void readMsg(String topic){
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);
        if (mMapFileModel == null){
            throw new RuntimeException("topic is invalid!");
        }
        byte[] content = mMapFileModel.readContent(0,10);
        System.out.println(new String(content));
    }

}
