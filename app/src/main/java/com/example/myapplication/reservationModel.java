package com.example.myapplication;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class reservationModel {
    private LocalDateTime startTime, endTime;
    private long durasi;
    private double harga, totalHarga;

    public reservationModel(LocalDateTime startTime, LocalDateTime endTime, int harga){
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

    public  double getTotalHarga(){
        return  totalHarga;
    }
}
