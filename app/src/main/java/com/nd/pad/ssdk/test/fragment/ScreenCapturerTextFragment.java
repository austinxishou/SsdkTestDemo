package com.nd.pad.ssdk.test.fragment;

import android.app.Fragment;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nd.pad.ssdk.test.R;
import com.nd.pad.ssdk.test.util.VideoFormat;

import java.io.IOException;
import java.nio.ByteBuffer;

import butterknife.OnClick;

/**
 * Created by Danny on 2017/3/30.
 */
public class ScreenCapturerTextFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ScreenCapturerTextFragment.class.getSimpleName();

    // Video Constants
    private final static String MIME_TYPE = "video/avc"; // H.264 Advanced Video
    private final static int HEAD_OFFSET = 512;

    private Button mStartBtn = null;
    private Button mStopBtn = null;
    private SurfaceView mScreenCapturerView = null;
    private SurfaceHolder mHolder = null;
    private MediaCodec mCodec = null;
    private VideoFormat mCurVideoFormat = VideoFormat.SCREEN_CAPTURER_1280_720_24;

    private boolean bIsStop = true;
    private boolean bIsInit = false;

    private Thread mPlayScreenThread = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screencapturer_test_demo, container, false);

        mStartBtn = (Button) view.findViewById(R.id.start_screencaputer);
        mStopBtn = (Button) view.findViewById(R.id.stop_screencaputer);

        mScreenCapturerView = (SurfaceView) view.findViewById(R.id.screencapturer_surfaceview);
        mHolder = mScreenCapturerView.getHolder();

        //MediaProjectionManager
        MediaFormat.createVideoFormat("video/avc", mCurVideoFormat.getWidth(), mCurVideoFormat.getHeight());
        return view;
    }

    @OnClick({R.id.stop_screencaputer, R.id.start_screencaputer})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_screencaputer:
                if (!bIsInit) {
                    initDecoder();
                    bIsInit = true;
                }

                //mPlayScreenThread = new Thread(readFile);
                //mPlayScreenThread.start();
                break;
            default:
                break;
        }
    }

    public void initDecoder() {

        try {
            mCodec = MediaCodec.createDecoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE,
                mCurVideoFormat.getWidth(), mCurVideoFormat.getHeight());
        mCodec.configure(mediaFormat, mScreenCapturerView.getHolder().getSurface(),
                null, 0);
        mCodec.start();
    }

    int mCount = 0;

    public boolean onFrame(byte[] buf, int offset, int length) {
        Log.e("Media", "onFrame start");
        Log.e("Media", "onFrame Thread:" + Thread.currentThread().getId());
        // Get input buffer index
        ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
        int inputBufferIndex = mCodec.dequeueInputBuffer(100);

        Log.e("Media", "onFrame index:" + inputBufferIndex);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(buf, offset, length);
            mCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount
                    * mCurVideoFormat.getFrameRate(), 0);
            mCount++;
        } else {
            return false;
        }

        // Get output buffer index
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 100);
        while (outputBufferIndex >= 0) {
            mCodec.releaseOutputBuffer(outputBufferIndex, true);
            outputBufferIndex = mCodec.dequeueOutputBuffer(bufferInfo, 0);
        }
        Log.e("Media", "onFrame end");
        return true;
    }

    /**
     * Find H264 frame head
     *
     * @param buffer
     * @param len
     * @return the offset of frame head, return 0 if can not find one
     */
    static int findHead(byte[] buffer, int len) {
        int i;
        for (i = HEAD_OFFSET; i < len; i++) {
            if (checkHead(buffer, i))
                break;
        }
        if (i == len)
            return 0;
        if (i == HEAD_OFFSET)
            return 0;
        return i;
    }

    /**
     * Check if is H264 frame head
     *
     * @param buffer
     * @param offset
     * @return whether the src buffer is frame head
     */
    static boolean checkHead(byte[] buffer, int offset) {
        // 00 00 00 01
        if (buffer[offset] == 0 && buffer[offset + 1] == 0
                && buffer[offset + 2] == 0 && buffer[3] == 1)
            return true;
        // 00 00 01
        if (buffer[offset] == 0 && buffer[offset + 1] == 0
                && buffer[offset + 2] == 1)
            return true;
        return false;
    }

    /*private Runnable mPlayRunable = new Runnable() {

        @Override
        public void run() {
            int h264Read = 0;
            int frameOffset = 0;
            byte[] buffer = new byte[100000];
            byte[] framebuffer = new byte[200000];
            boolean readFlag = true;
            try {
                fs = new FileInputStream(h264File);
                is = new BufferedInputStream(fs);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (!Thread.interrupted() && readFlag) {
                try {
                    int length = is.available();
                    if (length > 0) {
                        // Read file and fill buffer
                        int count = is.read(buffer);
                        Log.i("count", "" + count);
                        h264Read += count;
                        Log.d("Read", "count:" + count + " h264Read:"
                                + h264Read);
                        // Fill frameBuffer
                        if (frameOffset + count < 200000) {
                            System.arraycopy(buffer, 0, framebuffer,
                                    frameOffset, count);
                            frameOffset += count;
                        } else {
                            frameOffset = 0;
                            System.arraycopy(buffer, 0, framebuffer,
                                    frameOffset, count);
                            frameOffset += count;
                        }

                        // Find H264 head
                        int offset = findHead(framebuffer, frameOffset);
                        Log.i("find head", " Head:" + offset);
                        while (offset > 0) {
                            if (checkHead(framebuffer, 0)) {
                                // Fill decoder
                                boolean flag = onFrame(framebuffer, 0, offset);
                                if (flag) {
                                    byte[] temp = framebuffer;
                                    framebuffer = new byte[200000];
                                    System.arraycopy(temp, offset, framebuffer,
                                            0, frameOffset - offset);
                                    frameOffset -= offset;
                                    Log.e("Check", "is Head:" + offset);
                                    // Continue finding head
                                    offset = findHead(framebuffer, frameOffset);
                                }
                            } else {

                                offset = 0;
                            }

                        }
                        Log.d("loop", "end loop");
                    } else {
                        h264Read = 0;
                        frameOffset = 0;
                        readFlag = false;
                        // Start a new thread
                        readFileThread = new Thread(readFile);
                        readFileThread.start();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(mCurVideoFormat.getFrameRate());
                } catch (InterruptedException e) {

                }
            }
        }
    };*/
}
