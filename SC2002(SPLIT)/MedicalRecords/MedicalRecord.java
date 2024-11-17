package MedicalRecords;

import DataManagement.medicalRecordManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicalRecord {
    private final String patientID;
    private final medicalRecordManager medicalRecordManager;

    private String recordID;
    private String userID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String contactInfo;
    private String email;
    private String bloodType;
    private String doctorID;
    private List<String> diagnosesAndTreatments;

    // Constructor
    public MedicalRecord(String patientID, medicalRecordManager medicalRecordManager) {
        this.patientID = patientID;
        this.medicalRecordManager = medicalRecordManager;
        loadMedicalRecord(); // Load details from the database upon initialization
    }

    // Load medical record details from the database
    private void loadMedicalRecord() {
        try {
            String[] record = medicalRecordManager.getMedicalRecordByPatientID(patientID);
            if (record != null && record.length == 11) { // Ensure correct record structure
                this.recordID = record[0]; // Record ID
                this.userID = record[2]; // User ID
                this.name = record[3]; // Name
                this.dateOfBirth = record[4]; // Date of Birth
                this.gender = record[5]; // Gender
                this.contactInfo = record[6]; // Contact Information
                this.email = record[7]; // Email
                this.bloodType = record[8]; // Blood Type
                this.diagnosesAndTreatments = parseList(record[9]); // Diagnoses and Treatments
                this.doctorID = record[10]; // Doctor ID
            } else {
                System.out.println("No medical record found or record structure mismatch for patient ID: " + patientID);
            }
        } catch (IOException ex) {
            Logger.getLogger(MedicalRecord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Helper method to parse semi-colon-separated lists
    private List<String> parseList(String data) {
        if (data == null || data.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] items = data.split("; ");
        List<String> result = new ArrayList<>();
        for (String item : items) {
            result.add(item.trim());
        }
        return result;
    }

    // Print a summary of the medical record
    public void printSummary() {
        System.out.println("===== Medical Record Summary =====");
        System.out.println("Record ID: " + recordID);
        System.out.println("Patient ID: " + patientID);
        System.out.println("User ID: " + userID);
        System.out.println("Name: " + name);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Gender: " + gender);
        System.out.println("Contact: " + contactInfo);
        System.out.println("Email: " + email);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Diagnoses and Treatments: " + String.join("; ", diagnosesAndTreatments));
        System.out.println("Doctor ID: " + doctorID);
    }
}
