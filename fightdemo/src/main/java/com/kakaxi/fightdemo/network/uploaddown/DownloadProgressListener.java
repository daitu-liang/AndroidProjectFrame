package com.kakaxi.fightdemo.network.uploaddown;

/**
 * Created by leixiaoliang on 2017/4/15.
 * 邮箱：lxliang1101@163.com
 */

public interface DownloadProgressListener {
    /**
     * 下载进度
     * @param currentBytesCount
     * @param totalBytesCount
     * @param done
     */

    void onDownloadProgress(long currentBytesCount, long totalBytesCount,int progress, boolean done);
}
