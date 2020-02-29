package com.example.t_park.functions;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HttpRequest extends AsyncTask< HashMap<String,String>, Void, Void > {

    // RequestBodyインスタンスの作成に必要
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    @Override // doInBackgroundの引数でなぜか一回Arrayに入る
    protected Void doInBackground( HashMap<String,String>... map ) {
        System.out.println(map[0].get("purpose")); // AsyncTaskの実行内容を指定
        try {
            switch (map[0].get("purpose")){

                case "login":
                    postRequest(map[0], "http://10.0.2.2:3000/api/users/auth/");
                    break;

                case "register":
                    postRequest(map[0], "http://10.0.2.2:3000/api/users/");
                    break;

                default: System.out.println("エラー");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    // =======リクエスト用関数の記述=======

    private Void postRequest(HashMap<String,String> map ,String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        // マップからJSONに変換
        JSONObject json = new JSONObject(map);
        // リクエストボディにJSONを追加
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


