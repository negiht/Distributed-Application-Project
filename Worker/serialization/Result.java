package com.alexronnegi.serialization;

import java.io.Serializable;

public class Result implements Serializable {
    private double time;
    private double distance;
    private double elevation;
    private static final long serialVersionUID = -6470090944414208496L;

    public Result(double time, double distance, double elevation) {
        this.time = time;
        this.distance = distance;
        this.elevation = elevation;
    }

    public Result() {
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    @Override
    public String toString() {
        return "Result{" + "time=" + time + ", distance=" + distance + ", elevation=" + elevation + '}';
    }
    
    
}
