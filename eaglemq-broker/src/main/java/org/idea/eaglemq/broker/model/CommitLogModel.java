package org.idea.eaglemq.broker.model;


import java.util.concurrent.atomic.AtomicLong;

public class CommitLogModel {

    /**
     * 最新commitLog文件的名称
     */
    private String fileName;

    /**
     * CommitLog file limits
     */
    private Long offsetLimit;

    /**
     * 最新commitLog文件写入数据的地址
     */
    private AtomicLong offset;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long countDiff(){
        return this.offsetLimit - this.offset.get();
    }

    public AtomicLong getOffset() {
        return offset;
    }

    public void setOffset(AtomicLong offset) {
        this.offset = offset;
    }

    public Long getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Long offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    @Override
    public String toString() {
        return "CommitLogModel{" +
                "fileName='" + fileName + '\'' +
                ", offset=" + offset +
                '}';
    }
}
