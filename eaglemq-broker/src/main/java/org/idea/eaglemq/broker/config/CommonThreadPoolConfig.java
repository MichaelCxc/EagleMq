package org.idea.eaglemq.broker.config;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CommonThreadPoolConfig {
    public static ThreadPoolExecutor refreshEagleMqTopicExecutor = new ThreadPoolExecutor(1,
            1,
            30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("refresh-eagle-mq-topic-config");
            return thread;
        }
    });
}
