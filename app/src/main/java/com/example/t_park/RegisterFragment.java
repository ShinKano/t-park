package com.example.t_park;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.t_park.functions.HttpRequest;
import com.example.t_park.functions.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.invoke.ConstantCallSite;
import java.util.HashMap;


// Fragmentクラスを継承します
public class RegisterFragment extends Fragment {

    // Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 要素の取得
        final EditText emailET = view.findViewById(R.id.input_email);
        final EditText passwordET = view.findViewById(R.id.input_password);
        final EditText nameET = view.findViewById(R.id.input_name);
        final Spinner sexSpinner = view.findViewById(R.id.input_sex);
        final Button registerButton = view.findViewById(R.id.register_button);

        // Buttonのクリックした時の処理
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 要素を取得
                final Context parentContext = getContext();
                final String email    = editTextToString(emailET);
                final String password = editTextToString(passwordET);
                final String name     = editTextToString(nameET);
                final String sex      = (String)sexSpinner.getSelectedItem();

                // HashMapの作成
                final HashMap<String, String> map = new HashMap<String, String>() {
                    { put("mail",     email);
                      put("password", password);
                      put("name",     name);
                      put("sex",      sex);
                      put("purpose",  "register"); } // AsyncTaskの実行内容を指定
                };
                // 非同期処理の実行
                HttpRequest httpRequest = new HttpRequest(new HttpRequest.AsyncTaskCallback() {
                    // 非同期処理前
                    public void preExecute() {

                    }

                    // 非同期処理完了後の処理
                    public void postExecute(Bundle responseBundle) {
                        if (responseBundle.getInt("code") == 200) {
                            new SharedPreference().saveUser(parentContext, responseBundle);
                            replaceFragment(new LoginFragment());
                        } else {
                            // レスポンスにエラーメッセージが含まれる場合はログに出力する
                            System.out.println(responseBundle.getString("errorMessage"));
                        }
                    }

                    // キャンセル時の処理
                    public void cancel() {

                    }
                });
                httpRequest.execute(map);
            }
        });

    }


    private String editTextToString(EditText editText) {
        return editText.getText().toString();
    }

    // MainActivityからFragment切り替えを呼び出す
    private void replaceFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.replaceFragment(fragment);
    }
}