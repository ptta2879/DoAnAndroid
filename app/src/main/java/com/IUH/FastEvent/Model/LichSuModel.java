package com.IUH.FastEvent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.web3j.tuples.generated.Tuple4;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LichSuModel implements Parcelable {
    private BigInteger mssvBan;
    private String maVe;
    private BigInteger dateInt;
    private BigInteger mssvNhan;

    public LichSuModel(){

    }
    public LichSuModel(Tuple4<BigInteger, String, BigInteger, BigInteger> lichSu){
       this.mssvBan = lichSu.component1();
        this.maVe = lichSu.component2();
        this.dateInt = lichSu.component3();
        this.mssvNhan = lichSu.component4();
    }

    protected LichSuModel(Parcel in) {
        maVe = in.readString();
    }

    public static final Creator<LichSuModel> CREATOR = new Creator<LichSuModel>() {
        @Override
        public LichSuModel createFromParcel(Parcel in) {
            return new LichSuModel(in);
        }

        @Override
        public LichSuModel[] newArray(int size) {
            return new LichSuModel[size];
        }
    };

    public BigInteger getMssvBan() {
        return mssvBan;
    }

    public void setMssvBan(BigInteger mssvBan) {
        this.mssvBan = mssvBan;
    }


    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public BigInteger getDateInt() {
        return dateInt;
    }

    public void setDateInt(BigInteger dateInt) {
        this.dateInt = dateInt;
    }

    public BigInteger getMssvNhan() {
        return mssvNhan;
    }

    public void setMssvNhan(BigInteger mssvNhan) {
        this.mssvNhan = mssvNhan;
    }
    public String getDateString(){
        long dateLong = this.dateInt.longValue() *1000;
        Date date = new Date(dateLong);
        TimeZone timeZoneVN = TimeZone.getTimeZone("Asia/Ho_Chi_Minh") ;
        SimpleDateFormat ngayLapFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        ngayLapFormat.setTimeZone(timeZoneVN);
        return ngayLapFormat.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maVe);
    }
}
