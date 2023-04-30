package org.example;


public class Main {
    public static void main(String[] args) throws Exception {
        DataForNeuralNetwork.prepareData("dataXLSX/F8/","dataCSV/F8/","f8_stat_",1,225,true);
        DataForNeuralNetwork.prepareData("dataXLSX/F10/","dataCSV/F10/","f10_stat_",1,225,true);

    }
}