package com.example.viva;

public class Room {

    private String id, HallNo, maxPerson,Rules;

    public Room(){

    }

    public Room(String id, String hallNo, String maxPerson, String rules) {
        this.id = id;
        HallNo = hallNo;
        this.maxPerson = maxPerson;
        Rules = rules;
    }

    public String getId() {
        return id;
    }

    public String getHallNo() {
        return HallNo;
    }

    public void setHallNo(String hallNo) {
        HallNo = hallNo;
    }

    public String getMaxPerson() {
        return maxPerson;
    }

    public void setMaxPerson(String maxPerson) {
        this.maxPerson = maxPerson;
    }

    public String getRules() {
        return Rules;
    }

    public void setRules(String rules) {
        Rules = rules;
    }

}

