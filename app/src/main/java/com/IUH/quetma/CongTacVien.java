package com.IUH.quetma;

public class CongTacVien {
    private String email;
    private Integer phanQuyen;

    public Integer getPhanQuyen() {
        return phanQuyen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhanQuyen(Integer phanQuyen) {
        this.phanQuyen = phanQuyen;
    }
    public void setPhanQuyen(String phanQuyen) {
        Integer phanQuyenInt = Integer.valueOf(phanQuyen);
        this.phanQuyen = phanQuyenInt;
    }
}
