package DataManagement;

import AppointmentManagement.Appointment;
import UserManagement.Doctor;
import UserManagement.Patient;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class prescriptionManager {
    private final String prescriptionFilePath;
    private final String appointmentsFilePath; 
    private final CSVReader csvReader;
    private final CSVWriter csvWriter;
    private final doctorManager doctorManager;
    private final patientManager patientManager;
    private final String appointmentOutcomeFilePath; 
    private final userManager userManager; 
    private final appointmentManager appointmentManager;
    private final appointmentOutcomeManager appointmentOutcomeManager;
    private final medicalRecordManager medicalRecordManager;
    private final prescriptionManager prescriptionManager; // You don't really need to include itself, removing to avoid confusion.
    private final staffManager staffManager;

    // Updated Constructor
    public prescriptionManager(
            String prescriptionFilePath,
            String appointmentsFilePath,
            String appointmentOutcomeFilePath,
            CSVReader csvReader,
            CSVWriter csvWriter,
            doctorManager doctorManager,
            patientManager patientManager,
            userManager userManager,
            appointmentManager appointmentManager,
            appointmentOutcomeManager appointmentOutcomeManager,
            medicalRecordManager medicalRecordManager,
            staffManager staffManager,
            prescriptionManager prescriptionManager) {
        
        this.prescriptionFilePath = prescriptionFilePath;
        this.appointmentsFilePath = appointmentsFilePath;
        this.appointmentOutcomeFilePath = appointmentOutcomeFilePath;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        this.doctorManager = doctorManager;
        this.patientManager = patientManager;
        this.userManager = userManager;
        this.appointmentManager = appointmentManager;
        this.appointmentOutcomeManager = appointmentOutcomeManager;
        this.medicalRecordManager = medicalRecordManager;
        this.staffManager = staffManager;
        this.prescriptionManager = prescriptionManager;
    }




    public void updatePrescriptionStatus(String appointmentOutcomeFilePath, String appointmentID, String status) throws IOException {
        List<String[]> records = CSVReader.readCSV(appointmentOutcomeFilePath);
        boolean updated = false;

        for (String[] record : records) {
            if (record[0].equals(appointmentID)) {
                if (record[6].equalsIgnoreCase("Dispensed") && status.equalsIgnoreCase("Dispensed")) {
                    System.out.println("Prescription is already dispensed.");
                } else {
                    record[6] = status; // Update status to the specified status
                    updated = true;
                }
            }
        }

        if (!updated) {
            System.out.println("No records found for the given appointment ID or no changes were made.");
            return;
        }

        // Write updated records back to the CSV file
        CSVWriter.writeCSV(appointmentOutcomeFilePath, records);
        System.out.println("Prescription status updated successfully.");
    }

    public void addPrescriptionToCSV(String patientID, String doctorID, String date, String medicationDetails, String instructions) throws IOException {
        List<String[]> prescriptions = CSVReader.readCSV(prescriptionFilePath);

        // Generate the next Prescription ID
        String lastPrescriptionID = prescriptions.isEmpty() ? "RX000" : prescriptions.get(prescriptions.size() - 1)[0];
        int nextID = Integer.parseInt(lastPrescriptionID.substring(2)) + 1;
        String prescriptionID = String.format("RX%03d", nextID);

        // Ensure instructions are quoted for consistent formatting
        instructions = "\"" + instructions + "\"";

        String[] prescriptionRecord = {prescriptionID, patientID, doctorID, date, medicationDetails, instructions};

        // Append the new record to the CSV file
        CSVWriter.appendToCSV(prescriptionFilePath, prescriptionRecord);

        System.out.println("Prescription added successfully with ID: " + prescriptionID);
    }

    public void dispensePrescription(String appointmentID) throws IOException {
        boolean found = false;
    
        List<String[]> outcomeRecords = CSVReader.readCSV(appointmentOutcomeFilePath); // Use the member variable directly
    
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
    
        CSVWriter.writeCSV(appointmentOutcomeFilePath, outcomeRecords); // Write to CSV using the path member variable
    
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

    public Appointment getAppointmentByID(String appointmentID) {
        try {
            List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); // Read the appointments.csv
            for (String[] record : appointments) {
                if (record[0].equals(appointmentID)) { // Match the Appointment ID
                    // Retrieve Patient and Doctor objects using their IDs
                    // Patient patient = new Patient(record[1], this); 
                    // Doctor doctor = new Doctor(record[2], this);    
                    Patient patient = new Patient(
                        record[1], 
                        userManager, 
                        patientManager, 
                        staffManager, 
                        appointmentManager, 
                        medicalRecordManager, 
                        prescriptionManager, 
                        doctorManager, 
                        appointmentOutcomeManager
                    );
    
                    Doctor doctor = new Doctor(
                        record[2], 
                        appointmentManager, 
                        appointmentOutcomeManager, 
                        medicalRecordManager, 
                        prescriptionManager, 
                        userManager
                    );
    
                // Parse the Date and Time
    
                    // Parse the Date and Time
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(record[3]);
                    String time = record[4];
    
                    // Create a new Appointment object
                    Appointment appointment = new Appointment(patient, doctor, date, time);
                    appointment.setStatus(record[5]); // Set the status if it's different from the default
    
                    return appointment; // Return the matching Appointment object
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Error retrieving appointment: " + e.getMessage());
        }
        return null; // Return null if no match is found
    }
    
}
