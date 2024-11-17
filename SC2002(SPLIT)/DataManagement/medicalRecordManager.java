package DataManagement;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class medicalRecordManager {

    private final String medicalRecordsFilePath;

    public medicalRecordManager(String medicalRecordsFilePath) {
        this.medicalRecordsFilePath = medicalRecordsFilePath;
    }

    private List<String[]> readCSV(String filePath) throws IOException {
        return CSVReader.readCSV(filePath);
    }

    private void writeCSV(String filePath, List<String[]> data) throws IOException {
        CSVWriter.writeCSV(filePath, data);
    }

    private void appendToCSV(String filePath, String[] data) throws IOException {
        CSVWriter.appendToCSV(filePath, data);
    }

    public String[] getMedicalRecordByUserID(String userID) throws IOException {
        List<String[]> records = readCSV(medicalRecordsFilePath);
        for (String[] record : records) {
            if (record[1].equalsIgnoreCase(userID)) { // Assuming UserID is at index 1
                return record;
            }
        }
        return null; 
    }

    public String[] getMedicalRecordByPatientID(String patientID) throws IOException {
        List<String[]> medicalRecords = readCSV(medicalRecordsFilePath);
        for (String[] record : medicalRecords) {
            if (record.length > 1 && record[1].trim().equalsIgnoreCase(patientID.trim())) { // Match by Patient ID (Index 1)
                return record; // Return the matching record
            }
        }
        return null; 
    }

    public void updateMedicalRecordList(List<String[]> updatedRecords) throws IOException {
        writeCSV(medicalRecordsFilePath, updatedRecords);
        System.out.println("Medical record list updated successfully.");
    }

    public List<String[]> getMedicalRecords() throws IOException {
        return readCSV(medicalRecordsFilePath);
    }

    public void addMedicalRecord(String[] newRecord) throws IOException {
        appendToCSV(medicalRecordsFilePath, newRecord);
        System.out.println("New medical record added successfully.");
    }

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

    public void updateMedicalRecord(String patientId, String newDiagnosis, String newTreatment) throws IOException {
        List<String[]> medicalRecords = CSVReader.readCSVwithQuotes(medicalRecordsFilePath);


        for (String[] record : medicalRecords) {
            if (record[1].equals(patientId)) { // Match by Patient ID
                String updatedDiagnoses = record[9].trim();
                if (!updatedDiagnoses.endsWith(";") && !updatedDiagnoses.isEmpty()) {
                    updatedDiagnoses += ";"; // Ensure separation with a semicolon
                }
                updatedDiagnoses += " " + newDiagnosis + ": " + newTreatment + " (" + LocalDate.now() + ")";
                record[9] = updatedDiagnoses.trim();
                break;
            }
        }


        CSVWriter.writeCSVWithQuotes(medicalRecordsFilePath, medicalRecords);
    }


}
