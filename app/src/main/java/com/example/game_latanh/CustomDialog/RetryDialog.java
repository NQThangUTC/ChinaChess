package com.example.game_latanh.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.game_latanh.R;

import static com.example.game_latanh.HomeActivity.playEffect;
import static com.example.game_latanh.HomeActivity.selectMusic;

public class RetryDialog extends Dialog implements RadioGroup.OnCheckedChangeListener {
    public Button posBtn, negBtn;
    public RadioGroup holdGroup;
    public RadioButton holdRed, holdBlack;

    public boolean isPlayerRed;

    public RetryDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_retry);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
        isPlayerRed = true;
        holdRed.setChecked(true);
        holdGroup.setOnCheckedChangeListener(this);
    }

    private void initView() {
        posBtn = (Button) findViewById(R.id.posBtn);
        negBtn = (Button) findViewById(R.id.negBtn);

        holdGroup = (RadioGroup) findViewById(R.id.holdGroup);
        holdRed = (RadioButton) findViewById(R.id.holdRed);
        holdBlack = (RadioButton) findViewById(R.id.holdBlack);
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

    public RetryDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        playEffect(selectMusic);
        RadioButton checked = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        if (radioGroup.getId() == R.id.holdGroup) {
            if (checked.getId() == R.id.holdRed) {
                isPlayerRed = true;
            } else {
                isPlayerRed = false;
            }
        }

    }

    public interface OnClickBottomListener {
        public void onPositiveClick();

        public void onNegtiveClick();
    }
}
