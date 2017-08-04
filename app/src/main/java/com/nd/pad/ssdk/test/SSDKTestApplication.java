package com.nd.pad.ssdk.test;

import android.app.Application;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.nd.pad.ssdk.test.util.AppCommonUtils;
import com.nd.pad.ssdk.test.util.AppConstants;
import com.nd.romssdk.core.SystemUtil;
import com.nd.ssl.NDSSLSpecial;

/**
 * Created by Danny on 2017/3/27.
 */
public class SSDKTestApplication extends Application {

    private static final String TAG = "SSDKTestApplication";
    private static String sVerifyKey = null;

    private void verifyKey() {
        if (!SystemUtil.isConnected()) {
            SystemUtil.connect(this, new ServiceConnection() {

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    if (SystemUtil.verify(sVerifyKey)) {
                        Log.d(TAG, "tdl verify success!");
                    } else {
                        Log.d(TAG, "tdl verify fail!");
                    }
                }
            });
        } else {
            if (SystemUtil.verify(sVerifyKey)) {
                Log.d(TAG, "tdl verify success!");
            } else {
                Log.d(TAG, "tdl verify fail!");
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (AppConstants.APP_MAIN_PROCESS.equals(
                AppCommonUtils.getProcessName(getApplicationContext()))) {
            if (sVerifyKey == null) {
                sVerifyKey = NDSSLSpecial.encodeKey();
            }
            Log.d(TAG, "tdl verify:" + sVerifyKey);
            verifyKey();
        }
    }
}
