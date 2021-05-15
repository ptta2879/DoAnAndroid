package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.SuKien;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.r0adkll.slidr.Slidr;


import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.gas.DefaultGasProvider;


import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThongTinVe extends AppCompatActivity {
    private String mssv;
    private TextView nameSv,txtMssv,txtKhoa,txtLop,txtGioiTinh,txtNgaySinh,
            txtSoHuu,txtNguoiTao,txtTen,txtMaVe,txtNgayLap,txtMaSinhVien,
    txtMaSuKien,txtMaVeGiaoDich,txtMaSinhVienGiaoDich,txtNgayGiaoDich,txtMaSinhVienNhan;
    private ImageView barCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ve);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        Slidr.attach(this);
        AnhXa();
        Intent intent = getIntent();
        mssv = intent.getStringExtra("mssv");
        String url = "https://ptta-cnm.herokuapp.com/taikhoan/" + mssv;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<SinhVien> thongTinSinhVien = executorService.submit(new GetUrl(url));
        Future<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>> thongTinBlock = executorService.submit(new LayThongTin());
        try {
            SinhVien thongTinChiTiet = thongTinSinhVien.get();
            showSinhVien(thongTinChiTiet);
            Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> thongtinVe = thongTinBlock.get();
            showThongTinVe(thongtinVe);
            String maVe = thongtinVe.component6();
            Future<List> thongTinLichSu = executorService.submit(new LichSu(maVe));
            List idLichSu = thongTinLichSu.get();
            executorService.execute(new TimLichSu(idLichSu));

        } catch (ExecutionException | InterruptedException | WriterException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
    static class GetUrl implements Callable<SinhVien>{
        private final String url ;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                                                                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        public GetUrl(String url){
            this.url = url;
        }
        @Override
        public SinhVien call() throws Exception {
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SinhVien[] sinhVienGet = gson.fromJson(noiDung, SinhVien[].class);
            return sinhVienGet[0];

        }
    }
    class LayThongTin implements Callable<Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean>> {
        @Override
        public Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> call() throws Exception {
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien suKien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j, thongTinWeb3.getCredentialsWallet(),new DefaultGasProvider());
            BigInteger mssvInt = new BigInteger(mssv);
            Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> a = suKien_sol_sukien.searVe(mssvInt,maSuKien()).send();
            web3j.shutdown();
            return a;
        }
        public String maSuKien() throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noidung = response.body().string();
            Gson gson = new Gson();
            SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
            return suKiens[0].getMasukien();
        }
    }
    class LichSu implements Callable<List>{
        private final String maVe;

        public LichSu(String maVe){
            this.maVe=maVe;
        }
        @Override
        public List call() throws Exception {
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS
                                                                            ,web3j
                                                                            ,thongTinWeb3.getCredentialsWallet()
                                                                            ,new DefaultGasProvider());
            List a = sukien_sol_sukien.timLinhSu(maVe).send();
            web3j.shutdown();
            return a;
        }
    }
    class TimLichSu implements Runnable{
        List idLichSu;
        public TimLichSu(List thongTin){
            this.idLichSu=thongTin;
        }
        @Override
        public void run() {
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS
                    ,web3j
                    ,thongTinWeb3.getCredentialsWallet()
                    ,new DefaultGasProvider());
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.giaoDichView);
            LayoutInflater inflater = LayoutInflater.from(ThongTinVe.this);

            for(Object a: idLichSu){
                BigInteger id = new BigInteger(a.toString());
                try {
                    Tuple4<BigInteger, String, BigInteger, BigInteger> lichSu = sukien_sol_sukien.lichSu(id).send();
                    BigInteger mssvBan = lichSu.component1();
                    String mssvBanString = mssvBan.toString();
                    String maVe = lichSu.component2();
                    BigInteger dateInt = lichSu.component3();
                    BigInteger mssvNhan = lichSu.component4();
                    String mssvNhanString =mssvNhan.toString();
                    long dateLong = dateInt.longValue() *1000;
                    Date date = new Date(dateLong);
                    TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh") ;
                    SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    ngayLapFormat.setTimeZone(timeZoneVN);
                    String dateTimeGiaoDichString = ngayLapFormat.format(date);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            View view = inflater.inflate(R.layout.giaodich_layout, linearLayout, false);
                            txtMaVeGiaoDich =(TextView) view.findViewById(R.id.maVeGiaoDich);
                            txtMaSinhVienGiaoDich = (TextView) view.findViewById(R.id.mssvBanGiaoDich);
                            txtNgayGiaoDich = (TextView) view.findViewById(R.id.ngayBanGiaoDich);
                            txtMaSinhVienNhan = (TextView) view.findViewById(R.id.mssvNhanGiaoDich);
                            txtMaSinhVienGiaoDich.setText(mssvBanString);
                            txtMaVeGiaoDich.setText(maVe);
                            txtNgayGiaoDich.setText(dateTimeGiaoDichString);
                            txtMaSinhVienNhan.setText(mssvNhanString);
                            linearLayout.addView(view);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            web3j.shutdown();
        }
    }
    public void AnhXa(){
        txtGioiTinh = (TextView) findViewById(R.id.gioiTinh);
        txtKhoa =(TextView) findViewById(R.id.khoa);
        txtLop =(TextView)findViewById(R.id.lop);
        txtMssv =(TextView) findViewById(R.id.mssv);
        txtNgaySinh =(TextView)findViewById(R.id.ngaySinh);
        nameSv = (TextView)findViewById(R.id.nameSV);
        txtSoHuu = (TextView) findViewById(R.id.soHuu);
        txtNguoiTao =(TextView) findViewById(R.id.nguoiTao);
        txtTen=(TextView) findViewById(R.id.ten);
        txtMaVe=(TextView) findViewById(R.id.maVe);
        txtNgayLap=(TextView) findViewById(R.id.ngayLap);
        txtMaSinhVien=(TextView) findViewById(R.id.mssvVe);
        txtMaSuKien=(TextView) findViewById(R.id.maSuKien);
        barCode =(ImageView) findViewById(R.id.code39MaSinhVien);
    }
    protected void showSinhVien(SinhVien sinhvien) throws WriterException {
        Integer gioiTinh = sinhvien.getGoitinh();
        String hoVaTen = sinhvien.getHovaten();
        String khoa = sinhvien.getKhoa();
        String lop= sinhvien.getLop();
        String mssvGetSinhVien = sinhvien.getMssv();
        String ngaysinh = sinhvien.getNgaySinh();
        String ten = sinhvien.getTen();
        String fullName = hoVaTen+ " " + ten;
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(mssvGetSinhVien, BarcodeFormat.CODE_39,600,150);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        barCode.setImageBitmap(bitmap);
        if(gioiTinh ==1 ){
            txtGioiTinh.setText("Nam");
        }
        else{
            txtGioiTinh.setText("Nữ");
        }
        nameSv.setText(fullName);
        txtKhoa.setText(khoa);
        txtLop.setText(lop);
        txtMssv.setText(mssvGetSinhVien);
        txtNgaySinh.setText(ngaysinh);

    }
    protected void showThongTinVe(Tuple8<BigInteger, String, String, String, String, String, BigInteger, Boolean> info){
        if (!info.component8() && info.component1().compareTo(new BigInteger("0")) == 0 && info.component2().isEmpty() && info.component3().isEmpty()
                && info.component4().isEmpty() && info.component5().isEmpty() && info.component6().isEmpty()
                && info.component7().compareTo(new BigInteger("0")) == 0){
            String thongTinKhongCo = "Không Có Thông Tin Về Vé";
            txtMaVe.setText(thongTinKhongCo);
            Typeface typeface = ResourcesCompat.getFont(this, R.font.roboto_thin);
            txtMaVe.setTypeface(typeface);
            LinearLayout thongTin1 = findViewById(R.id.chiTietThongTinVe1);
            LinearLayout thongTin2 = findViewById(R.id.chiTietThongTinVe2);
            thongTin1.setVisibility(View.GONE);
            thongTin2.setVisibility(View.GONE);
        }else{
            Boolean soHuu = info.component8();
            BigInteger mssvGetBlockChain = info.component1();
            String mssvGetBlockChainString = mssvGetBlockChain.toString();
            String nguoiTao = info.component2();
            String maSuKien = info.component3();
            String ho = info.component4();
            String ten = info.component5();
            String maVe = info.component6();
            BigInteger dateTime = info.component7();
            String fullName = ho+" "+ten;
            long dateTimeLong = dateTime.longValue() * 1000;
            String dateTimeString;
            Date ngayLap = new Date(dateTimeLong);
            TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh") ;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            ngayLapFormat.setTimeZone(timeZoneVN);
            dateTimeString = ngayLapFormat.format(ngayLap);
            if (soHuu){
                txtSoHuu.setTextColor(Color.GREEN);
                txtSoHuu.setText("Vé Hợp Lệ");
            }else{
                txtSoHuu.setTextColor(Color.RED);
                txtSoHuu.setText("Không Sở Hữu");
            }
            txtNguoiTao.setText(nguoiTao);
            txtTen.setText(fullName);
            txtMaVe.setText(maVe);
            if(dateTime.intValue() == 0){
                txtNgayLap.setText("");
            }else{
                txtNgayLap.setText(dateTimeString);
            }
            txtMaSinhVien.setText(mssvGetBlockChainString);
            txtMaSuKien.setText(maSuKien);
        }
    }
}