package org.example.Model;

import java.util.Comparator;

public class Client {
    private int id;
    private int arrivalTime;
    private int serviceTime;

    public Client(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    //client info
    public String toString() {
        return "(" + "id" + this.id + ", " + "at" + this.arrivalTime + ", " + "st" + this.serviceTime + ")";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int ta) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int st) {
        this.serviceTime = serviceTime;
    }
}
