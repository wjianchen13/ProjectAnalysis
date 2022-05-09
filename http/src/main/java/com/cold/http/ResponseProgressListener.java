package com.cold.http;

/**
 * response 进度监听接口
 */
public interface ResponseProgressListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
