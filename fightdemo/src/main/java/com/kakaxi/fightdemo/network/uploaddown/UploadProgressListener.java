package com.kakaxi.fightdemo.network.uploaddown;

/**
 * Created by leixiaoliang on 2017/4/15.
 * 邮箱：lxliang1101@163.com
 */

public interface UploadProgressListener {
    /**
     * 上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     * @param done
     */

    void onUploadProgress(long currentBytesCount, long totalBytesCount,int progress,boolean done);
}
