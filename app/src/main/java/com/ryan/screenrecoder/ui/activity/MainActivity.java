package com.ryan.screenrecoder.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ryan.screenrecoder.R;
import com.ryan.screenrecoder.application.ScreenApplication;
import com.ryan.screenrecoder.application.SysValue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUEST_CODE = 0x11;
    private final int PERMISSION_CODE = 0x12;


    private Button button_local_preview;
    private Button button_local_save;
    private Button button_tcp_send;
    private EditText edittext_tcp_send_ip;

    private MediaProjectionManager mediaProjectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_local_preview = ((Button) findViewById(R.id.button_local_preview));
        button_local_save = ((Button) findViewById(R.id.button_local_save));
        button_tcp_send = ((Button) findViewById(R.id.button_tcp_send));
        edittext_tcp_send_ip = ((EditText) findViewById(R.id.edittext_tcp_send_ip));
        button_local_preview.setOnClickListener(this);
        button_local_save.setOnClickListener(this);
        button_tcp_send.setOnClickListener(this);
        if (SysValue.api >= Build.VERSION_CODES.M) {
            getAppPermission();
        } else if (SysValue.api >= 21) {
            getMeidProjection();
        } else {
            //todo 需要root权限或系统签名
            ScreenApplication.getInstance().setDisplayManager(((DisplayManager) getSystemService(Context.DISPLAY_SERVICE)));
        }
    }

    private void getAppPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_CODE);
    }

    private void getMeidProjection() {
        mediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ScreenApplication.getInstance().setMediaProjection(mediaProjectionManager.getMediaProjection(resultCode, data));
        } else {
            Toast.makeText(this, "无法录制屏幕", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMeidProjection();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_local_preview:
                //todo 本地预览
                break;
            case R.id.button_local_save:
                //todo 本地保存
                break;
            case R.id.button_tcp_send:
                //todo 网络传送
                startActivity(new Intent(this, TcpSendActivity.class));
                break;
        }
    }
}
