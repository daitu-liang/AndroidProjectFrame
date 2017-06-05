package com.kakaxi.fightdemo.network.uploaddowon;

import com.kakaxi.fightdemo.utils.Logger;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by leixiaoliang on 2017/4/15.
 * 邮箱：lxliang1101@163.com
 */

public class DownloadResponseBody extends ResponseBody {
    private Logger log = Logger.getLogger("DownloadResponseBody");
    //实际的待包装响应体
    private final ResponseBody responseBody;
    //进度回调接口
    private final DownloadProgressListener progressListener;
    //包装完成的BufferedSource
    private BufferedSource bufferedSource;

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件保存路径
     */

    private String savePath;

    /**
     * 下载文件名
     */
    private String fileName;
    /**
     * 构造函数，赋值
     *
     * @param responseBody     待包装的响应体
     * @param progressListener 回调接口
     */
    public DownloadResponseBody(ResponseBody responseBody, DownloadProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }


    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    /**
     * 重写进行包装source
     *
     * @return BufferedSource
     */
    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            //包装
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 读取，回调进度接口
     * @param source Source
     * @return Source
     */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            //当前读取字节数
            long currentBytesCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                currentBytesCount += bytesRead != -1 ? bytesRead : 0;
                //回调，如果contentLength()不知道长度，会返回-1

                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                if(progressListener != null){
                    Observable.just(currentBytesCount)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(@NonNull Long aLong) throws Exception {
                                    if(progressListener!=null){
                                        double readNum = (currentBytesCount / (double) totalBytesCount) * 100;
                                        int progress = (int) Math.ceil(readNum);
                                        progressListener.onDownloadProgress(currentBytesCount, totalBytesCount,progress, currentBytesCount==-1);
                                    }
                                }
                            });
                }
                return bytesRead;
            }
        };
    }
}
