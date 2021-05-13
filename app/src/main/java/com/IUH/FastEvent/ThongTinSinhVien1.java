package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.SuKien;
import com.IUH.FastEvent.Model.Ve;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
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
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
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
    private Button nutGiaoDich;
    private TextView mssvSinhVien1,tenSinhVien1,gioiTinhSinhVien1,khoaSinhVien1,lopSinhVien1,
            ngaySinhSinhVien1,soHuuSinhVien1,maVeSinhVien1,nguoiTaoVeSinhVien1,maSuKienSinhVien1,ngayLapVeSinhVien1;
    private String mssv;
    private SinhVien thongTinSinhVien;
    private Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> thongtinVe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_sinh_vien1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Slidr.attach(this);
        AnhXa();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Intent intent =getIntent();
        mssv = intent.getStringExtra("mssvGiaoDich");

        Future<SinhVien> threadSinhVien =  executorService.submit(new ThongTinSinhVien());
        Future<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>> threadVe
                = executorService.submit(new ThongTinVe());
        try {
            thongTinSinhVien = threadSinhVien.get();
            thongtinVe
                    = threadVe.get();
            if(thongTinSinhVien.getGoitinh() != null && thongTinSinhVien.getHovaten() != null &&
                    thongTinSinhVien.getKhoa() != null &&
                    thongTinSinhVien.getLop() != null &&
                    thongTinSinhVien.getMssv() != null && thongTinSinhVien.getNganh() != null &&
                    thongTinSinhVien.getNgaySinh() != null){
                showThongTin(thongTinSinhVien, thongtinVe);
            }else{
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Thông Báo")
                        .setContentText("Không có thông tin của sinh viên này")
                        .setConfirmText("Xác nhận")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                                Intent intent1 = new Intent(ThongTinSinhVien1.this, MenuChucNang.class);
                                startActivity(intent1);
                            }
                        })
                        .show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Alerter.create(ThongTinSinhVien1.this)
                    .setTitle("Thông Báo").setText("Xảy ra lỗi kết nối")
                    .setBackgroundColorRes(R.color.red)
                    .setIcon(R.drawable.ic_baseline_close_24)
                    .enableSwipeToDismiss().setDuration(4000).show();
        }
        nutGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ve ve = new Ve(thongtinVe);
                Intent intentSinhVien2 = new Intent(ThongTinSinhVien1.this, GiaoDichSinhVien2.class);
                intentSinhVien2.putExtra(GiaoDichSinhVien2.THONG_TIN_SINH_VIEN, thongTinSinhVien);
                intentSinhVien2.putExtra(GiaoDichSinhVien2.THONG_TIN_VE_SINH_VIEN1, ve);
                startActivity(intentSinhVien2);
            }
        });

    }
    private void showThongTin(SinhVien sinhVien,Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> ve ){

        String nguoiTao = ve.component2();
        String maSuKien = ve.component3();
        String maVe = ve.component6();
        BigInteger dateTime = ve.component7();
        long dateTimeLong = dateTime.longValue() * 1000;
        String dateTimeString;
        Date ngayLap = new Date(dateTimeLong);
        TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        ngayLapFormat.setTimeZone(timeZoneVN);
        dateTimeString = ngayLapFormat.format(ngayLap);
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = writer.encode(mssv,BarcodeFormat.CODE_39,600,150);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgBarCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mssvSinhVien1.setText(mssv);
        tenSinhVien1.setText(sinhVien.getHovaten());
        gioiTinhSinhVien1.setText(sinhVien.getGoitinh());
        khoaSinhVien1.setText(sinhVien.getKhoa());
        lopSinhVien1.setText(sinhVien.getLop());
        ngaySinhSinhVien1.setText(sinhVien.getNgaySinh());
        if (ve.getSize() == 0){
            String thongTinKhongCo = "Không Có Thông Tin Vé";
            maVeSinhVien1.setText(thongTinKhongCo);
            maVeSinhVien1.setTextSize(getResources().getDimension(R.dimen.text_size_thong_tin_ve));
            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_thin);
            maVeSinhVien1.setTypeface(typeface);
            LinearLayout thongTin1 = findViewById(R.id.chiTietThongTinVeSinhVien1);
            thongTin1.setVisibility(View.GONE);
            nutGiaoDich.setVisibility(View.GONE);
        }else {
            if (ve.component8()){
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
    }
    private void AnhXa(){
        nutGiaoDich = findViewById(R.id.nutSinhVien1);
        imgBarCode = findViewById(R.id.imageSinhVien1);
        mssvSinhVien1 = (TextView) findViewById(R.id.mssvSinhVien1);
        tenSinhVien1 = (TextView) findViewById(R.id.thongTinTenSinhVien1);
        gioiTinhSinhVien1 = (TextView) findViewById(R.id.thongTinGioiTinhSinhVien1);
        khoaSinhVien1 = (TextView) findViewById(R.id.thongTinKhoaSinhVien1);
        lopSinhVien1 = findViewById(R.id.thongTinLopSinhVien1);
        ngaySinhSinhVien1 = findViewById(R.id.thongTinNgaySinhSinhVien1);
        soHuuSinhVien1 = findViewById(R.id.thongTinSoHuuSinhVien1);
        maVeSinhVien1 = findViewById(R.id.thongTinMaVeSinhVien1);
        nguoiTaoVeSinhVien1 = findViewById(R.id.thongTinNguoiTaoVeSinhVien1);
        maSuKienSinhVien1 = findViewById(R.id.thongTinMaSuKienSinhVien1);
        ngayLapVeSinhVien1 = findViewById(R.id.thongTinNgayLapSinhVien1);
    }
    class ThongTinSinhVien implements Callable<SinhVien> {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        @Override
        public SinhVien call() throws Exception {
            String url = "https://ptta-cnm.herokuapp.com/taikhoan/" + mssv;
            Request.Builder builder = new Request.Builder();
            Request request  = builder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SinhVien[] sinhViens = gson.fromJson(noiDung,SinhVien[].class);
            return sinhViens[0];
        }
    }
    class ThongTinVe implements Callable<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>>{
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        @Override
        public Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> call() throws Exception {
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j,thongTinWeb3.getCredentialsWallet(),
                    new DefaultGasProvider());
            BigInteger mssvBigInteger = new BigInteger(mssv);
            Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> duLieu
                    = sukien_sol_sukien.searVe(mssvBigInteger,getSuKien()).send();
            web3j.shutdown();
            return  duLieu;
        }
        private String getSuKien() throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noidung = response.body().string();
            Gson gson = new Gson();
            SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
            return suKiens[0].getMasukien();
        }
    }
}