package com.example.t_park;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


@RequiresApi(api = Build.VERSION_CODES.O)
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
        final Button logOutButton = view.findViewById(R.id.logout_button);
        final ListView listView = (ListView) view.findViewById(R.id.schedule_list);


        // ログアウトボタンの処理
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharedPreference().deleteUserInfo(parentContext);
                replaceFragment(new LoginFragment());
            }
        });


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
                        jArray = sortJArray(jArray);
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

            final TextView tvName = (TextView) convertView.findViewById(R.id.samplelist_text1);
            TextView tvStart = (TextView) convertView.findViewById(R.id.samplelist_text2);
            TextView tvEnd = (TextView) convertView.findViewById(R.id.samplelist_text3);


            try {
                tvName.setText(book.getString("userName"));
                tvStart.setText(book.getString("startTime").replace("2020-", ""));
                tvEnd.setText(book.getString("endTime").replace("2020-", ""));

                // 予約のidとSharedPreferenceのidが一致したら色づけする
                if (book.getString("userId")
                        .equals(new SharedPreference().getUserInfo(getContext()).getString("id"))) {
                    tvName.setTextColor(Color.RED);
                    tvStart.setTextColor(Color.RED);
                    tvEnd.setTextColor(Color.RED);
                    tvName.setText(book.getString("userName") + "  🗑");

                    // ログインユーザーは自分の予約を削除できる
                    final String targetId = book.getString("id");
                    tvName.setClickable(true);
                    tvName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //リクエスト用HashMapの作成
                            final HashMap<String, String> map = new HashMap<String, String>() {
                                {   put("targetId", targetId);
                                    put("purpose", "deleteSchedule"); } // AsyncTaskの実行内容を指定
                            };
                            //
                            HttpRequest httpRequest = new HttpRequest(new HttpRequest.AsyncTaskCallback() {
                                // 非同期処理の前にやる事あれば書く
                                public void preExecute() {
                                }

                                // 非同期処理完了後の処理
                                public void postExecute(Bundle responseBundle) {
                                    if (responseBundle.getInt("code") == 204) {
                                        System.out.println("削除完了");
                                        replaceFragment(new ScheduleFragment());
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
                    });

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }


//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");
//    LocalDateTime now = LocalDateTime.now();
//    LocalDateTime time = LocalDateTime.parse("2019-01-01 11:22:33,444", formatter);

    public JSONArray sortJArray(JSONArray jsonArray) throws JSONException {

        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonValues.add(jsonArray.getJSONObject(i));
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private static final String KEY_NAME = "startTime";

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();
                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                }
                catch (JSONException e) {
                    //do something
                }
                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });
        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

}
