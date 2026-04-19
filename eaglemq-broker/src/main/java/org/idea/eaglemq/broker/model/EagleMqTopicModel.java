package org.idea.eaglemq.broker.model;

import java.util.List;

public class EagleMqTopicModel {
    private String topic;
    private CommitLogModel commitLogModel;
    private List<QueueModel> queueList;
    private String createAt;
    private String updateAt;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<QueueModel> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<QueueModel> queueList) {
        this.queueList = queueList;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public CommitLogModel getCommitLogModel() {
        return commitLogModel;
    }

    public void setCommitLogModel(CommitLogModel commitLogModel) {
        this.commitLogModel = commitLogModel;
    }

    @Override
    public String toString() {
        return "EagleMqTopicModel{" +
                "topic='" + topic + '\'' +
                ", commitLogfileName=" + commitLogModel.getFileName() +
                ", commitLogOffset=" + commitLogModel.getOffset() +
                ", queueList=" + queueList +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }
}
