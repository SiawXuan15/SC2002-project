package DataManagement;

import java.io.IOException;
import java.util.List;

public class medicalRecordManager {

    private final String medicalRecordsFilePath;

    // Constructor
    public medicalRecordManager(String medicalRecordsFilePath) {
        this.medicalRecordsFilePath = medicalRecordsFilePath;
    }

    // Generic CSV Read
    private List<String[]> readCSV(String filePath) throws IOException {
        return CSVReader.readCSV(filePath);
    }

    // Generic CSV Write
    private void writeCSV(String filePath, List<String[]> data) throws IOException {
        CSVWriter.writeCSV(filePath, data);
    }

    // Generic CSV Append
    private void appendToCSV(String filePath, String[] data) throws IOException {
        CSVWriter.appendToCSV(filePath, data);
    }

    // Get medical record by User ID
    public String[] getMedicalRecordByUserID(String userID) throws IOException {
        List<String[]> records = readCSV(medicalRecordsFilePath);
        for (String[] record : records) {
            if (record[1].equalsIgnoreCase(userID)) { // Assuming UserID is at index 1
                return record;
            }
        }
        return null; // Medical record not found
    }

    // Get medical record by Patient ID
    public String[] getMedicalRecordByPatientID(String patientID) throws IOException {
        List<String[]> medicalRecords = readCSV(medicalRecordsFilePath);
        for (String[] record : medicalRecords) {
            if (record.length > 1 && record[1].trim().equalsIgnoreCase(patientID.trim())) { // Match by Patient ID (Index 1)
                return record; // Return the matching record
            }
        }
        return null; // Return null if no matching record is found
    }

    // Update medical record list in CSV
    public void updateMedicalRecordList(List<String[]> updatedRecords) throws IOException {
        writeCSV(medicalRecordsFilePath, updatedRecords);
        System.out.println("Medical record list updated successfully.");
    }

    // Get all medical records
    public List<String[]> getMedicalRecords() throws IOException {
        return readCSV(medicalRecordsFilePath);
    }

    // Add new medical record to CSV
    public void addMedicalRecord(String[] newRecord) throws IOException {
        appendToCSV(medicalRecordsFilePath, newRecord);
        System.out.println("New medical record added successfully.");
    }

    // Update a specific medical record by patient ID
    public void updateMedicalRecord(String patientID, String[] updatedRecord) throws IOException {
        List<String[]> records = readCSV(medicalRecordsFilePath);
        boolean updated = false;

        for (int i = 0; i < records.size(); i++) {
            if (records.get(i)[0].equalsIgnoreCase(patientID)) { // Assuming column 0 is RecordID
                records.set(i, updatedRecord);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeCSV(medicalRecordsFilePath, records);
            System.out.println("Medical record for Patient ID " + patientID + " updated successfully.");
        } else {
            System.out.println("Medical record for Patient ID " + patientID + " not found.");
        }
    }

    // Delete a medical record by Record ID
    public void deleteMedicalRecord(String recordID) throws IOException {
        List<String[]> records = readCSV(medicalRecordsFilePath);
        boolean removed = records.removeIf(record -> record[0].equalsIgnoreCase(recordID));

        if (removed) {
            writeCSV(medicalRecordsFilePath, records);
            System.out.println("Medical record with Record ID " + recordID + " deleted successfully.");
        } else {
            System.out.println("Medical record with Record ID " + recordID + " not found.");
        }
    }

}
