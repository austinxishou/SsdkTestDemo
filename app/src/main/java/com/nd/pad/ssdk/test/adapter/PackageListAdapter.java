package com.nd.pad.ssdk.test.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nd.pad.ssdk.test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danny on 2017/6/21.
 */
public class PackageListAdapter extends BaseAdapter {

    private static final String TAG = "PackageListAdapter";

    private List<String> mPackageNameList = null;
    LayoutInflater mInfater = null;
    private Context mContext;

    public PackageListAdapter(Context context, List<String> packageNames) {
        mPackageNameList = packageNames;
        if (mPackageNameList == null) {
            mPackageNameList = new ArrayList<String>();
        }
        mContext = context;
        mInfater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void changeAndNotifyData(List<String> packageNames) {
        if (packageNames != null) {
            mPackageNameList = packageNames;
        } else {
            mPackageNameList = new ArrayList<String>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPackageNameList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mPackageNameList != null && position < mPackageNameList.size() && position >= 0) {
            return mPackageNameList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = null;
        TextView view = null;
        if (convertView != null) {
            rootView =  convertView;
        } else {
            rootView = mInfater.inflate(R.layout.adapter_item_text, null);
        }

        view = (TextView) rootView.findViewById(R.id.name_tv);

        if (mPackageNameList != null && position < mPackageNameList.size() && position >= 0) {
            view.setText(mPackageNameList.get(position));
            view.setEnabled(false);
            return rootView;
        }

        return null;
    }

}
