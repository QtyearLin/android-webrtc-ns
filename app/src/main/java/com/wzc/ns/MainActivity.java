package com.wzc.ns;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    //ns 与 nxs
    private static final boolean DO_NS = true;
    //输入文件路径 手机根目录下ns_out.pcm
    private static final String OUT_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ns_out.pcm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                doWork();
            }
        }
    }

    private void doWork() {
        if (DO_NS) {
            doNs();
        } else {
            doNsx();
        }
    }

    private void doNs() {
        try {
            NsUtils nsUtils = new NsUtils();
            nsUtils.useNs().setNsConfig(8000, 2).prepareNs();

            Toast.makeText(this, "开始测试", Toast.LENGTH_LONG).show();

            InputStream fInt = getResources().openRawResource(R.raw.test_input);
            FileOutputStream fOut = new FileOutputStream(OUT_FILE_PATH);
            byte[] buffer = new byte[160];
            int bytes;

            while (fInt.read(buffer) != -1) {
                short[] inputData = new short[80];
                short[] outData = new short[80];
                ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(inputData);
                int ret = nsUtils.nsProcess(inputData, null, outData, null);

                Log.e(TAG, "ret = " + ret);

                fOut.write(shortArrayToByteArray(outData));
            }

            fInt.close();
            fOut.close();

            Toast.makeText(this, "测试结束，输出文件位于手机根目录下/ns_out.pcm", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void doNsx() {
        try {
            NsUtils nsUtils = new NsUtils();
            nsUtils.useNsx().setNsxConfig(8000, 2).prepareNsx();
            Toast.makeText(this, "开始测试", Toast.LENGTH_LONG).show();

            InputStream fInt = getResources().openRawResource(R.raw.test_input);
            FileOutputStream fOut = new FileOutputStream(OUT_FILE_PATH);
            byte[] buffer = new byte[160];
            int bytes;

            while (fInt.read(buffer) != -1) {
                short[] inputData = new short[80];
                short[] outData = new short[80];
                ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(inputData);
                int ret = nsUtils.nsxProcess(inputData, null, outData, null);

                Log.e(TAG, "ret = " + ret);

                fOut.write(shortArrayToByteArray(outData));
            }

            fInt.close();
            fOut.close();

            Toast.makeText(this, "测试结束，输出文件位于手机根目录下/ns_out.pcm", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // shortArray to byteArray
    public byte[] shortArrayToByteArray(short[] data) {
        byte[] byteVal = new byte[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byteVal[i * 2] = (byte) (data[i] & 0xff);
            byteVal[i * 2 + 1] = (byte) ((data[i] & 0xff00) >> 8);
        }
        return byteVal;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;   //发现有未通过权限
                    break;
                }
            }
        }
        if (hasPermissionDismiss) {
        } else {
            doWork();
        }
    }

}
