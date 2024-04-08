package com.example.game_latanh.CustomDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.game_latanh.R;

public class OnlyReadDialog extends Dialog {
    public Button posBtn;
    public TextView tv_title, tv_content;
    public String title, content;

    public OnlyReadDialog(Context context, String title, String content) {
        super(context, R.style.CustomDialog);
        this.title = title;
        this.content = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_onlyread);
        setCanceledOnTouchOutside(false);
        initView();
        initEvent();
    }

    private void initView() {
        posBtn = (Button) findViewById(R.id.posBtn);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText(content);

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
    }

    public OnClickBottomListener onClickBottomListener;

    public OnlyReadDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        public void onPositiveClick();
    }
}
