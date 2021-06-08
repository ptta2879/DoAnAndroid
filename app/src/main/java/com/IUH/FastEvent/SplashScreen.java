package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.IUH.FastEvent.Model.CongTacVien;
import com.IUH.FastEvent.Model.SharedPreferencesFastEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG ="ManHinhSplash" ;
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
           private final StringBuilder url=new StringBuilder("https://ptta-cnm.herokuapp.com/congtacvien/");
           private final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS)
                   .retryOnConnectionFailure(true).build();
           @Override
           public void run() {
               if(fastEvent.getSharedPreferences(KEY_FIRST_APP)){
                   if (root.getCurrentUser() != null ){
                       new Thread(new Runnable(){

                           @Override
                           public void run() {
                               try {
                                   url.append(root.getUid());
                                   Request.Builder builder = new Request.Builder();
                                   builder.url(url.toString());
                                   Request request = builder.build();
                                   Response response = okHttpClient.newCall(request).execute();
                                   String noidung = Objects.requireNonNull(response.body()).string();
                                   Gson gson = new Gson();
                                   CongTacVien[] congTacVien = gson.fromJson(noidung, CongTacVien[].class);
                                   moActivity(MenuChucNang.class,congTacVien[0].getPhanQuyen());
                               }catch (IOException e) {
                                   e.printStackTrace();
                                   Log.w(TAG,e.getMessage(),e);
                               }
                           }
                       }).start();
                   }else {
                       moActivity(Login.class,null);
                   }
               }else{
                    moActivity(OnboardingScreen.class,null);
                    fastEvent.putFirstInstallApp(KEY_FIRST_APP,true);
               }
           }
       },2000);
    }
    private void anhXa(){
        root = FirebaseAuth.getInstance();
        logo = (ImageView) findViewById(R.id.imgSplash);
        logoIUH =(ImageView) findViewById(R.id.logoIuh);
    }
    private void moActivity(Class<?> cl, Integer phanQuyen){
        Intent ax = new Intent(SplashScreen.this, cl);
        if (phanQuyen != null){
            ax.putExtra(MenuChucNang.KEY_PHANQUYEN,phanQuyen);
        }
        startActivity(ax);
        finish();
    }
}