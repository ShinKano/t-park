package com.example.t_park;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;

import com.example.t_park.functions.SharedPreference;

public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Splashから通常Themeに戻す
        setContentView(R.layout.activity_main);

        // SharedPreferenceにデータがあるかどうかでログインと登録画面どちらかに遷移
        Bundle sharedPreference = new SharedPreference().getUserInfo(this);
        replaceFragment(sharedPreference.getString("id").isEmpty()
                ? new RegisterFragment()
                : new ScheduleFragment()
        );
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


}
