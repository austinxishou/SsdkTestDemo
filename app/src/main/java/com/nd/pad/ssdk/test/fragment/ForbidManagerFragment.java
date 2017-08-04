package com.nd.pad.ssdk.test.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nd.pad.ssdk.test.R;
import com.nd.pad.ssdk.test.adapter.AppListAdapter;
import com.nd.pad.ssdk.test.adapter.PackageListAdapter;
import com.nd.pad.ssdk.test.model.AppInfo;
import com.nd.pad.ssdk.test.util.SSDKUtil;
import com.nd.romssdk.core.blackwhitelist.BlackWhiteListUtil;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Danny on 2017/6/21.
 */
public class ForbidManagerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ForbidManagerFragment";

    private EditText mForbidInstallEdit;
    private EditText mForbidUninstallEdit;
    private EditText mForbidCleanEdit;
    private EditText mForbidRunEdit;
    private EditText mForbidVisiableEdit;

    private Button mForbidInstallRegBtn;
    private Button mForbidInstallUnregBtn;
    private Button mForbidUninstallRegBtn;
    private Button mForbidUninstallUnregBtn;
    private Button mForbidCleanRegBtn;
    private Button mForbidCleanUnregBtn;
    private Button mForbidRunRegBtn;
    private Button mForbidRunUnregBtn;
    private Button mForbidVisiableRegBtn;
    private Button mForbidVisiableUnregBtn;

    private Spinner mForbidInstallSpinner;
    private Spinner mForbidUninstallSpinner;
    private Spinner mForbidCleanSpinner;
    private Spinner mForbidRunSpinner;
    private Spinner mForbidVisiableSpinner;

    private List<String> mForbidInstallList;
    private List<AppInfo> mForbidUninstallList;
    private List<AppInfo> mForbidCleanList;
    private List<AppInfo> mForbidRunList;
    private List<AppInfo> mForbidVisiableList;

    private PackageListAdapter mForbidInstallAdapter;
    private AppListAdapter mForbidUninstallAdapter;
    private AppListAdapter mForbidCleanAdapter;
    private AppListAdapter mForbidRunAdapter;
    private AppListAdapter mForbidVisiableAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forbid_manager_demo, container, false);

        mForbidInstallEdit = (EditText) root.findViewById(R.id.forbid_install_edit);
        mForbidUninstallEdit = (EditText) root.findViewById(R.id.forbid_uninstall_edit);
        mForbidCleanEdit = (EditText) root.findViewById(R.id.forbid_clean_edit);
        mForbidRunEdit = (EditText) root.findViewById(R.id.forbid_run_edit);
        mForbidVisiableEdit = (EditText) root.findViewById(R.id.forbid_visiable_edit);

        mForbidInstallRegBtn = (Button) root.findViewById(R.id.forbid_install_register);
        mForbidInstallRegBtn.setOnClickListener(this);
        mForbidInstallUnregBtn = (Button) root.findViewById(R.id.forbid_install_unregister);
        mForbidInstallUnregBtn.setOnClickListener(this);
        mForbidUninstallRegBtn = (Button) root.findViewById(R.id.forbid_uninstall_register);
        mForbidUninstallRegBtn.setOnClickListener(this);
        mForbidUninstallUnregBtn = (Button) root.findViewById(R.id.forbid_uninstall_unregister);
        mForbidUninstallUnregBtn.setOnClickListener(this);
        mForbidCleanRegBtn = (Button) root.findViewById(R.id.forbid_clean_register);
        mForbidCleanRegBtn.setOnClickListener(this);
        mForbidCleanUnregBtn = (Button) root.findViewById(R.id.forbid_clean_unregister);
        mForbidCleanUnregBtn.setOnClickListener(this);
        mForbidRunRegBtn = (Button) root.findViewById(R.id.forbid_run_register);
        mForbidRunRegBtn.setOnClickListener(this);
        mForbidRunUnregBtn = (Button) root.findViewById(R.id.forbid_run_unregister);
        mForbidRunUnregBtn.setOnClickListener(this);
        mForbidVisiableRegBtn = (Button) root.findViewById(R.id.forbid_visiable_register);
        mForbidVisiableRegBtn.setOnClickListener(this);
        mForbidVisiableUnregBtn = (Button) root.findViewById(R.id.forbid_visiable_unregister);
        mForbidVisiableUnregBtn.setOnClickListener(this);

        mForbidInstallSpinner = (Spinner) root.findViewById(R.id.forbid_install_checkout);
        mForbidUninstallSpinner = (Spinner) root.findViewById(R.id.forbid_uninstall_checkout);
        mForbidCleanSpinner = (Spinner) root.findViewById(R.id.forbid_clean_checkout);
        mForbidRunSpinner = (Spinner) root.findViewById(R.id.forbid_run_checkout);
        mForbidVisiableSpinner = (Spinner) root.findViewById(R.id.forbid_visiable_checkout);

        initView();
        return root;
    }

    private void initView() {
        mForbidInstallAdapter = new PackageListAdapter(getActivity(), BlackWhiteListUtil.getRegisterForbidInstalled());
        mForbidInstallSpinner.setAdapter(mForbidInstallAdapter);

        mForbidUninstallAdapter = new AppListAdapter(getActivity(), SSDKUtil.packagesToAppInfos(getActivity(),
                BlackWhiteListUtil.getRegisterForbidUninstalled()));
        mForbidUninstallSpinner.setAdapter(mForbidUninstallAdapter);

        mForbidCleanAdapter = new AppListAdapter(getActivity(), SSDKUtil.packagesToAppInfos(getActivity(),
                BlackWhiteListUtil.getRegisterForbidSystemClean()));
        mForbidCleanSpinner.setAdapter(mForbidCleanAdapter);

        mForbidRunAdapter = new AppListAdapter(getActivity(), SSDKUtil.packagesToAppInfos(getActivity(),
                BlackWhiteListUtil.getRegisterForbidRun()));
        mForbidRunSpinner.setAdapter(mForbidRunAdapter);

        mForbidVisiableAdapter = new AppListAdapter(getActivity(), SSDKUtil.packagesToAppInfos(getActivity(),
                BlackWhiteListUtil.getRegisterForbidVisiable()));
        mForbidVisiableSpinner.setAdapter(mForbidVisiableAdapter);
    }

    public void onClick(View v) {
        String packageName = null;
        switch (v.getId()) {
            case R.id.forbid_install_register:
                packageName = mForbidInstallEdit.getText().toString();
                if (!BlackWhiteListUtil.isForbidInstalled(packageName)) {
                    BlackWhiteListUtil.registerForbidInstalled(packageName);
                    mForbidInstallAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidInstalled());
                }
                break;
            case R.id.forbid_install_unregister:
                packageName = mForbidInstallEdit.getText().toString();
                if (BlackWhiteListUtil.isForbidInstalled(packageName)) {
                    BlackWhiteListUtil.unregisterForbidInstalled(packageName);
                    mForbidInstallAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidInstalled());
                }
                break;
            case R.id.forbid_uninstall_register:
                packageName = mForbidUninstallEdit.getText().toString();
                if (!BlackWhiteListUtil.isForbidUninstalled(packageName)) {
                    BlackWhiteListUtil.registerForbidUninstalled(packageName);
                    mForbidUninstallAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidUninstalled());
                }
                break;
            case R.id.forbid_uninstall_unregister:
                packageName = mForbidUninstallEdit.getText().toString();
                if (BlackWhiteListUtil.isForbidUninstalled(packageName)) {
                    BlackWhiteListUtil.unregisterForbidUninstalled(packageName);
                    mForbidUninstallAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidUninstalled());
                }
                break;
            case R.id.forbid_clean_register:
                packageName = mForbidCleanEdit.getText().toString();
                if (!BlackWhiteListUtil.isForbidSystemClean(packageName)) {
                    BlackWhiteListUtil.registerForbidSystemClean(packageName);
                    mForbidCleanAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidSystemClean());
                }
                break;
            case R.id.forbid_clean_unregister:
                packageName = mForbidCleanEdit.getText().toString();
                if (BlackWhiteListUtil.isForbidSystemClean(packageName)) {
                    BlackWhiteListUtil.unregisterForbidSystemClean(packageName);
                    mForbidCleanAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidSystemClean());
                }
                break;
            case R.id.forbid_run_register:
                packageName = mForbidRunEdit.getText().toString();
                if (!BlackWhiteListUtil.isForbidRun(packageName)) {
                    BlackWhiteListUtil.registerForbidRun(packageName);
                    mForbidRunAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidRun());
                }
                break;
            case R.id.forbid_run_unregister:
                packageName = mForbidRunEdit.getText().toString();
                if (BlackWhiteListUtil.isForbidRun(packageName)) {
                    BlackWhiteListUtil.unregisterForbidRun(packageName);
                    mForbidRunAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidRun());
                }
                break;
            case R.id.forbid_visiable_register:
                packageName = mForbidVisiableEdit.getText().toString();
                if (!BlackWhiteListUtil.isForbidVisiable(packageName)) {
                    BlackWhiteListUtil.registerForbidVisiable(packageName);
                    mForbidVisiableAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidVisiable());
                }
                break;
            case R.id.forbid_visiable_unregister:
                packageName = mForbidVisiableEdit.getText().toString();
                if (BlackWhiteListUtil.isForbidVisiable(packageName)) {
                    BlackWhiteListUtil.unregisterForbidVisiable(packageName);
                    mForbidVisiableAdapter.changeAndNotifyData(BlackWhiteListUtil.getRegisterForbidVisiable());
                }
                break;
            default:
                break;
        }
    }
}
