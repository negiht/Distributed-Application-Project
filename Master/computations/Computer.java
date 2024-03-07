package com.alexronnegi.computations;

import com.alexronnegi.serialization.Gpx;
import com.alexronnegi.serialization.Result;
import com.alexronnegi.serialization.Wpt;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Computer {

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double distance = R * c;

        return distance.doubleValue();
    }

    public Result reduce(Gpx gpx) {
        Result result = new Result();

        double time = 0.0;
        double distance = 0.0;
        double elevation = 0.0;

        int n = gpx.getElements().size();

        for (int i = 0; i < n - 1; i++) {
            Wpt w1 = gpx.getElements().get(i);
            Wpt w2 = gpx.getElements().get(i + 1);

            double lat1 = w1.getLatitude();
            double lon1 = w1.getLongitude();
            double lat2 = w2.getLatitude();
            double lon2 = w2.getLongitude();

            distance += distance(lat1, lon1, lat2, lon2);

            double ele1 = w1.getElevation();
            double ele2 = w2.getElevation();

            elevation += Math.max(ele2 - ele1, 0);

            String t1 = w1.getTime();
            String t2 = w2.getTime();

            Date d1 = Date.from(Instant.parse(t1));
            Date d2 = Date.from(Instant.parse(t2));

            time += (d2.getTime() - d1.getTime());
        }

        result.setTime(time);
        result.setDistance(distance);
        result.setElevation(elevation);

        return result;
    }

    public Result reduce(Iterable<Result> results) {
        Result result = new Result();

        double time = 0.0;
        double distance = 0.0;
        double elevation = 0.0;

        for (Result r : results) {
            time += r.getTime();
            distance += r.getDistance();
            elevation += r.getElevation();
        }

        result.setTime(time);
        result.setDistance(distance);
        result.setElevation(elevation);

        return result;
    }
}
