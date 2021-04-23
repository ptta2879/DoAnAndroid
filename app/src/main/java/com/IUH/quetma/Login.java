package com.IUH.quetma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.alterac.blurkit.BlurKit;
import io.alterac.blurkit.BlurLayout;

public class Login extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private FirebaseAuth root;
    private TextView chao;
    private EditText username;
    private EditText password;
    private Button btnDangNhap;
    private BlurLayout blurLayout;
    private ImageView imageNen,imageLogo;
    private Animation  smalltobig,fleft, fhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ActivityCompat.checkSelfPermission(Login.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(Login.this,Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this,
                    new String[] { Manifest.permission.CAMERA, Manifest.permission.INTERNET }, REQUEST_CAMERA);
            return;
        }
        AnhXa();

        fleft = AnimationUtils.loadAnimation(this, R.anim.fleft);
        fhelper = AnimationUtils.loadAnimation(this, R.anim.fhelper);

        blurLayout.startBlur();
        imageLogo.setTranslationX(400);
        chao.setTranslationX(400);
        username.setTranslationX(400);
        password.setTranslationX(400);
        btnDangNhap.setTranslationX(400);

        imageLogo.setAlpha(0.0f);
        chao.setAlpha(0.0f);
        username.setAlpha(0.0f);
        password.setAlpha(0.0f);
        btnDangNhap.setAlpha(0.0f);


        imageLogo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        chao.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        username.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        btnDangNhap.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        root = FirebaseAuth.getInstance();
        btnDangNhap.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usernameString  = username.getText().toString();
                        String passwordString = password.getText().toString();
                        if(usernameString.isEmpty() || passwordString.isEmpty()){
                            Toast.makeText(Login.this, "Chưa nhập đủ username và password" ,Toast.LENGTH_SHORT).show();
                        }else{
                            root.signInWithEmailAndPassword(usernameString, passwordString)
                                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Intent ax = new Intent(Login.this, MenuChucNang.class);
                                                startActivity(ax);
                                                overridePendingTransition(R.anim.fleft,R.anim.fhelper);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Toast.makeText(Login.this, "Khong Thanh Cong" ,Toast.LENGTH_SHORT).show();
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
        blurLayout.startBlur();
        FirebaseUser check = root.getCurrentUser();
        //check dang nhap
        if (check != null){
            Intent ax = new Intent(Login.this, MenuChucNang.class);
            startActivity(ax);
            overridePendingTransition(R.anim.fleft,R.anim.fhelper);
        }
    }

    @Override
    protected void onStop() {
        AnhXa();
        blurLayout.pauseBlur();
        super.onStop();
    }

    public void AnhXa(){
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnDangNhap = (Button) findViewById(R.id.dangnhap);
        blurLayout = findViewById(R.id.blurLayout);
        imageNen = (ImageView) findViewById(R.id.imageView2);
        imageLogo = (ImageView) findViewById(R.id.imageView);
        chao = (TextView) findViewById(R.id.textView3);
    }
}