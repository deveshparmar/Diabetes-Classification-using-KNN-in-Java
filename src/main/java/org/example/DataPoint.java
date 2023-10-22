package org.example;

import java.util.Arrays;

public class DataPoint {
    double[] features;
    String label;
    double distance;

    public DataPoint(double[] features, String label) {
        this.features = features;
        this.label = label;
        this.distance = -1.0;
    }

    @Override
    public String toString() {
        return "DataPoint{" +
                "features=" + Arrays.toString(features) +
                ", label='" + label + '\'' +
                ", distance=" + distance +
                '}';
    }
}
