package org.example;


import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        //  DataForNeuralNetwork.prepareData("dataXLSX/F8/","dataCSV/F8/","f8_stat_",1,225);
        // DataForNeuralNetwork.prepareData("dataXLSX/F10/","dataCSV/F10/","f10_stat_",1,225,true);
        //DataForNeuralNetwork.prepareDynamicData("dataXLSX/F8/","dataCSV/F8/","f8_1p");
        int seed = 123;
        double learningRate = 0.01;
        int batchSize = 50;
        int nEpochs = 30;
        int nInputs = 4;
        int nOutputs = 2;

        RecordReader recordReader = new CSVRecordReader();
        recordReader.initialize(new FileSplit(new File("dataCSV/F8/f8_stat_1.csv")));
        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader, batchSize, 4, 2);


        MultiLayerConfiguration multiLayerConfiguration = new NeuralNetConfiguration.Builder()
                .seed(22)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(nInputs)
                        .nOut(8)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(8)
                        .nOut(8)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(8)
                        .nOut(nOutputs)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();

        MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(multiLayerConfiguration);
        multiLayerNetwork.init();
        multiLayerNetwork.setListeners(new ScoreIterationListener(10));
        //System.out.println(multiLayerConfiguration.toJson());
        for (int i = 0; i < 10; i++) {
            multiLayerNetwork.fit(dataSetIterator);

            // Evaluate model on training data
            dataSetIterator.reset();
            Evaluation eval = new Evaluation(2);
            while (dataSetIterator.hasNext()) {
                DataSet ds = dataSetIterator.next();
                eval.eval(ds.getLabels(), multiLayerNetwork.output(ds.getFeatures()));
            }
            System.out.println("Epoch " + i + " - Loss: " + eval.stats() + " Accuracy: " + eval.accuracy());

        }
    }
}