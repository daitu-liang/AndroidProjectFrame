package com.kakaxi.fightdemo.ui.acitivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kakaxi.fightdemo.R;
import com.kakaxi.fightdemo.network.ApiSubscriber;
import com.kakaxi.fightdemo.network.ParamsMapUtils;
import com.kakaxi.fightdemo.network.api.commom.ApiCommom;
import com.kakaxi.fightdemo.network.uploaddown.DownloadProgressListener;
import com.kakaxi.fightdemo.network.uploaddown.ServiceGenerator;
import com.kakaxi.fightdemo.network.uploaddown.UploadPart;
import com.kakaxi.fightdemo.network.uploaddown.UploadProgressListener;
import com.kakaxi.fightdemo.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadDownActivity extends AppCompatActivity implements DownloadProgressListener, UploadProgressListener {
    private static final String TAG = "UploadDownActivity";
    @BindView(R.id.progressBar_upload)
    ProgressBar progressBarUpload;
    @BindView(R.id.progressBar_upload_tv)
    TextView progressBarUploadTv;
    @BindView(R.id.progressBar_multi_upload)
    ProgressBar progressBarMultiUpload;
    @BindView(R.id.progressBar_multi_upload_tv)
    TextView progressBarMultiUploadTv;
    @BindView(R.id.btn_multi_upload)
    Button btnMultiUpload;
    @BindView(R.id.progressBar_params_upload)
    ProgressBar progressBarParamsUpload;
    @BindView(R.id.progressBar_params_upload_tv)
    TextView progressBarParamsUploadTv;
    @BindView(R.id.progressBar_upload_part)
    ProgressBar progressBarUploadPart;
    @BindView(R.id.progressBar_upload_part_tv)
    TextView progressBarUploadPartTv;
    private Logger log = Logger.getLogger(TAG);

    @BindView(R.id.btn_down)
    Button btnDown;
    @BindView(R.id.btn_upload)
    Button btnUpload;
    @BindView(R.id.progressBar_down)
    ProgressBar mProgressBar;
    @BindView(R.id.progressBar_down_tv)
    TextView progressBarTv;
    @BindView(R.id.path_tv)
    TextView pathTv;

    private Observable<File> observable;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, UploadDownActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_down);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_down, R.id.btn_upload, R.id.btn_multi_upload, R.id.btn_params_upload, R.id.btn_upload_part})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_down:
                downloadFileRxjava();
//                downloadFileByCall();
                break;
            case R.id.btn_upload:
                upLoadFile();
                break;
            case R.id.btn_upload_part:
                upLoadPartParamsFile();
                break;
            case R.id.btn_multi_upload:
                multiUpLoadFile();
                break;
            case R.id.btn_params_upload:
                paramsUpLoadFile();
                break;
        }
    }

    /**
     * 带参上传文件@QueryMap
     */
    private void paramsUpLoadFile() {
        String filePath = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/map.zip";
        //之前的请求方法
        ApiCommom uploadService = ServiceGenerator.createUploadService(ApiCommom.class, new UploadProgressListener() {
            @Override
            public void onUploadProgress(long currentBytesCount, long totalBytesCount, int progress, boolean done) {
                progressBarParamsUpload.setProgress(progress);
                progressBarParamsUploadTv.setText(progress + "%");
            }
        });
        MultipartBody.Part requestBody = UploadPart.getFilePart("file", filePath);
        Observable<ResponseBody> observable = uploadService.upLoadFile(ParamsMapUtils.getParamsMap(), requestBody);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<ResponseBody>() {
                    @Override
                    protected void onSuccess(ResponseBody bean) {
                        log.i("onResponse", "onSuccess--->-----上传成功--");
                        Toast.makeText(UploadDownActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 多文件上传带参
     * @PartMap Map<String, RequestBody>
     */
    private void multiUpLoadFile() {
        progressBarMultiUpload.setVisibility(View.VISIBLE);
        String filePath1 = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/map.zip";
        String filePath2 = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/vedio.wmv";
        String filePath3 = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/netapi.rar";
        String filePath4 = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/test_upload.jpg";
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(filePath4);
        pathList.add(filePath3);
        pathList.add(filePath2);
        pathList.add(filePath1);
        Map<String, RequestBody> bodyMap = new HashMap<>();
        bodyMap.put("user_id", UploadPart.toRequestBody("usedId11001"));
        bodyMap.put("pwd", UploadPart.toRequestBody("PartMap"));
        if (pathList.size() > 0) {
            for (int i = 0; i < pathList.size(); i++) {
                File file = new File(pathList.get(i));
                bodyMap.put("file" + i + "\";filename=\"" + file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
        }
        //之前的请求方法
        ApiCommom uploadService = ServiceGenerator.createUploadService(ApiCommom.class, new UploadProgressListener() {
            @Override
            public void onUploadProgress(long currentBytesCount, long totalBytesCount, int progress, boolean done) {
                progressBarMultiUpload.setProgress(progress);
                progressBarMultiUploadTv.setText(progress + "%");
            }
        });
        Observable<ResponseBody> observable = uploadService.upLoadMutiFile(bodyMap);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<ResponseBody>() {
                    @Override
                    protected void onSuccess(ResponseBody bean) {
                        log.i("onResponse", "onSuccess--->-----多文件上传成功--");
                        Toast.makeText(UploadDownActivity.this, "多文件上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 单文件上传带参@Part
     * @Part("description")RequestBody description, @Part MultipartBody.Part file
     */
    private void upLoadPartParamsFile() {
        progressBarUpload.setVisibility(View.VISIBLE);
        String filePath = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/test_upload.jpg";
        //之前的请求方法
        ApiCommom uploadService = ServiceGenerator.createUploadService(ApiCommom.class, new UploadProgressListener() {
            @Override
            public void onUploadProgress(long currentBytesCount, long totalBytesCount, int progress, boolean done) {
                progressBarUploadPart.setProgress(progress);
                progressBarUploadPartTv.setText(progress+"%");
            }
        });

        MultipartBody.Part requestBody = UploadPart.getFilePart("file-test", filePath);
        String descriptionString = "@Part-携带参数";
        RequestBody description = UploadPart.toRequestBody(descriptionString);

        Observable<ResponseBody> observable = uploadService.upLoadFile(description, requestBody);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<ResponseBody>() {
                    @Override
                    protected void onSuccess(ResponseBody bean) {
                        log.i("onResponse", "onSuccess--->-----上传成功--");
                        Toast.makeText(UploadDownActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 单文件上传
     * @Part MultipartBody.Part file
     */
    private void upLoadFile() {
        progressBarUpload.setVisibility(View.VISIBLE);
        String filePath = "/storage/emulated/0/Android/data/com.kakaxi.fightdemo/files/test_upload.jpg";
        //之前的请求方法
        ApiCommom uploadService = ServiceGenerator.createUploadService(ApiCommom.class, this);
        MultipartBody.Part requestBody = UploadPart.getFilePart("file-test", filePath);
        Observable<ResponseBody> observable = uploadService.upLoadFile(requestBody);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<ResponseBody>() {
                    @Override
                    protected void onSuccess(ResponseBody bean) {
                        log.i("onResponse", "onSuccess--->-----上传成功--");
                        Toast.makeText(UploadDownActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /**
     * 下载retofit+rxjava
     */
    private void downloadFileRxjava() {
        mProgressBar.setVisibility(View.VISIBLE);
        String url1 = "http://issuecdn.baidupcs.com/issue/netdisk/apk/BaiduNetdisk_7.15.1.apk";
        String url = "http://192.168.1.115:8080/downzone/test_upload.jpg";
        String savePath = getExternalFilesDir(null) + File.separator + "test_upload.jpg";
        ApiCommom downloadService = ServiceGenerator.createDownloadService(ApiCommom.class, this);
        observable = downloadService.downloadFile(url, savePath);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriber<File>() {
                    @Override
                    protected void onSuccess(File file) {
                        log.i("onResponse", "onSuccess--->-----下载结束--------file path:" + file.getPath());
                        pathTv.setText(file.getPath().toString());
                        Toast.makeText(UploadDownActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
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
        log.d(TAG, "onDownloadProgress--currentBytesCount=" + currentBytesCount + "---totalBytesCount=" + totalBytesCount + "--progress=" + progress + "%----是否完成下载=" + done);
        mProgressBar.setProgress(progress);
        progressBarTv.setText(progress + "%");
//        log.e(TAG, "onDownloadProgress--------》------thread=" + Thread.currentThread().getName());
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
        log.d(TAG, "onUploadProgress--currentBytesCount=" + currentBytesCount + "---totalBytesCount=" + totalBytesCount + "--progress=" + progress + "%----是否完成下载=" + done);
        progressBarUpload.setProgress(progress);
        progressBarUploadTv.setText(progress + "%");
//        log.e(TAG, "onUploadProgress--------》------thread=" + Thread.currentThread().getName());
    }
}
