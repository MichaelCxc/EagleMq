package org.idea.eaglemq.broker.core;

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


    /**
     * FIle MMap from target offset
     *
     * @param filePath  filepath
     * @param startOffset
     * @param mappedSize
     */
    public void loadFileInMMap(String filePath, int startOffset, int mappedSize) throws IOException {
        this.file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException("filePath is " + filePath + " invalid");
        }
        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        this.mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
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
     * @param content
     */
    public void writeContent(byte[] content){
        this.writeContent(content,false);
    }

    /**
     * Write to disk
     * @param content
     * @param force
     */
    public void writeContent(byte[] content, boolean force){
        // Default write to page cache
        // If hope to flush to disk, we need to adjust
        mappedByteBuffer.put(content);
        if(force){
            mappedByteBuffer.force();
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
