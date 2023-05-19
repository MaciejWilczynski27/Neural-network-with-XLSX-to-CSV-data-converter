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
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;

public class NeuralNetworkManager {

    public static MultiLayerConfiguration getMultiLayerNetworkConfig() {
        return new NeuralNetConfiguration.Builder()
                .seed(22)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(2)
                        .nOut(225)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(225)
                        .nOut(450)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(450)
                        .nOut(7501)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();
    }
    public static DataSet loadData(String path) throws IOException, InterruptedException {
        DataSet dataSet = new DataSet();
        RecordReader recordReader = new CSVRecordReader(0,',');
        recordReader.initialize(new FileSplit(new File(path)));
        DataSetIterator dataSetIterator = new RecordReaderDataSetIterator(recordReader,64,2,7501);
        dataSet = dataSetIterator.next();
        return dataSet;
    }
    public static void trainNetwork(MultiLayerNetwork multiLayerNetwork,String sourceDirectory, String fileName,String fileNameEnding, int firstIndex, int lastIndex) throws IOException, InterruptedException {
        for (int i = firstIndex; i < lastIndex + 1; i++) {
            System.out.println(i);
            DataSet dataSet = NeuralNetworkManager.loadData(sourceDirectory + fileName + "_" + i + "_" +fileNameEnding);
            DataNormalization dataNormalization = new NormalizerStandardize();
            dataNormalization.fit(dataSet);
            dataNormalization.transform(dataSet);
            multiLayerNetwork.fit(dataSet);
        }
    }
    public static Evaluation evaluate(MultiLayerNetwork multiLayerNetwork, DataSet dataSet) {
        INDArray output = multiLayerNetwork.output(dataSet.getFeatures());
        Evaluation evaluation = new Evaluation(7501);
        evaluation.eval(dataSet.getLabels(),output);
        return evaluation;
    }
}
