package org.idea.eaglemq.broker.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SpinLock implements PutMessageLock{

    AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void lock() {
        do{
            int res = atomicInteger.getAndIncrement();
            if(res == 1){
                return;
            }
        }while(true);

    }

    @Override
    public void unlock() {
        atomicInteger.decrementAndGet();
    }
}
