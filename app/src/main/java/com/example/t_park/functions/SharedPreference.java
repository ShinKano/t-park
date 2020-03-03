package com.example.t_park.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SharedPreference {

    public void saveUser(Context mContext, Bundle result) {
        SharedPreferences userInfo = mContext.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("id", result.getString("id"));
        editor.putString("name", result.getString("name"));
        editor.putString("sex", result.getString("sex"));
        editor.commit();

        String infoName = userInfo.getString("name", "");
        System.out.println(infoName + "はログイン状態です");
    }
}
