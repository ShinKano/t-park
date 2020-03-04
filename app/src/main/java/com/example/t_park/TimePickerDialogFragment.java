package com.example.t_park;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimePickerDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private final TextView targetTextView;

    public TimePickerDialogFragment(TextView textView) {
        this.targetTextView = textView;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, true);
        return timePickerDialog;
    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Date date = new Date();   // 本日の日付を取得
        date.setHours(hourOfDay); // ピッカーから時間をセット
        date.setMinutes(minute);  // ピッカーから分をセット
        // 文字列に変換してフォームに入力
        String strTime = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date);
        targetTextView.setText(strTime);
    }
}
