package com.monsordi.gotravel;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by diego on 17/04/18.
 */

public class MyPreference {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final int PRIVATE_MODE = 0;
    public static final String USER_NAME = "name";
    public static final String ID = "id";
    public static final String ORDER_ID = "order id";
    public static final String TOKEN = "token";
    public static final String IS_FIRST_TIME = "isFirstTime";
    public static final String PREF_NAME = "preferences";

    public MyPreference(Context context) {
        this.context = context;
        this.preferences = this.context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        this.editor = preferences.edit();
    }

    public boolean isFirsTime(){
        return preferences.getBoolean(IS_FIRST_TIME,true);
    }

    public void setOld(boolean b){
        if(b){
            editor.putBoolean(IS_FIRST_TIME,false);
            editor.commit();
        } else {
            editor.putBoolean(IS_FIRST_TIME,true);
            editor.commit();
        }
    }

    public void setId(Long id){
        editor.putLong(ID,id);
        editor.commit();
    }

    public Long getId(){
        return preferences.getLong(ID,-1);
    }

    public void setOrderId(Long id){
        editor.putLong(ORDER_ID,id);
        editor.commit();
    }

    public Long getOrderId(){
        return preferences.getLong(ORDER_ID,-1);
    }


    public void setToken(String token){
        editor.putString(TOKEN,token);
        editor.commit();
    }

    public String getToken(){
        return preferences.getString(TOKEN,"");
    }

    public void setUserName(String name){
        editor.putString(USER_NAME,name);
        editor.commit();
    }

    public String getUserName(){
        return preferences.getString(USER_NAME,"");
    }
}
