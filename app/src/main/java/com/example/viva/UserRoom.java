package com.example.viva;

public class UserRoom {
    private String roomName, time, date, calendarDate;
    private int price, hour;

    //public  UserRoom(String roomName, String time, String date, String calendar_date, int roomResImg, int price, int hour){ }

    public UserRoom(String roomName, String time, String date, String calendarDate, int price, int hour) {
        this.roomName = roomName;
        this.time = time;
        this.date = date;
        this.calendarDate = calendarDate;
        this.price = price;
        this.hour = hour; }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(String calendarDate) {
        this.calendarDate = calendarDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
