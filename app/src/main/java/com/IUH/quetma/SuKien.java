package com.IUH.quetma;


public class SuKien {
    private Integer chongoi;
    private String masukien;
    private String[] hoatdong;

    public SuKien(Integer chongoi, String masukien, String[] hoatdong){
        this.chongoi = chongoi;
        this.masukien = masukien;
        this.hoatdong = hoatdong;
    }
    public void setHoatdong(String[] hoatdong) {
        this.hoatdong = hoatdong;
    }

    public void setMasukien(String masukien) {
        this.masukien = masukien;
    }

    public void setChongoi(Integer chongoi) {
        this.chongoi = chongoi;
    }

    public String getMasukien() {
        return masukien;
    }

    public Integer getChongoi() {
        return chongoi;
    }

    public String[] getHoatdong() {
        return hoatdong;
    }
}
