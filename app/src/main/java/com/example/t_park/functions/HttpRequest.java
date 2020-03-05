package com.example.t_park.functions;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
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


public class HttpRequest extends AsyncTask<HashMap<String, String>, Void, Bundle> {

    // =======Activityへのコールバック用interface=======
    public interface AsyncTaskCallback {
        void preExecute();
        void postExecute(Bundle responseBundle);
        void cancel();
    }

    private AsyncTaskCallback callback = null;

    public HttpRequest(AsyncTaskCallback _callback) {
        this.callback = _callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callback.preExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        callback.cancel();
    }

    @Override
    protected void onPostExecute(Bundle responseBundle) {
        super.onPostExecute(responseBundle);
        callback.postExecute(responseBundle);
    }

    // ======================================




    @Override // doInBackgroundの引数でなぜか一回Arrayに入る
    protected Bundle doInBackground(HashMap<String,String>... map ) {

        Bundle responseBundle = null;

        try {
            // purposeによってAsyncTaskの実行内容を分岐
            switch (map[0].get("purpose")){

                case "login":
                    responseBundle = postRequest(map[0], "http://10.0.2.2:3000/api/users/auth/");
                    break;

                case "register":
                    responseBundle = postRequest(map[0], "http://10.0.2.2:3000/api/users/");
                    break;

                case "book":
                    responseBundle = postRequest(map[0], "http://10.0.2.2:3000/api/reserve");
                    break;

                case "getSchedule":
                    responseBundle = getRequest("http://10.0.2.2:3000/api/reserve");
                    break;

                default:
                    System.out.println("エラーだよ");
                    return null;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return responseBundle;
    }



    // =======リクエスト用関数の記述=======

    // RequestBodyインスタンスの作成に必要
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // POSTリクエスト：POSTボディに含めるHashMapとURLを引数にとる
    private Bundle postRequest(HashMap<String,String> map , String url) throws IOException, JSONException {
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
        // Callの実行
        Response response = call.execute();
        ResponseBody body = response.body();
        // ResponseをJSONに変換
        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(body.string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ログの出力
        System.out.println(responseJSON);
        System.out.println(response.toString());
        System.out.println(response.code());

        Bundle responseBundle = new Bundle();
        if (response.code() == 200) {
            responseBundle.putInt("code", response.code());
            responseBundle.putString("id", responseJSON.getString("id"));
            responseBundle.putString("name", responseJSON.has("name")
                    ? responseJSON.getString("name")
                    : responseJSON.getString("userName"));
            responseBundle.putString("sex", responseJSON.has("sex")
                    ? responseJSON.getString("sex")
                    : "No need when booking");
        } else {
            responseBundle.putInt("code", response.code());
            responseBundle.putString("errorMessage", responseJSON.getString("errorMessage"));
        }
        System.out.println(responseBundle);
        return responseBundle;
    }


    // GETリクエスト
    private Bundle getRequest(String url) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);
        // Callの実行
        Response response = call.execute();
        ResponseBody body = response.body();
        // ResponseをJSONに変換
        JSONArray arrayResponseJSON = null;
        try {
            arrayResponseJSON = new JSONArray(body.string());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ログの出力
        System.out.println("これはhttpの中のログ" + arrayResponseJSON);


        Bundle responseBundle = new Bundle();
        if (response.code() == 200) {
            responseBundle.putInt("code", response.code());
            responseBundle.putString("book", arrayResponseJSON.toString());

        } else {
            responseBundle.putInt("code", response.code());

        }
        System.out.println("これが最終的なReturn:" + responseBundle);
        return responseBundle;
    }






}


