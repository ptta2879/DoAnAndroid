package com.IUH.FastEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Toast;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.YeuCau;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;
import com.r0adkll.slidr.Slidr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GiaoDichSinhVien1 extends AppCompatActivity {
    private static final String TAG ="GiaoDinhSinhVien" ;
    private RecyclerView recyclerView;
    private Common common;
    private YeuCauAdapter yeuCauAdapter;
    private YeuCauViewModel yeuCauViewModel;
    private YeuCauAdapter.OnClickYeuCau listener;
    private DocumentSnapshot lastVisible;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_giao_dich_moi);
        common = new Common();
        Slidr.attach(this);
        recyclerView = findViewById(R.id.rcvYeuCau);
        yeuCauAdapter = new YeuCauAdapter();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        yeuCauViewModel = new ViewModelProvider(this).get(YeuCauViewModel.class);
        yeuCauViewModel.getMutableLiveData().observe(this, new Observer<ArrayList<YeuCau>>() {
            @Override
            public void onChanged(ArrayList<YeuCau> yeuCaus) {
                setOnClickListner(yeuCaus);
                yeuCauAdapter.setYeuCauArrayList(yeuCaus,listener);
                recyclerView.setAdapter(yeuCauAdapter);
            }
        });
    }

    private void setOnClickListner(ArrayList<YeuCau> yeuCaus) {
        listener = new YeuCauAdapter.OnClickYeuCau() {
            @Override
            public void onItemClick(View view, int position) {
                SweetAlertDialog pDialog = new SweetAlertDialog(GiaoDichSinhVien1.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();
                String mssvYeuCau = yeuCaus.get(position).getMssvyeucau().toString();
                String mssvNhan = yeuCaus.get(position).getMssvnhan().toString();

                db.collection("yeucau").whereEqualTo("mssvyeucau",yeuCaus.get(position).getMssvyeucau()).whereEqualTo("trangthai",0)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc: task.getResult()){
                            if (doc.get("tuongtac").equals("")){
                                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                db.collection("yeucau").document(doc.getId()).update("tuongtac", email).addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(GiaoDichSinhVien1.this, ThongTinSinhVien1.class);
                                                intent.putExtra(ThongTinSinhVien1.KEY_MSSV1, mssvYeuCau);
                                                intent.putExtra(ThongTinSinhVien1.KEY_MSSV2, mssvNhan);
                                                startActivity(intent);
                                                pDialog.dismissWithAnimation();
                                            }
                                        }
                                );
                            }
                            pDialog.dismissWithAnimation();
                        }
                    }
                });

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);
        db.collection("yeucau").whereEqualTo("trangthai",0).whereEqualTo("tuongtac","").limit(20)
                .addSnapshotListener(this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e(TAG,error.toString());
                        }
                        ArrayList<YeuCau> yeuCaus = new ArrayList<>();
                        for (DocumentSnapshot doc : value){
                            YeuCau yeuCau = doc.toObject(YeuCau.class);
                            if (yeuCau != null){
                                yeuCaus.add(yeuCau);
                            }
                        }
                        yeuCauViewModel.setMutableLiveData(yeuCaus);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(common);
    }
}