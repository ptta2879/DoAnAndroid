package com.IUH.FastEvent.Model;

public class VeAo {
    private String mave;
    private String mssv;
    private String vitri;

    public String getMave() {
        return mave;
    }

    public void setMave(String mave) {
        this.mave = mave;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }

    public VeAo() {
    }

    public VeAo(String mave, String mssv, String vitri) {
        this.mave = mave;
        this.mssv = mssv;
        this.vitri = vitri;
    }
}
