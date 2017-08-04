package com.nd.ssl;

import android.util.Log;

public class NDSSLSpecial {
	
	private final static String TAG = NDSSLSpecial.class.getSimpleName();
	
	static {
        Log.i(TAG, "NDSSLSpecial LoadLibrary");
        System.loadLibrary("special_ndssl");
    }
	
    public static native String getApp();
    public static native String encodeKey();
}