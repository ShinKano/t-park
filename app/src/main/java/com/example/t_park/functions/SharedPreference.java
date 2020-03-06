package com.example.t_park.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


public class SharedPreference {

    public void saveUser(Context context, Bundle responseBundle) {
        SharedPreferences userInfo = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("id", responseBundle.getString("id"));
        editor.putString("name", responseBundle.getString("name"));
        editor.putString("sex", responseBundle.getString("sex"));
        editor.commit();

        String infoName = userInfo.getString("name", "");
        System.out.println(infoName + "はログイン状態です");
    }


    public Bundle getUserInfo(Context context) {
        SharedPreferences userInfo = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        Bundle userInfoBundle = new Bundle();
        userInfoBundle.putString("id", userInfo.getString("id", ""));
        userInfoBundle.putString("name", userInfo.getString("name", ""));
        // userInfoBundle.putString("sex", userInfo.getString("sex", ""));
        return userInfoBundle;
    }

    public void deleteUserInfo(Context context) {
        SharedPreferences userInfo = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.remove("id");
        editor.remove("name");
        editor.remove("sex");
        editor.commit();
    }
}
