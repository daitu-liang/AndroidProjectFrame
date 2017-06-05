package com.kakaxi.fightdemo.app;

import android.app.Application;
import android.content.Context;

import com.kakaxi.fightdemo.utils.PreferencesManager;

/**
 * Created by leixiaoliang on 2017/4/11.
 * 邮箱：lxliang1101@163.com
 */

public class FightApplication extends Application {
    public static Context CONTEXT;
    public static PreferencesManager preferenceManager;
    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
        init();
    }
    private void init() {
        preferenceManager = PreferencesManager.getInstance(this);

    }
    private static void setContext(Context mContext) {
        CONTEXT = mContext;
    }

    public static Context getContext() {
        return CONTEXT;
    }
    public static PreferencesManager getPreferenceManager() {
        return preferenceManager;
    }
}
