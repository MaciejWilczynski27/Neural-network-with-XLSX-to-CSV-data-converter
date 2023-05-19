package org.example;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.api.TrainingConfig;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
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
import java.util.Collections;

public class Main {
    public static void main(String[] args) throws Exception {
         DataForNeuralNetwork.prepareTrainingData("dataXLSX/F8/","dataCSV/F8/","f8_stat_",1,225);
         DataForNeuralNetwork.prepareTrainingData("dataXLSX/F10/","dataCSV/F10/","f10_stat_",1,225);
        //DataForNeuralNetwork.prepareDynamicData("dataXLSX/F8/","dataCSV/F8/","f8_1p");
       // DataSet dataSet = NeuralNetworkManager.loadData("dataCSV/F8/f8_stat_1_trainingX.csv");
       // DataNormalization dataNormalization = new NormalizerStandardize();
       // dataNormalization.fit(dataSet);
     //   dataNormalization.transform(dataSet);

        MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(NeuralNetworkManager.getMultiLayerNetworkConfig());
        multiLayerNetwork.init();
        multiLayerNetwork.setListeners(new ScoreIterationListener(10));
     //   for (int i =0; i < 100; i++) {
            NeuralNetworkManager.trainNetwork(multiLayerNetwork, "dataCSV/F8/", "f8_stat", "trainingX.csv", 1, 225);
            NeuralNetworkManager.trainNetwork(multiLayerNetwork, "dataCSV/F8/", "f8_stat", "trainingY.csv", 1, 225);
       // }
        //  for (int i =0; i < 10;i++) {
       //     multiLayerNetwork.fit(dataSet);
       // }
       // System.out.println(NeuralNetworkManager.evaluate(multiLayerNetwork,dataSet).stats());


   /*     MultiLayerNetwork multiLayerNetwork = new MultiLayerNetwork(NeuralNetworkManager.getMultiLayerNetworkConfig());
        multiLayerNetwork.init();
        multiLayerNetwork.setListeners(new ScoreIterationListener(10));
        INDArray indArray = new NDArray();


     //   indArray.





        RecordReader recordReader = new CSVRecordReader(0,',');
        recordReader.initialize(new FileSplit(new File("dataCSV/F8/f8_stat_1.csv")));
       // DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader, 64, new int[]{4,5},1);
        RecordReaderDataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader, 64, 4,1);
        //System.out.println(multiLayerConfiguration.toJson());
    multiLayerNetwork.evaluate(dataSetIterator);


        for (int i = 0; i < 10; i++) {
            multiLayerNetwork.fit(dataSetIterator);

            // Evaluate model on training data
           // dataSetIterator.reset();
           // Evaluation eval = new Evaluation(2);
          //  while (dataSetIterator.hasNext()) {
           //     DataSet ds = dataSetIterator.next();
               // eval.eval(ds.getLabels(), multiLayerNetwork.output(ds.getFeatures()));
           // }
          //  System.out.println("Epoch " + i + " - Loss: " + eval.stats() + " Accuracy: " + eval.accuracy());

        }
*/
    }
}