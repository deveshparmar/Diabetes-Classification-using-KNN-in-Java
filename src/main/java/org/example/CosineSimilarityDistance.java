package org.example;

public class CosineSimilarityDistance implements DistanceMetric {
    @Override
    public double calculateDistance(double[] x, double[] y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("Input arrays must not be null");
        }
        double dotProduct = 0;
        double magnitudeX = 0;
        double magnitudeY = 0;

        for (int i = 0; i < x.length; i++) {
            dotProduct += x[i] * y[i];
            magnitudeX += Math.pow(x[i], 2);
            magnitudeY += Math.pow(y[i], 2);
        }

        magnitudeX = Math.sqrt(magnitudeX);
        magnitudeY = Math.sqrt(magnitudeY);

        return 1 - dotProduct / (magnitudeX * magnitudeY);
    }
}
