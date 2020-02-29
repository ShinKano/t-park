package com.example.t_park.functions;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpRequest extends AsyncTask<HashMap<String,String>, Void, Void> {

    // RequestBodyインスタンスの作成に必要
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Override // doInBackgroundの引数でなぜか一回Arrayに入る
    protected Void doInBackground(HashMap<String,String>... map) {
        System.out.println(map[0].get("purpose"));
        try {
            switch (map[0].get("purpose")){
                case "login":
                    getGET(map[0]);
                    break;
                case "register":
                    System.out.println("登録の処理します");
                    break;
                default:
                    System.out.println("エラー");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    // =======リクエスト用関数の記述=======

    private Void getGET(HashMap<String,String> map) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000/api/users/auth/";
        // マップからJSONに変換
        JSONObject json = new JSONObject(map);
        System.out.println(json);
        RequestBody requestBody = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        Response response = call.execute();
        ResponseBody body = response.body();
        System.out.println(body);
        System.out.println(response.toString());
        System.out.println(response.code());

        return null;
    }

}


