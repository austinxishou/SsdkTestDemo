package com.nd.pad.ssdk.test.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nd.pad.ssdk.test.R;
import com.nd.romssdk.core.sdcard.SdcardUtil;
import com.nd.romssdk.core.systemfile.SystemFileUtil;
import com.nd.romssdk.core.usb.UsbUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/8/4.
 */

public class SdcardUsbSystemFileFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = DeviceControlFragment.class.getSimpleName();
    private Button mSdcardEnableBtn;
    private Button mSdcardDisableBtn;
    private Button mSdcardGetstatusBtn;
    private TextView mSdcardGetStatusTxv;
    private Button mUsbEnableBtn;
    private Button mUsbDisableBtn;
    private Button mUsbGetstatusBtn;
    private TextView mUsbGetStatusTxv;

    private EditText mSystenmFileMkdirEdt;
    private Button mSystenmFileMkdirBtn;

    private EditText mSystenmFileRmdirEdt;
    private Button mSystenmFileRmdirBtn;

    private EditText mSystenmFileSetpemPathEdt;
    private EditText mSystenmFileSetpemModEdt;
    private EditText mSystenmFileSetpemUidEdt;
    private EditText mSystenmFileSetpemPidEdt;
    private Button mSystenmFileSetpemBtn;

    private EditText mSystenmFileCopySrcEdt;
    private EditText mSystenmFileCopyDestEdt;
    private Button mSystenmFileCopyBtn;




    private Handler mHandler;
    private static class MyHandler extends Handler {
        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<Context>(context);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sdcard_usb_systemfile, container, false);
        mHandler = new MyHandler(getActivity());
        mSdcardEnableBtn = (Button) view.findViewById(R.id.sdcard_enable_btn);
        mSdcardDisableBtn = (Button) view.findViewById(R.id.sdcard_disable_btn);
        mSdcardGetstatusBtn = (Button) view.findViewById(R.id.sdcard_get_status_btn);
        mSdcardGetStatusTxv = (TextView) view.findViewById(R.id.sdcard_status_textView);
        mUsbEnableBtn = (Button) view.findViewById(R.id.usb_enable_btn);
        mUsbDisableBtn = (Button) view.findViewById(R.id.usb_disable_btn);
        mUsbGetstatusBtn = (Button) view.findViewById(R.id.usb_get_status_btn);
        mUsbGetStatusTxv = (TextView) view.findViewById(R.id.usb_status_textView);

        mSystenmFileMkdirEdt = (EditText) view.findViewById(R.id.systemfile_mkdir_edt);
        mSystenmFileMkdirBtn = (Button) view.findViewById(R.id.systemfile_mkdir_btn);
        mSystenmFileRmdirEdt = (EditText) view.findViewById(R.id.systemfile_rmdir_edt);
        mSystenmFileRmdirBtn = (Button) view.findViewById(R.id.systemfile_rmdir_btn);
        mSystenmFileSetpemPathEdt = (EditText) view.findViewById(R.id.systemfile_set_permisson_path_edt);
        mSystenmFileSetpemModEdt = (EditText) view.findViewById(R.id.systemfile_set_permisson_mod_edt);
        mSystenmFileSetpemUidEdt = (EditText) view.findViewById(R.id.systemfile_set_permisson_uid_edt);
        mSystenmFileSetpemPidEdt = (EditText) view.findViewById(R.id.systemfile_set_permisson_gid_edt);
        mSystenmFileSetpemBtn = (Button) view.findViewById(R.id.systemfile_set_permisson_btn);
        mSystenmFileCopySrcEdt = (EditText) view.findViewById(R.id.systemfile_copy_src_edt);
        mSystenmFileCopyDestEdt = (EditText) view.findViewById(R.id.systemfile_copy_dest_edt);
        mSystenmFileCopyBtn = (Button) view.findViewById(R.id.systemfile_copy_file_btn);
        mSdcardEnableBtn.setOnClickListener(this);
        mSdcardDisableBtn.setOnClickListener(this);
        mSdcardGetstatusBtn.setOnClickListener(this);

        mUsbEnableBtn.setOnClickListener(this);
        mUsbDisableBtn.setOnClickListener(this);
        mUsbGetstatusBtn.setOnClickListener(this);

        mSystenmFileMkdirBtn.setOnClickListener(this);
        mSystenmFileRmdirBtn.setOnClickListener(this);
        mSystenmFileSetpemBtn.setOnClickListener(this);
        mSystenmFileCopyBtn.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        String packageName;
        int res;
        try {
            switch (v.getId()) {
                case R.id.sdcard_enable_btn:
                    SdcardUtil.enableExternalSdcard();
                    break;
                case R.id.sdcard_disable_btn:
                    SdcardUtil.diableExternalSdcard();
                    break;
                case R.id.sdcard_get_status_btn:
                    int sdcardStatus = SdcardUtil.getExternalSdcardStatus();
                    if(sdcardStatus == 1){
                        mSdcardGetStatusTxv.setText("当前外置sdcard状态为使能");
                    }else{
                        mSdcardGetStatusTxv.setText("当前外置sdcard状态为禁止");
                    }
                    break;
                case R.id.usb_enable_btn:
                    UsbUtil.enableUsb();
                    break;
                case R.id.usb_disable_btn:
                    UsbUtil.diableUsb();
                    break;
                case R.id.usb_get_status_btn:
                    int usbStatus = UsbUtil.getUsbStatus();
                    if(usbStatus == 0){
                        mUsbGetStatusTxv.setText("当前USB状态为使能");
                    }else{
                        mUsbGetStatusTxv.setText("当前USB状态为仅充电");
                    }
                    break;
                case R.id.systemfile_mkdir_btn:
                    String mkPath = mSystenmFileMkdirEdt.getText().toString();
                    SystemFileUtil.mkSystemDir(mkPath);
                    break;
                case R.id.systemfile_rmdir_btn:
                    String rmPath = mSystenmFileRmdirEdt.getText().toString();
                    SystemFileUtil.rmSystemDir(rmPath);
                    break;
                case R.id.systemfile_set_permisson_btn:
                    String pemPath = mSystenmFileSetpemPathEdt.getText().toString();
                    int pemUid = get(mSystenmFileSetpemUidEdt);
                    int pemGid = get(mSystenmFileSetpemPidEdt);
                    String pemModStr = Integer.valueOf(mSystenmFileSetpemModEdt.getText().toString(),8).toString();
                    int pemMod = Integer.parseInt(pemModStr);
//                    Log.d(TAG,"systemfile_set_permisson input pemPath="+pemPath+" pemMod="+pemMod+" pemUid="+pemUid+" pemGid="+pemGid);
                    SystemFileUtil.setPermissions(pemPath, pemMod, pemUid, pemGid);
                    break;
                case R.id.systemfile_copy_file_btn:
                    String copySrcPath = mSystenmFileCopySrcEdt.getText().toString();
                    String copyDestPath = mSystenmFileCopyDestEdt.getText().toString();
                    SystemFileUtil.copyToSystemFile(copySrcPath, copyDestPath);
                    break;
                default:
                    break;
            }
        }catch (RemoteException e){
            Log.e("NDRomSSDK", Log.getStackTraceString(e));
        }
    }
}
