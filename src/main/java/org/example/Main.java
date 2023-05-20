package org.example;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.api.TrainingConfig;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.modelimport.keras.config.KerasModelConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.cpu.nativecpu.NDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.lossfunctions.LossFunctions;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
         //DataForNeuralNetwork.prepareTrainingData("dataXLSX/F8/","dataCSV/F8/","f8_stat_",1,225);
       //  DataForNeuralNetwork.prepareTrainingData("dataXLSX/F10/","dataCSV/F10/","f10_stat_",1,225);
        //DataForNeuralNetwork.prepareDynamicData("dataXLSX/F8/","dataCSV/F8/","f8_1p");
        DataSet dataSet = NeuralNetworkManager.loadData("dataCSV/F8/f8_3p_dynamicX.csv");
        List<String> list1 = new ArrayList<>();
        for(int i = 0; i < 256; i++) {
            list1.add(String.valueOf(i));
        }
        dataSet.setLabelNames(list1);
        //f8_1p_dynamicX.csv
        DataNormalization dataNormalization = new NormalizerStandardize();
        dataNormalization.fit(dataSet);
        dataNormalization.transform(dataSet);



        MultiLayerNetwork multiLayerNetwork = MultiLayerNetwork.load(new File("multiLayerNetwork"),false);
        INDArray output = multiLayerNetwork.output(dataSet.getFeatures());
         List<String> list = multiLayerNetwork.predict(dataSet);

         list.forEach(s -> System.out.println(s));
        System.out.println("List size " + list.size());
       // System.out.println(output.columns());
       // System.out.println(output.rows());

        System.out.println(output.toString());

        /*               new MultiLayerNetwork(NeuralNetworkManager.getMultiLayerNetworkConfig());
        multiLayerNetwork.init();
        multiLayerNetwork.setListeners(new ScoreIterationListener(10));
        for (int i =0; i < 10; i++) {
            NeuralNetworkManager.trainNetwork(multiLayerNetwork, "dataCSV/F8/", "f8_stat", "trainingX.csv", 1, 225);
            NeuralNetworkManager.trainNetwork(multiLayerNetwork, "dataCSV/F8/", "f8_stat", "trainingY.csv", 1, 225);
            NeuralNetworkManager.trainNetwork(multiLayerNetwork, "dataCSV/F10/", "f10_stat", "trainingX.csv", 1, 225);
            NeuralNetworkManager.trainNetwork(multiLayerNetwork, "dataCSV/F10/", "f10_stat", "trainingY.csv", 1, 225);
        }
        */

    //    INDArray output = multiLayerNetwork.output(dataSet.getFeatures());
        //multiLayerNetwork.save(new File("multiLayerNetwork"));
       // INDArray output = multiLayerNetwork.output(dataSet.getFeatures());
       // System.out.println(output.toString());
        System.out.println(NeuralNetworkManager.evaluate(multiLayerNetwork,dataSet).stats());

    }
}