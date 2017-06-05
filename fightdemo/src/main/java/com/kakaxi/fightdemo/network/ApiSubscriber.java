package com.kakaxi.fightdemo.network;

import android.content.Context;
import android.widget.Toast;

import com.kakaxi.fightdemo.app.FightApplication;
import com.kakaxi.fightdemo.utils.Logger;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by leixiaoliang on 2017/1/6.
 */
public abstract class ApiSubscriber<T> extends DisposableObserver<T> {
    private Logger log = Logger.getLogger("ApiSubscriber");

    private ProgressDialogHandler mProgressDialogHandler;
    private boolean showDialog = true;
    private Context context;
    public ApiSubscriber() {
        showDialog=false;
    }
    public ApiSubscriber(Context context) {
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context,  true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(showDialog){
            showProgressDialog();
        }
        log.e("","onStart-ApiSubscriberr="+ this.isDisposed()+"-showDialog--"+showDialog);
    }

    @Override
    public void onNext(T t) {
        log.e("","onNext-ApiSubscriber="+ this.isDisposed());
        onSuccess(t);

    }
    protected abstract void onSuccess(T bean);

    @Override
    public void onError(Throwable e) {

        log.e("","onError---ApiSubscriber="+ e.getMessage());

        if (e instanceof SocketTimeoutException) {
            Toast.makeText(FightApplication.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(FightApplication.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FightApplication.getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
//        Toast.makeText(FightApplication.getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        log.e("","onError-CanlSubscriberror="+ this.isDisposed());
        doCanlSubscribe();
    }

    @Override
    public void onComplete() {
        log.e("","onComplete-CanlSubscriberror="+ this.isDisposed()+"-showDialog--"+showDialog);
        if(showDialog){
            dismissProgressDialog();
        }

        doCanlSubscribe();
    }

    /**
     * 断开上下流，解绑
     */
    public void doCanlSubscribe(){
        if(!this.isDisposed()){
            this.dispose();
            log.e("","doCanlSubscribe="+ this.isDisposed());
        }
    }

    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
//            mProgressDialogHandler.removeCallbacksAndMessages(null);
            mProgressDialogHandler = null;
        }
    }
}