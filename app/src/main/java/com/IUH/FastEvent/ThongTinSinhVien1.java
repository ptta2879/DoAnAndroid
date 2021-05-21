package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.SuKien;
import com.IUH.FastEvent.Model.Ve;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.r0adkll.slidr.Slidr;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.io.Serializable;
import java.io.SerializablePermission;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import jnr.ffi.annotations.In;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThongTinSinhVien1 extends AppCompatActivity {
    private ImageView imgBarCode;
    private FloatingActionButton nutGiaoDich;
    private TextView mssvSinhVien1,tenSinhVien1,gioiTinhSinhVien1,khoaSinhVien1,lopSinhVien1,
            ngaySinhSinhVien1,soHuuSinhVien1,maVeSinhVien1,nguoiTaoVeSinhVien1,maSuKienSinhVien1,ngayLapVeSinhVien1;
    private String mssv;
    private SinhVien thongTinSinhVien;
    private Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> thongtinVe;
    private Common common;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private CheckTienTrinh checkTienTrinh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common = new Common();
        checkTienTrinh = new CheckTienTrinh();
        setContentView(R.layout.activity_thong_tin_sinh_vien1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Slidr.attach(this);
        AnhXa();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        linearLayout.setVisibility(View.GONE);
        Intent intent =getIntent();
        mssv = intent.getStringExtra("mssvGiaoDich");

        executorService.submit(new ThongTinSinhVien());
        executorService.submit(new ThongTinVe());
        nutGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ve ve = new Ve(thongtinVe);

                executorService.shutdown();
            }
        });
    }
    class CheckTienTrinh{
        private boolean t1;
        private boolean t2;

        public void setT1(boolean t1) {
            this.t1 = t1;
        }

        public void setT2(boolean t2) {
            this.t2 = t2;
        }
        public void check(){
            if (t1 && t2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }
    private void showVe(Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> ve){
        String nguoiTao = ve.component2();
        String maSuKien = ve.component3();
        String maVe = ve.component6();
        BigInteger dateTime = ve.component8();
        long dateTimeLong = dateTime.longValue() * 1000;
        String dateTimeString;
        Date ngayLap = new Date(dateTimeLong);
        TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        ngayLapFormat.setTimeZone(timeZoneVN);
        dateTimeString = ngayLapFormat.format(ngayLap);
        if (!ve.component9() && ve.component1().compareTo(new BigInteger("0")) == 0 && ve.component2().isEmpty() && ve.component3().isEmpty()
                && ve.component4().isEmpty() && ve.component5().isEmpty() && ve.component6().isEmpty()
                && ve.component8().compareTo(new BigInteger("0")) == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String thongTinKhongCo = "Không Có Thông Tin Vé";
                    maVeSinhVien1.setText(thongTinKhongCo);
                    LinearLayout thongTin1 = findViewById(R.id.chiTietThongTinVeSinhVien1);
                    thongTin1.setVisibility(View.GONE);
                    nutGiaoDich.setVisibility(View.GONE);
                }
            });

        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ve.component9()){
                        soHuuSinhVien1.setTextColor(Color.GREEN);
                        soHuuSinhVien1.setText("Vé Hợp Lệ");
                    }else{
                        soHuuSinhVien1.setTextColor(Color.RED);
                        soHuuSinhVien1.setText("Không Sở Hữu");
                        nutGiaoDich.setVisibility(View.GONE);
                    }
                    maVeSinhVien1.setText(maVe);
                    nguoiTaoVeSinhVien1.setText(nguoiTao);
                    maSuKienSinhVien1.setText(maSuKien);
                    ngayLapVeSinhVien1.setText(dateTimeString);
                }
            });
        }
    }
    private void showThongTin(SinhVien sinhVien ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = writer.encode(mssv,BarcodeFormat.CODE_39,600,150);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imgBarCode.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                String fullname = sinhVien.getHovaten() + " " +sinhVien.getTen();
                mssvSinhVien1.setText(mssv);
                tenSinhVien1.setText(fullname);
                if(sinhVien.getGoitinh() == 1 ){
                    gioiTinhSinhVien1.setText("Nam");
                }else{
                    gioiTinhSinhVien1.setText("Nữ");
                }
                khoaSinhVien1.setText(sinhVien.getKhoa());
                lopSinhVien1.setText(sinhVien.getLop());
                ngaySinhSinhVien1.setText(sinhVien.getNgaySinh());
            }
        });

    }
    private void AnhXa(){
        nutGiaoDich =(FloatingActionButton) findViewById(R.id.nutSinhVien1);
        imgBarCode = findViewById(R.id.imageSinhVien1);
        mssvSinhVien1 = (TextView) findViewById(R.id.mssvSinhVien1);
        tenSinhVien1 = (TextView) findViewById(R.id.thongTinTenSinhVien1);
        gioiTinhSinhVien1 = (TextView) findViewById(R.id.thongTinGioiTinhSinhVien1);
        khoaSinhVien1 = (TextView) findViewById(R.id.thongTinKhoaSinhVien1);
        lopSinhVien1 = (TextView) findViewById(R.id.thongTinLopSinhVien1);
        ngaySinhSinhVien1 = findViewById(R.id.thongTinNgaySinhSinhVien1);
        soHuuSinhVien1 = (TextView) findViewById(R.id.thongTinSoHuuSinhVien1);
        maVeSinhVien1 = (TextView) findViewById(R.id.thongTinMaVeSinhVien1);
        nguoiTaoVeSinhVien1 = (TextView) findViewById(R.id.thongTinNguoiTaoVeSinhVien1);
        maSuKienSinhVien1 = (TextView) findViewById(R.id.thongTinMaSuKienSinhVien1);
        ngayLapVeSinhVien1 = (TextView) findViewById(R.id.thongTinNgayLapSinhVien1);
        linearLayout = findViewById(R.id.noiDungVe);
        progressBar = findViewById(R.id.progressBar2);
    }
    class ThongTinSinhVien implements Runnable {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        @Override
        public void run() {
            try {
                String url = "https://ptta-cnm.herokuapp.com/taikhoan/" + mssv;
                Request.Builder builder = new Request.Builder();
                Request request  = builder.url(url).build();
                Response response = okHttpClient.newCall(request).execute();
                String noiDung = Objects.requireNonNull(response.body()).string();
                Gson gson = new Gson();
                SinhVien[] sinhViens = gson.fromJson(noiDung,SinhVien[].class);
                thongTinSinhVien = sinhViens[0];
                showThongTin(thongTinSinhVien);
                checkTienTrinh.setT1(true);
                checkTienTrinh.check();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
                Alerter.create(ThongTinSinhVien1.this)
                        .setTitle("Thông Báo").setText("Xảy ra lỗi kết nối")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nutGiaoDich.setVisibility(View.GONE);
                    }
                });
            }

        }
    }
    class ThongTinVe implements Runnable{
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        private String getSuKien() throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noidung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
            return suKiens[0].getMasukien();
        }

        @Override
        public void run() {
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j,thongTinWeb3.getCredentialsWallet(),
                    new DefaultGasProvider());
            BigInteger mssvBigInteger = new BigInteger(mssv);
            try {
                Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> duLieu
                        = sukien_sol_sukien.searVe(mssvBigInteger,getSuKien()).send();
                thongtinVe = duLieu;
                showVe(duLieu);
                checkTienTrinh.setT2(true);
                checkTienTrinh.check();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", e.getMessage());
                Alerter.create(ThongTinSinhVien1.this)
                        .setTitle("Thông Báo").setText("Xảy ra lỗi kết nối")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nutGiaoDich.setVisibility(View.GONE);
                    }
                });
            }
            web3j.shutdown();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(common);
    }
}