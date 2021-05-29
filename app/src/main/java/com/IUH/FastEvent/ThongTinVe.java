package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.LichSuModel;
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
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.gas.DefaultGasProvider;


import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private static final String TAG = "ThongTinVe";
    private String mssv,url;
    private TextView nameSv,txtMssv,txtKhoa,txtLop,txtGioiTinh,txtNgaySinh,
            txtSoHuu,txtNguoiTao,txtTen,txtMaVe,txtNgayLap,txtMaSinhVien,
    txtMaSuKien,txtMaVeGiaoDich,txtMaSinhVienGiaoDich,txtNgayGiaoDich,txtMaSinhVienNhan,txtViTri,txtViTriTieuDe;
    private ImageView barCode;
    private Common common;
    private SinhVien thongTinChiTiet;
    private Ve thongtinVe;
    private ExecutorService executorService;
    private LinearLayout chiTietVe;
    private ProgressBar progressBar;
    private CheckTienTrinh checkTienTrinh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common= new Common();
        setContentView(R.layout.activity_thong_tin_ve);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Slidr.attach(this);
        AnhXa();
        checkTienTrinh = new CheckTienTrinh();
        Intent intent = getIntent();
        mssv = intent.getStringExtra("mssv");
        url = "https://ptta-cnm.herokuapp.com/taikhoan/" + mssv;
        chiTietVe.setVisibility(View.GONE);
        executorService = Executors.newFixedThreadPool(5);
        executorService.execute(new GetUrl(url));
        executorService.execute(new LayThongTin());

        executorService.shutdown();
    }

    class CheckTienTrinh{
        private boolean tienTrinh1;
        private boolean tienTrinh2;

        public void setTienTrinh1(boolean tienTrinh1) {
            this.tienTrinh1 = tienTrinh1;
        }

        public void setTienTrinh2(boolean tienTrinh2) {
            this.tienTrinh2 = tienTrinh2;
        }
        public void checkTienTrinh(){
            if (this.tienTrinh1 && this.tienTrinh2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chiTietVe.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }
    }


    class GetUrl implements Runnable{
        private final String url ;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                                                                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        public GetUrl(String url){
            this.url = url;
        }
        @Override
        public void run() {
            try {
                Request.Builder builder = new Request.Builder();
                builder.url(url);
                Request request = builder.build();
                Response response = okHttpClient.newCall(request).execute();
                String noiDung = Objects.requireNonNull(response.body()).string();
                Gson gson = new Gson();
                SinhVien[] sinhVienGet = gson.fromJson(noiDung, SinhVien[].class);
                if (sinhVienGet.length == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Alerter.create(ThongTinVe.this)
                                    .setTitle("Thông Báo").setText("Không có thông tin của sinh viên trong hệ thống")
                                    .setBackgroundColorRes(R.color.red)
                                    .setIcon(R.drawable.ic_baseline_close_24)
                                    .enableSwipeToDismiss().setDuration(4000).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }else{
                    showSinhVien(sinhVienGet[0]);
                    checkTienTrinh.setTienTrinh1(true);
                    checkTienTrinh.checkTienTrinh();
                }
            } catch (IOException | WriterException e) {
                e.printStackTrace();
            }
        }
    }
    class LayThongTin implements Runnable {
        private Tuple9<BigInteger, String, String, String, String, String, String, BigInteger, Boolean> a;
//        private ArrayList<Integer> listId;
//        private Tuple4<BigInteger, String, BigInteger, BigInteger> lichSu;
        @Override
        public void run() {
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien suKien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j, thongTinWeb3.getCredentialsWallet(),new DefaultGasProvider());
            BigInteger mssvInt = new BigInteger(mssv);
            try {
                a = suKien_sol_sukien.searVe(mssvInt,maSuKien()).send();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (a!= null){
                thongtinVe = new Ve(a);
            }else{
                return;
            }

//            try {
//                listId = (ArrayList<Integer>) suKien_sol_sukien.timLinhSu(thongtinVe.getMave()).send();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ArrayList<LichSuModel> lichSuList = new ArrayList<LichSuModel>();
//            for(Object a: listId){
//                BigInteger id = new BigInteger(a.toString());
//
//                try {
//                    lichSu = suKien_sol_sukien.lichSu(id).send();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                LichSuModel lichSu1 = new LichSuModel(Objects.requireNonNull(lichSu));
//                lichSuList.add(lichSu1);
//            }
            web3j.shutdown();
            try {
                showThongTinVe(thongtinVe);
            } catch (WriterException e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage(),e);
                Alerter.create(ThongTinVe.this)
                        .setTitle("Thông Báo").setText("Phát hiện lỗi không thể load được thông tin")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
//            showLichSu(lichSuList);
            checkTienTrinh.setTienTrinh2(true);
            checkTienTrinh.checkTienTrinh();
        }
        public String maSuKien() throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noidung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
            return suKiens[0].getMasukien();
        }
    }

//    public void showLichSu(ArrayList<LichSuModel> lichSuModelArrayList){
//        for (LichSuModel lichSuModel : lichSuModelArrayList) {
//            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.giaoDichView);
//            LayoutInflater inflater = LayoutInflater.from(ThongTinVe.this);
//            BigInteger mssvBan = lichSuModel.getMssvBan();
//            String mssvBanString = mssvBan.toString();
//            String maVe = lichSuModel.getMaVe();
//            String dateTimeGiaoDichString = lichSuModel.getDateString();
//            BigInteger mssvNhan = lichSuModel.getMssvNhan();
//            String mssvNhanString = mssvNhan.toString();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    View view = inflater.inflate(R.layout.giaodich_layout, linearLayout, false);
//                    txtMaVeGiaoDich = (TextView) view.findViewById(R.id.maVeGiaoDich);
//                    txtMaSinhVienGiaoDich = (TextView) view.findViewById(R.id.mssvBanGiaoDich);
//                    txtNgayGiaoDich = (TextView) view.findViewById(R.id.ngayBanGiaoDich);
//                    txtMaSinhVienNhan = (TextView) view.findViewById(R.id.mssvNhanGiaoDich);
//                    txtMaSinhVienGiaoDich.setText(mssvBanString);
//                    txtMaVeGiaoDich.setText(maVe);
//                    txtNgayGiaoDich.setText(dateTimeGiaoDichString);
//                    txtMaSinhVienNhan.setText(mssvNhanString);
//                    linearLayout.addView(view);
//                }
//            });
//        }
//    }
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
        progressBar = findViewById(R.id.progressBar);
        chiTietVe = findViewById(R.id.noiDungThongTinVe);
        txtViTri = (TextView)findViewById(R.id.viTri);
        txtViTriTieuDe =(TextView) findViewById(R.id.viTriTieuDe);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
        });

    }
    protected void showThongTinVe(Ve info) throws WriterException {
                if (!info.getSohuu() && info.getMssv().compareTo(new BigInteger("0")) == 0 && info.getNguoiTao().isEmpty() && info.getMasukien().isEmpty()
                        && info.getHo().isEmpty() && info.getTen().isEmpty() && info.getMave().isEmpty()
                        && info.getDate().compareTo(new BigInteger("0")) == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            barCode.setVisibility(View.GONE);
                            String thongTinKhongCo = "Không Có Thông Tin Về Vé";
                            txtMaVe.setText(thongTinKhongCo);
                            LinearLayout thongTin1 = findViewById(R.id.chiTietThongTinVe1);
                            LinearLayout thongTin2 = findViewById(R.id.chiTietThongTinVe2);
                            thongTin1.setVisibility(View.GONE);
                            thongTin2.setVisibility(View.GONE);
                            txtViTriTieuDe.setVisibility(View.GONE);
                        }
                    });
                }else{

                    Boolean soHuu = info.getSohuu();
                    BigInteger mssvGetBlockChain = info.getMssv();
                    String mssvGetBlockChainString = mssvGetBlockChain.toString();
                    String nguoiTao = info.getNguoiTao();
                    String maSuKien = info.getMasukien();
                    String ho = info.getHo();
                    String ten = info.getTen();
                    String maVe = info.getMave();
                    BigInteger dateTime = info.getDate();
                    String fullName = ho+" "+ten;
                    long dateTimeLong = dateTime.longValue() * 1000;
                    String dateTimeString;
                    Date ngayLap = new Date(dateTimeLong);
                    TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh") ;
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                    ngayLapFormat.setTimeZone(timeZoneVN);
                    dateTimeString = ngayLapFormat.format(ngayLap);
                    MultiFormatWriter writer = new MultiFormatWriter();
                    BitMatrix bitMatrix = writer.encode(maVe, BarcodeFormat.CODE_39,300,50);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (soHuu){

                                txtSoHuu.setTextColor(Color.GREEN);
                                txtSoHuu.setText("Vé Hợp Lệ");
                            }else{
                                txtSoHuu.setTextColor(Color.RED);
                                txtSoHuu.setText("Không Sở Hữu");
                            }
                            barCode.setImageBitmap(bitmap);
                            txtNguoiTao.setText(nguoiTao);
                            txtTen.setText(fullName);
                            txtViTri.setText(info.getVitri());
                            txtMaVe.setText(maVe);
                            if(dateTime.intValue() == 0){
                                txtNgayLap.setText("");
                            }else{
                                txtNgayLap.setText(dateTimeString);
                            }
                            txtMaSinhVien.setText(mssvGetBlockChainString);
                            txtMaSuKien.setText(maSuKien);
                            barCode.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(ThongTinVe.this,TruyXuatVe.class);
                                    intent.putExtra(TruyXuatVe.KEY_MA_VE,maVe);
                                    intent.putExtra(TruyXuatVe.KEY_MA_SU_KIEN,maSuKien);
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter= new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(common);
    }
}