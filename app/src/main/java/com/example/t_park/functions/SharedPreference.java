package com.example.t_park.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;


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
}
