package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class appointmentOutcomeManager {

    private final String appointmentOutcomeFilePath;
    private final String appointmentsFilePath;

    public appointmentOutcomeManager(String appointmentOutcomeFilePath, String appointmentsFilePath) {
        this.appointmentOutcomeFilePath = appointmentOutcomeFilePath;
        this.appointmentsFilePath = appointmentsFilePath;
    }

    public List<String[]> getAllAppointmentOutcomeRecords() throws IOException {
        return CSVReader.readCSV(appointmentOutcomeFilePath); 
    }

    public List<String[]> getAppointmentOutcomeRecordsByAppointmentId(String appointmentId) throws IOException {
        List<String[]> filteredRecords = new ArrayList<>();
        List<String[]> allRecords = getAllAppointmentOutcomeRecords();

        for (String[] record : allRecords) {
            if (record[0].equalsIgnoreCase(appointmentId)) { 
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public List<String[]> getAppointmentOutcomeRecordsByPatientId(String patientId) throws IOException {
        List<String[]> filteredRecords = new ArrayList<>();
        List<String[]> allRecords = getAllAppointmentOutcomeRecords();

        for (String[] record : allRecords) {
            if (record[1].equalsIgnoreCase(patientId)) { 
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public Map<String, String[]> mapAppointmentOutcomes() throws IOException {
        List<String[]> outcomeRecords = getAllAppointmentOutcomeRecords();
        Map<String, String[]> outcomeMap = new HashMap<>();

        for (String[] outcome : outcomeRecords) {
            String appointmentID = outcome[0]; 
            outcomeMap.put(appointmentID, outcome);
        }

        return outcomeMap;
    }

    public void addToOutcome(String appointmentID) throws IOException {
        List<String[]> appointmentsData = CSVReader.readCSV(appointmentsFilePath); 
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
            appointmentID,             
            patientID,                 
            doctorID,                 
            appointmentDate,          
            "-",                       
            "-",                       
            "-",                      
            "-"                        
        };

        CSVWriter.appendToCSV(appointmentOutcomeFilePath, outcomeData); 
    }

    public void removeFromOutcome(String appointmentID) throws IOException {
        List<String[]> allOutcomes = CSVReader.readCSV(appointmentOutcomeFilePath); 
        List<String[]> updatedOutcomes = new ArrayList<>();

        for (String[] outcome : allOutcomes) {
            if (!outcome[0].equals(appointmentID)) {
                updatedOutcomes.add(outcome);
            }
        }

        CSVWriter.writeCSV(appointmentOutcomeFilePath, updatedOutcomes); 
    }

    public void updateAppointmentOutcome(String appointmentID, String typeOfService, String prescriptionName, String consultationNotes) throws IOException {
        List<String[]> outcomeRecords = CSVReader.readCSV(appointmentOutcomeFilePath); 
        boolean recordFound = false;

        for (String[] outcome : outcomeRecords) {
            if (outcome[0].equals(appointmentID)) {
                outcome[4] = typeOfService;            
                outcome[5] = prescriptionName;         
                outcome[6] = "Pending";                
                outcome[7] = consultationNotes;        
                recordFound = true;
                break;
            }
        }

        if (!recordFound) {
            throw new IOException("Appointment ID not found in the outcome records.");
        }

        CSVWriter.writeCSV(appointmentOutcomeFilePath, outcomeRecords); 
    }
}
