package com.nd.pad.ssdk.test.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.nd.pad.ssdk.test.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 2017/3/22.
 */
public final class SSDKUtil {

    private static final String TAG = "SSDKUtil";

    public static AppInfo packageToAppInfo(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();

        AppInfo appInfo = new AppInfo();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            //Log.e(TAG, String.format("packageName(%s)", packageName), e);
            applicationInfo = null;
        }

        if (applicationInfo != null) {
            appInfo.setAppIcon(applicationInfo.loadIcon(pm));
            appInfo.setPkgName(applicationInfo.packageName);
            String appName = applicationInfo.loadLabel(pm).toString();
            appInfo.setAppLabel(appName);
            return appInfo;
        } else if (packageName != null && !"".equals(packageName.trim())) {
            appInfo.setPkgName(packageName);
            return appInfo;
        }

        return null;
    }

    public static List<AppInfo> packagesToAppInfos(Context context, List<String> packageNames) {
        List<AppInfo> tempInfos = new ArrayList<AppInfo>();
        if (packageNames != null) {
            for (String packageName : packageNames) {
                AppInfo appInfo = packageToAppInfo(context, packageName);
                if (appInfo != null) {
                    tempInfos.add(appInfo);
                }
            }
            return tempInfos;
        }
        return null;
    }
}
