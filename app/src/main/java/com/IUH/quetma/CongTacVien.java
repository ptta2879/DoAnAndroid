package com.IUH.quetma;

public class CongTacVien {
    private String email;
    private Integer phanquyen;
    private String hoatdong;

    public Integer getPhanQuyen() {
        return phanquyen;
    }

    public String getEmail() {
        return email;
    }

    public String getHoatdong() {
        return hoatdong;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhanQuyen(Integer phanquyen) {
        this.phanquyen = phanquyen;
    }
    public void setPhanQuyen(String phanquyen) {
        this.phanquyen = Integer.parseInt(phanquyen);
    }

}
