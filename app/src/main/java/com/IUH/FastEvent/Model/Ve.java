package com.IUH.FastEvent.Model;

import android.annotation.SuppressLint;

import org.web3j.tuples.generated.Tuple9;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Ve implements Serializable{
    private BigInteger mssv;
    private String nguoiTao;
    private String masukien;
    private String ho;
    private String ten;
    private String mave;
    private String vitri;
    private BigInteger date;
    private Boolean sohuu;

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }

    public Ve(BigInteger mssv, String nguoiTao, String masukien, String ho, String ten, String mave, String vitri, BigInteger date, Boolean sohuu) {
        this.mssv = mssv;
        this.nguoiTao = nguoiTao;
        this.masukien = masukien;
        this.ho = ho;
        this.ten = ten;
        this.mave = mave;
        this.vitri = vitri;
        this.date = date;
        this.sohuu = sohuu;
    }
    public Ve(){

    }
    public Ve(Tuple9<BigInteger, String, String, String, String, String,String, BigInteger, Boolean> ve){
        this.mssv=ve.component1();
        this.nguoiTao=ve.component2();
        this.masukien=ve.component3();
        this.ho=ve.component4();
        this.ten=ve.component5();
        this.mave=ve.component6();
        this.vitri= ve.component7();
        this.date=ve.component8();
        this.sohuu=ve.component9();
    }


    public void setMssv(BigInteger mssv) {
        this.mssv = mssv;
    }

    public void setNguoiTao(String nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public void setMasukien(String masukien) {
        this.masukien = masukien;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setMave(String mave) {
        this.mave = mave;
    }

    public void setDate(BigInteger date) {
        this.date = date;
    }

    public void setSohuu(Boolean sohuu) {
        this.sohuu = sohuu;
    }

    public BigInteger getMssv() {
        return mssv;
    }

    public String getNguoiTao() {
        return nguoiTao;
    }

    public String getMasukien() {
        return masukien;
    }

    public String getHo() {
        return ho;
    }

    public String getTen() {
        return ten;
    }

    public String getMave() {
        return mave;
    }

    public BigInteger getDate() {
        return date;
    }

    public Boolean getSohuu() {
        return sohuu;
    }
    public String getDateString(){
        long dateTimeLong = date.longValue() * 1000;
        String dateTimeString;
        Date ngayLap = new Date(dateTimeLong);
        TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        ngayLapFormat.setTimeZone(timeZoneVN);
        dateTimeString = ngayLapFormat.format(ngayLap);
        return dateTimeString;
    }

}
