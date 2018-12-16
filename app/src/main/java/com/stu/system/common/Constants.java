package com.stu.system.common;

import android.graphics.Color;
import android.os.Environment;

import java.io.File;

public class Constants {
    public static final int CLICK_INTERVAL = 1000;
    public static final String BUGLY_ID = "61d6a998af";

    public static final String IS_LOGIN = "isLogin";

    public static final String NAME = "name";
    public static final String UNAME = "uname";
    public static final String UID = "uid";
    public static final String HOST = "host";
    public static final String CLASS_LIST = "classList";

    public static final int MAIN_COLOR = Color.parseColor("#00A6EC");
    public static final int TRAN_MAIN_COLOR = Color.parseColor("#00A6EC") & 0x80FFFFFF;

    public static final String PROJECT_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "student_system" + File.separator;
    public static final String VIDEO_THUMB_IMG = PROJECT_PATH + "video_thumb" + File.separator;
    public static final String HISTORY_IMG = PROJECT_PATH + "history_img" + File.separator;
    public static final String HISTORY_VIDEO = PROJECT_PATH + "history_video" + File.separator;

}
