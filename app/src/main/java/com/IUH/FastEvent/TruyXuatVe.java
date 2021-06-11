package com.IUH.FastEvent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.LichSuModel;
import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import java8.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TruyXuatVe extends AppCompatActivity {
    private static final String TAG ="TruyXuatVe.java" ;
    private TextView txtTenSinhVien1,txtGioiTinh1,txtKhoa1,txtNgaySinh1,txtLop1,txtMssv1,
            txtTenSinhVien2,txtGioiTinh2,txtKhoa2,txtNgaySinh2,txtLop2,txtMssv2, txtMaVe, txtDateBan
            ,txtThongTinNull;
    private ImageView barCode;
    private String maVe;
    private String maSuKien;
    public static final String KEY_MA_VE="key_maVe";
    public static final String KEY_MA_SU_KIEN="Sukien_ma";
    private Common common;
    private ProgressBar progressBarTruyXuat;
    private Web3j web3j;
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_truy_xuat_ve);
        progressBarTruyXuat = findViewById(R.id.progressBarTruyXuat);
        txtThongTinNull = findViewById(R.id.textThongTinTruyXuat);
        ImageButton backTruyXuat = findViewById(R.id.truyxuatback);
        backTruyXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        common = new Common();
        Intent intent = getIntent();
        maVe =  intent.getStringExtra(KEY_MA_VE);
        maSuKien = intent.getStringExtra(KEY_MA_SU_KIEN);
        executorService = Executors.newCachedThreadPool();
        Future<?> thread1 = executorService.submit(new GetLichSu());

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(common);
        executorService.shutdown();
        web3j.shutdown();
    }
    class GetLichSu implements Runnable{
        private final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        @Override
        public void run() {
            ArrayList<LichSuModel> lichSuModelArrayList = new ArrayList<LichSuModel>();
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien= Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j,thongTinWeb3.getCredentialsWallet(),
                    new DefaultGasProvider());
            try {
                List idLichSu = sukien_sol_sukien.timLinhSu(maVe).send();
                for (Object id: idLichSu){
                    BigInteger idBigInteger = new BigInteger(id.toString());
                    Tuple4<BigInteger, String, BigInteger, BigInteger> thongTin = sukien_sol_sukien.lichSu(idBigInteger).send();
                    LichSuModel lichSuModel = new LichSuModel(thongTin);
                    lichSuModelArrayList.add(lichSuModel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG,e.getMessage(),e);
            }
            if (lichSuModelArrayList.isEmpty()){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBarTruyXuat.setVisibility(View.GONE);
                        txtThongTinNull.setVisibility(View.VISIBLE);
                    }
                });
            }else{
                for (LichSuModel chiTiet: lichSuModelArrayList){
                    try {
                        SinhVien sinhVien1 = getSinhVien(chiTiet.getMssvBan());
                        SinhVien sinhVien2 = getSinhVien(chiTiet.getMssvNhan());
                        LinearLayout linearLayout = findViewById(R.id.truyXuatNew);
                        LayoutInflater inflater = LayoutInflater.from(TruyXuatVe.this);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                View v = inflater.inflate(R.layout.giaodich_layout,linearLayout,false);
                                anhxa(v);
                                String fullname = sinhVien1.getHovaten() + " "+ sinhVien1.getTen();
                                txtTenSinhVien1.setText(fullname);
                                if (sinhVien1.getGoitinh() ==1){
                                    txtGioiTinh1.setText("Nam");
                                }else{
                                    txtGioiTinh1.setText("Nữ");
                                }
                                txtKhoa1.setText(sinhVien1.getKhoa());
                                txtNgaySinh1.setText(sinhVien1.getNgaySinh());
                                txtLop1.setText(sinhVien1.getLop());
                                txtMssv1.setText(sinhVien1.getMssv());
                                ///////////////////////////////////////
                                //////////////////////////////////////
                                String fullNameSinhVien2 = sinhVien2.getHovaten() + " "+ sinhVien2.getTen();
                                txtTenSinhVien2.setText(fullNameSinhVien2);
                                if (sinhVien2.getGoitinh() ==1){
                                    txtGioiTinh2.setText("Nam");
                                }else{
                                    txtGioiTinh2.setText("Nữ");
                                }
                                txtKhoa2.setText(sinhVien2.getKhoa());
                                txtNgaySinh2.setText(sinhVien2.getNgaySinh());
                                txtLop2.setText(sinhVien2.getLop());
                                txtMssv2.setText(sinhVien2.getMssv());
                                txtMaVe.setText(maVe);
                                txtDateBan.setText(chiTiet.getDateString());
                                try {
                                    MultiFormatWriter writer = new MultiFormatWriter();
                                    BitMatrix bitMatrix = writer.encode(maVe, BarcodeFormat.CODE_39,300,50);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                    barCode.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                                linearLayout.addView(v);
                                progressBarTruyXuat.setVisibility(View.GONE);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.w(TAG,e.getMessage(),e);
                    }
                }
            }

        }
        private void anhxa(View v){
            txtMssv1 = v.findViewById(R.id.mssvBanGiaoDich);
            txtMssv2 = v.findViewById(R.id.mssvNhanGiaoDich);
            txtTenSinhVien1 = v.findViewById(R.id.truyXuatHoVaTen1);
            txtTenSinhVien2 = v.findViewById(R.id.truyXuatHoVaTen2);
            txtGioiTinh1 = v.findViewById(R.id.truyXuatGioiTinh1);
            txtGioiTinh2 = v.findViewById(R.id.truyXuatGioiTinh2);
            txtKhoa1 = v.findViewById(R.id.truyXuatKhoa1);
            txtKhoa2 = v.findViewById(R.id.truyXuatKhoa2);
            txtLop1 = v.findViewById(R.id.truyXuatLop1);
            txtLop2 = v.findViewById(R.id.truyXuatLop2);
            txtNgaySinh1 = v.findViewById(R.id.truyXuatNgaySinh1);
            txtNgaySinh2 = v.findViewById(R.id.truyXuatNgaySinh2);
            txtMaVe = v.findViewById(R.id.truyXuatMaVe);
            barCode = v.findViewById(R.id.truyXuatBarCode);
            txtDateBan = v.findViewById(R.id.truyXuatNgayBan);
        }
        private SinhVien getSinhVien(BigInteger maSinhVien) throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/taikhoan/"+ maSinhVien.toString();
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SinhVien[] sinhViens = gson.fromJson(noiDung,SinhVien[].class);
            return sinhViens[0];
        }
    }
}