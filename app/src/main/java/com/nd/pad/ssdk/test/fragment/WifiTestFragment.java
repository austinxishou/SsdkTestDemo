package com.nd.pad.ssdk.test.fragment;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nd.pad.ssdk.test.R;
import com.nd.pad.ssdk.test.adapter.AppListAdapter;
import com.nd.pad.ssdk.test.model.AppInfo;
import com.nd.romssdk.core.blackwhitelist.BlackWhiteListUtil;
import com.nd.romssdk.core.wifi.WifiUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Danny on 2017/3/16.
 */
public class WifiTestFragment extends Fragment implements AdapterView.OnItemSelectedListener, ToggleButton.OnCheckedChangeListener {

    private static final String TAG = "WifiTestFragment";

    private Spinner mSleepPolicySpinner = null;
    private ArrayAdapter<String> mSleepPolicyAdapter = null;
    private String[] mSleepPolicyStr = null;
    private int mSleepPolicy;

    private ToggleButton mWifiOptimizationBtn = null;
    private boolean mIsOptimization = false;

    private ToggleButton mWifiInstantRateBtn = null;
    private ToggleButton mWifiAppRateBtn = null;
    private Spinner mAppListSpinner = null;
    private AppListAdapter mAppListAdapter = null;
    private List<AppInfo> mlistAppInfo = new ArrayList<AppInfo>();
    private String mCurTestAppName = null;

    private TextView mWifiInstantRateTv = null;
    private TextView mWifiAppTv = null;

    private TimerTask mUpdateInstantRateTask = null;
    private TimerTask mUpdateAppRateTask = null;

    private Handler mHandler = new Handler() {};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_test_demo, container, false);

        readWifiSleepPolicy();
        readWifiOptimization();
        mSleepPolicyStr = getActivity().getResources().getStringArray(R.array.wifi_sleep_policy_mode_array);
        mSleepPolicySpinner = (Spinner) view.findViewById(R.id.setNdWifiSleepPolicyMode);
        mSleepPolicyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mSleepPolicyStr);
        mSleepPolicyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSleepPolicySpinner.setAdapter(mSleepPolicyAdapter);
        mSleepPolicySpinner.setSelection(mSleepPolicy);
        mSleepPolicySpinner.setOnItemSelectedListener(this);
        registerForWifiSleepPolicyChange(mHandler);

        mWifiOptimizationBtn = (ToggleButton) view.findViewById(R.id.setNdWifiOptimization);
        mWifiOptimizationBtn.setChecked(mIsOptimization);
        mWifiOptimizationBtn.setOnCheckedChangeListener(this);
        registerForWifiOptimizationsChange(mHandler);

        mWifiInstantRateBtn = (ToggleButton) view.findViewById(R.id.setWifiInstantRateMonitorEnabled);
        mWifiInstantRateBtn.setOnCheckedChangeListener(this);

        mWifiAppRateBtn = (ToggleButton) view.findViewById(R.id.setWifiAppRateMonitorEnabled);
        mWifiAppRateBtn.setOnCheckedChangeListener(this);
        mWifiAppRateBtn.setEnabled(false);
        mAppListSpinner = (Spinner) view.findViewById(R.id.app_list);

        /*mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int pos = 0;
                queryAppInfo(); // 查询所有应用程序信息
                if (mCurTestAppName == null) {
                    mCurTestAppName = mlistAppInfo.get(pos).getAppLabel();
                } else {
                    for (int i = 0; i < mlistAppInfo.size(); i++) {
                        if (mCurTestAppName.equals(mlistAppInfo.get(i).getAppLabel())) {
                            break;
                        }
                        pos++;
                    }
                }
                mAppListAdapter = new AppListAdapter(getActivity(), mlistAppInfo);
                mAppListSpinner.setAdapter(mAppListAdapter);
                mAppListSpinner.setSelection(pos);
                mAppListSpinner.setOnItemSelectedListener(WifiTestFragment.this);
                mWifiAppRateBtn.setEnabled(true);
            }
        }, 100);*/

        mWifiInstantRateTv = (TextView) view.findViewById(R.id.instant_rate);
        mWifiAppTv = (TextView) view.findViewById(R.id.app_rate);

        Button connectBtn = (Button) view.findViewById(R.id.connect_ssid);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = "com.example.android.navigationdrawerexample";
                WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                WifiInfo WifiInfo = wifiManager.getConnectionInfo();
                if (WifiInfo != null) {
                    String CSSID = WifiInfo.getSSID();
                    if (CSSID != null && CSSID.equals("\""+"NetDragon-FZ"+"\"")) {
                        WifiUtil.connectPriorityNetwork("ND-Guest", "");
                    } else {
                        WifiUtil.connectPriorityNetwork("NetDragon-FZ", "");
                    }
                }
                Log.d("tandongli", "succees!!!");
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int pos = 0;
                queryAppInfo(); // 查询所有应用程序信息
                if (mCurTestAppName == null) {
                    mCurTestAppName = mlistAppInfo.get(pos).getAppLabel();
                } else {
                    for (int i = 0; i < mlistAppInfo.size(); i++) {
                        if (mCurTestAppName.equals(mlistAppInfo.get(i).getAppLabel())) {
                            break;
                        }
                        pos++;
                    }
                }
                mAppListAdapter = new AppListAdapter(getActivity(), mlistAppInfo);
                mAppListSpinner.setAdapter(mAppListAdapter);
                mAppListSpinner.setSelection(pos);
                mAppListSpinner.setOnItemSelectedListener(WifiTestFragment.this);
                mWifiAppRateBtn.setEnabled(true);
            }
        }, 200);
    }

    private void startUpdateInstantRate() {
        if (!mWifiInstantRateBtn.isChecked()) {
            return ;
        }
        final String rateFormat = getResources().getString(R.string.wifi_up_download_rate);
        if (mUpdateInstantRateTask == null) {
            mUpdateInstantRateTask = new TimerTask() {
                @Override
                public void run() {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String instantRate = String.format(rateFormat, WifiUtil.getWifiInstantUploadRate(),
                                    WifiUtil.getWifiInstantDownloadRate());
                            mWifiInstantRateTv.setText(instantRate);
                        }
                    });
                }
            };
            new Timer().schedule(mUpdateInstantRateTask, 0, 500);
        }
    }

    private void stopUpdateInstatntRate() {
        if (mUpdateInstantRateTask != null) {
            mUpdateInstantRateTask.cancel();
            mUpdateInstantRateTask = null;
        }
    }

    private void startUpdateAppRate() {
        if (!mWifiAppRateBtn.isChecked()) {
            return ;
        }

        final String rateFormat = getResources().getString(R.string.wifi_up_download_rate);
        if (mUpdateAppRateTask == null) {
            mUpdateAppRateTask = new TimerTask() {
                @Override
                public void run() {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String appRate = String.format(rateFormat, WifiUtil.getAppWifiUploadRate(mCurTestAppName),
                                    WifiUtil.getAppWifiDownloadRate(mCurTestAppName));
                            mWifiAppTv.setText(appRate);
                            Log.d(TAG, "mCurTestAppPkgName : " + mCurTestAppName);
                        }
                    });
                }
            };
            new Timer().schedule(mUpdateAppRateTask, 0, 500);
        }
    }

    private void stopUpdateAppRate() {
        if (mUpdateAppRateTask != null) {
            mUpdateAppRateTask.cancel();
            mUpdateAppRateTask = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startUpdateInstantRate();
        startUpdateAppRate();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopUpdateInstatntRate();
        stopUpdateAppRate();
    }

    /**
     * Observes changes to wifi sleep policy
     */
    private void registerForWifiOptimizationsChange(Handler handler) {
        ContentObserver contentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                readWifiOptimization();
                mWifiOptimizationBtn.setChecked(mIsOptimization);
            }
        };
        getActivity().getContentResolver().registerContentObserver(
                Settings.Global.getUriFor("wifi_suspend_optimizations_enabled"), false, contentObserver);
    }

    /**
     * Observes changes to wifi sleep policy
     */
    private void registerForWifiSleepPolicyChange(Handler handler) {
        ContentObserver contentObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                readWifiSleepPolicy();
                mSleepPolicySpinner.setSelection(mSleepPolicy);
            }
        };
        getActivity().getContentResolver().registerContentObserver(
                Settings.Global.getUriFor(Settings.Global.WIFI_SLEEP_POLICY), false, contentObserver);
    }

    private void readWifiSleepPolicy() {
        mSleepPolicy = WifiUtil.getNdWifiSleepPolicyMode();
    }

    private void writeWifiSleepPolicy(int sleepPolicy) {
        if (mSleepPolicy != sleepPolicy) {
            if (WifiUtil.setNdWifiSleepPolicyMode(sleepPolicy)) {
                mSleepPolicy = sleepPolicy;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.setNdWifiSleepPolicyMode:
                Log.d("tandongli", "position=" + position);
                writeWifiSleepPolicy(position);
                break;
            case R.id.app_list:
                String nowAPPName = mlistAppInfo.get(position).getAppLabel();
                if (!mCurTestAppName.equals(nowAPPName)) {
                    setWifiAppRateMonitorEnable(mCurTestAppName, false);
                    mCurTestAppName = nowAPPName;
                    if (mWifiAppRateBtn.isChecked()) {
                        setWifiAppRateMonitorEnable(mCurTestAppName, true);
                    }
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void readWifiOptimization() {
        mIsOptimization = WifiUtil.getNdWifiOptimization();
    }

    private void writeWifiOptimization(boolean optimization) {
        if (mIsOptimization != optimization) {
            if (WifiUtil.setNdWifiOptimization(optimization)) {
                mIsOptimization = optimization;
            }
        }
    }

    private void setWifiInstantRateMonitorEnable(boolean enable) {
        if (enable) {
            WifiUtil.startWifiInstantRateMonitor();
        } else {
            WifiUtil.stopWifiInstantRateMonitor();
        }
    }

    private void setWifiAppRateMonitorEnable(String pkgName, boolean enable) {
        if (enable) {
            WifiUtil.startAppWifiRateMonitor(pkgName);
        } else {
            WifiUtil.stopAppWifiRateMonitor(pkgName);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.setNdWifiOptimization:
                writeWifiOptimization(isChecked);
                break;
            case R.id.setWifiInstantRateMonitorEnabled:
                setWifiInstantRateMonitorEnable(isChecked);
                if (isChecked) {
                    startUpdateInstantRate();
                } else {
                    stopUpdateInstatntRate();
                }
                break;
            case R.id.setWifiAppRateMonitorEnabled:
                int pos = mAppListSpinner.getSelectedItemPosition();
                if (mlistAppInfo != null && pos < mlistAppInfo.size() && pos >= 0) {
                    String nowAPPName = mlistAppInfo.get(mAppListSpinner.getSelectedItemPosition()).getAppLabel();
                    if (!mCurTestAppName.equals(nowAPPName)) {
                        mCurTestAppName = nowAPPName;
                    }
                    setWifiAppRateMonitorEnable(mCurTestAppName, isChecked);
                    if (isChecked) {
                        startUpdateAppRate();
                    } else {
                        stopUpdateAppRate();
                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 获得所有app
     **/
    public void queryAppInfo() {
        PackageManager pm = getActivity().getPackageManager(); // 获得PackageManager对象

        // 通过查询，获得所有ResolveInfo对象.
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(0);
        // 调用系统排序，根据name排序
        // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
        Collections.sort(applicationInfos,new ApplicationInfo.DisplayNameComparator(pm));
        if (applicationInfos != null) {
            mlistAppInfo.clear();
            for (ApplicationInfo applicationInfo : applicationInfos) {
                String pkgName = applicationInfo.packageName;               // 获得应用程序的包名
                String appLabel = (String) applicationInfo.loadLabel(pm);   // 获得应用程序的Label
                Drawable icon = applicationInfo.loadIcon(pm);               // 获得应用程序图标

                // 创建一个AppInfo对象，并赋值
                AppInfo appInfo = new AppInfo();
                appInfo.setAppLabel(appLabel);
                appInfo.setPkgName(pkgName);
                appInfo.setAppIcon(icon);
                mlistAppInfo.add(appInfo); // 添加至列表中
            }
        }
    }
}
