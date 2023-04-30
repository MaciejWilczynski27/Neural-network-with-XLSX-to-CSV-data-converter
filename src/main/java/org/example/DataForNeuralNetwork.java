package org.example;


import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataForNeuralNetwork {
    public static void prepareTrainingData(String sourceDirectory, String targetDirectory, String fileName, int firstIndex, int lastIndex) throws Exception {
        String dataType = "_training";;
        for(int i = firstIndex; i < lastIndex + 1; i++) {
            StringBuilder stringBuilder = new StringBuilder(sourceDirectory);
            Workbook workbook = new Workbook(stringBuilder.append(fileName).append(i).append(".xlsx").toString());
            prepareColumns(workbook);
            stringBuilder = new StringBuilder(targetDirectory).append(fileName).append(i).append(dataType).append(".csv");
            String pathToConverted = stringBuilder.toString();
            workbook.save(pathToConverted);
            List<String[]> rows = readCsvFile(pathToConverted);
            rows.remove(rows.size() - 1);
            saveCsvFile(rows, pathToConverted);
        }
    }
    public static void prepareDynamicData(String sourceDirectory, String targetDirectory,String fileName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder(sourceDirectory);
        Workbook workbook = new Workbook(stringBuilder.append(fileName).append(".xlsx").toString());
        prepareColumns(workbook);
        String dataType = "_dynamic";
        stringBuilder = new StringBuilder(targetDirectory).append(fileName).append(dataType).append(".csv");
        String pathToConverted = stringBuilder.toString();
        workbook.save(pathToConverted);
        List<String[]> rows = readCsvFile(pathToConverted);
        rows.remove(rows.size() - 1);
        saveCsvFile(rows, pathToConverted);
    }

    private static void prepareColumns(Workbook workbook) {
        Worksheet worksheet = workbook.getWorksheets().get(0);
        worksheet.getCells().deleteRows(0, 1, true);
        worksheet.getCells().deleteColumns(0, 23, true);
        worksheet.getCells().deleteColumns(2, 7, true);
        worksheet.getCells().deleteColumns(4, 1, true);
    }

    private static List<String[]> readCsvFile(String filePath) throws IOException {
        CSVReader csvReader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = csvReader.readAll();
        csvReader.close();
        return rows;
    }

    private static void saveCsvFile(List<String[]> rows, String filePath) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath));
        csvWriter.writeAll(rows);
        csvWriter.close();
    }
}
