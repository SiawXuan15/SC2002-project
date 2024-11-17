package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class doctorManager {
    
    private final String staffFilePath;  // Path to the CSV file where staff data is stored
    private final String patientFilePath;  // Path to the CSV file where patients are stored
    private final String medicalRecordsFilePath;  // Path to the CSV file for medical records
    private final String userFilePath;  // Path to the CSV file for user data

    // Constructor
    public doctorManager(String staffFilePath, String patientFilePath, String medicalRecordsFilePath, String userFilePath) {
        this.staffFilePath = staffFilePath;
        this.patientFilePath = patientFilePath;
        this.medicalRecordsFilePath = medicalRecordsFilePath;
        this.userFilePath = userFilePath;
    }

    // Get a doctor by their User ID
    public String[] getDoctorByUserID(String userID) throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);
        for (String[] staff : staffList) {
            if (staff[1].equalsIgnoreCase(userID) && "Doctor".equalsIgnoreCase(staff[3])) { // Assuming Role is at index 3
                return staff;
            }
        }
        return null; // Doctor not found
    }

    // Get all patients assigned to a specific doctor by Doctor ID
    public List<String[]> getPatientsByDoctorID(String doctorID) throws IOException {
        List<String[]> allPatients = CSVReader.readCSV(patientFilePath); // Read from the patient CSV
        List<String[]> filteredPatients = new ArrayList<>();

        for (String[] patient : allPatients) {
            if (patient[8].equalsIgnoreCase(doctorID)) { // Assuming DoctorID is at index 8
                filteredPatients.add(patient);
            }
        }
        return filteredPatients;
    }

    // Get doctor name using Doctor ID
    public String getDoctorName(String doctorID) throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);

        for (String[] staff : staffList) {
            if (staff[1].equalsIgnoreCase(doctorID)) { // Assuming UserID is at index 1
                return staff[2]; // Assuming Name is at index 2
            }
        }

        return "not found";
    }

    // Get medical records associated with a specific doctor
    public List<String[]> getMedicalRecordsByDoctorID(String doctorID) throws IOException {
        List<String[]> medicalRecords = CSVReader.readCSV(medicalRecordsFilePath); // Read from the medical records CSV
        List<String[]> doctorRecords = new ArrayList<>();

        for (String[] record : medicalRecords) {
            if (record.length > 10 && record[10].trim().equalsIgnoreCase(doctorID.trim())) { // Assuming DoctorID is at index 10
                doctorRecords.add(record);
            }
        }

        return doctorRecords;
    }

    // Get the Doctor ID from the user list using the User ID
    public String getDoctorIDFromUserList(String userID) throws IOException {
        List<String[]> userList = CSVReader.readCSV(userFilePath); // Read User_List.csv

        for (String[] user : userList) {
            if (user.length > 3 && user[0].equalsIgnoreCase(userID) && "Doctor".equalsIgnoreCase(user[3])) {
                return user[0].trim(); // Return User ID as Doctor ID if Role is Doctor
            }
        }

        return null; // Doctor ID not found
    }

    // Method to get all doctors from the staff list
    public List<String[]> getAllDoctors() throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);
        List<String[]> doctorsList = new ArrayList<>();

        for (String[] staff : staffList) {
            if ("Doctor".equalsIgnoreCase(staff[3])) { // Assuming Role is at index 3
                doctorsList.add(staff);
            }
        }

        return doctorsList;
    }
}
