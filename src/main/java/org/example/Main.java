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
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        // DataForNeuralNetwork.prepareTrainingData("dataXLSX/F8/","dataCSV/F8/","f8_stat_",1,225);
        // DataForNeuralNetwork.prepareTrainingData("dataXLSX/F10/","dataCSV/F10/","f10_stat_",1,225);
        // DataForNeuralNetwork.prepareDynamicData("dataXLSX/F8/","dataCSV/F8/","f8_1p");
        // DataForNeuralNetwork.prepareDynamicData("dataXLSX/F10/","dataCSV/F10/","f10_1p");


        MultiLayerNetwork multiLayerNetworkX = MultiLayerNetwork.load(new File("multiLayerNetworkX"),false);
        //new MultiLayerNetwork(NeuralNetworkManager.getMultiLayerNetworkConfig());
        /*
        multiLayerNetworkX.init();
        for (int i = 0; i < 10; i++) {
            NeuralNetworkManager.trainNetwork(multiLayerNetworkX, "dataCSV/F8/", "f8_stat", "trainingX.csv", 1, 225);
            NeuralNetworkManager.trainNetwork(multiLayerNetworkX, "dataCSV/F10/", "f10_stat", "trainingX.csv", 1, 225);
        }
        multiLayerNetworkX.save(new File("multiLayerNetworkX"));
        */
        MultiLayerNetwork multiLayerNetworkY = MultiLayerNetwork.load(new File("multiLayerNetworkY"),false);

        //new MultiLayerNetwork(NeuralNetworkManager.getMultiLayerNetworkConfig());
        /*  multiLayerNetworkY.init();
        for (int i = 0; i < 10; i++) {
            NeuralNetworkManager.trainNetwork(multiLayerNetworkY, "dataCSV/F8/", "f8_stat", "trainingY.csv", 1, 225);
            NeuralNetworkManager.trainNetwork(multiLayerNetworkY, "dataCSV/F10/", "f10_stat", "trainingY.csv", 1, 225);
        }
        multiLayerNetworkY.save(new File("multiLayerNetworkY"));
        */
        NeuralNetworkManager.correctData(multiLayerNetworkX,multiLayerNetworkY,"dataCSV/F10/f10_1p_dynamic");


    }
}