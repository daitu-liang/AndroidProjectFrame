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

    private Context context;
    public ApiSubscriber() {
    }
    public ApiSubscriber( Context context) {

        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context,  true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        showProgressDialog();
        log.e("","onStart-CanlSubscriberror="+ this.isDisposed());
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
        log.e("","onNext-CanlSubscriberror="+ this.isDisposed());
    }
    protected abstract void onSuccess(T bean);

    @Override
    public void onError(Throwable e) {

        log.e("","onError---getContext="+ FightApplication.getContext());

        if (e instanceof SocketTimeoutException) {
            Toast.makeText(FightApplication.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(FightApplication.getContext(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(FightApplication.getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
        Toast.makeText(FightApplication.getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        log.e("","onError="+ e.getMessage());
        log.e("","onError-CanlSubscriberror="+ this.isDisposed());
        doCanlSubscribe();
    }

    @Override
    public void onComplete() {
        log.e("","onComplete-CanlSubscriberror="+ this.isDisposed());
        dismissProgressDialog();
        doCanlSubscribe();
    }

    /**
     * 断开上下流，解绑
     */
    public void doCanlSubscribe(){
        if(!this.isDisposed()){
            this.dispose();
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