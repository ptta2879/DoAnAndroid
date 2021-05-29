package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.SuKien;
import com.IUH.FastEvent.Model.Ve;
import com.IUH.FastEvent.Model.VeAo;
import com.IUH.FastEvent.Model.YeuCau;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.r0adkll.slidr.Slidr;
import com.ramotion.foldingcell.FoldingCell;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple9;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import java8.util.concurrent.CompletableFuture;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

interface XuLyYeuCau{
    void xacNhanYeuCau(BigInteger mssvYeuCau,BigInteger mssvNhan, Ve ve,SinhVien sinhVien);
    void huyYeuCau();
}
public class ThongTinSinhVien1 extends AppCompatActivity implements XuLyYeuCau{
    public static final String KEY_MSSV2 = "KEY_MSSV2" ;
    public static final String KEY_MSSV1 ="KEY_MSSV1" ;
    private static final String TAG ="ThongTinSinhVien" ;
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
    private ImageView imgBarCode,imgBarCode2;
    private FloatingActionButton nutGiaoDich,nutHuy;
    private TextView mssvSinhVien1,tenSinhVien1,gioiTinhSinhVien1,khoaSinhVien1,lopSinhVien1,
            ngaySinhSinhVien1,soHuuSinhVien1,maVeSinhVien1,nguoiTaoVeSinhVien1,maSuKienSinhVien1,ngayLapVeSinhVien1
            , tenSinhVien1Dau,viTriSinhVien1,mssvSinhVien2,tenSinhVien2,gioiTinhSinhVien2,khoaSinhVien2,lopSinhVien2,
            ngaySinhSinhVien2,soHuuSinhVien2,maVeSinhVien2,nguoiTaoVeSinhVien2,maSuKienSinhVien2,ngayLapVeSinhVien2,viTriSinhVien2
            , tenSinhVien2Dau, viTriTieuDe1, viTriTieuDe2;
    private String mssv,mssv2;
    private SinhVien thongTinSinhVien,sinhVien2;
    private Ve thongTinVe;
    private Common common;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private CheckTienTrinh checkTienTrinh;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String docId;
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        common = new Common();
        checkTienTrinh = new CheckTienTrinh();
        setContentView(R.layout.activity_thong_tin_sinh_vien1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        AnhXa();
        final FoldingCell fc = findViewById(R.id.folding_cell);
        final FoldingCell fc2 = findViewById(R.id.folding_cell2);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
        fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc2.toggle(false);
            }
        });
        executorService = Executors.newFixedThreadPool(5);
        linearLayout.setVisibility(View.GONE);
        Intent intent = getIntent();
        mssv = intent.getStringExtra(KEY_MSSV1);
        mssv2 = intent.getStringExtra(KEY_MSSV2);
        executorService.submit(new ThongTinSinhVien());
        executorService.submit(new ThongTinVe());
        executorService.submit(new ThongTinSinhVien2());
        executorService.submit(new ThongTinVe2());
        nutGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigInteger mssvBigInt = new BigInteger(mssv);
                BigInteger mssvBigInt2 = new BigInteger(mssv2);
                xacNhanYeuCau(mssvBigInt,mssvBigInt2,thongTinVe,sinhVien2);
            }
        });
        nutHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                huyYeuCau();
            }
        });
    }

    @Override
    public void xacNhanYeuCau(BigInteger mssvYeuCau, BigInteger mssvNhan, Ve ve, SinhVien sinhVien){

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Vui lòng đợi...");
        pDialog.setCancelable(false);
        pDialog.show();
        ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
        Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
        String nguoiTao = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j,
                thongTinWeb3.getCredentialsWallet(),new StaticGasProvider(ThongTinWeb3.GAS_PRICE,ThongTinWeb3.GAS_LIMIT));
        try {
            CompletableFuture<TransactionReceipt> tinhTrang = sukien_sol_sukien.giaoDich(mssvYeuCau,mssvNhan,nguoiTao,sinhVien.getHovaten()
            ,sinhVien.getTen(),ve.getMasukien(),ve.getMave(),ve.getVitri()).sendAsync();
            tinhTrang.thenAccept(transactionReceipt -> {
                if (transactionReceipt.isStatusOK()){
                    firestore.collection("yeucau").document(docId)
                            .update("trangthai",1)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pDialog.cancel();
                                    nutGiaoDich.setVisibility(View.GONE);
                                    nutHuy.setVisibility(View.GONE);
                                    Alerter.create(ThongTinSinhVien1.this)
                                            .setTitle("Xác nhận yêu cầu thành công")
                                            .setText("Bạn đã xác nhận yêu cầu thành công."+
                                                    "Bạn có thể quay lại và xác nhận yêu cầu khác")
                                            .setBackgroundColorRes(R.color.success)
                                            .setIcon(R.drawable.ic_baseline_check)
                                            .enableSwipeToDismiss().setDuration(4000).show();
                                    web3j.shutdown();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            nutGiaoDich.setVisibility(View.GONE);
                            nutHuy.setVisibility(View.GONE);
                            pDialog.cancel();
                            web3j.shutdown();
                            Log.w(TAG, e.getMessage(), e);
                            Alerter.create(ThongTinSinhVien1.this)
                                    .setTitle("Phát hiện lỗi")
                                    .setText("Chưa thay đổi được trạng thái vì lỗi phát sinh")
                                    .setBackgroundColorRes(R.color.red)
                                    .setIcon(R.drawable.ic_baseline_close_24)
                                    .enableSwipeToDismiss().setDuration(4000).show();
                        }
                    });
                }else{
                    Alerter.create(ThongTinSinhVien1.this)
                            .setTitle("Phát hiện lỗi")
                            .setText("Xác nhận yêu cầu không thành công")
                            .setBackgroundColorRes(R.color.red)
                            .setIcon(R.drawable.ic_baseline_close_24)
                            .enableSwipeToDismiss().setDuration(4000).show();
                }
            });
        } catch (Exception e) {
            pDialog.cancel();
            web3j.shutdown();
            e.printStackTrace();
            Log.w(TAG,e.getMessage(),e);
            Alerter.create(ThongTinSinhVien1.this)
                    .setTitle("Phát hiện lỗi không thể thực hiện chức năng này")
                    .setText("Đã có lỗi xảy ra không thể thực hiện yêu cầu chuyển vé")
                    .setBackgroundColorRes(R.color.red)
                    .setIcon(R.drawable.ic_baseline_close_24)
                    .enableSwipeToDismiss().setDuration(4000).show();
        }
    }

    @Override
    public void huyYeuCau() {
        firestore.collection("yeucau").document(docId)
                .update("trangthai",2).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                nutGiaoDich.setVisibility(View.GONE);
                nutHuy.setVisibility(View.GONE);
                Alerter.create(ThongTinSinhVien1.this)
                        .setTitle("Hủy Yêu Cầu Thành Công")
                        .setText("Bạn đã hủy yêu cầu thành công."+
                                "Bạn có thể quay lại và xác nhận yêu cầu khác")
                        .setBackgroundColorRes(R.color.success)
                        .setIcon(R.drawable.ic_baseline_check)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG,e.getMessage(),e);
                Alerter.create(ThongTinSinhVien1.this)
                        .setTitle("Phát hiện lỗi")
                        .setText("Có lỗi xảy ra không thể hủy yêu cầu")
                        .setBackgroundColorRes(R.color.red)
                        .setIcon(R.drawable.ic_baseline_close_24)
                        .enableSwipeToDismiss().setDuration(4000).show();
            }
        });
    }

    class CheckTienTrinh{
        private boolean t1;
        private boolean t2;
        private boolean t3;
        private boolean t4;

        public void setT3(boolean t3) {
            this.t3 = t3;
        }

        public void setT4(boolean t4) {
            this.t4 = t4;
        }

        public void setT1(boolean t1) {
            this.t1 = t1;
        }
        public void setT2(boolean t2) {
            this.t2 = t2;
        }
        public void check(){
            if (t1 && t2 && t3 && t4){
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
        String viTri = ve.component7();
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
                && ve.component4().isEmpty()&& ve.component7().isEmpty() && ve.component5().isEmpty() && ve.component6().isEmpty()
                && ve.component8().compareTo(new BigInteger("0")) == 0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String thongTinKhongCo = "Không Có Thông Tin Vé";
                    maVeSinhVien1.setText(thongTinKhongCo);
                    LinearLayout thongTin1 = findViewById(R.id.chiTietThongTinVeSinhVien1);
                    thongTin1.setVisibility(View.GONE);
                    viTriTieuDe1.setVisibility(View.GONE);
                    nutGiaoDich.setVisibility(View.GONE);
                }
            });

        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MultiFormatWriter writer = new MultiFormatWriter();
                        BitMatrix bitMatrix = writer.encode(maVe, BarcodeFormat.CODE_39,300,50);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imgBarCode.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    mssvSinhVien1.setText(maVe);
                    if (ve.component9()){
                        soHuuSinhVien1.setTextColor(Color.GREEN);
                        soHuuSinhVien1.setText("Vé Hợp Lệ");
                    }else{
                        soHuuSinhVien1.setTextColor(Color.RED);
                        soHuuSinhVien1.setText("Không Sở Hữu");
                    }
                    viTriSinhVien1.setText(viTri);
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
                String fullname = sinhVien.getHovaten() + " " +sinhVien.getTen();
                tenSinhVien1.setText(fullname);
                tenSinhVien1Dau.setText(fullname);
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
        imgBarCode2 = findViewById(R.id.imageSinhVien2);
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
        viTriSinhVien1 = (TextView) findViewById(R.id.thongTinViTriSinhVien1);
        viTriSinhVien2 = (TextView) findViewById(R.id.thongTinViTriSinhVien2);
        tenSinhVien1Dau = (TextView) findViewById(R.id.thongTinTenSinhVien1Dau);
        mssvSinhVien2 = (TextView) findViewById(R.id.mssvSinhVien2);
        tenSinhVien2 = (TextView) findViewById(R.id.thongTinTenSinhVien2);
        gioiTinhSinhVien2 = (TextView) findViewById(R.id.thongTinGioiTinhSinhVien2);
        khoaSinhVien2 = (TextView) findViewById(R.id.thongTinKhoaSinhVien2);
        lopSinhVien2 = (TextView) findViewById(R.id.thongTinLopSinhVien2);
        ngaySinhSinhVien2 = findViewById(R.id.thongTinNgaySinhSinhVien2);
        soHuuSinhVien2 = (TextView) findViewById(R.id.thongTinSoHuuSinhVien2);
        maVeSinhVien2 = (TextView) findViewById(R.id.thongTinMaVeSinhVien2);
        nguoiTaoVeSinhVien2 = (TextView) findViewById(R.id.thongTinNguoiTaoVeSinhVien2);
        maSuKienSinhVien2 = (TextView) findViewById(R.id.thongTinMaSuKienSinhVien2);
        ngayLapVeSinhVien2 = (TextView) findViewById(R.id.thongTinNgayLapSinhVien2);
        tenSinhVien2Dau = (TextView) findViewById(R.id.thongTinTenSinhVien2Dau);
        linearLayout = findViewById(R.id.noiDungVe);
        progressBar = findViewById(R.id.progressBar2);
        nutHuy= (FloatingActionButton) findViewById(R.id.nutHuySinhVien1);
        viTriTieuDe1 = findViewById(R.id.viTriTieuDe1);
        viTriTieuDe2 = findViewById(R.id.viTriTieuDe2);
    }
    public String getSuKien(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        String noidung = Objects.requireNonNull(response.body()).string();
        Gson gson = new Gson();
        SuKien[] suKiens = gson.fromJson(noidung,SuKien[].class);
        return suKiens[0].getMasukien();
    }
    public SinhVien getSinhVien(String mssv) throws IOException {
        String url = "https://ptta-cnm.herokuapp.com/taikhoan/" + mssv;
        Request.Builder builder = new Request.Builder();
        Request request  = builder.url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        String noiDung = Objects.requireNonNull(response.body()).string();
        Gson gson = new Gson();
        SinhVien[] sinhViens = gson.fromJson(noiDung,SinhVien[].class);
        if (sinhViens.length ==0){
            return null;
        }else{
            return sinhViens[0];
        }
    }
    class ThongTinSinhVien implements Runnable {

        @Override
        public void run() {
            try {
                if (getSinhVien(mssv) != null){
                    thongTinSinhVien = getSinhVien(mssv);
                    showThongTin(thongTinSinhVien);
                    checkTienTrinh.setT1(true);
                    checkTienTrinh.check();
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Alerter.create(ThongTinSinhVien1.this)
                                    .setTitle("Thông Báo").setText("Không có thông tin vé của sinh viên trong hệ thống")
                                    .setBackgroundColorRes(R.color.red)
                                    .setIcon(R.drawable.ic_baseline_close_24)
                                    .enableSwipeToDismiss().setDuration(4000).show();
                            progressBar.setVisibility(View.GONE);
                            nutGiaoDich.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }

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

        @Override
        public void run() {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j,thongTinWeb3.getCredentialsWallet(),
                    new DefaultGasProvider());
            BigInteger mssvBigInteger = new BigInteger(mssv);
            try {
                Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> duLieu
                        = sukien_sol_sukien.searVe(mssvBigInteger,getSuKien(url)).send();
                thongTinVe = new Ve(duLieu);
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
    class ThongTinVe2 implements Runnable{

        @Override
        public void run() {
            String url = "https://ptta-cnm.herokuapp.com/sukien/trangthai";
            ThongTinWeb3 thongTinWeb3 = new ThongTinWeb3();
            Web3j web3j = Web3j.build(new HttpService(ThongTinWeb3.URL));
            Sukien_sol_Sukien sukien_sol_sukien = Sukien_sol_Sukien.load(ThongTinWeb3.ADDRESS,web3j,thongTinWeb3.getCredentialsWallet(),
                    new DefaultGasProvider());
            BigInteger mssvBigInteger = new BigInteger(mssv2);
            try {
                Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> duLieu
                        = sukien_sol_sukien.searVe(mssvBigInteger,getSuKien(url)).send();
                showVeSinhVien2(duLieu);
                checkTienTrinh.setT3(true);
                checkTienTrinh.check();
            } catch (Exception e) {
                e.printStackTrace();
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
        private void showVeSinhVien2(Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> ve){
            String nguoiTao = ve.component2();
            String maSuKien = ve.component3();
            String maVe = ve.component6();
            String viTri = ve.component7();
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
                    && ve.component4().isEmpty()&& ve.component7().isEmpty() && ve.component5().isEmpty() && ve.component6().isEmpty()
                    && ve.component8().compareTo(new BigInteger("0")) == 0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String thongTinKhongCo = "Không Có Thông Tin Vé";
                        maVeSinhVien2.setText(thongTinKhongCo);
                        LinearLayout thongTin1 = findViewById(R.id.chiTietThongTinVeSinhVien2);
                        thongTin1.setVisibility(View.GONE);
                        viTriTieuDe2.setVisibility(View.GONE);
                    }
                });

            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MultiFormatWriter writer = new MultiFormatWriter();
                            BitMatrix bitMatrix = writer.encode(maVe, BarcodeFormat.CODE_39,300,50);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            imgBarCode2.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        mssvSinhVien2.setText(maVe);
                        if (ve.component9()){
                            nutGiaoDich.setVisibility(View.GONE);
                            soHuuSinhVien2.setTextColor(Color.GREEN);
                            soHuuSinhVien2.setText("Vé Hợp Lệ");
                        }else{
                            soHuuSinhVien2.setTextColor(Color.RED);
                            soHuuSinhVien2.setText("Không Sở Hữu");
                        }
                        viTriSinhVien2.setText(viTri);
                        maVeSinhVien2.setText(maVe);
                        nguoiTaoVeSinhVien2.setText(nguoiTao);
                        maSuKienSinhVien2.setText(maSuKien);
                        ngayLapVeSinhVien2.setText(dateTimeString);
                    }
                });
            }
        }
    }
    class ThongTinSinhVien2 implements Runnable{

        @Override
        public void run() {
            try {
                if (getSinhVien(mssv2) != null){
                    sinhVien2 = getSinhVien(mssv2);
                    showThongTinSinhVien2(sinhVien2);
                    checkTienTrinh.setT4(true);
                    checkTienTrinh.check();
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Alerter.create(ThongTinSinhVien1.this)
                                    .setTitle("Thông Báo").setText("Không có thông tin vé của sinh viên trong hệ thống")
                                    .setBackgroundColorRes(R.color.red)
                                    .setIcon(R.drawable.ic_baseline_close_24)
                                    .enableSwipeToDismiss().setDuration(4000).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }
        }
        private void showThongTinSinhVien2(SinhVien sinhVien){
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 String fullname = sinhVien.getHovaten() + " " +sinhVien.getTen();
                 tenSinhVien2.setText(fullname);
                 tenSinhVien2Dau.setText(fullname);
                 if(sinhVien.getGoitinh() == 1 ){
                     gioiTinhSinhVien2.setText("Nam");
                 }else{
                     gioiTinhSinhVien2.setText("Nữ");
                 }
                 khoaSinhVien2.setText(sinhVien.getKhoa());
                 lopSinhVien2.setText(sinhVien.getLop());
                 ngaySinhSinhVien2.setText(sinhVien.getNgaySinh());
             }
         });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);
        Integer integer = Integer.valueOf(mssv);
        firestore.collection("yeucau").whereEqualTo("mssvyeucau",integer)
                .whereEqualTo("trangthai",0)
                .addSnapshotListener(this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        String email = auth.getCurrentUser().getEmail();
                        if(error != null){
                            Toast.makeText(ThongTinSinhVien1.this, "Có lỗi khi load dữ liệu",Toast.LENGTH_LONG).show();
                            Log.d(TAG,error.getMessage());
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value){
                            if (doc.get("tuongtac") != null){
                                docId = doc.getId();
                                if (!Objects.equals(doc.get("tuongtac"), email)){
                                    Toast.makeText(ThongTinSinhVien1.this,"Đã có người xử lý",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        }
                    }
                });
    }


    @Override
    protected void onStop() {
        super.onStop();
        executorService.shutdown();
        unregisterReceiver(common);
        firestore.collection("yeucau").document(docId).update("tuongtac","");

    }

}