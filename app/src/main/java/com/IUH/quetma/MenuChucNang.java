package com.IUH.quetma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.alterac.blurkit.BlurLayout;

public class MenuChucNang extends AppCompatActivity {
    private FirebaseAuth root;
    private Button btnDangXuat;
    private BlurLayout blurLayout;
    private ImageView backImage,logo;
    private TextView chu;
    private CardView chuNang1,chuNang2,chuNang3,chuNang4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chuc_nang);
        AnhXa();
        blurLayout.startBlur();

        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.setPivotX(0.5f);
        logo.setPivotY(0.5f);
        chu.setTranslationX(400);
        chuNang1.setTranslationX(400);
        chuNang2.setTranslationY(400);
        chuNang3.setTranslationY(400);
        chuNang4.setTranslationX(400);

        logo.setAlpha(0.0f);
        chu.setAlpha(0.0f);
        chuNang1.setAlpha(0.0f);
        chuNang2.setAlpha(0.0f);
        chuNang3.setAlpha(0.0f);
        chuNang4.setAlpha(0.0f);

        logo.animate().scaleY(1).scaleX(1).alpha(1).setDuration(800).setStartDelay(600).start();
        chu.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        chuNang1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();
        chuNang2.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        chuNang3.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(1000).start();
        chuNang4.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        root = FirebaseAuth.getInstance();
        System.out.println(root.getCurrentUser().getEmail());
        chuNang2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent quetma = new Intent(MenuChucNang.this, MainActivity.class);
                startActivity(quetma);
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(MenuChucNang.this, Login.class);
                startActivity(login);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnhXa();
        blurLayout.startBlur();
    }

    @Override
    protected void onStop() {
        AnhXa();
        blurLayout.startBlur();
        super.onStop();
    }

    public void AnhXa(){
        blurLayout = findViewById(R.id.blurLayout);
        backImage =(ImageView) findViewById(R.id.backChucNang);
        logo =(ImageView) findViewById(R.id.logoChucNag);
        chu =(TextView) findViewById(R.id.txtChucNang);
        chuNang1 =(CardView) findViewById(R.id.chucnang1);
        chuNang2 =(CardView) findViewById(R.id.chucnang2);
        chuNang3 =(CardView) findViewById(R.id.chucnang3);
        chuNang4 =(CardView) findViewById(R.id.chucnang4);
        btnDangXuat = (Button) findViewById(R.id.dangxuat);

    }
}