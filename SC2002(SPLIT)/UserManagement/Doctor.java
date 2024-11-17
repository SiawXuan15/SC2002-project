package UserManagement;

import AppointmentManagement.Appointment;
import AppointmentManagement.ScheduleDoctor;
import AppointmentManagement.TimeSlot;
import DataManagement.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Doctor extends Users implements ScheduleDoctor {
    private List<Patient> patientList;
    private List<TimeSlot> availability;
    private String specialization;
    private String licenseNumber;
    private List<Appointment> appointmentList;
    private final appointmentManager appointmentManager;
    private final medicalRecordManager medicalRecordManager;
    private final prescriptionManager prescriptionManager;
    private final appointmentOutcomeManager appointmentOutcomeManager;
    private final userManager userManager;
    private final String userID;


    public Doctor(String userID, appointmentManager appointmentManager, appointmentOutcomeManager appointmentOutcomeManager,medicalRecordManager medicalRecordManager, prescriptionManager prescriptionManager, userManager userManager) throws IOException {
        super(userID, userManager);
        this.userID = userID;
        this.appointmentManager = appointmentManager;
        this.appointmentOutcomeManager = appointmentOutcomeManager;
        this.medicalRecordManager = medicalRecordManager;
        this.prescriptionManager = prescriptionManager;
        this.userManager = userManager;
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=========================================================");
            System.out.println("Doctor Menu:");
            System.out.println("1. View Patient Medical Record");
            System.out.println("2. Update Patient Medical Record");
            System.out.println("3. View Schedule");
            System.out.println("4. Set Availability");
            System.out.println("5. Accept Appointment");
            System.out.println("6. Decline Appointment");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");
            System.out.println("Please select an option (1-8): ");
            System.out.println("=========================================================");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllMedicalRecords();
                    break;
                case 2:
                    System.out.println("Enter patient ID to update medical record:");
                    String patientId = scanner.next();
                    Patient patient = findPatientById(patientId); 
                    if (patient != null) {
                        System.out.println("Enter diagnosis:");
                        String diagnosis = scanner.next();
                        System.out.println("Enter treatment:");
                        String treatment = scanner.next();
                        // Prescription prescription = new Prescription(); 
                        // updateMedicalRecord(patient, diagnosis, prescription, treatment);
                    } else {
                        System.out.println("Patient not found.");
                    }
                    break;
                case 3:
                    viewSchedule();
                    break;
                case 4:
                    setAvailability();
                    break;
                case 5:
                    acceptAppointment();
                    break;
                case 6:
                    declineAppointment();
                    break;
                case 7:
                    recordAppointmentOutcome();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please select again.");
            }
        }
    }


    public List<TimeSlot> viewAvailableSlots() {
        return availability;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }
// Appointment Management 
public void recordAppointmentOutcome() {
    if (viewSchedule()==false) {
        System.out.println("No appointments to record outcomes for.");
        return;
    }
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter the Appointment ID for which you want to record the outcome:");
    String appointmentID = scanner.nextLine();

    // Try to retrieve all the appointment records
    List<String[]> appointmentsData = null;
    try {
        appointmentsData = appointmentOutcomeManager.getAllAppointmentOutcomeRecords();  // Assuming this method reads from the file
    } catch (IOException e) {
        System.out.println("Error reading appointment data: " + e.getMessage());
        return;  // Exit the method if there's an error reading the file
    }

    String[] appointmentData = null;

    // Search for the appointment by its ID
    for (String[] row : appointmentsData) {
        if (row[0].equals(appointmentID)) {
            appointmentData = row;
            break;
        }
    }

    // If the appointment is not found, display an error message
    if (appointmentData == null) {
        System.out.println("Appointment ID not found.");
        return;
    }
    
    String patientID = appointmentData[1];    
    String doctorID = appointmentData[2];     
    String date = appointmentData[3];
    
     // Add Prescription
    System.out.println("\n--- Prescription Details ---");
    System.out.print("Enter medication details (e.g., M001:500mg:30|M002:200mg:15): ");
    String medicationDetails = scanner.nextLine();

    System.out.print("Enter instructions (e.g., Take with food): ");
    String instructions = scanner.nextLine();

       // Call dbManager to handle prescription creation
       try {
        prescriptionManager.addPrescriptionToCSV(patientID, doctorID, date, medicationDetails, instructions);
    } catch (IOException e) {
        System.out.println("Error writing prescription data: " + e.getMessage());
        return;  // Exit the method if there's an error writing the prescription data
    }

    
    // Collect outcome details from the doctor
    System.out.println("\n--- Appointment Outcome Details ---");
    System.out.print("Enter type of service (Consultation, X-ray, Blood test, etc.):");
    String typeOfService = scanner.nextLine();

    System.out.print("Enter consultation notes: ");
    String consultationNotes = scanner.nextLine();



    // Collect outcome details from the doctor
    //System.out.println("Enter type of service (Consultation, X-ray, Blood test, etc.):");
    //String typeOfService = scanner.nextLine();

    //System.out.println("Enter prescription name:");
    //String prescriptionName = scanner.nextLine();

    //System.out.println("Enter consultation notes:");
    //String consultationNotes = scanner.nextLine();

    // Attempt to update the appointment outcome in the database
    try {
        //dbManager.updateAppointmentOutcome(appointmentID, typeOfService, prescriptionName, consultationNotes);
        appointmentOutcomeManager.updateAppointmentOutcome(appointmentID, typeOfService, medicationDetails, consultationNotes);
        appointmentManager.setAppointmentStatus(appointmentID, "Completed"); // added
        System.out.println("Appointment outcome recorded successfully.");
    } catch (IOException e) {
        System.out.println("An error occurred while recording the outcome: " + e.getMessage());
    }
}

public Patient findPatientById(String patientId) {
    for (Patient patient : patientList) {
        if (patient.getUserID().equals(patientId)) { 
            return patient;
        }
    }
    System.out.println("Patient with ID " + patientId + " not found.");
    return null;
}

    public boolean viewSchedule() {
        boolean found = false;
        try {
            List<String[]> doctorAppointments = appointmentManager.getAppointmentsByDoctor(this.getUserID());
            String doctorName = userManager.getUserName(this.getUserID());

            if (doctorAppointments.isEmpty()) {
                System.out.println("No appointments found for doctor " + doctorName);
            } else {
                found = true;
                System.out.println("Scheduled appointments for doctor " + doctorName + ":");
                for (String[] appointment : doctorAppointments) {
                    if ("Confirmed".equals(appointment[5]) || "Pending".equals(appointment[5]) || "Completed".equals(appointment[5])) {
                        found = true;
                        System.out.println("Appointment ID: " + appointment[0] +
                                ", Patient: " + userManager.getUserName(appointment[1]) +
                                ", Doctor: " + doctorName +
                                ", Date: " + appointment[3] +
                                ", Time: " + appointment[4] +
                                ", Status: " + appointment[5]);
                    }
                }

                if (!found) {
                    System.out.println("No appointments found for doctor " + doctorName);
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading appointments: " + e.getMessage());
        }
        return found;
    }

    public void setAvailability() {
        try {
            Scanner scanner = new Scanner(System.in);

            List<String[]> doctorAppointments = appointmentManager.getAppointmentsByDoctor(this.getUserID());
            String doctorName = userManager.getUserName(this.getUserID());

            List<String[]> availableSlots = new ArrayList<>();
            for (String[] appointment : doctorAppointments) {
                if (appointment[5].equals("-") || "Available".equals(appointment[5])) {
                    availableSlots.add(appointment);
                }
            }

            if (availableSlots.isEmpty()) {
                System.out.println("No available slots for doctor " + doctorName);
            } else {
                System.out.println("Available slots for doctor " + doctorName + ":");
                for (String[] appointment : availableSlots) {
                    System.out.println("Appointment ID: " + appointment[0] +
                            ", Date: " + appointment[3] +
                            ", Time: " + appointment[4] +
                            ", Current Status: " + (appointment[5] != null ? appointment[5] : "Available"));
                }

                System.out.println("Enter appointment ID to set availability:");
                String appointmentID = scanner.next();

                System.out.println("Set availability to (Available/Unavailable):");
                String availabilityStatus = scanner.next();

                appointmentManager.setAvailability(appointmentID, availabilityStatus);
            }
        } catch (IOException e) {
            System.out.println("Error updating availability: " + e.getMessage());
        }
    }

    public void acceptAppointment() {
        try {
            Scanner scanner = new Scanner(System.in);
            List<String[]> doctorAppointments = appointmentManager.getAppointmentsByDoctor(this.getUserID());
            String doctorName = userManager.getUserName(this.getUserID());

            List<String[]> pendingAppointments = new ArrayList<>();
            for (String[] appointment : doctorAppointments) {
                if ("Pending".equals(appointment[5])) {
                    pendingAppointments.add(appointment);
                }
            }

            if (pendingAppointments.isEmpty()) {
                System.out.println("No pending appointments for doctor " + doctorName);
            } else {
                System.out.println("Pending appointments for doctor " + doctorName + ":");
                for (String[] appointment : pendingAppointments) {
                    System.out.println("Appointment ID: " + appointment[0] +
                            ", Patient ID: " + userManager.getUserName(appointment[1]) +
                            ", Date: " + appointment[3] +
                            ", Time: " + appointment[4] +
                            ", Status: " + appointment[5]);
                }

                System.out.println("Enter appointment ID to accept:");
                String appointmentID = scanner.next();

                appointmentManager.setAppointmentStatus(appointmentID, "Confirmed");
                appointmentOutcomeManager.addToOutcome(appointmentID);
            }
        } catch (IOException e) {
            System.out.println("Error accepting appointment: " + e.getMessage());
        }
    }

    public void declineAppointment() {
        try {
            Scanner scanner = new Scanner(System.in);
            List<String[]> doctorAppointments = appointmentManager.getAppointmentsByDoctor(this.getUserID());
            String doctorName = userManager.getUserName(this.getUserID());

            List<String[]> pendingAppointments = new ArrayList<>();
            for (String[] appointment : doctorAppointments) {
                if ("Pending".equals(appointment[5])) {
                    pendingAppointments.add(appointment);
                }
            }

            if (pendingAppointments.isEmpty()) {
                System.out.println("No pending appointments for doctor " + doctorName);
            } else {
                System.out.println("Pending appointments for doctor " + doctorName + ":");
                for (String[] appointment : pendingAppointments) {
                    System.out.println("Appointment ID: " + appointment[0] +
                    ", Patient ID: " + userManager.getUserName(appointment[1]) +
                    ", Date: " + appointment[3] +
                    ", Time: " + appointment[4] +
                    ", Status: " + appointment[5]);
                }

                System.out.println("Enter appointment ID to decline:");
                String appointmentID = scanner.next();

                appointmentManager.setAppointmentStatus(appointmentID, "Cancelled");
            }
        } catch (IOException e) {
            System.out.println("Error declining appointment: " + e.getMessage());
        }
    }

    public void viewAllMedicalRecords() {
        try {
            String doctorID = getUserID();
            List<String[]> medicalRecords = medicalRecordManager.getMedicalRecords();

            if (medicalRecords.isEmpty()) {
                System.out.println("No medical records found for patients under your care.");
                return;
            }

            System.out.println("=== Medical Records for Patients Under Your Care ===");
            for (String[] record : medicalRecords) {
                System.out.println("Record ID: " + record[0]);
                System.out.println("Patient ID: " + record[1]);
                System.out.println("User ID: " + record[2]);
                System.out.println("Name: " + record[3]);
                System.out.println("Date of Birth: " + record[4]);
                System.out.println("Gender: " + record[5]);
                System.out.println("Contact Information: " + record[6]);
                System.out.println("Blood Type: " + record[7]);
                System.out.println("Past Diagnoses and Treatments: " + record[8]);
                System.out.println("Doctor ID: " + record[9]);
                System.out.println("--------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Error retrieving medical records: " + e.getMessage());
        }
    }
}
