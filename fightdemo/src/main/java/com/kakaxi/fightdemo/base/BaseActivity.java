package com.kakaxi.fightdemo.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kakaxi.fightdemo.utils.NetWorkUtil;
import com.kakaxi.fightdemo.utils.ScreenManager;

/**
 * Created by leixiaoliang on 2017/4/12.
 * 邮箱：lxliang1101@163.com
 */

public class BaseActivity extends AppCompatActivity {
    protected boolean isDestroy;
    private Toast mToast;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ScreenManager.getScreenManager().pushActivity(this, true);
    }

    /**
     * 显示toast
     */
    public void showToast(String msg) {
        if (isDestroy)
            return;
        // 不用getApplicationContext的话,导致内存泄露
        if (mToast == null) {
            mToast = Toast.makeText(this.getApplicationContext(), msg,
                    Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(msg);
            mToast.show();

        }
    }

    /**
     * 显示toast
     */
    public void showToast(int msg) {
        if(NetWorkUtil.isNetworkConnected(getApplicationContext())){
            showToast(getString(msg));
        }
    }

    @Override
    protected void onDestroy() {
        isDestroy = true;
        super.onDestroy();
        ScreenManager.getScreenManager().popActivity(this);
    }

}
