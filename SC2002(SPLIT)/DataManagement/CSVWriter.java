package DataManagement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    public static void writeCSV(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }

    public static void appendToCSV(String filePath, String[] newRow) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(",", newRow));
            bw.newLine();
        }
    }
    public static void writeCSVWithQuotes(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] record : data) {
                for (int i = 0; i < record.length; i++) {
                    // Properly quote fields with special characters
                    if (record[i].contains(",") || record[i].contains(";") || record[i].contains("\"")) {
                        record[i] = "\"" + record[i].replace("\"", "\"\"") + "\"";
                    }
                }
                bw.write(String.join(",", record));
                bw.newLine();
            }
        }
}
}
