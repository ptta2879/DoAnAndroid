package com.IUH.quetma;

public class SinhVien {
    private Integer gioitinh;
    private String hovaten;
    private String khoa;
    private String lop;
    private String mssv;
    private String nganh;
    private String ngaysinh;
    private String ten;
    public SinhVien(Integer goitinh, String hovaten,String khoa,String lop,String mssv,String nganh,String ngaysinh,String ten){
        this.ten = ten;
        this.mssv = mssv;
        this.gioitinh = goitinh;
        this.hovaten = hovaten;
        this.khoa = khoa;
        this.lop = lop;
        this.nganh = nganh;
        this.ngaysinh = ngaysinh;
    }
    public SinhVien(){

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
}
