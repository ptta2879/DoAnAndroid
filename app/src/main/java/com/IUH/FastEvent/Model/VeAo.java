package com.IUH.FastEvent.Model;

public class VeAo {
    private String mask;

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    private String mave;
    private String mssv;
    private Integer vitri;

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
        return vitri.toString();
    }

    public void setVitri(String vitri) {
        this.vitri = Integer.valueOf(vitri);
    }

    public VeAo() {
    }

    public VeAo(String mave, String mssv, String vitri) {
        this.mave = mave;
        this.mssv = mssv;
        this.vitri = Integer.valueOf(vitri);
    }
}
