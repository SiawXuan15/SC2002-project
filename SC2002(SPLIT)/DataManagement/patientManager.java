package DataManagement;

import java.io.IOException;
import java.util.List;

public class patientManager {
    private final String patientFilePath;
    private final String medicalRecordsFilePath;
    private final String userFilePath;
    // private final CSVReader csvReader; // Assuming non-static methods will still use an instance
    // private final CSVWriter csvWriter; // Assuming non-static methods will still use an instance

    // Constructor
    public patientManager(String patientFilePath, String medicalRecordsFilePath, String userFilePath) {
        this.patientFilePath = patientFilePath;
        this.medicalRecordsFilePath = medicalRecordsFilePath;
        this.userFilePath = userFilePath;
        // this.csvReader = csvReader;
        // this.csvWriter = csvWriter;
    }

    public String[] getPatientByUserID(String userID) throws IOException {
        List<String[]> patients = CSVReader.readCSV(medicalRecordsFilePath); // Static method accessed via class name
        for (String[] patient : patients) {
            if (patient[1].equals(userID)) { // Assuming UserID is at index 1
                return patient;
            }
        }
        return null; // Patient not found
    }

    public List<String[]> getPatientList() throws IOException {
        return CSVReader.readCSV(patientFilePath); // Static method accessed via class name
    }

    public void addPatient(String[] newPatient) throws IOException {
        CSVWriter.appendToCSV(patientFilePath, newPatient); // Static method accessed via class name
    }

    public void updatePatientList(List<String[]> updatedPatients) throws IOException {
        CSVWriter.writeCSV(patientFilePath, updatedPatients); // Static method accessed via class name
    }

    public String getPatientDOB(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[4]; // Assuming DOB is at index 4
        }
        return null;
    }

    public String getPatientBloodType(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[8]; // Assuming Blood Type is at index 8
        }
        return null;
    }

    public int getPatientEmergencyContact(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return Integer.parseInt(patientDetails[6]); // Assuming Emergency Contact is at index 6
        }
        return 0;
    }

    public String getPatientName(String userID) throws IOException {
        List<String[]> patients = CSVReader.readCSV(patientFilePath);
        for (String[] patient : patients) {
            if (patient[1].equals(userID)) { // Assuming UserID is at index 1
                return patient[3]; // Assuming Name is at index 3
            }
        }
        return null; // Return null if no match is found
    }
    
    // for doctor to retrieve patient name to book follow up appointment
    public String getPatientID(String name) throws IOException {
        List<String[]> patients = CSVReader.readCSV(patientFilePath);
        for (String[] patient : patients) {
            if (patient[3].equals(name)) { // Assuming Name is at index 3
                return patient[1]; // Assuming UserID is at index 1
            }
        }
        return null; // Return null if no match is found
    }

    public String getPatientGender(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[5]; // Assuming Gender is at index 5
        }
        return null;
    }

    public String getPastDiagnosesAndTreatments(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[9]; // Assuming Past Diagnoses and Treatments is at index 9
        }
        return null;
    }

    public String getPatientEmailAddress(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[7]; // Assuming Email Address is at index 7
        }
        return null;
    }

    public String getPatientPayment(String userID) throws IOException {
        List<String[]> patients = getPatientList();
        for (String[] patient : patients) {
            if (patient[1].equalsIgnoreCase(userID)) { // Assuming UserID is at index 1
                return patient[9]; // Assuming Payment method is at index 9
            }
        }
        return "Payment method not found for user ID: " + userID;
    }

    public String getPatientInsuranceDetails(String userID) {
        // If insurance details are part of Patient_List.csv or another file, retrieve them here.
        // For now, return a default value
        return "Default Insurance";
    }

    public boolean updatePatientContactInfo(String userID, String newEmail, String newContact) throws IOException {
        boolean updated = false;

        // Update in Patient_List.csv
        List<String[]> patientList = CSVReader.readCSV(patientFilePath); // Static method accessed via class name
        for (String[] patient : patientList) {
            if (patient[1].equals(userID)) { // Assuming UserID is at index 1
                patient[7] = newContact; // Assuming Contact Number is at index 7
                patient[8] = newEmail;   // Assuming Email Address is at index 8
                updated = true;
                break;
            }
        }
        if (updated) {
            CSVWriter.writeCSV(patientFilePath, patientList); // Static method accessed via class name
        }

        // Update in User_List.csv
        List<String[]> userList = CSVReader.readCSV(userFilePath); // Static method accessed via class name
        for (String[] user : userList) {
            if (user[0].equals(userID)) { // Assuming UserID is at index 0
                user[5] = newContact; // Assuming Contact Number is at index 5
                user[4] = newEmail;   // Assuming Email Address is at index 4
                break;
            }
        }
        CSVWriter.writeCSV(userFilePath, userList); // Static method accessed via class name

        // Update in MedicalRecords.csv
        List<String[]> medicalRecords = CSVReader.readCSV(medicalRecordsFilePath); // Static method accessed via class name
        for (String[] record : medicalRecords) {
            if (record[2].equals(userID)) { // Assuming UserID is at index 2
                record[6] = newContact; // Assuming Contact Number is at index 6
                record[7] = newEmail;   // Assuming Email Address is at index 7
                break;
            }
        }
        CSVWriter.writeCSV(medicalRecordsFilePath, medicalRecords); // Static method accessed via class name

        return updated;
    }
}
