package com.example.t_park;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // 画面ステータス
    boolean isLoginPage = true; // ? ログイン:レジスター
    Context context = this;
    // 画面の要素
    Button loginOrRegisterButton;
    // フラグメントインスタンス
    Fragment loginFragment = new LoginFragment();
    Fragment registerFragment = new RegisterFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ボタン要素の取得
        loginOrRegisterButton = findViewById(R.id.login_register);


        // 条件に合ったFragmentとButtonテキストを設定
        replaceFragment(isLoginPage ? loginFragment :registerFragment);
        loginOrRegisterButton.setText(isLoginPage ? R.string.新規登録はこちら : R.string.ログインはこちら);


        // ログインと登録画面の切り替えボタン
        loginOrRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginPage = switchBoolean(isLoginPage); //画面ステータス切り替え
                loginOrRegisterButton.setText(isLoginPage ? R.string.新規登録はこちら : R.string.ログインはこちら);
                replaceFragment(isLoginPage ? loginFragment :registerFragment);//フラグメント切り替え
            }
        });
    }




    // フラグメントの切り替え
    public void replaceFragment(Fragment fragment) {
        // FragmentTransactionを生成して処理を開始
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    // 画面ステータスの切り替え
    public Boolean switchBoolean(Boolean status) {
        return !status;
    }


}
