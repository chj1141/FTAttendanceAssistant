package com.example.chj.ftattendanceassistant.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.chj.ftattendanceassistant.R;

/**
 * Created by chenghj on 2018/10/12.
 * 实现APP上方的标题栏
 */

public class UserTitleBar extends LinearLayout {
    public UserTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.user_title_bar, this, true);
    }
}
