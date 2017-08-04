package com.nd.pad.ssdk.test.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nd.pad.ssdk.test.model.AppInfo;

import com.nd.pad.ssdk.test.R;
import com.nd.pad.ssdk.test.util.SSDKUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 2017/3/21.
 */
public class AppListAdapter extends BaseAdapter {

    private List<AppInfo> mlistAppInfo = null;
    LayoutInflater mInfater = null;
    private Context mContext;

    public AppListAdapter(Context context, List<AppInfo> apps) {
        mContext = context;
        mInfater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (apps != null) {
            mlistAppInfo = apps;
        } else {
            mlistAppInfo = new ArrayList<AppInfo>();
        }
    }

    public void changeAndNotifyData(List<String> packageNames) {
        if (packageNames != null) {
            mlistAppInfo = SSDKUtil.packagesToAppInfos(mContext, packageNames);
        }  else {
            mlistAppInfo = new ArrayList<AppInfo>();
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlistAppInfo.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < mlistAppInfo.size()) {
            return mlistAppInfo.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            view = mInfater.inflate(R.layout.adapter_item_icon_text, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else{
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        AppInfo appInfo = (AppInfo) getItem(position);
        if (appInfo != null) {
            Drawable appIcon = appInfo.getAppIcon();
            if (appIcon != null) {
                holder.icon.setBackground(appIcon);
            }
            //appIcon.setBounds(0, 0, appIcon.getMinimumWidth(), appIcon.getMinimumHeight());
            if (appInfo.getAppLabel() != null) {
                holder.tvAppName.setText(appInfo.getAppLabel());
            } else if (appInfo.getPkgName() != null) {
                holder.tvAppName.setText(appInfo.getPkgName());
            }
            //holder.tvAppName.setCompoundDrawables(appIcon, null, null, null);
            return view;
        }
        return null;
    }

    class ViewHolder {
        ImageView icon;
        TextView tvAppName;

        public ViewHolder(View view) {
            this.icon = (ImageView) view.findViewById(R.id.app_icon_iv);
            this.tvAppName = (TextView) view.findViewById(R.id.app_name_tv);
        }
    }
}
