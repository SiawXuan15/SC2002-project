package MedicalRecords;

import DataManagement.CSVDatabaseManager;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AppointmentOutcomeRecord {

    private final CSVDatabaseManager dbManager;

    private String appointmentID;
    private String patientID;
    private String doctorID;
    private Date dateOfAppointment;
    private String typeOfService;
    private String prescriptionName;
    private String prescriptionStatus;
    private String consultationNotes;

    // Constructor
    public AppointmentOutcomeRecord(String appointmentID, CSVDatabaseManager dbManager) {
        this.appointmentID = appointmentID;
        this.dbManager = dbManager;
        loadAppointmentOutcomeRecord(); // Load details from the database upon initialization
    }

    public AppointmentOutcomeRecord(String appointmentID, String patientID, String doctorID, Date dateOfAppointment, 
    String typeOfService, String prescriptionName, String prescriptionStatus, 
    String consultationNotes,CSVDatabaseManager dbManager) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID; 
        this.dateOfAppointment = dateOfAppointment;
        this.typeOfService = typeOfService;
        this.prescriptionName = prescriptionName;
        this.prescriptionStatus = prescriptionStatus;
        this.consultationNotes = consultationNotes;
        this.dbManager = dbManager;
    }
    // Load appointment outcome record details from the database
    private void loadAppointmentOutcomeRecord() {
        try {
            List<String[]> records = dbManager.getAppointmentOutcomeRecordsByAppointmentId(appointmentID);
            if (!records.isEmpty()) {
                // Assuming we only need the first matching record
                String[] record = records.get(0);

                this.patientID = record[1]; // Patient ID at index 1
                this.doctorID = record[2]; // Doctor ID at index 2
                this.dateOfAppointment = parseDate(record[3]); // Date of Appointment at index 3
                this.typeOfService = record[4]; // Type of Service at index 4
                this.prescriptionName = record[5]; // Prescription Name at index 5
                this.prescriptionStatus = record[6]; // Prescription Status at index 6
                this.consultationNotes = record[7]; // Consultation Notes at index 7
            } else {
                System.out.println("No appointment outcome record found for appointment ID: " + appointmentID);
            }
        } catch (IOException e) {
            System.err.println("Error loading appointment outcome record: " + e.getMessage());
        }
    }

    // Helper method to parse the date string from the CSV file
    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

    // Print a summary of the appointment outcome record
    public void printSummary() {
        if (patientID != null) { // Check if the record is properly loaded by verifying key fields
            System.out.println("===== Appointment Outcome Record Summary =====");
            System.out.println("Appointment ID: " + appointmentID);
            System.out.println("Patient ID: " + patientID);
            System.out.println("Doctor ID: " + doctorID);
            System.out.println("Date of Appointment: " + (dateOfAppointment != null ? new SimpleDateFormat("yyyy-MM-dd").format(dateOfAppointment) : "N/A"));
            System.out.println("Type of Service: " + typeOfService);
            System.out.println("Prescription Name: " + prescriptionName);
            System.out.println("Prescription Status: " + prescriptionStatus);
            System.out.println("Consultation Notes: " + consultationNotes);
        } else {
            System.out.println("No appointment outcome record found for appointment ID: " + appointmentID);
        }
    }
}

