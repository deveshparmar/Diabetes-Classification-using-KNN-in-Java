package org.example;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KNN {
    private List<DataPoint> trainingData;
    private int k;
    private ExecutorService executorService;
    private final PriorityQueue<DataPoint> nearestNeighbors;


    public KNN(List<DataPoint> trainingData, int k) {
        this.trainingData = trainingData;
        this.k = k;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.nearestNeighbors = new PriorityQueue<>((a,b)->Double.compare(b.distance,a.distance));
    }

    public String classify(double[] features, DistanceMetric distanceMetric){
        nearestNeighbors.clear();
        List<DataPoint>nearestNeighbors = findNearestNeighbors(features,distanceMetric);
        String predictedLabel = findVote(nearestNeighbors);
        return predictedLabel;
    }

    private String findVote(List<DataPoint> neighbors) {
        Map<String,Double> votes = new HashMap<>();

        for(DataPoint neighbor : neighbors){
            String label = neighbor.label;
            double weight = 1.0/ (neighbor.distance + 1e-10);
            votes.put(label, votes.getOrDefault(label,0.0)+weight);
        }

        String predictedLabel = Collections.max(votes.entrySet(),Map.Entry.comparingByValue()).getKey();
        return predictedLabel;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private List<DataPoint> findNearestNeighbors(double[] features, DistanceMetric distanceMetric) {
        List<Future<Void>>tasks = new ArrayList<>();

        for(DataPoint dataPoint : trainingData){
            Future<Void>task = executorService.submit(()->{
                double distance = distanceMetric.calculateDistance(features,dataPoint.features);
//                dataPoint.features=null;
//                dataPoint.distance=distance;

                synchronized (nearestNeighbors){
                    if(nearestNeighbors.size()<k){
                        dataPoint.distance=distance;
                        nearestNeighbors.offer(dataPoint);
                    }else {
                        assert nearestNeighbors.peek() != null;
                        if(distance< nearestNeighbors.peek().distance){
                            nearestNeighbors.poll();
                            dataPoint.distance=distance;
                            nearestNeighbors.offer(dataPoint);
                        }
                    }
                }
                return null;
            });
            tasks.add(task);
        }

        for (Future<Void> task : tasks) {
            try {
                task.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>(nearestNeighbors);
    }
}
