package org.idea.eaglemq.broker.utils;

import org.idea.eaglemq.broker.cache.CommonCache;
import org.idea.eaglemq.broker.constants.BrokerConstants;

public class CommitLogFileNameUtil {


    /**
     * Constuct a commit log file name
     * @return
     */
    public static String buildFirstCommitLogName(){
        return "00000000";
    }

    /**
     *
     * @param topicName
     * @param commitLogFileName
     * @return
     */
    public static String buildCommitLogFilePath(String topicName, String commitLogFileName){
        return CommonCache.getGlobalProperties().getEagleMqHome()
                + BrokerConstants.BASE_STORE_PATH
                + topicName
                + "/"
                + commitLogFileName;
    }

    /**
     * Generate new commitlog file name according to old commitlog file
     * @param oldFileName
     * @return
     */
    public static String incrCommitLogFileName(String oldFileName){
        if(oldFileName.length() != 8){
            throw new IllegalArgumentException("File name must has 8 chars.");
        }
        Long fileIndex = Long.valueOf(oldFileName);
        fileIndex++;
        String newFileName = String.valueOf(fileIndex);
        int newFileNameLen = newFileName.length();
        int needFullLen = 8 - newFileNameLen;
        if(needFullLen < 0){
            throw new RuntimeException("Unknown file name error");
        }

        StringBuffer stb = new StringBuffer();
        for(int i = 0; i < needFullLen; i++){
            stb.append("0");
        }
        stb.append(newFileName);
        return stb.toString();
    }

    public static void main(String[] args) {
        String newFileName = CommitLogFileNameUtil.incrCommitLogFileName("00000011");
        System.out.println(newFileName);
    }
}
