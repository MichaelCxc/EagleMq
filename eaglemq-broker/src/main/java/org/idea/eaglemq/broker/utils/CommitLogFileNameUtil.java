package org.idea.eaglemq.broker.utils;

public class CommitLogFileNameUtil {


    /**
     * Constuct a commit log file name
     * @return
     */
    public static String buildFirstCommitLogName(){
        return "00000000";
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
