package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.SuKien;
import com.IUH.FastEvent.Model.VeAo;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.zxing.Result;
import com.r0adkll.slidr.Slidr;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import java8.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

interface GetThongTinTaoVe{
    SinhVien getThongTin(String mssv) throws IOException;
}
interface GetSuKien{
    SuKien getSuKien() throws IOException;
}
public class ThemVe extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{
    private static final String TAG="ThemVe.java";
    private SweetAlertDialog pDialog;
    private ExecutorService executorService;
    private static final int REQUEST_CAMERA = 1;
    private CodeScanner mCodeScanner;
    private Common common;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_ve);
        common= new Common();
        Slidr.attach(this);
        executorService = Executors.newCachedThreadPool();
        CodeScannerView codeScannerView = findViewById(R.id.scannerThemVe);
        mCodeScanner = new CodeScanner(this, codeScannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog = new SweetAlertDialog(ThemVe.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Đang xử lý");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        executorService.submit(new TaoVe(result.getText()));
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @AfterPermissionGranted(123)
            @Override
            public void onClick(View v) {
                String[] perms = {Manifest.permission.CAMERA};
                if (EasyPermissions.hasPermissions(ThemVe.this, perms)){
                    mCodeScanner.startPreview();
                }else{
                    EasyPermissions.requestPermissions(ThemVe.this,"Chúng tôi cần quyền Camera để có thể quét được mã sinh viên",
                            123, perms);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
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
        executorService.shutdown();
        unregisterReceiver(common);
    }

    @Override
    @AfterPermissionGranted(123)
    protected void onResume() {
        super.onResume();
        String[] perms = {Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)){
            mCodeScanner.startPreview();
        }else{
            EasyPermissions.requestPermissions(this,"Chúng tôi cần quền này để có thể quét mã sinh viên",
                    123, perms);
        }

    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){

        }
    }
    class ThongBao{
        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        private Boolean success;

    }
    class TaoVe implements Runnable{
        private final String maSinhVien;
        private Boolean checkHoatDongTrueFalse;
        private SuKien thongTinSuKien;
        private SinhVien thongTinSinhVien;
        private VeAo thongTinVeAo;
        private final StringBuilder url = new StringBuilder("https://ptta-cnm.herokuapp.com/taikhoan/");
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).readTimeout(20,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true).build();
        public TaoVe(String mssv){
            this.maSinhVien=mssv;
        }
        @Override
        public void run() {
            try {
                checkHoatDongTrueFalse = checkHoatDong();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GetThongTinTaoVe thongTin = (mssv) -> {
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
            GetSuKien getSuKien =()->{
              String urlSukien = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
              Request.Builder builder = new Request.Builder();
              builder.url(urlSukien);
              Request request= builder.build();
              Response response = okHttpClient.newCall(request).execute();
              String noiDungSuKien = Objects.requireNonNull(response.body()).string();
              Gson gson = new Gson();
              SuKien[] suKiens = gson.fromJson(noiDungSuKien,SuKien[].class);
              return suKiens[0];
            };
            try {
                thongTinSinhVien = thongTin.getThongTin(maSinhVien);
                thongTinSuKien = getSuKien.getSuKien();
                thongTinVeAo = getVe();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Boolean checkNamHocSinhVien = checkNamSinhVien();
                if(checkHoatDongTrueFalse || checkNamHocSinhVien){
                    String nguoiTao = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                    ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
                    Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
                    Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,
                            web3j,
                            thongTinWeb3.getCredentialsWallet(), new StaticGasProvider(ThongTinWeb3.GAS_PRICE,ThongTinWeb3.GAS_LIMIT));

                    BigInteger mssvInter = new BigInteger(maSinhVien);
                    if(thongTinSinhVien.getHovaten() != null || thongTinSinhVien.getTen() != null
                            || thongTinSuKien.getMasukien() != null || thongTinSuKien.getChongoi() != null){
                        try {
                            BigInteger ve = sukien_sol_sukien.veMapping(thongTinSuKien.getMasukien()).send();
                            BigInteger choGoi = BigInteger.valueOf(thongTinSuKien.getChongoi().longValue());
                            if(ve.compareTo(choGoi) == 0){
                                pDialog.dismissWithAnimation();
                                Alerter.create(ThemVe.this)
                                        .setTitle("Thông Báo").setText("Số lượng vé đã đủ")
                                        .setBackgroundColorRes(R.color.red)
                                        .setIcon(R.drawable.ic_baseline_close_24)
                                        .enableSwipeToDismiss().setDuration(4000).show();
                                web3j.shutdown();
                            }else{
                                if (setMssvVeAo(thongTinVeAo.getMave(),mssvInter.toString())){
                                    sukien_sol_sukien.createVe(mssvInter, nguoiTao, thongTinSinhVien.getHovaten(), thongTinSinhVien.getTen(), thongTinSuKien.getMasukien(), thongTinVeAo.getMave()
                                            , thongTinVeAo.getVitri()).sendAsync().thenAccept(transactionReceipt -> {
                                        if (transactionReceipt.isStatusOK()) {
                                            pDialog.dismissWithAnimation();
                                            Alerter.create(ThemVe.this)
                                                    .setTitle("Thông Báo").setText("Cấp phát vé thành công")
                                                    .setBackgroundColorRes(R.color.success)
                                                    .setIcon(R.drawable.ic_baseline_check)
                                                    .enableSwipeToDismiss().setDuration(4000).show();
                                            web3j.shutdown();
                                        } else {
                                            pDialog.dismissWithAnimation();
                                            Alerter.create(ThemVe.this)
                                                    .setTitle("Thông Báo").setText("Cấp phát vé không thành công" +
                                                    "Kết nối với ETH gặp sự cố")
                                                    .setBackgroundColorRes(R.color.red)
                                                    .setIcon(R.drawable.ic_baseline_close_24)
                                                    .enableSwipeToDismiss().setDuration(4000).show();
                                            web3j.shutdown();
                                        }
                                    }).exceptionally(throwable -> {
                                        pDialog.dismissWithAnimation();
                                        Log.d(TAG,throwable.getMessage(),throwable);
                                        Alerter.create(ThemVe.this)
                                                .setTitle("Cấp phát vé không thành công").setText("Vé đã được cấp phát")
                                                .setBackgroundColorRes(R.color.red)
                                                .setIcon(R.drawable.ic_baseline_close_24)
                                                .enableSwipeToDismiss().setDuration(4000).show();
                                        return null;
                                    });
                                }else{
                                    pDialog.dismissWithAnimation();
                                    Alerter.create(ThemVe.this)
                                            .setTitle("Thông Báo").setText("Cấp phát vé không thành công"+
                                            "Cloud gặp sự cố")
                                            .setBackgroundColorRes(R.color.red)
                                            .setIcon(R.drawable.ic_baseline_close_24)
                                            .enableSwipeToDismiss().setDuration(4000).show();
                                    web3j.shutdown();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG,e.getMessage(),e);
                            pDialog.dismissWithAnimation();
                            Alerter.create(ThemVe.this)
                                    .setTitle("Thông Báo").setText("Không lấy được số lượng vé đã cấp phát")
                                    .setBackgroundColorRes(R.color.red)
                                    .setIcon(R.drawable.ic_baseline_close_24)
                                    .enableSwipeToDismiss().setDuration(4000).show();
                            web3j.shutdown();
                        }
                    }else{
                        pDialog.dismissWithAnimation();
                        Alerter.create(ThemVe.this)
                                .setTitle("Thông Báo").setText("Không có thông tin")
                                .setBackgroundColorRes(R.color.red)
                                .setIcon(R.drawable.ic_baseline_close_24)
                                .enableSwipeToDismiss().setDuration(4000).show();
                    }
                }else{
                    pDialog.dismissWithAnimation();
                    Alerter.create(ThemVe.this)
                            .setTitle("Không đủ điều kiện").setText("Phải tham gia đủ các" +
                            " hoạt động hoặc bạn là sinh viên năm nhất")
                            .setBackgroundColorRes(R.color.red)
                            .setIcon(R.drawable.ic_baseline_close_24)
                            .enableSwipeToDismiss().setDuration(4000).show();
                }
        }
        public Boolean checkHoatDong() throws IOException {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            String urlSinhVien = "https://ptta-cnm.herokuapp.com/taikhoan/"+maSinhVien;
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            Request requestSinhVien = builder.url(urlSinhVien).build();
            Response responseSinhVien = okHttpClient.newCall(requestSinhVien).execute();
            String noiDungSinhVien = Objects.requireNonNull(responseSinhVien.body()).string();
            String noidung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            SinhVien[] sinhViens = gson.fromJson(noiDungSinhVien, SinhVien[].class);
            SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
            if(sinhViens.length == 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.dismissWithAnimation();
                        Alerter.create(ThemVe.this)
                                .setTitle("Thông Báo").setText("Không có thông tin")
                                .setBackgroundColorRes(R.color.red)
                                .setIcon(R.drawable.ic_baseline_close_24)
                                .enableSwipeToDismiss().setDuration(4000).show();
                    }
                });
            }else{
                HashMap<String, Boolean> hoatDongSinhVien = sinhViens[0].getHoatdong();
                String[] hoatDong = suKiens[0].getHoatdong();
                for(String hoatDongString : hoatDong){
                    if(!hoatDongSinhVien.containsKey(hoatDongString)){
                        return false;
                    }
                }
                return true;
            }
           return null;
        }
        public Boolean checkNamSinhVien(){
            String namNhapHoc = maSinhVien.substring(0,2);
            Integer namNhapHocInt = Integer.valueOf(namNhapHoc);
            String[] namHoc = thongTinSuKien.getNamhoc().split("-");
            Integer namHocInt = Integer.parseInt(namHoc[0].substring(2));
            int yearCheck = namHocInt-namNhapHocInt;
            if (yearCheck ==0){
                return true;
            }else{
                return false;
            }
        }
        public VeAo getVe() throws IOException {
            String urlVeAo = "https://ptta-cnm.herokuapp.com/ve/"+thongTinSuKien.getMasukien();
            Request.Builder builder = new Request.Builder();
            builder.url(urlVeAo);
            Request request = builder.build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            VeAo[] veAos = gson.fromJson(noiDung, VeAo[].class);
            return veAos[0];
        }
        protected Boolean setMssvVeAo(String maVe, String mssv) throws IOException {
            String urlSetMssvVeAo = "https://ptta-cnm.herokuapp.com/ve/"+maVe+"/"+mssv;
            Request request = new Request.Builder().url(urlSetMssvVeAo).build();
            Response response = okHttpClient.newCall(request).execute();
            String noiDung = Objects.requireNonNull(response.body()).string();
            Gson gson = new Gson();
            ThongBao thongBao = gson.fromJson(noiDung,ThongBao.class);
            return thongBao.getSuccess();
        }
    }

}