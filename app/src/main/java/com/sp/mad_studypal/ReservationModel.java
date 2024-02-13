package com.sp.mad_studypal;

public class ReservationModel {
    private String name;
    private String date;
    private String time;
    private int seatNo;
    private String confirmstatus;

    public ReservationModel(String name, String date, String time, int seatNo ,String confirmstatus) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.seatNo = seatNo;
        this.confirmstatus = confirmstatus;
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

    public int getSeatNo() {
        return seatNo;
    }
    public String getConfirmstatus(){
        return confirmstatus;
    }
}

