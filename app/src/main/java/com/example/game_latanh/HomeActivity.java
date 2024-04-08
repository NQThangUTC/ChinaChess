package com.example.game_latanh;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.game_latanh.AICore.KnowledgeBase;
import com.example.game_latanh.AICore.TransformTable;
import com.example.game_latanh.CustomDialog.CommonDialog;
import com.example.game_latanh.CustomDialog.OnlyReadDialog;
import com.example.game_latanh.CustomDialog.SettingDialog;
import com.example.game_latanh.Info.ChessInfo;
import com.example.game_latanh.Info.InfoSet;
import com.example.game_latanh.Info.SaveInfo;
import com.example.game_latanh.Info.Setting;
import com.example.game_latanh.Info.Zobrist;
import com.example.game_latanh.Utils.PermissionUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public Button btn_pvm;
    public Button btn_pvp;
    public Button btn_help;
    public Button btn_about;
    public Button btn_setting;

    public static Setting setting;

    public static MediaPlayer backMusic;
    public static MediaPlayer selectMusic;
    public static MediaPlayer clickMusic;
    public static MediaPlayer checkMusic;
    public static MediaPlayer winMusic;

    public static SharedPreferences sharedPreferences;

    public static Zobrist zobrist;

    public static long curClickTime = 0L;
    public static long lastClickTime = 0L;
    public static final int MIN_CLICK_DELAY_TIME = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //PermissionUtils.isGrantExternalRW(this, 1);
        getPermission();
        initMusic();

        sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);

        zobrist = new Zobrist();

        setting = new Setting(sharedPreferences);

        btn_pvm = (Button) findViewById(R.id.btn_pvm);
        btn_pvm.setOnClickListener(this);

        btn_pvp = (Button) findViewById(R.id.btn_pvp);
        btn_pvp.setOnClickListener(this);

        btn_help = (Button) findViewById(R.id.btn_help);
        btn_help.setOnClickListener(this);

        btn_about = (Button) findViewById(R.id.btn_about);
        btn_about.setOnClickListener(this);

        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(this);
    }

    public void initMusic() {
        backMusic = MediaPlayer.create(this, R.raw.background);
        backMusic.setLooping(true);
        backMusic.setVolume(0.2f, 0.2f);
        selectMusic = MediaPlayer.create(this, R.raw.select);
        selectMusic.setVolume(5f, 5f);
        clickMusic = MediaPlayer.create(this, R.raw.click);
        clickMusic.setVolume(5f, 5f);
        checkMusic = MediaPlayer.create(this, R.raw.checkmate);
        checkMusic.setVolume(5f, 5f);
        winMusic = MediaPlayer.create(this, R.raw.win);
        winMusic.setVolume(5f, 5f);
    }

    public static void playMusic(MediaPlayer mediaPlayer) {
        if (setting.isMusicPlay) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
        }
    }

    public static void stopMusic(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
    }

    public static void playEffect(MediaPlayer mediaPlayer) {
        if (setting.isEffectPlay) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            }
            mediaPlayer.start();
        }
    }

    @Override
    public void onClick(View view) {
        lastClickTime = System.currentTimeMillis();
        if (lastClickTime - curClickTime < MIN_CLICK_DELAY_TIME) {
            return;
        }
        curClickTime = lastClickTime;
        Intent intent;
        playEffect(selectMusic);
        int viewId = view.getId();
        if (viewId == R.id.btn_pvm) {
            intent = new Intent(HomeActivity.this, PvMActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.btn_pvp) {
            intent = new Intent(HomeActivity.this, PvPActivity.class);
            startActivity(intent);
        } else if (viewId == R.id.btn_help) {
            final OnlyReadDialog helpDialog = new OnlyReadDialog(this, "Trợ giúp", "Game Cờ Tướng này có 2 lựa chọn chơi\nlà chơi người với người và người với máy.\nĐể chơi vui lòng tham khảo\nluật chơi Cờ Tướng");
            helpDialog.setOnClickBottomListener(new OnlyReadDialog.OnClickBottomListener() {

                @Override
                public void onPositiveClick() {
                    playEffect(selectMusic);
                    helpDialog.dismiss();
                }
            });
            helpDialog.show();
        } else if (viewId == R.id.btn_about) {
            final OnlyReadDialog aboutDialog = new OnlyReadDialog(this, "Thông tin", "3 con cừu");
            aboutDialog.setOnClickBottomListener(new OnlyReadDialog.OnClickBottomListener() {

                @Override
                public void onPositiveClick() {
                    playEffect(selectMusic);
                    aboutDialog.dismiss();
                }
            });
            aboutDialog.show();
        } else if (viewId == R.id.btn_setting) {
            final SettingDialog settingDialog = new SettingDialog(this);
            settingDialog.setOnClickBottomListener(new SettingDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    playEffect(selectMusic);
                    Editor editor = sharedPreferences.edit();
                    boolean flag = false;
                    if (setting.isMusicPlay != settingDialog.isMusicPlay) {
                        setting.isMusicPlay = settingDialog.isMusicPlay;
                        if (setting.isMusicPlay) {
                            playMusic(backMusic);
                        } else {
                            stopMusic(backMusic);
                        }
                        editor.putBoolean("isMusicPlay", settingDialog.isMusicPlay);
                        flag = true;
                    }
                    if (setting.isEffectPlay != settingDialog.isEffectPlay) {
                        setting.isEffectPlay = settingDialog.isEffectPlay;
                        editor.putBoolean("isEffectPlay", settingDialog.isEffectPlay);
                        flag = true;
                    }
                    if (flag) {
                        editor.commit();
                    }
                    settingDialog.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    playEffect(selectMusic);
                    settingDialog.dismiss();
                }
            });
            settingDialog.show();
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
                    playEffect(selectMusic);
                    backDialog.dismiss();
                    finish();
                }

                @Override
                public void onNegtiveClick() {
                    playEffect(selectMusic);
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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Log.e("chen", "获取存储权限成功");
//
//                    Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("isMusicPlay", true);
//                    editor.putBoolean("isEffectPlay", true);
//                    editor.putBoolean("isPlayerRed", true);
//                    editor.putInt("mLevel", 2);
//                    editor.commit();
//                    try {
//                        SaveInfo.SerializeChessInfo(new ChessInfo(), "ChessInfo_pvp.bin");
//                        SaveInfo.SerializeInfoSet(new InfoSet(), "InfoSet_pvp.bin");
//                        SaveInfo.SerializeChessInfo(new ChessInfo(), "ChessInfo_pvm.bin");
//                        SaveInfo.SerializeInfoSet(new InfoSet(), "InfoSet_pvm.bin");
//                        SaveInfo.SerializeKnowledgeBase(new KnowledgeBase(), "KnowledgeBase.bin");
//                        SaveInfo.SerializeTransformTable(new TransformTable(), "TransformTable.bin");
//                    } catch (Exception e) {
//                        Log.e("chen", e.toString());
//                    }
//
//                    playMusic(backMusic);
//                } else {
//                    Toast.makeText(this, "获取存储权限失败，请手动开启存储权限", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    protected void onPause() {
        stopMusic(backMusic);
        super.onPause();
    }

    @Override
    protected void onStart() {
        playMusic(backMusic);
        super.onStart();
    }
}
