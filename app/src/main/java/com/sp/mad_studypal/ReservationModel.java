package com.sp.mad_studypal;

import java.lang.annotation.Retention;

public class ReservationModel {
    private String name;
    private String date;
    private String time;
    private String confirmstatus;
    private String qrcode;

    public ReservationModel(String name, String date, String time,String confirmstatus,String qrcode) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.confirmstatus = confirmstatus;
        this.qrcode = qrcode;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    public String getConfirmstatus(){
        return confirmstatus;
    }
    public String getQrcode(){
        return qrcode;
    }
}

