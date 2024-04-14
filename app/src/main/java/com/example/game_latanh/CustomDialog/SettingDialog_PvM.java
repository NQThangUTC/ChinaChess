package com.example.game_latanh.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.game_latanh.HomeActivity;
import com.example.game_latanh.R;

public class SettingDialog_PvM extends Dialog implements RadioGroup.OnCheckedChangeListener {
    public Button posBtn, negBtn;
    public RadioGroup levelGroup;
    public RadioButton level_1, level_2, level_3;

    public int mLevel;

    public SettingDialog_PvM(Context context) {
        super(context, R.style.CustomDialog);

        mLevel = HomeActivity.setting.mLevel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting_pvm);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        if (mLevel == 1) {
            level_1.setChecked(true);
        } else if (mLevel == 2) {
            level_2.setChecked(true);
        } else {
            level_3.setChecked(true);
        }
        levelGroup.setOnCheckedChangeListener(this);
    }

    private void initView() {
        posBtn = (Button) findViewById(R.id.posBtn);
        negBtn = (Button) findViewById(R.id.negBtn);

        levelGroup = (RadioGroup) findViewById(R.id.levelGroup);
        level_1 = (RadioButton) findViewById(R.id.level_1);
        level_2 = (RadioButton) findViewById(R.id.level_2);
        level_3 = (RadioButton) findViewById(R.id.level_3);
    }


    private void initEvent() {
        posBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        negBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    public OnClickBottomListener onClickBottomListener;

    public SettingDialog_PvM setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        RadioButton checked = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioGroup.getId() == R.id.levelGroup) {
            if (checked.getId() == R.id.level_1) {
                mLevel = 1;
            } else if (checked.getId() == R.id.level_2) {
                mLevel = 2;
            } else {
                mLevel = 3;
            }
        }

    }

    public interface OnClickBottomListener {
        public void onPositiveClick();

        public void onNegtiveClick();
    }
}