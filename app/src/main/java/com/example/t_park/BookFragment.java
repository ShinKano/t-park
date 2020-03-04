package com.example.t_park;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


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

}
