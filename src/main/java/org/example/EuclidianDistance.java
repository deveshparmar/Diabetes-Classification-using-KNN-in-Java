package org.example;

public class EuclidianDistance implements DistanceMetric{
    @Override
    public double calculateDistance(double[] x, double[] y) {
        if (x == null || y == null) {
            System.out.println(x.length + " " + y.length);
            throw new IllegalArgumentException("Input arrays must not be null and must have the same length");
        }
        double sum = 0;
        for(int i=0;i<x.length;i++){
            double diff = x[i]-y[i];
            sum+=diff*diff;
        }

        return Math.sqrt(sum);
    }
}
