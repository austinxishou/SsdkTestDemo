package com.nd.pad.ssdk.test.util;

public class VideoFormat {
	
	/**
	 * 1280*720；24帧率
	 */
	public static VideoFormat SCREEN_CAPTURER_1280_720_24 = new VideoFormat(1280, 720, 24, 0);
	
	/**
	 * 1280*720；25帧率
	 */
	public static VideoFormat SCREEN_CAPTURER_1280_720_25 = new VideoFormat(1280, 720, 25, 0);

	/**
	 * 1280*720；30帧率
	 */
	public static VideoFormat SCREEN_CAPTURER_1280_720_30 = new VideoFormat(1280, 720, 30, 0);
	
	/**
	 * 1920*1080；24帧率
	 */
	public static VideoFormat SCREEN_CAPTURER_1920_1080_24 = new VideoFormat(1920, 1080, 24, 0);

	/**
	 * 1920*1080；25帧率
	 */
	public static VideoFormat SCREEN_CAPTURER_1920_1080_25 = new VideoFormat(1920, 1080, 25, 0);
	
	/**
	 * 1920*1080；30帧率
	 */
	public static VideoFormat SCREEN_CAPTURER_1920_1080_30 = new VideoFormat(1920, 1080, 30, 0);

	private int mWidth;

	private int mHeight;

	private int mFrameRate;

	private int mBitRate;

	private VideoFormat(int width, int height, int framerate, int bitrate) {
		this.mWidth = width;
		this.mHeight = height;
		this.mFrameRate = framerate;
		this.mBitRate = bitrate;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public int getFrameRate() {
		return mFrameRate;
	}

	public int getBitRate() {
		return mBitRate;
	}
}
