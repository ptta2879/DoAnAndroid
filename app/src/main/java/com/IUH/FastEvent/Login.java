package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.CongTacVien;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Login extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final String TAG ="Login.java" ;
    private FirebaseAuth root;
    private TextView chao, chaoCongTacVien, txtDangNhap;
    private TextInputLayout username;
    private TextInputLayout password;
    private FloatingActionButton btnDangNhap;
    private ImageView imageNen,imageLogo;
    private Common common;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        common = new Common();
        AnhXa();


        imageLogo.setTranslationX(400);
        chao.setTranslationX(400);
        username.setTranslationX(400);
        password.setTranslationX(400);
        btnDangNhap.setTranslationX(400);
        chaoCongTacVien.setTranslationX(400);
        txtDangNhap.setTranslationX(400);

        imageLogo.setAlpha(0.0f);
        chao.setAlpha(0.0f);
        username.setAlpha(0.0f);
        password.setAlpha(0.0f);
        btnDangNhap.setAlpha(0.0f);
        chaoCongTacVien.setAlpha(0.0f);
        txtDangNhap.setAlpha(0.0f);


        imageLogo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        chao.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        btnDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        chaoCongTacVien.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
         txtDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        root = FirebaseAuth.getInstance();
        btnDangNhap.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usernameString  = Objects.requireNonNull(username.getEditText()).getText().toString();
                        String passwordString = Objects.requireNonNull(password.getEditText()).getText().toString();
                        if(usernameString.isEmpty() || passwordString.isEmpty()){
                            if(usernameString.isEmpty()){
                                username.setError("Không được để trống");
                            }
                            if (passwordString.isEmpty()){
                                password.setError("Không được để trống");
                            }
                            if(usernameString.isEmpty() && passwordString.isEmpty()){
                                username.setError("Không được để trống");
                                password.setError("Không được để trống");
                            }
                        }else{
                            pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Đang xử lý");
                            pDialog.setCancelable(false);
                            pDialog.show();
                            root.signInWithEmailAndPassword(usernameString, passwordString)
                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                new Thread(new Runnable() {
                                                    private final StringBuilder url=new StringBuilder("https://ptta-cnm.herokuapp.com/congtacvien/");
                                                    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS)
                                                            .retryOnConnectionFailure(true).build();
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
                                                            pDialog.dismissWithAnimation();
                                                            Intent ax = new Intent(Login.this, MenuChucNang.class);
                                                            ax.putExtra(MenuChucNang.KEY_PHANQUYEN,congTacVien[0].getPhanQuyen());
                                                            startActivity(ax);
                                                            finish();
                                                        }catch (IOException e) {
                                                            e.printStackTrace();
                                                            Log.w(TAG,e.getMessage(),e);
                                                        }
                                                    }
                                                }).start();
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                pDialog.dismissWithAnimation();
                                                Toast.makeText(Login.this, "Sai Tài Khoản Hoặc Mật Khẩu" ,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    }
                }
        );
    }

    @Override
    protected void onResume() {
        AnhXa();
        super.onResume();

        //check dang nhap
        if ( root.getCurrentUser() != null){
            Intent ax = new Intent(Login.this, MenuChucNang.class);
            startActivity(ax);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);
    }

    @Override
    protected void onStop() {
        AnhXa();
        super.onStop();
        unregisterReceiver(common);
    }

    public void AnhXa(){
        username = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.password);
        btnDangNhap = (FloatingActionButton) findViewById(R.id.dangnhap);
        imageNen = (ImageView) findViewById(R.id.imageView2);
        imageLogo = (ImageView) findViewById(R.id.imageView);
        chao = (TextView) findViewById(R.id.textView3);
        chaoCongTacVien = (TextView) findViewById(R.id.xinChaoCongTacVien);
        txtDangNhap = findViewById(R.id.textDangNhap);
    }

}