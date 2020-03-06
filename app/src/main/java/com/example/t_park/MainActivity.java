package com.example.t_park;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // 画面ステータス
    boolean isLoginPage = true; // ? ログイン:レジスター

    // フラグメントインスタンス
    Fragment loginFragment = new LoginFragment();
    Fragment registerFragment = new RegisterFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
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

    // TimePickerを表示する（呼び出し先のフラグメントでも呼び出されるので注意！）
    public void showTimePicker(EditText editText) {
        TimePickerDialogFragment timePicker = new TimePickerDialogFragment(editText);
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    // 画面ステータスの切り替え
    public Boolean switchBoolean(Boolean status) {
        return !status;
    }


}
