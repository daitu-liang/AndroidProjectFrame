package com.kakaxi.fightdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by leixiaoliang  ddd on 2016/7/28.
 */
public abstract class BasePreferencesManager {

    private static final String FILE_NAME = "app_fight_preferences";

    protected SharedPreferences settings;
    protected SharedPreferences.Editor editor;

    public BasePreferencesManager(Context context) {
        context = context.getApplicationContext();
        settings = context
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void clear(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
    
    public boolean getBooleanValue(String key, boolean defValue) {
    	return settings.getBoolean(key, defValue);
    }

    protected boolean getBoolean(String key, boolean defValue) {
        return settings.getBoolean(key, defValue);
    }

    protected float getFloat(String key, float defValue) {
        return settings.getFloat(key, defValue);
    }

    protected int getInt(String key, int defValue) {
        return settings.getInt(key, defValue);
    }

    protected long getLong(String key, long defValue) {
        return settings.getLong(key, defValue);
    }

    protected String getString(String key, String defValue) {
        return settings.getString(key, defValue);
    }



   public abstract void logout();
}
