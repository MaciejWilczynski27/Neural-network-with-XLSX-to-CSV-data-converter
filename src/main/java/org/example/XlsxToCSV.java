package org.example;


import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class XlsxToCSV {
    public static void convert(String sourceDirectory,String targetDirectory,String fileName,int firstIndex,int lastIndex) throws Exception {
        //"dataXLSX/F8/f8_stat_7.xlsx"
        for(int i = firstIndex; i < lastIndex + 1; i++) {
            StringBuilder stringBuilder = new StringBuilder(sourceDirectory);
            Workbook workbook = new Workbook(stringBuilder.append(fileName).append(i).append(".xlsx").toString());
            Worksheet worksheet = workbook.getWorksheets().get(0);
            worksheet.getCells().deleteRows(0, 1, true);
            stringBuilder = new StringBuilder(targetDirectory).append(fileName).append(i).append(".csv");
            String pathToConverted = stringBuilder.toString();
            workbook.save(pathToConverted);
            List<String[]> rows = readCsvFile(pathToConverted);
            rows.remove(rows.size() - 1);
            saveCsvFile(rows, pathToConverted);
        }
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
