package com.alexronnegi.serialization;

import java.io.Serializable;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Wpt implements Serializable {

    @Attribute(name = "lon")
    private double longitude;

    @Attribute(name = "lat")
    private double latitude;

    @Element(name = "ele")
    private double elevation;

    @Element(name = "time")
    private String time;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Wpt{" + "longitude=" + longitude + ", latitude=" + latitude + ", elevation=" + elevation + ", time=" + time + '}';
    }
}
