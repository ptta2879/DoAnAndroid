package com.IUH.quetma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

interface GetThongTinTaoVe{
    SinhVien getThongTin(String mssv) throws IOException;
}
public class ThemVe extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private CodeScanner mCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ve);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CodeScannerView codeScannerView = findViewById(R.id.scannerThemVe);
        mCodeScanner = new CodeScanner(this, codeScannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    class TaoVe implements Runnable{
        private final String maSinhVien;
        private SinhVien thongTinSinhVien;
        private StringBuilder url = new StringBuilder("https://ptta-cnm.herokuapp.com/taikhoan/");
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
        public TaoVe(String mssv){
            this.maSinhVien=mssv;
        }
        @Override
        public void run() {
            try {
                checkHoatDong();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GetThongTinTaoVe thongTin = (mssv) ->{
                String urlFull = url.append(mssv).toString();
                Request.Builder builder = new Request.Builder();
                builder.url(urlFull);
                Request request = builder.build();
                Response response = okHttpClient.newCall(request).execute();
                String noiDung = Objects.requireNonNull(response.body()).string();
                Gson gson= new Gson();
                SinhVien[] sinhVien = gson.fromJson(noiDung,SinhVien[].class);
                return sinhVien[0];
            };
            try {
                thongTinSinhVien = thongTin.getThongTin(maSinhVien);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(thongTinSinhVien.getMssv() != null){
                ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
                Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
                Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,
                        web3j,
                        thongTinWeb3.getCredentialsWallet(),
                        new DefaultGasProvider());
            }else{
                Alerter.create(ThemVe.this)
                        .setTitle("Thông Báo").setText("Không có thông tin")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24).enableProgress(true)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
        }
        public Boolean checkHoatDong() throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            String noidung = response.body().string();
            Gson gson = new Gson();
            SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
            Log.e("test", suKiens[0].toString());
            return null;
        }
    }
}