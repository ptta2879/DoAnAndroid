package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.IUH.FastEvent.Model.CongTacVien;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.tapadoo.alerter.Alerter;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuChucNang extends AppCompatActivity {
    private FirebaseAuth root;
    private ImageButton btnDangXuat;
    private ImageView backImage,logo;
    private TextView chu,chaoUser;
    private CardView chuNang1,chuNang2,chuNang3,chuNang4;
    private Integer phanQuyen = 0;
    BlurView blurView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_chuc_nang);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        AnhXa();
        logo.setScaleX(0f);
        logo.setScaleY(0f);
        logo.setPivotX(0.5f);
        logo.setPivotY(0.5f);
        chu.setTranslationX(400);
        chaoUser.setTranslationX(400);
        chuNang1.setTranslationX(400);
        chuNang2.setTranslationY(400);
        chuNang3.setTranslationY(400);
        chuNang4.setTranslationX(400);
        logo.setAlpha(0.0f);
        chu.setAlpha(0.0f);
        chaoUser.setAlpha(0.0f);
        chuNang1.setAlpha(0.0f);
        chuNang2.setAlpha(0.0f);
        chuNang3.setAlpha(0.0f);
        chuNang4.setAlpha(0.0f);

        logo.animate().scaleY(1).scaleX(1).alpha(1).setDuration(800).setStartDelay(600).start();
        chu.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        chaoUser.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(750).start();
        chuNang1.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(800).start();
        chuNang2.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(900).start();
        chuNang3.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(1000).start();
        chuNang4.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();
        root = FirebaseAuth.getInstance();
        FirebaseUser user = root.getCurrentUser();
        String email = user != null ? user.getEmail() : null;
        if (email != null){
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Integer> quyen  =  executorService.submit(new GetQuyen(email));
            chaoUser.setText("Xin Chào\n"+email);
            try {
                phanQuyen = quyen.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            Log.e("Error Email","Lỗi email không lấy được");
        }
        chuNang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phanQuyen == 1 ){
                    Intent intent = new Intent(MenuChucNang.this,ThemVe.class);
                    startActivity(intent);
                }else {
                    Alerter.create(MenuChucNang.this)
                            .setTitle("Thông Báo").setText("Không có quyền thực hiện điều này")
                            .setBackgroundColorRes(R.color.red)
                            .setIcon(R.drawable.ic_baseline_close_24)
                            .enableSwipeToDismiss().setDuration(4000).show();
                }
            }
        });
        chuNang3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phanQuyen ==3){
                    Intent intent = new Intent(MenuChucNang.this, GiaoDichSinhVien1.class);
                    startActivity(intent);
                }else{
                    Alerter.create(MenuChucNang.this)
                            .setTitle("Thông Báo").setText("Không có quyền thực hiện điều này")
                            .setBackgroundColorRes(R.color.red)
                            .setIcon(R.drawable.ic_baseline_close_24)
                            .enableSwipeToDismiss().setDuration(4000).show();
                }
            }
        });
        chuNang4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phanQuyen == 2){
                    Intent intent = new Intent(MenuChucNang.this,XacNhanHoatDong.class);
                    startActivity(intent);
                }else {
                    Alerter.create(MenuChucNang.this)
                            .setTitle("Thông Báo").setText("Không có quyền thực hiện điều này")
                            .setBackgroundColorRes(R.color.red)
                            .setIcon(R.drawable.ic_baseline_close_24)
                            .enableSwipeToDismiss().setDuration(4000).show();
                }
            }
        });
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
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnhXa();

    }

    @Override
    protected void onStop() {
        AnhXa();

        super.onStop();
    }
    public void setBlurView(){
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable drawable = decorView.getBackground();

        blurView = findViewById(R.id.genaral_blur);
        blurView.setupWith(rootView).setFrameClearDrawable(drawable)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(20f)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }

    public void AnhXa(){

        backImage =(ImageView) findViewById(R.id.backChucNang);
        logo =(ImageView) findViewById(R.id.logoChucNag);
        chu =(TextView) findViewById(R.id.txtChucNang);
        chuNang1 =(CardView) findViewById(R.id.chucnang1);
        chuNang2 =(CardView) findViewById(R.id.chucnang2);
        chuNang3 =(CardView) findViewById(R.id.chucnang3);
        chuNang4 =(CardView) findViewById(R.id.chucnang4);
        chaoUser = (TextView) findViewById(R.id.chaoUser);
        btnDangXuat =(ImageButton) findViewById(R.id.dangxuat);
    }
    static class GetQuyen implements Callable<Integer>{
        private final String emailGet;
        private final StringBuilder url=new StringBuilder("https://ptta-cnm.herokuapp.com/congtacvien/");
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
        public GetQuyen(String email){
            this.emailGet = email;
        }
        @Override
        public Integer call() throws Exception {
            url.append(emailGet);
            Request.Builder builder = new Request.Builder();
            builder.url(url.toString());
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            String noidung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            CongTacVien[] congTacVien = gson.fromJson(noidung, CongTacVien[].class);
            return congTacVien[0].getPhanQuyen();
        }
    }
}