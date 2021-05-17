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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class Login extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private FirebaseAuth root;
    private TextView chao, chaoCongTacVien, txtDangNhap;
    private TextInputLayout username;
    private TextInputLayout password;
    private FloatingActionButton btnDangNhap;
    private ImageView imageNen,imageLogo;
    private Common common;

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
                            root.signInWithEmailAndPassword(usernameString, passwordString)
                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Toast.makeText(Login.this, "Đang Xử Lý..." ,Toast.LENGTH_SHORT).show();
                                                Intent ax = new Intent(Login.this, MenuChucNang.class);
                                                startActivity(ax);
                                                finish();
                                            } else {
                                                // If sign in fails, display a message to the user.
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