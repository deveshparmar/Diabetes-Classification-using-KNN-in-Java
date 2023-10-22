package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {

        List<DataPoint> dataPoints = readDataFromCSV("C:\\Users\\deves\\OneDrive\\Desktop\\New folder\\SpringBoot\\ML\\src\\main\\java\\org\\example\\diabetes.csv");
        List<DataPoint> trainingData = new ArrayList<>();
        List<DataPoint> testData = new ArrayList<>();
        double trainSet = 0.8;
        Random random = new Random();

        for (DataPoint dataPoint : dataPoints) {
            if (random.nextDouble() < trainSet) {
                trainingData.add(dataPoint);
            } else {
                testData.add(dataPoint);
            }
        }


        KNN knn = new KNN(trainingData,3);
        printClassificationReport(testData,knn);

        double[] sampleFeatures = {0, 118, 84, 47, 230, 45.8, 0.551, 31};
        String predClass = knn.classify(sampleFeatures,new EuclidianDistance());
        System.out.println("predicted class for - " + Arrays.toString(sampleFeatures) + " is - "+ predClass);

        knn.shutdown();
    }

    private static List<DataPoint> createManualDataset() {
        List<DataPoint> dataPoints = new ArrayList<>();

        dataPoints.add(new DataPoint(new double[]{6, 148, 72, 35, 0, 33.6, 0.627, 50}, "1"));
        dataPoints.add(new DataPoint(new double[]{1, 85, 66, 29, 0, 26.6, 0.351, 31}, "0"));
        dataPoints.add(new DataPoint(new double[]{8, 183, 64, 0, 0, 23.3, 0.672, 32}, "1"));
        dataPoints.add(new DataPoint(new double[]{1, 89, 66, 23, 94, 28.1, 0.167, 21}, "0"));
        dataPoints.add(new DataPoint(new double[]{0, 137, 40, 35, 168, 43.1, 2.288, 33}, "1"));
        dataPoints.add(new DataPoint(new double[]{5, 116, 74, 0, 0, 25.6, 0.201, 30}, "0"));
        dataPoints.add(new DataPoint(new double[]{3, 78, 50, 32, 88, 31, 0.248, 26}, "1"));
        dataPoints.add(new DataPoint(new double[]{10, 115, 0, 0, 0, 35.3, 0.134, 29}, "0"));
        dataPoints.add(new DataPoint(new double[]{2, 197, 70, 45, 543, 30.5, 0.158, 53}, "1"));
        dataPoints.add(new DataPoint(new double[]{8, 125, 96, 0, 0, 0, 0.232, 54}, "1"));
        dataPoints.add(new DataPoint(new double[]{8, 125, 96, 0, 0, 0, 0.232, 54}, "0"));
        dataPoints.add(new DataPoint(new double[]{10, 168, 74, 0, 0, 38, 0.537, 34}, "1"));
        dataPoints.add(new DataPoint(new double[]{10, 139, 80, 0, 0, 27.1, 1.441, 57}, "0"));
        dataPoints.add(new DataPoint(new double[]{1, 189, 60, 23, 846, 30.1, 0.398, 59}, "1"));
        dataPoints.add(new DataPoint(new double[]{5, 166, 72, 19, 175, 25.8, 0.587, 51}, "1"));
        dataPoints.add(new DataPoint(new double[]{7, 100, 0, 0, 0, 30, 0.484, 32}, "1"));
        dataPoints.add(new DataPoint(new double[]{0, 118, 84, 47, 230, 45.8, 0.551, 31}, "1"));
        dataPoints.add(new DataPoint(new double[]{7, 107, 74, 0, 0, 29.6, 0.254, 31}, "1"));
        dataPoints.add(new DataPoint(new double[]{1, 103, 30, 38, 83, 43.3, 0.183, 33}, "0"));
        dataPoints.add(new DataPoint(new double[]{1, 115, 70, 30, 96, 34.6, 0.529, 32}, "1"));
        dataPoints.add(new DataPoint(new double[]{3, 126, 88, 41, 235, 39.3, 0.704, 27}, "0"));
        dataPoints.add(new DataPoint(new double[]{8, 99, 84, 0, 0, 35.4, 0.388, 50}, "0"));
        dataPoints.add(new DataPoint(new double[]{7, 196, 90, 0, 0, 39.8, 0.451, 41}, "1"));
        dataPoints.add(new DataPoint(new double[]{9, 119, 80, 35, 0, 29, 0.263, 29}, "1"));

        return dataPoints;
    }

    private static List<DataPoint> readDataFromCSV(String fileName) {
        List<DataPoint> dataPoints = new ArrayList<>();

        try(CSVReader csvReader = new CSVReader(new FileReader(fileName))){
            String[] nextLine;
            boolean isHeaderRow = true;
            while((nextLine= csvReader.readNext())!=null){
                if(isHeaderRow){
                    isHeaderRow = false;
                    continue;
                }
                double[] features = new double[nextLine.length-1];
                boolean hasNull = false;
                for(int i=0;i< features.length;i++){
                    try {
                        features[i] = Double.parseDouble(nextLine[i]);
                    }catch (NumberFormatException e){
                        features[i] = 0.0;
                        hasNull = true;
                    }
                }
                if(!hasNull) {
                    String label = nextLine[nextLine.length - 1];
                    dataPoints.add(new DataPoint(features, label));
                }
            }
        }catch (IOException | CsvValidationException e){
            e.printStackTrace();
        }
       return dataPoints;
    }


    public static void printClassificationReport(List<DataPoint> testData, KNN knn) {
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;

        for (DataPoint dataPoint : testData) {
            String predicted = knn.classify(dataPoint.features, new EuclidianDistance());
            String actualLabel = dataPoint.label;

            if (predicted.equals("1") && actualLabel.equals("1")) {
                truePositives++;
            } else if (predicted.equals("1") && actualLabel.equals("0")) {
                falsePositives++;
            } else if (predicted.equals("0") && actualLabel.equals("0")) {
                trueNegatives++;
            } else if (predicted.equals("0") && actualLabel.equals("1")) {
                falseNegatives++;
            }
        }

        int actualPositive = truePositives + falseNegatives;
        int actualNegative = trueNegatives + falsePositives;

        double precision = (double) truePositives / (truePositives + falsePositives);
        double recall = (double) truePositives / actualPositive;
        double f1Score = 2 * (precision * recall) / (precision + recall);
        double accuracy = (double) (truePositives + trueNegatives) / testData.size();
        double sensitivity = (double) truePositives / actualPositive;
        double specificity = (double) trueNegatives / actualNegative;

        System.out.println("Confusion Matrix:");
        System.out.printf("%20s %20s%n", "Actual Positive", "Actual Negative");
        System.out.printf("Predicted Positive %12d %12d%n", truePositives, falsePositives);
        System.out.printf("Predicted Negative %12d %12d%n", falseNegatives, trueNegatives);
        System.out.println();
        System.out.println("Classification Report:");
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F1 Score: " + f1Score);
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Sensitivity: " + sensitivity);
        System.out.println("Specificity: " + specificity);
        System.out.println();
    }

}