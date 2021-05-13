package com.IUH.FastEvent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.Ve;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;
import java.util.Objects;
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

public class GiaoDichSinhVien2 extends AppCompatActivity implements GiaoDichBottomSheet.XacNhanListener {
    public static final String THONG_TIN_SINH_VIEN = "thongtinsinhvien";
    public static final String THONG_TIN_VE_SINH_VIEN1 = "thongtinvesinhvien";
    private Ve thongTinVe1;
    private CodeScanner codeScanner;
    private ExecutorService executorService;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dich_sinh_vien1);
        Intent intent = getIntent();
        thongTinVe1 = (Ve) intent.getParcelableExtra(THONG_TIN_VE_SINH_VIEN1);
        CodeScannerView codeScannerView = findViewById(R.id.giaoDichSinhVien1);
        executorService = Executors.newFixedThreadPool(1);
        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog = new SweetAlertDialog(GiaoDichSinhVien2.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Vui lòng đợi...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        Future<SinhVien> futureSinhVien2 = executorService.submit(new ThongTinSinhVien(result.getText().trim()));
                        try {
                            pDialog.cancel();
                            showBottomSheet(futureSinhVien2.get());
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    private void showBottomSheet(SinhVien sinhVien) {
        GiaoDichBottomSheet giaoDichBottomSheet = GiaoDichBottomSheet.newInstance(sinhVien,thongTinVe1);
        giaoDichBottomSheet.show(getSupportFragmentManager() , giaoDichBottomSheet.getTag());
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onStop() {
        codeScanner.releaseResources();
        super.onStop();
    }

    @Override
    public void btnXacNhan(Ve ve, BigInteger mssv1) {
        pDialog = new SweetAlertDialog(GiaoDichSinhVien2.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Vui lòng đợi...");
        pDialog.setCancelable(false);
        pDialog.show();
        executorService.submit(new GiaoDich(ve,mssv1));
    }

    static class ThongTinSinhVien implements Callable<SinhVien>{
        private final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
        private final String maSinhVien2;
        public ThongTinSinhVien(String ma){
            this.maSinhVien2 = ma;
        }
        @Override
        public SinhVien call() throws Exception {
            String url = "https://ptta-cnm.herokuapp.com/taikhoan/"+maSinhVien2;
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SinhVien[] sinhViens = gson.fromJson(noiDung,SinhVien[].class);
            return sinhViens[0];
        }
    }
    class GiaoDich implements Runnable{
        private final Ve ve;
        private final BigInteger mssv1;
        public GiaoDich(Ve ve,BigInteger mssv1){
            this.mssv1 = mssv1;
            this.ve=ve;
        }
        @Override
        public void run() {
            String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j
            , thongTinWeb3.getCredentialsWallet(), new StaticGasProvider(ThongTinWeb3.GAS_PRICE,ThongTinWeb3.GAS_LIMIT));
            try {
                sukien_sol_sukien.giaoDich(ve.getMssv(),mssv1,user,ve.getHo(),ve.getTen()
                        ,ve.getMasukien(),ve.getMave()).send();
                pDialog.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      new SweetAlertDialog(GiaoDichSinhVien2.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Thành Công")
                                .setContentText("Giao Dịch Vé Thành Công")
                                .setConfirmText("Xác Nhận")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        web3j.shutdown();
                                        executorService.shutdown();
                                        sDialog.cancel();
                                        Intent intent = new Intent(GiaoDichSinhVien2.this,MenuChucNang.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Alerter.create(GiaoDichSinhVien2.this)
                        .setTitle("Lỗi").setText("Sinh viên nhận vé đã có vé hoặc sinh viên giao dịch không sở hữu vé")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }

        }
    }
}
