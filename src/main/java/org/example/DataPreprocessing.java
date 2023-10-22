package org.example;

import java.util.List;

public class DataPreprocessing {
    public static void minMaxScaling(List<DataPoint> dataPoints) {
        double[] minValues = new double[dataPoints.get(0).features.length];
        double[] maxValues = new double[dataPoints.get(0).features.length];


        for (DataPoint dataPoint : dataPoints) {
            for (int i = 0; i < dataPoint.features.length; i++) {
                if (dataPoint.features[i] < minValues[i]) {
                    minValues[i] = dataPoint.features[i];
                }
                if (dataPoint.features[i] > maxValues[i]) {
                    maxValues[i] = dataPoint.features[i];
                }
            }
        }
        for (DataPoint dataPoint : dataPoints) {
            for (int i = 0; i < dataPoint.features.length; i++) {
                dataPoint.features[i] = (dataPoint.features[i] - minValues[i]) / (maxValues[i] - minValues[i]);
            }
        }
    }
}
