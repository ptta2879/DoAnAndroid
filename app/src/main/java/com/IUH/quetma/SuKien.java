package com.IUH.quetma;

public class SuKien {
    private Integer choNgoi;
    private String MaSuKien;
    private String hoatDong;
    public void setChoNgoi(Integer choNgoi) {
        this.choNgoi = choNgoi;
    }

    public void setMaSuKien(String maSuKien) {
        MaSuKien = maSuKien;
    }

    public void setHoatDong(String hoatDong) {
        this.hoatDong = hoatDong;
    }

    public Integer getChoNgoi() {
        return choNgoi;
    }

    public String getMaSuKien() {
        return MaSuKien;
    }

    public String getHoatDong() {
        return hoatDong;
    }
}
