package org.idea.eaglemq.broker.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MessageAppendHandler {

    private MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();
    private String filePath = "F:\\Java\\eaglemq\\broker\\store\\order_cancel_topic\\00000000";
    private static String topicName = "order_cancel_topic";

    public MessageAppendHandler() throws IOException {
        this.prepareMMapLoading();
    }

    private void prepareMMapLoading() throws IOException {
        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(filePath, 0, 1 *1024*1024);
        mMapFileModelManager.put(topicName,mMapFileModel);
    }

    public void appendMsg(String topic, String content){
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);
        if (mMapFileModel == null){
            throw new RuntimeException("topic is invalid!");
        }

        mMapFileModel.writeContent(content.getBytes());
    }

    public void readMsg(String topic){
        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);
        if (mMapFileModel == null){
            throw new RuntimeException("topic is invalid!");
        }
        byte[] content = mMapFileModel.readContent(0,20);
        System.out.println(new String(content));
    }

    public static void main(String[] args) throws IOException{
        MessageAppendHandler messageAppendHandler = new MessageAppendHandler();
        messageAppendHandler.appendMsg(topicName, "This is the content");
        messageAppendHandler.readMsg(topicName);
    }
}
