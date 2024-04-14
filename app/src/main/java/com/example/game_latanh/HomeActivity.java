package com.example.game_latanh;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.game_latanh.CustomDialog.CommonDialog;
import com.example.game_latanh.Info.Setting;
import com.example.game_latanh.Info.Zobrist;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public Button btn_pvm;
    public Button btn_pvp;
    public static Setting setting;
    public static SharedPreferences sharedPreferences;

    public static Zobrist zobrist;

    public static long curClickTime = 0L;
    public static long lastClickTime = 0L;
    public static final int MIN_CLICK_DELAY_TIME = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getPermission();
        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);

        zobrist = new Zobrist();

        setting = new Setting(sharedPreferences);

        btn_pvm = (Button) findViewById(R.id.btn_pvm);
        btn_pvm.setOnClickListener(this);

        btn_pvp = (Button) findViewById(R.id.btn_pvp);
        btn_pvp.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        curClickTime = lastClickTime;
        Intent intent;
        int viewId = view.getId();
        if (viewId == R.id.btn_pvm) {
            intent = new Intent(HomeActivity.this, PvMActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.btn_pvp) {
            intent = new Intent(HomeActivity.this, PvPActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        curClickTime = lastClickTime;

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final CommonDialog backDialog = new CommonDialog(this, "Thoát", "Bạn có chắc chắn muốn thoát không");
            backDialog.setOnClickBottomListener(new CommonDialog.OnClickBottomListener() {

                @Override
                public void onPositiveClick() {
                    backDialog.dismiss();
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    backDialog.dismiss();
                }
            });
            backDialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void getPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},999);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},999);
        }
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
