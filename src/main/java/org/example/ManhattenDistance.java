package org.example;

public class ManhattenDistance implements DistanceMetric{
    @Override
    public double calculateDistance(double[] x, double[] y) {
        if (x == null || y == null) {
            System.out.println(x.length + " " + y.length);
            throw new IllegalArgumentException("Input arrays must not be null and must have the same length");
        }
        double sum = 0;
        for(int i=0;i<x.length;i++){
            sum += Math.abs(y[i]-x[i]);
        }
        return sum;
    }
}
