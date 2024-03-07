package com.alexronnegi.serialization;

import java.io.Serializable;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "gpx")
public class Gpx implements Serializable {

    @Attribute(name = "version")
    private double version;

    @Attribute(name = "creator")
    private String user;

    @ElementList(inline = true)
    private List<Wpt> elements;

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Wpt> getElements() {
        return elements;
    }

    public void setElements(List<Wpt> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "Gpx{" + "version=" + version + ", user=" + user + '}';
    }

    
}
