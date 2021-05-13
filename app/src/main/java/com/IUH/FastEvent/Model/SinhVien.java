package com.IUH.FastEvent.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class SinhVien implements Parcelable {
    private Integer gioitinh;
    private String hovaten;
    private String khoa;
    private String lop;
    private String mssv;
    private String nganh;
    private String ngaysinh;
    private String ten;
    private String mave;
    private HashMap<String, Boolean> hoatdong;
    public SinhVien(Integer goitinh, String hovaten,String khoa,String lop,String mssv,String nganh,String ngaysinh,String ten,
                    HashMap<String, Boolean> hoatdong){
        this.hoatdong = new HashMap<String, Boolean>();
        this.ten = ten;
        this.mssv = mssv;
        this.gioitinh = goitinh;
        this.hovaten = hovaten;
        this.khoa = khoa;
        this.lop = lop;
        this.nganh = nganh;
        this.ngaysinh = ngaysinh;
        this.hoatdong = hoatdong;
    }
    public SinhVien(){
        this.hoatdong = new HashMap<String, Boolean>();
    }

    protected SinhVien(Parcel in) {
        if (in.readByte() == 0) {
            gioitinh = null;
        } else {
            gioitinh = in.readInt();
        }
        hovaten = in.readString();
        khoa = in.readString();
        lop = in.readString();
        mssv = in.readString();
        nganh = in.readString();
        ngaysinh = in.readString();
        ten = in.readString();
        mave = in.readString();
    }

    public static final Creator<SinhVien> CREATOR = new Creator<SinhVien>() {
        @Override
        public SinhVien createFromParcel(Parcel in) {
            return new SinhVien(in);
        }

        @Override
        public SinhVien[] newArray(int size) {
            return new SinhVien[size];
        }
    };

    public void setMave(String mave) {
        this.mave = mave;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public void setGioitinh(Integer gioitinh) {
        this.gioitinh = gioitinh;
    }

    public void setHovaten(String hovaten) {
        this.hovaten = hovaten;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }

    public void setNgaySinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public void setHoatdong(HashMap<String, Boolean> hoatdong) {
        this.hoatdong = hoatdong;
    }

    public String getTen() {
        return ten;
    }

    public Integer getGoitinh() {
        return gioitinh;
    }

    public String getHovaten() {
        return hovaten;
    }

    public String getKhoa() {
        return khoa;
    }

    public String getLop() {
        return lop;
    }

    public String getMssv() {
        return mssv;
    }

    public String getNganh() {
        return nganh;
    }

    public String getNgaySinh() {
        return ngaysinh;
    }

    public String getMave() {
        return mave;
    }

    public HashMap<String, Boolean> getHoatdong() {
        return hoatdong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        if (gioitinh == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(gioitinh);
        }
        dest.writeString(hovaten);
        dest.writeString(khoa);
        dest.writeString(lop);
        dest.writeString(mssv);
        dest.writeString(nganh);
        dest.writeString(ngaysinh);
        dest.writeString(ten);
        dest.writeString(mave);
    }
}
