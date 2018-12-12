package com.stu.system.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LightStatusBarUtils {

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void setLightStatusBar(Activity activity, boolean dark) {
        int availableRomType = RomUtils.getLightStatausBarAvailableRomType();
        //Log.e("LightStatusBarUtils", "availableRomType=" + availableRomType);
        switch (availableRomType) {
            case RomUtils.AvailableRomType.MIUI:
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    dark = false;
                }
                setMIUILightStatusBar(activity, dark);
                break;

            case RomUtils.AvailableRomType.FLYME:
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    dark = false;
                }
                setFlymeLightStatusBar(activity, dark);
                break;

            case RomUtils.AvailableRomType.ANDROID_NATIVE:
                setAndroidNativeLightStatusBar(activity, dark);
                break;

            case RomUtils.AvailableRomType.NA:
                // N/A do nothing
                setAndroidNativeLightStatusBar(activity, dark);
                break;
            default:
                break;
        }
    }

    @SuppressLint("NewApi")
    private static boolean setMIUILightStatusBar(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);

            //适配MIUI9
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean setFlymeLightStatusBar(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            String model = Build.MODEL; // 手机型号
            //Log.e("LightStatusBarUtils", "model=" + model);
            if (!TextUtils.isEmpty(model) && model.startsWith("Le")) {
            } else if (!TextUtils.isEmpty(model) && model.startsWith("vivo")) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                ToolbarUtil.setColor(activity, Color.LTGRAY);
            } else if (!TextUtils.isEmpty(model) && model.startsWith("OPPO")) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                ToolbarUtil.setColor(activity, Color.LTGRAY);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            // We want to change tint color to white again.
            // You can also record the flags in advance so that you can turn UI back completely if
            // you have set other flags before, such as translucent or full screen.
            decor.setSystemUiVisibility(0);
        }
    }
}
