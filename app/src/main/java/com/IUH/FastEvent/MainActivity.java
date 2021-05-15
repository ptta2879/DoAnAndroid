package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int REQUEST_CAMERA = 1;
    public CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Slidr.attach(this);
        CodeScannerView codeScannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, codeScannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent thongtin = new Intent(MainActivity.this, ThongTinVe.class);
                        thongtin.putExtra("mssv", result.getText());
                        startActivity(thongtin);
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @AfterPermissionGranted(123)
            @Override
            public void onClick(View v) {
                String[] perms = {Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(MainActivity.this, perms)){
                    mCodeScanner.startPreview();
                }else {
                    EasyPermissions.requestPermissions(MainActivity.this,"Chúng tôi cần quền này để có thể quét mã sinh viên",
                            123, perms);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @AfterPermissionGranted(123)
    @Override
    protected void onResume() {
        super.onResume();
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)){
            mCodeScanner.startPreview();
        }else {
            EasyPermissions.requestPermissions(this,"Chúng tôi cần quền này để có thể quét mã sinh viên",
                    123, perms);
        }

    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}