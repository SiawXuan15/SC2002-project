package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class appointmentOutcomeManager {

    private final String appointmentOutcomeFilePath;
    private final String appointmentsFilePath;
    // private final CSVReader csvReader; // Assuming non-static methods are still used with an instance
    // private final CSVWriter csvWriter; // Assuming non-static methods are still used with an instance

    // Constructor
    public appointmentOutcomeManager(String appointmentOutcomeFilePath, String appointmentsFilePath) {
        this.appointmentOutcomeFilePath = appointmentOutcomeFilePath;
        this.appointmentsFilePath = appointmentsFilePath;
    }

    // Get all appointment outcome records
    public List<String[]> getAllAppointmentOutcomeRecords() throws IOException {
        return CSVReader.readCSV(appointmentOutcomeFilePath); // Static method accessed via class name
    }

    // Get outcome records by appointment ID
    public List<String[]> getAppointmentOutcomeRecordsByAppointmentId(String appointmentId) throws IOException {
        List<String[]> filteredRecords = new ArrayList<>();
        List<String[]> allRecords = getAllAppointmentOutcomeRecords();

        for (String[] record : allRecords) {
            if (record[0].equalsIgnoreCase(appointmentId)) { // Assuming Appointment ID is the first column
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    // Get outcome records by patient ID
    public List<String[]> getAppointmentOutcomeRecordsByPatientId(String patientId) throws IOException {
        List<String[]> filteredRecords = new ArrayList<>();
        List<String[]> allRecords = getAllAppointmentOutcomeRecords();

        for (String[] record : allRecords) {
            if (record[1].equalsIgnoreCase(patientId)) { // Assuming Patient ID is the second column
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    // Map appointment outcomes by appointment ID
    public Map<String, String[]> mapAppointmentOutcomes() throws IOException {
        List<String[]> outcomeRecords = getAllAppointmentOutcomeRecords();
        Map<String, String[]> outcomeMap = new HashMap<>();

        for (String[] outcome : outcomeRecords) {
            String appointmentID = outcome[0]; // Assuming appointmentID is at index 0
            outcomeMap.put(appointmentID, outcome);
        }

        return outcomeMap;
    }

    // Add a new outcome record
    public void addToOutcome(String appointmentID) throws IOException {
        List<String[]> appointmentsData = CSVReader.readCSV(appointmentsFilePath); // Static method accessed via class name
        String[] appointmentData = null;

        for (String[] row : appointmentsData) {
            if (row[0].equals(appointmentID)) {
                appointmentData = row;
                break;
            }
        }

        if (appointmentData == null) {
            throw new IOException("Appointment ID not found.");
        }

        String patientID = appointmentData[1];
        String doctorID = appointmentData[2];
        String appointmentDate = appointmentData[3];

        String[] outcomeData = {
            appointmentID,             // Appointment ID
            patientID,                 // Patient ID
            doctorID,                  // Doctor ID
            appointmentDate,           // Date of appointment
            "-",                       // Type of service
            "-",                       // Prescription name
            "-",                       // Prescription status
            "-"                        // Consultation notes
        };

        CSVWriter.appendToCSV(appointmentOutcomeFilePath, outcomeData); // Static method accessed via class name
    }

    // Remove an outcome record by appointment ID
    public void removeFromOutcome(String appointmentID) throws IOException {
        List<String[]> allOutcomes = CSVReader.readCSV(appointmentOutcomeFilePath); // Static method accessed via class name
        List<String[]> updatedOutcomes = new ArrayList<>();

        // Add rows to the new ArrayList if the appointment ID does not match
        for (String[] outcome : allOutcomes) {
            if (!outcome[0].equals(appointmentID)) {
                updatedOutcomes.add(outcome);
            }
        }

        CSVWriter.writeCSV(appointmentOutcomeFilePath, updatedOutcomes); // Static method accessed via class name
    }

    // Update an appointment outcome record
    public void updateAppointmentOutcome(String appointmentID, String typeOfService, String prescriptionName, String consultationNotes) throws IOException {
        List<String[]> outcomeRecords = CSVReader.readCSV(appointmentOutcomeFilePath); // Static method accessed via class name
        boolean recordFound = false;

        for (String[] outcome : outcomeRecords) {
            if (outcome[0].equals(appointmentID)) {
                outcome[4] = typeOfService;            // Type of service
                outcome[5] = prescriptionName;         // Prescription name
                outcome[6] = "Pending";                // Prescription status
                outcome[7] = consultationNotes;        // Consultation notes
                recordFound = true;
                break;
            }
        }

        if (!recordFound) {
            throw new IOException("Appointment ID not found in the outcome records.");
        }

        CSVWriter.writeCSV(appointmentOutcomeFilePath, outcomeRecords); // Static method accessed via class name
    }
}
