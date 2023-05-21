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
import org.nd4j.linalg.learning.config.AdaBelief;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class NeuralNetworkManager {

    public static MultiLayerConfiguration getMultiLayerNetworkConfig() {
        return new NeuralNetConfiguration.Builder()
                .seed(300)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(new AdaBelief.Builder().learningRate(0.00001).build())
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(2)
                        .nOut(64)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(64)
                        .nOut(128)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new DenseLayer.Builder()
                        .nIn(128)
                        .nOut(256)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.RELU)
                        .build())
                .layer(3, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(256)
                        .nOut(256)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.SOFTMAX)
                        .build())
                .build();
    }
    public static DataSetIterator loadData(String path) throws IOException, InterruptedException {
        RecordReader recordReader = new CSVRecordReader(0,',');
        recordReader.initialize(new FileSplit(new File(path)));

        return new RecordReaderDataSetIterator(recordReader,64,2,256);
    }
    public static void trainNetwork(MultiLayerNetwork multiLayerNetwork,String sourceDirectory, String fileName,String fileNameEnding, int firstIndex, int lastIndex) throws IOException, InterruptedException {
        for (int i = firstIndex; i < lastIndex + 1; i++) {
            DataSetIterator dataSetIterator = loadData(sourceDirectory + fileName + "_" + i + "_" +fileNameEnding);
            while (dataSetIterator.hasNext()) {
                DataSet dataSet = dataSetIterator.next();
                DataNormalization dataNormalization = new NormalizerStandardize();
                dataNormalization.fit(dataSet);
                dataNormalization.transform(dataSet);
                multiLayerNetwork.fit(dataSet);
            }
        }
    }
    public static Evaluation evaluate(MultiLayerNetwork multiLayerNetwork, DataSet dataSet) {
        INDArray output = multiLayerNetwork.output(dataSet.getFeatures());
        Evaluation evaluation = new Evaluation(256);
        evaluation.eval(dataSet.getLabels(),output);
        return evaluation;
    }
    public static void correctData(MultiLayerNetwork multiLayerNetworkX,MultiLayerNetwork multiLayerNetworkY, String path) throws IOException, InterruptedException {
        List<String> list1 = new ArrayList<>();
        for(int i = 0; i < 256; i++) {
            list1.add(String.valueOf(i));
        }
        DataSetIterator dataSetIteratorX = NeuralNetworkManager.loadData(path + "X.csv");
        DataSetIterator dataSetIteratorY = NeuralNetworkManager.loadData(path + "Y.csv");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        ArrayList<List<String>> listX = new ArrayList<>();
        ArrayList<List<String>> listY = new ArrayList<>();
        while (dataSetIteratorX.hasNext()) {
            DataSet dataSet = dataSetIteratorX.next();
            dataSet.setLabelNames(list1);
            DataNormalization dataNormalization = new NormalizerStandardize();
            dataNormalization.fit(dataSet);
            dataNormalization.transform(dataSet);
            List<String> list = multiLayerNetworkX.predict(dataSet);
            listX.add(list);
            list.forEach(s -> atomicInteger.incrementAndGet());
        }
        while (dataSetIteratorY.hasNext()) {
            DataSet dataSet = dataSetIteratorY.next();
            dataSet.setLabelNames(list1);
            DataNormalization dataNormalization = new NormalizerStandardize();
            dataNormalization.fit(dataSet);
            dataNormalization.transform(dataSet);
            List<String> list = multiLayerNetworkY.predict(dataSet);
            listY.add(list);
        }
        List<String[]> measuredX = DataForNeuralNetwork.readCsvFile(path + "X.csv");
        List<String[]> measuredY = DataForNeuralNetwork.readCsvFile(path + "Y.csv");
        List<String[]> solution = new Vector<>(atomicInteger.get());
        for(int i = 0; i < listX.size();i++) {
            for (int j = 0; j < listX.get(i).size(); j++ ) {
                String [] row = new String[2];
                row[0] =  String.format("%.3f",Double.valueOf(listX.get(i).get(j))/100 * Double.valueOf(measuredX.get(i*64 +j)[1]));
                row[1] = String.format("%.3f",Double.valueOf(listY.get(i).get(j))/100 * Double.valueOf(measuredY.get(i*64 +j)[1]));
                solution.add(row);
            }
        }
        DataForNeuralNetwork.saveCsvFile(solution,"gitarra.csv");
    }
}
