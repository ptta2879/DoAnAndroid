package com.IUH.quetma;

public class CongTacVien {
    private String email;
    private Integer phanquyen;

    public Integer getPhanQuyen() {
        return phanquyen;
    }

    public String getEmail() {
        return email;
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
