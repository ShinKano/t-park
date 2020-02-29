package com.example.t_park;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.t_park.functions.HttpRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment {

    // Fragmentで表示するViewを作成するメソッド
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    // Viewが生成し終わった時に呼ばれるメソッド
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 要素の取得
        final EditText emailET = view.findViewById(R.id.input_email);
        final EditText passwordET = view.findViewById(R.id.input_password);

        Button moveToRegisterButton = view.findViewById(R.id.login_button);
        // Buttonのクリックした時の処理を書きます
        moveToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // フォーム内の値を取得
                final String email    = editTextToString(emailET);
                final String password = editTextToString(passwordET);
                //HashMapの作成
                final HashMap<String, String> map = new HashMap<String, String>() {
                    { put("mail",     email);
                      put("password", password);
                      put("purpose", "login"); }
                };

                new HttpRequest().execute(map);
            }
        });

    }



    public String editTextToString(EditText editText) {
        return editText.getText().toString();
    }





}