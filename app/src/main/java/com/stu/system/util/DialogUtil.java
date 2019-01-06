package com.stu.system.util;

import android.app.Activity;

import com.stu.system.view.ProgressHUD;


public class DialogUtil {

    public static ProgressHUD progressDialog;

    public static void showProgressDialog(Activity activity, CharSequence message) {
        try {
            dismissProgressDialog();
            progressDialog = ProgressHUD.show(activity, message, true, true, null);
            //外部不可点击
            progressDialog.setCanceledOnTouchOutside(false);
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
