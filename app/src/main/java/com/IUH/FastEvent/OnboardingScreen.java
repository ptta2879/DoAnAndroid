package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class OnboardingScreen extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private FirebaseAuth root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.onboarding_main_layout);
        root=FirebaseAuth.getInstance();
        PaperOnboardingEngine paperOnboardingEngine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView) ,
                getPaper(),this);
        paperOnboardingEngine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                quyenCamera();
            }
        });
    }
    private ArrayList<PaperOnboardingPage> getPaper(){
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Xin Chào",
                "Chào mừng các cộng tác viên đến với Fast Event",
                Color.parseColor("#678FB4"), R.drawable.hello_lon, R.drawable.hello_icon);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("BlockChain",
                "Với công nghệ BlockChain sẽ đảm bảo thông tin vé không thể thay đổi và minh bạch",
                Color.parseColor("#65B0B4"), R.drawable.blockchain_lon, R.drawable.blockchain_icon);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Ethereum",
                "Fast Event được xây dựng dựa vào nền tảng ETH được dựa trên công nghệ BlockChain",
                Color.parseColor("#9B90BC"), R.drawable.ethereum_lon, R.drawable.ethereum_icon);
        PaperOnboardingPage scr4 = new PaperOnboardingPage("BarCode",
                "Ứng dụng cho phép quét mã vạch trên thẻ sinh viên một cách nhanh chóng. " +
                        "Vì thế cần được cấp quyền Camera",
                Color.parseColor("#bc90b8"), R.drawable.barcode_lon, R.drawable.barcode_icon);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        elements.add(scr4);
        return elements;
    }
    @AfterPermissionGranted(123)
    private void quyenCamera(){
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)){
            if(root.getCurrentUser() != null){
                Intent intent = new Intent(this, MenuChucNang.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
            }

        }else{
            EasyPermissions.requestPermissions(this,"Chúng tôi cần quền này để có thể quét mã sinh viên",
                    123, perms);
        }
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