package com.example.t_park;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.t_park.functions.HttpRequest;
import com.example.t_park.functions.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ScheduleFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }


    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 親要素の取得
        final Context parentContext = getContext();
        // 画面要素の取得
        final Button toBookButton = view.findViewById(R.id.to_book_button);
        final ListView listView = (ListView) view.findViewById(R.id.schedule_list);


        // 予約画面に遷移ボタン
        toBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new BookFragment());
            }
        });


        //リクエスト用HashMapの作成
        final HashMap<String, String> map = new HashMap<String, String>() {
            { put("purpose", "getSchedule"); } // AsyncTaskの実行内容を指定
        };

        // 予約スケジュールをGETリクエスト
        HttpRequest httpRequest = new HttpRequest(new HttpRequest.AsyncTaskCallback() {
            // 非同期処理の前にやる事あれば書く
            public void preExecute() {

            }

            // 非同期処理完了後の処理
            public void postExecute(Bundle responseBundle) {
                if (responseBundle.getInt("code") == 200) {
                    // レスポンス内の予約一覧（文字列）を取得
                    String strJ = responseBundle.getString("book");

                    try { // 文字列をJSONオブジェクトにに変換
                        JSONArray jArray = new JSONArray(strJ);
                        // ListView展開のために配列化
                        JSONObject[] bookArray = new JSONObject[jArray.length()];
                        for (int i=0; i<jArray.length(); i++){
                            bookArray[i] = jArray.getJSONObject(i);
                        }
                        // AdapterでListViewに展開
                        BookAdapter adapter = new BookAdapter(parentContext, R.layout.listitem_schedule, bookArray);
                        listView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    // レスポンスにエラーメッセージが含まれる場合はログに出力する
                    System.out.println(responseBundle.getString("errorMessage"));
                }
            }

            // キャンセル時にやる事あれば書く
            public void cancel() {

            }
        });
        httpRequest.execute(map);
    }


    // MainActivityからFragment切り替えを呼び出す
    private void replaceFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(fragment);
    }


    //ArrayAdapterを継承したクラス
    class BookAdapter extends ArrayAdapter<JSONObject> {
        Context context;

        public BookAdapter(Context context, int resource, JSONObject[] objects) {
            super(context, resource, objects);
            this.context = context;
        }

        //リストの行が生成されるたびにListViewから呼ばれる
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem_schedule, parent, false);
            }

            JSONObject book = (JSONObject) getItem(position);

            TextView tvName = (TextView) convertView.findViewById(R.id.samplelist_text1);
            TextView tvStart = (TextView) convertView.findViewById(R.id.samplelist_text2);
            TextView tvEnd = (TextView) convertView.findViewById(R.id.samplelist_text3);

            try {
                tvName.setText(book.getString("id"));
                tvStart.setText(book.getString("startTime"));
                tvEnd.setText(book.getString("endTime"));


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }


}
