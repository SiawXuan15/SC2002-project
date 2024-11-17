package DataManagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class prescriptionManager {
    private final String prescriptionFilePath;
    private final String appointmentsFilePath; 
    private final String appointmentOutcomeFilePath; 
    private final appointmentManager appointmentManager;

    public prescriptionManager(
            String prescriptionFilePath,
            String appointmentsFilePath,
            String appointmentOutcomeFilePath,
            appointmentManager appointmentManager
          ) {
        
        this.prescriptionFilePath = prescriptionFilePath;
        this.appointmentsFilePath = appointmentsFilePath;
        this.appointmentOutcomeFilePath = appointmentOutcomeFilePath;
        this.appointmentManager = appointmentManager;
    }




    public void updatePrescriptionStatus(String appointmentOutcomeFilePath, String appointmentID, String status) throws IOException {
        List<String[]> records = CSVReader.readCSV(appointmentOutcomeFilePath);
        boolean updated = false;

        for (String[] record : records) {
            if (record[0].equals(appointmentID)) {
                if (record[6].equalsIgnoreCase("Dispensed") && status.equalsIgnoreCase("Dispensed")) {
                    System.out.println("Prescription is already dispensed.");
                } else {
                    record[6] = status; 
                    updated = true;
                }
            }
        }

        if (!updated) {
            System.out.println("No records found for the given appointment ID or no changes were made.");
            return;
        }

        CSVWriter.writeCSV(appointmentOutcomeFilePath, records);
        System.out.println("Prescription status updated successfully.");
    }

    public void addPrescriptionToCSV(String patientID, String doctorID, String date, String medicationDetails, String instructions) throws IOException {
        List<String[]> prescriptions = CSVReader.readCSV(prescriptionFilePath);

        String lastPrescriptionID = prescriptions.isEmpty() ? "RX000" : prescriptions.get(prescriptions.size() - 1)[0];
        int nextID = Integer.parseInt(lastPrescriptionID.substring(2)) + 1;
        String prescriptionID = String.format("RX%03d", nextID);

        instructions = "\"" + instructions + "\"";

        String[] prescriptionRecord = {prescriptionID, patientID, doctorID, date, medicationDetails, instructions};

        CSVWriter.appendToCSV(prescriptionFilePath, prescriptionRecord);

        System.out.println("Prescription added successfully with ID: " + prescriptionID);
    }

    public void dispensePrescription(String appointmentID) throws IOException {
        boolean found = false;
    
        List<String[]> outcomeRecords = CSVReader.readCSV(appointmentOutcomeFilePath); 
    
        for (String[] record : outcomeRecords) {
            if (record.length < 7) continue;
    
            if (record[0].equalsIgnoreCase(appointmentID)) {
                if (!"Pending".equalsIgnoreCase(record[6].trim())) {
                    System.out.println("Prescription for Appointment ID " + appointmentID + " is already dispensed.");
                    return;
                }
    
                record[6] = "Dispensed";
                found = true;
                break;
            }
        }
    
        if (!found) {
            System.out.println("No pending prescription found for Appointment ID: " + appointmentID);
            return;
        }
    
        CSVWriter.writeCSV(appointmentOutcomeFilePath, outcomeRecords); 
    
        List<String[]> prescriptionRecords = CSVReader.readCSV(prescriptionFilePath);
        boolean prescriptionRemoved = prescriptionRecords.removeIf(record -> {
            if (record.length < 2) return false;
            return record[0].equalsIgnoreCase(appointmentID);
        });
    
        if (!prescriptionRemoved) {
            System.out.println("No prescription found in Prescription.csv for Appointment ID: " + appointmentID);
            return;
        }
    
        CSVWriter.writeCSV(prescriptionFilePath, prescriptionRecords);
    
        System.out.println("Prescription for Appointment ID " + appointmentID + " dispensed and removed successfully.");
    }
    

    public List<String[]> getPendingPrescriptions() throws IOException {
        return CSVReader.readCSV(prescriptionFilePath);
    }

    public boolean removePrescriptionByID(String prescriptionID) throws IOException {
        List<String[]> prescriptions = CSVReader.readCSV(prescriptionFilePath);
        boolean removed = prescriptions.removeIf(prescription -> prescription[0].equals(prescriptionID));
        if (removed) {
            CSVWriter.writeCSV(prescriptionFilePath, prescriptions);
        }
        return removed;
    }

    public void updateAppointmentOutcomePrescriptionStatus(String appointmentOutcomeFilePath, String prescriptionID, String newStatus) throws IOException {
        List<String[]> records = CSVReader.readCSV(appointmentOutcomeFilePath);
        for (String[] record : records) {
            if (record[5].equals(prescriptionID)) {
                record[6] = newStatus;
                break;
            }
        }
        CSVWriter.writeCSV(appointmentOutcomeFilePath, records);
    }

    public String getPrescriptionNameByAppointmentID(String appointmentsFilePath, String appointmentID) {
        try {
            List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath);
            String patientID = null;
            for (String[] record : appointments) {
                if (record[0].equals(appointmentID)) {
                    patientID = record[1];
                    break;
                }
            }

            if (patientID == null) {
                return null;
            }

            List<String[]> prescriptions = CSVReader.readCSV(prescriptionFilePath);
            for (String[] record : prescriptions) {
                if (record[1].equals(patientID)) {
                    return record[4];
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving prescription: " + e.getMessage());
        }

        return null;
    }

    public Map<String, String> getAppointmentDetailsByUserID(String userID) {
    try {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath);

        for (String[] record : appointments) {
            if (record[1].equalsIgnoreCase(userID) || record[2].equalsIgnoreCase(userID)) {
                Map<String, String> appointmentDetails = new HashMap<>();
                appointmentDetails.put("AppointmentID", record[0]);
                appointmentDetails.put("PatientID", record[1]);
                appointmentDetails.put("DoctorID", record[2]);
                appointmentDetails.put("Date", record[3]);
                appointmentDetails.put("Time", record[4]);
                appointmentDetails.put("Status", record[5]);

                return appointmentDetails; 
            }
        }

    } catch (IOException e) {
        System.err.println("Error reading appointments: " + e.getMessage());
    }
    
    return null; 
}
    
}
