package com.example.game_latanh.Info;

import android.content.SharedPreferences;

import java.util.concurrent.Semaphore;

import static android.content.Context.MODE_PRIVATE;


public class Setting {
    public boolean isMusicPlay = false;
    public boolean isEffectPlay = true;
    public boolean isPlayerRed = true;
    public int mLevel = 2;

    public Setting(SharedPreferences sharedPreferences) {
        isMusicPlay = sharedPreferences.getBoolean("isMusicPlay", true);
        isEffectPlay = sharedPreferences.getBoolean("isEffectPlay", true);
        isPlayerRed = sharedPreferences.getBoolean("isPlayerRed", true);
        mLevel = sharedPreferences.getInt("mLevel", 2);
    }
}
