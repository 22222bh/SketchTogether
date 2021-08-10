package edu.skku.sketchdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Classifier {

    private int K;
    private double splitRatio;
    private double accuracy = 0;
    private int featureLen = 1280;
    private int featureNum = 598; // TODO


    private DistanceAlgorithm distanceAlgorithm;
    private List<DataPoint> listDataPoint;
    private List<DataPoint> listTrainData;
    private List<DataPoint> listTestData;
    private List<DataPoint> listTestValidator;
    private List<String> listNeighbors;
    private double[][] featureSet;

    public Classifier(){
        K = 5;
        splitRatio = 0.8;
        distanceAlgorithm = new EuclideanDistance();
        listDataPoint = new ArrayList<>();
        listTrainData = new ArrayList<>();
        listTestData = new ArrayList<>();
        listTestValidator = new ArrayList<>();
        featureSet = new double[featureNum + 1][featureLen];
    }

    public int getK() {
        return K;
    }

    public void setK(int k) {
        K = k;
    }

    public double getSplitRatio() {
        return splitRatio;
    }

    public void setSplitRatio(double splitRatio) {
        this.splitRatio = splitRatio;
    }

    public List<DataPoint> getListDataPoint() {
        return listDataPoint;
    }

    public void setListDataPoint(List<DataPoint> listDataPoint) {
        this.listDataPoint.clear();
        this.listDataPoint.addAll(listDataPoint);
    }

    public List<DataPoint> getListTrainData() {
        return listTrainData;
    }

    public List<DataPoint> getListTestData() {
        return listTestData;
    }

    public DistanceAlgorithm getDistanceAlgorithm() {
        return distanceAlgorithm;
    }

    public void setDistanceAlgorithm(DistanceAlgorithm distanceAlgorithm) {
        this.distanceAlgorithm = distanceAlgorithm;
    }

    public double getAccuracy() {
        return accuracy;
    }

    private List<DataInfo> calculateDistances(DataPoint point){ // 주어진 점과 모든 점간의 거리를 계산해서 리스트로 리턴함
        List<DataInfo> listDistance = new ArrayList<>();
        for (DataPoint dataPoint:listTrainData){
            double distance = distanceAlgorithm.calculateDistance(point.getX(), point.getY(),
                    dataPoint.getX(), dataPoint.getY());
            String filename = dataPoint.getFilename();
            DataInfo dataInfo = new DataInfo(filename, distance); // 이거 메모리 설정 안해줘도 되나?
            listDistance.add(dataInfo);
        }
        return listDistance;
    }

    private List<String> classify(DataPoint point){ // 외부에서 들어옴.
        HashMap<String, Integer> hashMap = new HashMap<>(); // 초기 용량 지정 가능!!! 나중에 전체 개수 정해지면 초기용량 ㄱㄱ
        List<DataInfo> listDistance = calculateDistances(point);
        for (int i = 0; i < K; i++){
            double min = Double.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < listDistance.size(); j++){
                if (listDistance.get(j).getDistance() < min){
                    min = listDistance.get(j).getDistance();
                    minIndex = j;
                }
            }

            String nn = listTrainData.get(minIndex).getFilename();
            listNeighbors.add(nn);
            DataInfo tempDataInfo = new DataInfo(null, Double.MAX_VALUE);
            listDistance.set(minIndex, tempDataInfo); // 제일 가까운 애를 맥스로 갱신 - 다음 번에는 얘는 자연스럽게 제외됨.
        }
        return listNeighbors;
    }

    public void reset() {
        listDataPoint.clear();
        listTestData.clear();
        listTrainData.clear();
        listNeighbors.clear();
    }
}