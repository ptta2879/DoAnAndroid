package com.IUH.FastEvent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.IUH.FastEvent.Model.SinhVien;
import com.IUH.FastEvent.Model.Ve;
import com.IUH.FastEvent.Web3j.Sukien_sol_Sukien;
import com.IUH.FastEvent.Web3j.ThongTinWeb3;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tapadoo.alerter.Alerter;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import cn.pedant.SweetAlert.SweetAlertDialog;

interface XacNhanListener{
    void btnXacNhan(Ve ve, BigInteger mssv1);
}
public class GiaoDichBottomSheet extends BottomSheetDialogFragment {
    private static final String KEY_SINHVIEN_GIAODICH = "sinhvien_object" ;
    private static final String KEY_VE_SINHVIEN1 ="sinhvien_Ve" ;
    private SinhVien thongTinSinhVien;
    private Ve thongTinVeSinhVien1;
    private TextView tenSinhVien2, mssvSinhVien2,gioiTinhSinhVien2;
    private Button btnXacNhan,btnHuy;
    private XacNhanListener xacNhanListener;

    public static GiaoDichBottomSheet newInstance(SinhVien sinhVien, Ve ve){
        GiaoDichBottomSheet giaoDichBottomSheet = new GiaoDichBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SINHVIEN_GIAODICH,(Serializable) sinhVien);
        bundle.putSerializable(KEY_VE_SINHVIEN1,(Serializable) ve);
        giaoDichBottomSheet.setArguments(bundle);
        return giaoDichBottomSheet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleSinhVien = getArguments();
        if (bundleSinhVien != null){
            thongTinSinhVien = (SinhVien) bundleSinhVien.get(KEY_SINHVIEN_GIAODICH);
            thongTinVeSinhVien1 = (Ve) bundleSinhVien.get(KEY_VE_SINHVIEN1);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_giaodich_bottom_sheet,null);
        bottomSheetDialog.setContentView(view);

        anhXa(view);
        setData();

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigInteger mssv1 = new BigInteger(thongTinSinhVien.getMssv());
                xacNhanListener.btnXacNhan(thongTinVeSinhVien1,mssv1);
                bottomSheetDialog.dismiss();
            }
        });
        return bottomSheetDialog;
    }
    private void anhXa(View view){
        tenSinhVien2 = view.findViewById(R.id.tenSinhVien2);
        mssvSinhVien2 = view.findViewById(R.id.mssvSinhVien2);
        gioiTinhSinhVien2 = view.findViewById(R.id.gioiTinhSinhVien2);
        btnHuy = view.findViewById(R.id.btnHuySinhVien2);
        btnXacNhan = view.findViewById(R.id.xacNhanGiaoDich);
    }
    private void setData(){
        if(thongTinSinhVien ==null){
            return;
        }else{
            String hoVaTen = thongTinSinhVien.getHovaten() + " " + thongTinSinhVien.getTen();
            tenSinhVien2.setText(hoVaTen);
            mssvSinhVien2.setText(thongTinSinhVien.getMssv());
            if(thongTinSinhVien.getGoitinh() ==1){
                gioiTinhSinhVien2.setText("Nam");
            }else{
                gioiTinhSinhVien2.setText("Nữ");
            }
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            xacNhanListener = (XacNhanListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+"phai implement XacNhanListener");
        }

    }
}
