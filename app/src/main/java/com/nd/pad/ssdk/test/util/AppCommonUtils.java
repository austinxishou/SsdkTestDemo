package com.nd.pad.ssdk.test.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.List;

public class AppCommonUtils {
    public static String getProcessName(Context context) {
        final int pid = Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
//                Log.d("AppCommonUtils", procInfo.processName);
                return procInfo.processName;
            }
        }
        return null;
    }
}
