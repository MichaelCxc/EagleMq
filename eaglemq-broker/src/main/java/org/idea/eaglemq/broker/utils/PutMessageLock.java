package org.idea.eaglemq.broker.utils;

public interface PutMessageLock {
    void lock();
    void unlock();
}
