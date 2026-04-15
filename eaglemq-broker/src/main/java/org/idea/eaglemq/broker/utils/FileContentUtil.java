package org.idea.eaglemq.broker.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static com.alibaba.fastjson2.JSON.parseArray;

public class FileContentUtil {

    public static String readFromFile(String path){
        try(BufferedReader in = new BufferedReader(new FileReader(path))) {
            StringBuilder stb = new StringBuilder();
            while(in.ready()){
                stb.append(in.readLine());
            }
            return stb.toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void overWriteToFile(String path, String content){
        try(FileWriter fileWriter = new FileWriter(path)){
            fileWriter.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        String content = FileContentUtil.readFromFile("F:\\Java\\eaglemq\\broker\\config\\eaglemq-topic.json");
//        System.out.println(content);
//        List<EagleMqTopicModel> eagleMqTopicModelList = parseArray(content, EagleMqTopicModel.class);
//        System.out.println(eagleMqTopicModelList);
//    }
}
