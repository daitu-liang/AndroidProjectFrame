package com.kakaxi.fightdemo.ui.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kakaxi.fightdemo.R;
import com.kakaxi.fightdemo.network.api.commom.ApiCommom;
import com.kakaxi.fightdemo.network.uploaddowon.DownloadProgressListener;
import com.kakaxi.fightdemo.network.uploaddowon.ServiceGenerator;
import com.kakaxi.fightdemo.network.uploaddowon.UploadProgressListener;
import com.kakaxi.fightdemo.utils.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadDowonActivity extends AppCompatActivity implements DownloadProgressListener, UploadProgressListener {
    private static final String TAG = "UploadDowonActivity";
    private Logger log = Logger.getLogger(TAG);

    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.progressBar_tv)
    TextView progressBarTv;
    @BindView(R.id.path_tv)
    TextView pathTv;

    private Observable<File> observable;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UploadDowonActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_down);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_down, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_down:
                downloadFileRxjava();
//                downloadFileByCall();
                break;
            case R.id.btn_upload:
                upLoadFile();
                break;
        }
    }

    /**
     * 上传
     */
    private void upLoadFile() {
        mProgressBar.setVisibility(View.VISIBLE);
        //之前的请求方法
        ApiCommom uploadService = ServiceGenerator.createUploadService(ApiCommom.class, this);
        File file = new File("/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/test_upload.jpg");
        log.e(TAG, "file=" + file);
        if (file == null) return;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Observable<ResponseBody> observable = uploadService.upLoadFile(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@NonNull ResponseBody responseBody) throws Exception {
                        log.e("onResponse", "upLoadFile--->-----上传结束--:" + responseBody.contentLength());
                    }
                });
    }

    /**
     * 下载retofit+rxjava
     */
    private void downloadFileRxjava() {
        mProgressBar.setVisibility(View.VISIBLE);
        String url1 = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk";
        String url = "http://192.168.1.115:8080/downzone/map.zip";
        String savePath = getExternalFilesDir(null) + File.separator + "map.zip";
        ApiCommom downloadService = ServiceGenerator.createDownloadService(ApiCommom.class, this);
        observable = downloadService.downloadFile(url, savePath);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(@NonNull File file) throws Exception {
                        log.e("onResponse", "downloadFileRxjava--->-----下载结束--------file path:" + file.getPath());
                        pathTv.setText("保存路径：" + file.getPath());
                    }
                });
    }

    /**
     * 下载okhttp
     */
    private void downloadFileByCall() {
        String url = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk";
        String url2 = "http://t.img.i.hsuperior.com/a38ee054-b941-4eb9-9e83-ba45a2ae13a8";
        ApiCommom downloadService = ServiceGenerator.createDownloadService(ApiCommom.class, this);
        String savePath = getExternalFilesDir(null) + File.separator + "BaiduNetdisk.apk";
        mProgressBar.setVisibility(View.VISIBLE);
        Call call = downloadService.downloadFileCall(url, savePath);
        call.enqueue(new Callback<File>() {
            @Override
            public void onResponse(Call<File> call, Response<File> response) {
                if (response.isSuccessful() && response.body() != null) {
                    log.e("onResponse", "call_onResponse_file path:" + response.body().getPath());
                }
            }

            @Override
            public void onFailure(Call<File> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 下载下载进度
     *
     * @param currentBytesCount
     * @param totalBytesCount
     * @param done
     */
    @Override
    public void onDownloadProgress(long currentBytesCount, long totalBytesCount, int progress, boolean done) {
        log.e(TAG, "onDownloadProgress--bytesRead=" + currentBytesCount + "---totalBytesCount=" + totalBytesCount + "--progress=" + progress + "----是否完成下载=" + done);
        mProgressBar.setProgress(progress);
        progressBarTv.setText(progress + "%");
        log.e(TAG, "onDownloadProgress--------》------thread=" + Thread.currentThread().getName());
    }


    /**
     * 上传文件进度
     *
     * @param currentBytesCount
     * @param totalBytesCount
     * @param done
     */
    @Override
    public void onUploadProgress(long currentBytesCount, long totalBytesCount, int progress, boolean done) {
        log.e(TAG, "onUploadProgress--progress=" + progress);
        mProgressBar.setProgress(progress);
        progressBarTv.setText(progress + "%");
        progressBarTv.setText(progress + "%");
        log.e(TAG, "onUploadProgress--------》------thread=" + Thread.currentThread().getName());
    }
}
