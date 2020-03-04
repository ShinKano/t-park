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

    // フラグメントインスタンス
    Fragment loginFragment = new LoginFragment();
    Fragment registerFragment = new RegisterFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 条件に合ったFragmentを設定
        replaceFragment(isLoginPage ? loginFragment :registerFragment);

    }


    // フラグメントの切り替え（呼び出し先のフラグメントでも呼び出されるので注意！）
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
