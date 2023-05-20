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
            System.out.println(i);
            StringBuilder stringBuilderX = new StringBuilder(sourceDirectory);
            StringBuilder stringBuilderY = new StringBuilder(sourceDirectory);
            Workbook workbookX = new Workbook(stringBuilderX.append(fileName).append(i).append(".xlsx").toString());
            Workbook workbookY = new Workbook(stringBuilderY.append(fileName).append(i).append(".xlsx").toString());
            prepareColumns(workbookX);
            prepareColumns(workbookY);
            prepareXAxis(workbookX);
            prepareYAxis(workbookY);

            stringBuilderX = new StringBuilder(targetDirectory).append(fileName).append(i).append(dataType).append("X").append(".csv");
            stringBuilderY = new StringBuilder(targetDirectory).append(fileName).append(i).append(dataType).append("Y").append(".csv");
            workbookX.save(stringBuilderX.toString());
            workbookY.save(stringBuilderY.toString());
            formatWorkbook(stringBuilderX.toString(),false);
            formatWorkbook(stringBuilderY.toString(),false);
        }
    }
    public static void prepareDynamicData(String sourceDirectory, String targetDirectory,String fileName) throws Exception {
        StringBuilder stringBuilderX = new StringBuilder(sourceDirectory);
        StringBuilder stringBuilderY = new StringBuilder(sourceDirectory);
        Workbook workbookX = new Workbook(stringBuilderX.append(fileName).append(".xlsx").toString());
        Workbook workbookY = new Workbook(stringBuilderY.append(fileName).append(".xlsx").toString());
        prepareColumns(workbookX);
        prepareColumns(workbookY);
        prepareXAxis(workbookX);
        prepareYAxis(workbookY);
        String dataType = "_dynamic";
        stringBuilderX = new StringBuilder(targetDirectory).append(fileName).append(dataType).append("X").append(".csv");
        stringBuilderY = new StringBuilder(targetDirectory).append(fileName).append(dataType).append("Y").append(".csv");
        workbookX.save(stringBuilderX.toString());
        workbookY.save(stringBuilderY.toString());
        formatWorkbook(stringBuilderX.toString(),true);
        formatWorkbook(stringBuilderY.toString(),true);
    }
    private static void formatWorkbook(String path,boolean isDynamic) throws IOException {
        List<String[]> rows = readCsvFile(path);
        rows.remove(rows.size() - 1);
        if (isDynamic) {
            for (String[] row : rows) {
                row[2] = row[2].replace(',', '.');

            }
        }
        for (String[] row : rows) {
            if(Double.valueOf(row[1]) < 1 ) {
                row[1] = row[2];
            } else if(Double.valueOf(row[2]) < 1) {
                row[2] = row[1];
            }
            row[2] = String.valueOf(Math.round(Double.valueOf(row[2]) /  Double.valueOf(row[1]) *100));
            if(Integer.valueOf(row[2]) < 0 || Integer.valueOf(row[2]) > 255) row[2] = String.valueOf(100);
            //System.out.println(row[2]);
        }
        saveCsvFile(rows, path);
    }
    private static void prepareXAxis(Workbook workbook) {
        Worksheet worksheet = workbook.getWorksheets().get(0);
        worksheet.getCells().deleteColumns(1,1,true);
        worksheet.getCells().deleteColumns(2,1,true);
        worksheet.getCells().deleteColumns(3,1,true);

    }
    private static void prepareYAxis(Workbook workbook) {
        Worksheet worksheet = workbook.getWorksheets().get(0);
        worksheet.getCells().deleteColumns(0,1,true);
        worksheet.getCells().deleteColumns(1,1,true);
        worksheet.getCells().deleteColumns(2,1,true);
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
