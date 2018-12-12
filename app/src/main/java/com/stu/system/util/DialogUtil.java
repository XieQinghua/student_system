package com.stu.system.util;

import android.app.Activity;

import com.stu.system.view.ProgressHUD;


public class DialogUtil {

    public static ProgressHUD progressDialog;

    public static void showProgressDialog(Activity activity, CharSequence message) {
        try {
            dismissProgressDialog();
            progressDialog = ProgressHUD.show(activity, message, true, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
