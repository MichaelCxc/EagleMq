package org.idea.eaglemq.broker.model;

import org.idea.eaglemq.broker.utils.ByteConvertUtils;

public class CommitLogMessageModel {

    /**
     * message size
     */
    private int size;

    /**
     * content
     */
    private byte[] content;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] convertToBytes(){
        byte[] sizeByte = ByteConvertUtils.intToBytes(this.getSize());
        byte[] content = this.getContent();
        byte[] mergeResultByte = new byte[sizeByte.length + content.length];
        int j = 0;
        for(int i = 0; i < sizeByte.length; i++,j++){
            mergeResultByte[j] =sizeByte[i];
        }
        for(int i = 0; i < content.length;i++,j++){
            mergeResultByte[j] = content[i];
        }
        return mergeResultByte;
    }
}
