package com.example.myapplication.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Reservation {
    private LocalDateTime startTime, endTime;
    private long durasi;
    private double harga, totalHarga;

    private String universityName;


    public Reservation(LocalDateTime startTime, LocalDateTime endTime, int harga){
        this.startTime = startTime;
        this.endTime = endTime;
        this.harga = harga;
    }
    private long calculateDuration(){
        return ChronoUnit.MINUTES.between(startTime, endTime);
    }

    private double calculateTotalHarga(){
        return harga*durasi;
    }
    public  LocalDateTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
        this.durasi = calculateDuration();
        this.totalHarga = calculateTotalHarga();
    }

    public LocalDateTime getEndTime(){
        return  endTime;
    }

    public void  setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
        this.durasi = calculateDuration();
        this.totalHarga = calculateTotalHarga();
    }

    public  long getDurasi(){
        return  durasi;
    }

    public double getHarga(){
        return  harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
        this.totalHarga = calculateTotalHarga();
    }
    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public  double getTotalHarga(){
        return  totalHarga;
    }
}
