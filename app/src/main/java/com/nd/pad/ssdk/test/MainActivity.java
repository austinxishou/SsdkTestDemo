package com.nd.pad.ssdk.test;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.mzule.fantasyslide.FantasyListener;
import com.github.mzule.fantasyslide.SideBar;
import com.nd.pad.ssdk.test.fragment.AboutMeFragment;
import com.nd.pad.ssdk.test.fragment.DeviceControlFragment;
import com.nd.pad.ssdk.test.fragment.ForbidManagerFragment;
import com.nd.pad.ssdk.test.fragment.WifiTestFragment;
import com.nd.romssdk.core.SystemUtil;

public class MainActivity extends AppCompatActivity implements FantasyListener {

    private SideBar mLeftSideBar = null;

    private AboutMeFragment mAboutMeFragment;
    private WifiTestFragment mWifiTestFragment;
    private ForbidManagerFragment mForbidManagerFragment;
    private DeviceControlFragment mDeviceControlFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLeftSideBar = (SideBar) findViewById(R.id.leftSideBar);
        mLeftSideBar.setFantasyListener(this);

        mAboutMeFragment = new AboutMeFragment();
        changeFragment(mAboutMeFragment);
    }

    private void changeFragment(Fragment fragment) {
        Log.d("tdl", "tdl changeFragment " + fragment);
        if (fragment == null) {
            if (mAboutMeFragment == null) {
                mAboutMeFragment = new AboutMeFragment();
            }
            fragment = mAboutMeFragment;
        }

        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content, fragment);
        transaction.commit();
    }

    /**
     * Hover 是指上面效果图中，高亮的状态，此时手指仍在屏幕上 move.
     * 默认的 hover 处理逻辑是设置 view 的 pressed 状态为 true.
     * 重写 onHover(View) 方法返回 false 可以改写默认逻辑。
     * @param view
     * @param index
     * @return
     */
    @Override
    public boolean onHover(@Nullable View view, int index) {
        return false;
    }

    /**
     * Select 是指 hover 状态时手指离开屏幕，触发 select 状态。
     * 默认的处理逻辑是调用 view 的 onClick 事件。
     * 重写 onSelect(View) 方法返回 false 可以改写默认逻辑。
     * @param view
     * @param index
     * @return
     */
    @Override
    public boolean onSelect(View view, int index) {
        int id = view.getId();
        switch (id) {
            case R.id.userInfo:
                if (mAboutMeFragment == null) {
                    mAboutMeFragment = new AboutMeFragment();
                }
                changeFragment(mAboutMeFragment);
                break;
            case R.id.ssdk_wifi_test:
                if (mWifiTestFragment == null) {
                    mWifiTestFragment = new WifiTestFragment();
                }
                changeFragment(mWifiTestFragment);
                break;
            case R.id.ssdk_forbid_manager_test:
                if (mForbidManagerFragment == null) {
                    mForbidManagerFragment = new ForbidManagerFragment();
                }
                changeFragment(mForbidManagerFragment);
                break;
            case R.id.ssdk_device_control_test:
                if (mDeviceControlFragment == null) {
                    mDeviceControlFragment = new DeviceControlFragment();
                }
                changeFragment(mDeviceControlFragment);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Cancel 是指手指离开屏幕时，没有任何 view 触发 select 状态，则为 cancel，无默认处理逻辑。
     */
    @Override
    public void onCancel() {

    }
}
