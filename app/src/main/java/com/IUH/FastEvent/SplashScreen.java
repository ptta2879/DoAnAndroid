package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.IUH.FastEvent.Model.SharedPreferencesFastEvent;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    private ImageView logo,logoIUH;
    private FirebaseAuth root;
    Animation topanim, bottomAnim;
    private static final String KEY_FIRST_APP="Lan_dau";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);
        anhXa();
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_splash);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_splash);

        logo.setAnimation(topanim);
        logoIUH.setAnimation(bottomAnim);

        SharedPreferencesFastEvent fastEvent = new SharedPreferencesFastEvent(this);

       new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
           @Override
           public void run() {
               if(fastEvent.getSharedPreferences(KEY_FIRST_APP)){
                   if (root.getCurrentUser() != null ){
                       moActivity(MenuChucNang.class);
                   }else {
                       moActivity(Login.class);
                   }
               }else{
                    moActivity(OnboardingScreen.class);
                    fastEvent.putFirstInstallApp(KEY_FIRST_APP,true);
               }

           }
       },5000);
    }
    private void anhXa(){
        root = FirebaseAuth.getInstance();
        logo = (ImageView) findViewById(R.id.imgSplash);
        logoIUH =(ImageView) findViewById(R.id.logoIuh);
    }
    private void moActivity(Class<?> cl){
        Intent ax = new Intent(SplashScreen.this, cl);
        startActivity(ax);
        finish();
    }
}