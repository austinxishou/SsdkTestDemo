package com.nd.pad.ssdk.test.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.nd.pad.ssdk.test.R;
import com.nd.romssdk.core.SystemUtil;
import com.nd.romssdk.core.screen.ScreenUtil;
import com.nd.romssdk.core.settings.SettingsControlUtil;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * Created by Administrator on 2017/7/21.
 */
public class DeviceControlFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = DeviceControlFragment.class.getSimpleName();

    private Button mDeviceRebootBtn;
    private Button mDeviceShutdownBtn;
    private Button mDeviceLockScreenBtn;
    private Button mDeviceWakeUpBtn;
    private EditText mDeviceWakeUpSecondsEdit;

    private EditText mDeviceSysTimeYearEdit;
    private EditText mDeviceSysTimeMonthEdit;
    private EditText mDeviceSysTimeDayEdit;
    private EditText mDeviceSysTimeHourOfDayEdit;
    private EditText mDeviceSysTimeMinuteEdit;
    private EditText mDeviceSysTimeSecondEdit;
    private Button mDeviceSetSysTimeBtn;

    private Button mDeviceOpenAirplaneModeBtn;
    private Button mDeviceCloseAirplaneModeBtn;

    private Button mDeviceOpenDevelopmentSettingsBtn;
    private Button mDeviceCloseDevelopmentSettingsBtn;

    private EditText mDevicePackageNameEdit;
    private Button mDeviceForceStopPackageBtn;
    private Button mDeviceUninstallPackageBtn;
    private Button mDeviceEnablePackageBtn;
    private Button mDeviceDisenablePackageBtn;
    private Button mDeviceCleanPackageDataBtn;

    private Button mDeviceCheckUpdateBtn;
    private Button mDeviceSystemUpdateBtn;

    private Handler mHandler;

    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<Context>(context);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_control_demo, container, false);

        mHandler = new MyHandler(getActivity());

        mDeviceRebootBtn = (Button) view.findViewById(R.id.device_reboot_btn);
        mDeviceRebootBtn.setOnClickListener(this);
        mDeviceShutdownBtn = (Button) view.findViewById(R.id.device_shutdown_btn);
        mDeviceShutdownBtn.setOnClickListener(this);
        mDeviceLockScreenBtn = (Button) view.findViewById(R.id.device_lock_screen_btn);
        mDeviceLockScreenBtn.setOnClickListener(this);
        mDeviceWakeUpBtn = (Button) view.findViewById(R.id.device_wake_up_btn);
        mDeviceWakeUpBtn.setOnClickListener(this);
        mDeviceWakeUpSecondsEdit = (EditText) view.findViewById(R.id.device_secends_wake_up_edit);

        mDeviceSysTimeYearEdit = (EditText) view.findViewById(R.id.device_systime_year_edit);
        mDeviceSysTimeMonthEdit = (EditText) view.findViewById(R.id.device_systime_month_edit);
        mDeviceSysTimeDayEdit = (EditText) view.findViewById(R.id.device_systime_day_edit);
        mDeviceSysTimeHourOfDayEdit = (EditText) view.findViewById(R.id.device_systime_hour_of_day_edit);
        mDeviceSysTimeMinuteEdit = (EditText) view.findViewById(R.id.device_systime_minute_edit);
        mDeviceSysTimeSecondEdit = (EditText) view.findViewById(R.id.device_systime_second_edit);
        mDeviceSetSysTimeBtn = (Button) view.findViewById(R.id.device_set_systime_btn);
        mDeviceSetSysTimeBtn.setOnClickListener(this);

        mDeviceOpenAirplaneModeBtn = (Button) view.findViewById(R.id.device_open_airplane_mode_btn);
        mDeviceOpenAirplaneModeBtn.setOnClickListener(this);
        mDeviceCloseAirplaneModeBtn = (Button) view.findViewById(R.id.device_close_airplane_mode_btn);
        mDeviceCloseAirplaneModeBtn.setOnClickListener(this);

        mDeviceOpenDevelopmentSettingsBtn = (Button) view.findViewById(R.id.device_open_development_settings_btn);
        mDeviceOpenDevelopmentSettingsBtn.setOnClickListener(this);
        mDeviceCloseDevelopmentSettingsBtn = (Button) view.findViewById(R.id.device_close_development_settings_btn);
        mDeviceCloseDevelopmentSettingsBtn.setOnClickListener(this);

        mDevicePackageNameEdit = (EditText) view.findViewById(R.id.device_package_name_edit);
        mDeviceForceStopPackageBtn = (Button) view.findViewById(R.id.device_force_stop_package_btn);
        mDeviceForceStopPackageBtn.setOnClickListener(this);
        mDeviceUninstallPackageBtn = (Button) view.findViewById(R.id.device_uninstall_package_btn);
        mDeviceUninstallPackageBtn.setOnClickListener(this);
        mDeviceEnablePackageBtn = (Button) view.findViewById(R.id.device_enable_package_btn);
        mDeviceEnablePackageBtn.setOnClickListener(this);
        mDeviceDisenablePackageBtn = (Button) view.findViewById(R.id.device_disenable_package_btn);
        mDeviceDisenablePackageBtn.setOnClickListener(this);
        mDeviceCleanPackageDataBtn = (Button) view.findViewById(R.id.device_clean_package_data_btn);
        mDeviceCleanPackageDataBtn.setOnClickListener(this);

        mDeviceCheckUpdateBtn = (Button) view.findViewById(R.id.device_check_update_btn);
        mDeviceCheckUpdateBtn.setOnClickListener(this);
        mDeviceSystemUpdateBtn = (Button) view.findViewById(R.id.device_system_update_btn);
        mDeviceSystemUpdateBtn.setOnClickListener(this);

        return view;
    }

    private int get(EditText editText) {
        String fieldStr = editText.getText().toString().trim();
        if ("".equals(fieldStr)) {
            return 0;
        } else {
            return Integer.parseInt(fieldStr);
        }
    }

    private Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, get(mDeviceSysTimeYearEdit));
        calendar.set(Calendar.MONTH, get(mDeviceSysTimeMonthEdit) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, get(mDeviceSysTimeDayEdit));
        calendar.set(Calendar.HOUR_OF_DAY, get(mDeviceSysTimeHourOfDayEdit));
        calendar.set(Calendar.MINUTE, get(mDeviceSysTimeMinuteEdit));
        calendar.set(Calendar.SECOND, get(mDeviceSysTimeSecondEdit));
        Log.e(TAG, "time:" + calendar.getTimeInMillis());

        return calendar;
    }

    @Override
    public void onClick(View v) {
        String packageName;
        int res;
        switch (v.getId()) {
            case R.id.device_reboot_btn:
                SystemUtil.restartDevice();
                break;
            case R.id.device_shutdown_btn:
                SystemUtil.shutDownDevice();
                break;
            case R.id.device_lock_screen_btn:
                SystemUtil.systemGoToSleep();
                break;
            case R.id.device_wake_up_btn:
                ScreenUtil.closeScreen();
                if (mHandler == null) {
                    mHandler = new MyHandler(getActivity());
                }
                int delayedSenconds = Integer.parseInt(mDeviceWakeUpSecondsEdit.getText().toString());
                int delayedMs = delayedSenconds * 1000;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScreenUtil.wakeUpScreen();
                    }
                }, delayedMs > 0 ? delayedMs : 5000);
                break;
            case R.id.device_set_systime_btn:
                Log.e(TAG, "setSysDateTime");
                if (SettingsControlUtil.setSysDateTime(getCalendar()) != 0) {
                    Log.w(TAG, "setSysDateTime fail!!!");
                }
                break;
            case R.id.device_open_airplane_mode_btn:
                SettingsControlUtil.openAirplaneMode();
                break;
            case R.id.device_close_airplane_mode_btn:
                SettingsControlUtil.closeAirplaneMode();
                break;
            case R.id.device_open_development_settings_btn:
                SettingsControlUtil.openDevelopmentSettings();
                break;
            case R.id.device_close_development_settings_btn:
                SettingsControlUtil.closeDevelopmentSettings();
                break;
            case R.id.device_force_stop_package_btn:
                packageName = mDevicePackageNameEdit.getText().toString();
                res =SettingsControlUtil.forceStopPackage(packageName);
                Log.d(TAG, "forceStopPackage res=" + res);
                break;
            case R.id.device_uninstall_package_btn:
                packageName = mDevicePackageNameEdit.getText().toString();
                res = SettingsControlUtil.uninstallPackage(packageName);
                Log.d(TAG, "uninstallPackage res=" + res);
                break;
            case R.id.device_enable_package_btn:
                packageName = mDevicePackageNameEdit.getText().toString();
                res = SettingsControlUtil.enablePackage(packageName);
                Log.d(TAG, "enablePackage res=" + res);
                break;
            case R.id.device_disenable_package_btn:
                packageName = mDevicePackageNameEdit.getText().toString();
                res = SettingsControlUtil.disenablePackage(packageName);
                Log.d(TAG, "disenablePackage res=" + res);
                break;
            case R.id.device_clean_package_data_btn:
                packageName = mDevicePackageNameEdit.getText().toString();
                res = SettingsControlUtil.cleanPackageData(packageName);
                Log.d(TAG, "cleanPackageData res=" + res);
                break;
            case R.id.device_system_update_btn:
                res = SettingsControlUtil.systemUpdate();
                Log.d(TAG, "systemUpdate res=" + res);
                break;
            default:
                break;
        }
    }
}
