package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.IUH.FastEvent.Model.CongTacVien;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.r0adkll.slidr.Slidr;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class XacNhanHoatDong extends AppCompatActivity {
    private CodeScanner codeScanner;
    private ExecutorService executorService;
    protected String mssv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_hoat_dong);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Slidr.attach(this);
        CodeScannerView codeScannerView = findViewById(R.id.xacNhanHoatDong);
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                mssv = result.getText();
                executorService = Executors.newFixedThreadPool(1);
                executorService.execute(new HoatDong(mssv));
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    protected void onStop() {
        executorService.shutdown();
        super.onStop();
    }
    static class ThongBao {
        private Boolean success;

        public Boolean getSuccess() {
            return success;
        }
    }
    class HoatDong implements Runnable{
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
        private final String maSoSinhVien;
        private CongTacVien congTacVien;
        public HoatDong(String mssv){
            this.maSoSinhVien = mssv;
        }
        @Override
        public void run() {
            try {
                congTacVien = congTacVien();
            } catch (IOException e) {
                e.printStackTrace();
                Alerter.create(XacNhanHoatDong.this)
                        .setTitle("Thông Báo").setText("Lỗi kết nối")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
            if (congTacVien.getHoatdong() != null){
                try {
                    capNhapHoatDong(congTacVien.getHoatdong());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Alerter.create(XacNhanHoatDong.this)
                        .setTitle("Thông Báo").setText("Chưa phân công hoạt động")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
        }
        protected CongTacVien congTacVien() throws IOException {
            String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
            String url = "https://ptta-cnm.herokuapp.com/congtacvien/"+ user;
            Request.Builder builder = new Request.Builder().url(url);
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            CongTacVien[] congTacViens = gson.fromJson(noiDung,CongTacVien[].class);
            return congTacViens[0];
        }
        public void  capNhapHoatDong(String hoatDong) throws IOException {
            String mssvHoatDong = maSoSinhVien+"/"+hoatDong;
            Request.Builder builder = new Request.Builder().url("https://ptta-cnm.herokuapp.com/taikhoan/hoatdong/" + mssvHoatDong);
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            String noidDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            ThongBao success = gson.fromJson(noidDung,ThongBao.class);
            if(success.getSuccess()){
                Alerter.create(XacNhanHoatDong.this)
                        .setTitle("Thông Báo").setText("Xác nhận hoạt động thành công")
                        .setBackgroundColorRes(R.color.success)
                        .setIcon(R.drawable.ic_baseline_check)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }else{
                Alerter.create(XacNhanHoatDong.this)
                        .setTitle("Thông Báo").setText("Xảy ra lỗi")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
        }
    }
}