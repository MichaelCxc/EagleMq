package org.idea.eaglemq.broker.config;

public class GlobalProperties {


    /**
     * read absolute file path of mq from environmental variable
     */
    private String eagleMqHome;

    public String getEagleMqHome(){
        return eagleMqHome;
    }

    public void setEagleMqHome(String eagleMqHome){
        this.eagleMqHome = eagleMqHome;
    }
}
