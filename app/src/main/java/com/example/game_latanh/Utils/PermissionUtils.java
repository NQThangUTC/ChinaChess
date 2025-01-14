package com.example.game_latanh.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;



public class PermissionUtils {
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * Giải quyết vấn đề Android 6.0 trở lên không thể đọc quyền lưu trữ bên ngoài
     *
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String str : PERMISSIONS_STORAGE) {
                if (activity.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(PERMISSIONS_STORAGE, requestCode);
                    return false;
                }
            }
        }
        return true;
    }
}
