package org.idea.eaglemq.broker.core;

import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.constants.BrokerConstants;
import org.idea.eaglemq.broker.model.CommitLogMessageModel;
import org.idea.eaglemq.broker.model.CommitLogModel;
import org.idea.eaglemq.broker.model.EagleMqTopicModel;
import org.idea.eaglemq.broker.utils.ByteConvertUtils;
import org.idea.eaglemq.broker.utils.CommitLogFileNameUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class MMapFileModel {
    private File file;
    //private int mappedSize;
    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;
    private String topic;


    /**
     * FIle MMap from target offset
     *
     * @param topicName     消息主题
     * @param startOffset
     * @param mappedSize
     */
    public void loadFileInMMap(String topicName, int startOffset, int mappedSize) throws IOException {
        String filePath = getLatestCommitLogFile(topicName);
        this.topic = topicName;
        this.doMMap(filePath,startOffset, mappedSize);
    }

    private void doMMap(String filePath,  int startOffset, int mappedSize) throws IOException {
        this.file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException("filePath is " + filePath + " invalid");
        }

        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        this.mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * Get latest commitlog file
     * @param topicName
     * @return
     */
    private String getLatestCommitLogFile(String topicName){
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getEagleMqTopicModelMap().get(topicName);
        if(eagleMqTopicModel == null){
            throw new IllegalArgumentException("Topic in inValid: topicName is "+ topicName);
        }
        CommitLogModel commitLogModel = eagleMqTopicModel.getCommitLogModel();
        long diff = commitLogModel.getOffsetLimit() - commitLogModel.getOffset();
        String filePath = null;
        if(diff == 0){
            filePath = this.createNewCommitLogFile(topicName,commitLogModel);
        }else if(diff > 0){
            filePath = CommonCache.getGlobalProperties().getEagleMqHome()
                    + BrokerConstants.BASE_STORE_PATH
                    + topicName
                    + commitLogModel.getFileName();
        }
        return filePath;
    }

    private String createNewCommitLogFile(String topicName, CommitLogModel commitLogModel){
        String newFileName = CommitLogFileNameUtil.incrCommitLogFileName(commitLogModel.getFileName());
        String newFilePath = CommonCache.getGlobalProperties().getEagleMqHome()
                + BrokerConstants.BASE_STORE_PATH
                + topicName
                + newFileName;
        File newCommitLogFile = new File(newFilePath);
        try {
            newCommitLogFile.createNewFile();
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
        return newFilePath;
    }

    /**
     *
     * @param readOffset
     * @param size
     * @return
     */
    public byte[] readContent(int readOffset, int size){
        mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];
        int j = 0;
        for(int i = 0; i < size;i++){
            //Read from local cache
            byte b = mappedByteBuffer.get(readOffset+i);
            content[j++] = b;
        }
        return content;
    }

    /**
     *
     * @param commitLogMessageModel
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel) throws IOException {
        this.writeContent(commitLogMessageModel,false);
    }

    /**
     * Write to disk
     * @param commitLogMessageModel
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel, boolean force) throws IOException {
        //定位到最新的commitLog文件中，记录下当前文件是否已经写满，如果写满，则创建新的文件
        //并且做新的mmap映射，如果当前文件没有写满，对content内容做一层封装，再判断写入是否会导致commitlog写满
        //如果不会，则选择当前commitLog,如果会则创建新文件，并且做mmap映射
        //定位到最新的commitlog文件之后，写入
        //定义一个对象，专门管理各个topic的最新写入offset值，并且定时刷新到磁盘中（mmap?)
        //写入数据，offset变更，如果是高并发场景，offset是不是会被多个线程访问？

        this.checkCommitLogHasEnabledSpace(commitLogMessageModel);
        //offset会用一个原子类AtomicLong去管理
        //线程安全问题： 线程1：111，线程2:122
        //加锁机制（锁的选择非常重要）

        // Default write to page cache
        // If hope to flush to disk, we need to adjust
//        MappedByteBuffer byteBuffer = mappedByteBuffer.slice();
//        byteBuffer.position(111);
//        byteBuffer.put(content);



        mappedByteBuffer.put(commitLogMessageModel.converToBytes());
        if(force){
            //强制刷盘
            mappedByteBuffer.force();
        }
    }

    private void checkCommitLogHasEnabledSpace(CommitLogMessageModel commitLogMessageModel) throws IOException {
        EagleMqTopicModel eagleMqTopicModel = CommonCache.getEagleMqTopicModelMap().get(this.topic);
        CommitLogModel commitLogModel = eagleMqTopicModel.getCommitLogModel();
        long writeAbleOffsetNum = commitLogModel.getOffsetLimit() - commitLogModel.getOffset();
        // Not enough space to write, need to create new file
        if(!(writeAbleOffsetNum >= commitLogMessageModel.getSize())){
            //00000000 file ->> 00000001 file
            String newCommitLogPath = this.createNewCommitLogFile(topic, commitLogModel);
            this.doMMap(newCommitLogPath, 0, BrokerConstants.COMMIT_LOG_DEFAULT_MMAP_SIZE);
        }
    }

    public void clean(){
        if(mappedByteBuffer == null || !mappedByteBuffer.isDirect() || mappedByteBuffer.capacity() == 0)
            return;

        invoke(invoke(viewed(mappedByteBuffer), "cleaner"), "clean");
    }

    private Object invoke(final Object target, final String methodName, final Class<?>...args){
        return AccessController.doPrivileged(new PrivilegedAction<Object>() {
            public Object run(){
                try{
                    Method method = method(target, methodName, args);
                    method.setAccessible(true);
                    return method.invoke(target);
                }catch (Exception e){
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    private Method method(Object target, String methodName, Class<?>[] args) throws NoSuchMethodException{
        try {
            return target.getClass().getMethod(methodName, args);
        }catch (NoSuchMethodException e){
            return target.getClass().getDeclaredMethod(methodName,args);
        }
    }

    private ByteBuffer viewed(ByteBuffer buffer){
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for(int i = 0; i < methods.length; i++){
            if(methods[i].getName().equals("attachment")){
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if(viewedBuffer == null)
            return buffer;
        else
            return viewed(viewedBuffer);
    }

}
