package com.nd.pad.ssdk.test.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nd.pad.ssdk.test.R;

/**
 * Created by Danny on 2017/3/16.
 */
public class AboutMeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_me, container, false);

        return view;
    }
}
