package com.sp.mad_studypal;

public class ReservationModel {
    private String name;
    private String studyarea;
    private String date;
    private String time;
    private String confirmstatus;
    private String qrcode;
    private String booking_id;

    public ReservationModel(String name,String studyarea, String date, String time, String confirmstatus, String qrcode, String booking_id) {
        this.name = name;
        this.studyarea = studyarea;
        this.date = date;
        this.time = time;
        this.confirmstatus = confirmstatus;
        this.qrcode = qrcode;
        this.booking_id = booking_id;
    }

    public String getName() {
        return name;
    }

    public String getStudyarea() {
        return studyarea;
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

    public String getBooking_id(){
        return booking_id;
    }
}

