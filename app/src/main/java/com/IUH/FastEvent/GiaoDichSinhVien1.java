package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GiaoDichSinhVien1 extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public CodeScannerView codeScannerView;
    private CodeScanner codeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dich_sinh_vien1);
        Slidr.attach(this);
        codeScannerView = findViewById(R.id.giaoDichSinhVien1);
        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GiaoDichSinhVien1.this,"Đang Xử Lý...",Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(GiaoDichSinhVien1.this,ThongTinSinhVien1.class);
                                intent.putExtra("mssvGiaoDich", result.getText());
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @AfterPermissionGranted(123)
            @Override
            public void onClick(View v) {
                String[] perms = {Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(GiaoDichSinhVien1.this,perms)){
                    codeScanner.startPreview();
                }else{
                    EasyPermissions.requestPermissions(GiaoDichSinhVien1.this,"Chúng tôi cần quền này để có thể quét mã sinh viên",
                            123, perms);
                }
            }
        });
    }

    @AfterPermissionGranted(123)
    @Override
    protected void onResume() {
        super.onResume();
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this,perms)){
            codeScanner.startPreview();
        }else{
            EasyPermissions.requestPermissions(this,"Chúng tôi cần quền này để có thể quét mã sinh viên",
                    123, perms);
        }

    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults, this);
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