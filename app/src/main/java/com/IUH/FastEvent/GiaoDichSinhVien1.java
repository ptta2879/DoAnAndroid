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
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.IUH.FastEvent.Model.Common;
import com.IUH.FastEvent.Model.YeuCau;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
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

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GiaoDichSinhVien1 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Common common;
    private YeuCauAdapter yeuCauAdapter;
    private YeuCauViewModel yeuCauViewModel;
    private YeuCauAdapter.OnClickYeuCau listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_giao_dich_moi);
        common = new Common();
        Slidr.attach(this);
        recyclerView = findViewById(R.id.rcvYeuCau);
        yeuCauAdapter = new YeuCauAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
                Toast.makeText(getApplicationContext(),yeuCaus.get(position).getMssvnhan().toString(),Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(common,intentFilter);

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
        yeuCauViewModel.stopListenYeuCau();
    }


}