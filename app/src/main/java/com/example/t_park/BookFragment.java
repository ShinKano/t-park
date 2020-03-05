package com.example.t_park;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.t_park.functions.HttpRequest;
import com.example.t_park.functions.SharedPreference;

import java.util.HashMap;


public class BookFragment extends Fragment {


    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 親要素の取得
        final Context parentContext = getContext();
        // 画面要素の取得
        final EditText startTimeET = view.findViewById(R.id.input_start_time);
        final EditText endTimeET = view.findViewById(R.id.input_end_time);
        final Button bookButton = view.findViewById(R.id.book_button);
        final Button toScheduleButton = view.findViewById(R.id.to_schedule_button);

        // startTime入力フォームにフォーカスで発火
        startTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setTimeFromTimePicker(startTimeET);
            }
        });

        // endTime入力フォームにフォーカスで発火
        endTimeET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) setTimeFromTimePicker(endTimeET);
            }
        });

        toScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ScheduleFragment());
            }
        });

        // 予約ボタンの処理
        bookButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 入力データの取得
                final String startTime    = editTextToString(startTimeET);
                final String endTime = editTextToString(endTimeET);

                // ログインユーザー情報の取得
                final Bundle userInfoBundle = new SharedPreference().getUserInfo(parentContext);
                final String userId = userInfoBundle.getString("id");
                final String userName = userInfoBundle.getString("name");

                //HashMapの作成
                final HashMap<String, String> map = new HashMap<String, String>() {
                    { put("startTime", startTime);
                      put("endTime",   endTime);
                      put("userId",    userId);
                      put("userName",  userName);
                      put("purpose",   "book");  } // AsyncTaskの実行内容を指定
                };

                // 非同期処理の実行
                HttpRequest httpRequest = new HttpRequest(new HttpRequest.AsyncTaskCallback() {
                    // 非同期処理の前にやる事あれば書く
                    public void preExecute() {

                    }
                    // 非同期処理完了後の処理
                    public void postExecute(Bundle responseBundle) {
                        if (responseBundle.getInt("code") == 200) {
                            // 予約完了ダイアログの表示
                            AlertDialog.Builder builder = new AlertDialog.Builder(parentContext);
                            builder.setMessage("予約が完了しました！");
                            builder.show();
                            // 予約一覧画面に遷移
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
        }));

    }

    // MainActivityからFragment切り替えを呼び出す
    private void replaceFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(fragment);
    }

    // MainActivityからTimePickerを呼び出す（入力フォームを引数に）
    private void setTimeFromTimePicker(EditText editText) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showTimePicker(editText);
    }

    private String editTextToString(EditText editText) {
        return editText.getText().toString();
    }

}
