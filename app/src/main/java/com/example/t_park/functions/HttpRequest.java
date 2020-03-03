package com.example.t_park.functions;

import android.os.AsyncTask;

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


public class HttpRequest extends AsyncTask<HashMap<String, String>, Void, JSONObject> {

    // =======Activityへのコールバック用interface=======
    public interface AsyncTaskCallback {
        void preExecute();
        void postExecute(JSONObject responseJSON);
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
    protected void onPostExecute(JSONObject responseJSON) {
        super.onPostExecute(responseJSON);
        callback.postExecute(responseJSON);
    }

    // ======================================




    @Override // doInBackgroundの引数でなぜか一回Arrayに入る
    protected JSONObject doInBackground(HashMap<String,String>... map ) {

        JSONObject responseJSON = null;

        try {
            // purposeによってAsyncTaskの実行内容を分岐
            switch (map[0].get("purpose")){

                case "login":
                    responseJSON = postRequest(map[0], "http://10.0.2.2:3000/api/users/auth/");
                    break;

                case "register":
                     responseJSON =postRequest(map[0], "http://10.0.2.2:3000/api/users/");
                    break;

                default:
                    System.out.println("エラー");
                    return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseJSON;
    }



    // =======リクエスト用関数の記述=======

    // RequestBodyインスタンスの作成に必要
    private final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    // POSTリクエスト：POSTボディに含めるHashMapとURLを引数にとる
    private JSONObject postRequest(HashMap<String,String> map ,String url) throws IOException {
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

        return responseJSON;
    }

}


