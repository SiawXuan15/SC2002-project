package UserManagement;


import DataManagement.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Doctor extends Users {
    private List<Patient> patientList;
    private String specialization;
    private final appointmentManager appointmentManager;
    private final medicalRecordManager medicalRecordManager;
    private final prescriptionManager prescriptionManager;
    private final appointmentOutcomeManager appointmentOutcomeManager;
    private final userManager userManager;
    private final patientManager patientManager;
    private final medicineManager medicineManager;
    private final String userID;


    public Doctor(String userID, appointmentManager appointmentManager,
            appointmentOutcomeManager appointmentOutcomeManager, medicalRecordManager medicalRecordManager,
            prescriptionManager prescriptionManager, userManager userManager, patientManager patientManager,
            medicineManager medicineManager)
            throws IOException {
        super(userID, userManager);
        this.userID = userID;
        this.appointmentManager = appointmentManager;
        this.appointmentOutcomeManager = appointmentOutcomeManager;
        this.medicalRecordManager = medicalRecordManager;
        this.prescriptionManager = prescriptionManager;
        this.userManager = userManager;
        this.patientManager = patientManager;
        this.medicineManager = medicineManager;
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
            System.out.println("8. Schedule Follow-Up Appointment");
            System.out.println("9. Logout");
            System.out.println("Please select an option (1-9): ");
            System.out.println("=========================================================");


            int choice = scanner.nextInt();
            scanner.nextLine();


            switch (choice) {
                case 1:
                    viewAllMedicalRecords();
                    break;
                case 2:
                    updatePatientMedicalRecord();

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
                    scheduleFollowUp();
                    break;
                case 9:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please select again.");
            }
        }
    }


    public void recordAppointmentOutcome() {
        if (viewSchedule() == false) {
            System.out.println("No appointments to record outcomes for.");
            return;
        }
        Scanner scanner = new Scanner(System.in);


        System.out.println("Enter the Appointment ID for which you want to record the outcome:");
        String appointmentID = scanner.nextLine();


        List<String[]> appointmentsData = null;
        try {
            appointmentsData = appointmentOutcomeManager.getAllAppointmentOutcomeRecords(); // Assuming this method
                                                                                            // reads from the file
        } catch (IOException e) {
            System.out.println("Error reading appointment data: " + e.getMessage());
            return; // Exit the method if there's an error reading the file
        }


        String[] appointmentData = null;


        for (String[] row : appointmentsData) {
            if (row[0].equals(appointmentID)) {
                appointmentData = row;
                break;
            }
        }


        if (appointmentData == null) {
            System.out.println("Appointment ID not found.");
            return;
        }


        String patientID = appointmentData[1];
        String doctorID = appointmentData[2];
        String date = appointmentData[3];


        System.out.println("\n--- Available Medicines ---");
        List<String[]> medicines = null;
        try {
            medicines = medicineManager.getMedicineList();
        } catch (IOException e) {
            System.out.println("Error reading medicine data: " + e.getMessage());
            return;
        }


        // Check if medicines list is valid and non-empty
        if (medicines != null && !medicines.isEmpty()) {
            for (String[] medicine : medicines) {
                // Print each medicine's details
                System.out.printf("%-11s | %-12s | %s\n", medicine[0], medicine[1], medicine[4]);
            }
        } else {
            System.out.println("No medicines available.");
        }


        System.out.println("\n--- Prescription Details ---");
        System.out.print("Enter medication details (e.g., M001:500mg:30|M002:200mg:15): ");
        String medicationDetails = scanner.nextLine();


        System.out.print("Enter instructions (e.g., Take with food): ");
        String instructions = scanner.nextLine();


        try {
            prescriptionManager.addPrescriptionToCSV(appointmentID, patientID, doctorID, date,
                    medicationDetails, instructions);
        } catch (IOException e) {
            System.out.println("Error writing prescription data: " + e.getMessage());
            return;
        }


        System.out.println("\n--- Appointment Outcome Details ---");
        System.out.print("Enter type of service (Consultation, X-ray, Blood test, etc.):");
        String typeOfService = scanner.nextLine();


        System.out.print("Enter consultation notes: ");
        String consultationNotes = scanner.nextLine();


        try {
            appointmentOutcomeManager.updateAppointmentOutcome(appointmentID, typeOfService, medicationDetails,
                    consultationNotes);
            appointmentManager.setAppointmentStatus(appointmentID, "Completed");
            System.out.println("Appointment outcome recorded successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while recording the outcome: " + e.getMessage());
        }
    }


    /**
     * @param patientId
     * @return Patient
     */
    public Patient findPatientById(String patientId) {
        for (Patient patient : patientList) {
            if (patient.getUserID().equals(patientId)) {
                return patient;
            }
        }
        System.out.println("Patient with ID " + patientId + " not found.");
        return null;
    }


    /**
     * @return boolean
     */
    public boolean viewSchedule() {
        boolean found = false;
        try {
            List<String[]> doctorAppointments = appointmentManager.getAppointmentsByDoctor(this.getUserID());
            List<String[]> scheduledAppointments = new ArrayList<>();
            String doctorName = userManager.getUserName(this.getUserID());


            if (doctorAppointments.isEmpty()) {
                System.out.println("No appointments found for doctor " + doctorName);
            } else {
                System.out.println("Scheduled appointments for doctor " + doctorName + ":");
                for (String[] appointment : doctorAppointments) {
                    if ("Confirmed".equals(appointment[5]) || "Pending".equals(appointment[5])
                            || "Completed".equals(appointment[5])) {
                        found = true;


                        scheduledAppointments.add(appointment);


                        System.out.println("Appointment ID: " + appointment[0] +
                                ", Patient: " + userManager.getUserName(appointment[1]) +
                                ", Doctor: " + doctorName +
                                ", Date: " + appointment[3] +
                                ", Time: " + appointment[4] +
                                ", Status: " + appointment[5]);
                    }
                }


                if (scheduledAppointments.isEmpty()) {
                    System.out.println("No appointments found for doctor " + doctorName);
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
            for (int i = 1; i < medicalRecords.size(); i++) {
                String[] record = medicalRecords.get(i);
                System.out.println("Record ID: " + record[0]);
                System.out.println("Patient ID: " + record[1]);
                System.out.println("User ID: " + record[2]);
                System.out.println("Name: " + record[3]);
                System.out.println("Date of Birth: " + record[4]);
                System.out.println("Gender: " + record[5]);
                System.out.println("Contact Information: " + record[6]);
                System.out.println("Email: " + record[7]);
                System.out.println("Blood Type: " + record[8]);
                System.out.println("Past Diagnoses and Treatments: " + record[9]);
                System.out.println("Doctor ID: " + record[10]);
                System.out.println("--------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Error retrieving medical records: " + e.getMessage());
        }
    }




    public void scheduleFollowUp() {
        Scanner scanner = new Scanner(System.in);


        List<String[]> patientList = new ArrayList<>();


        try {
            patientList = patientManager.getPatientList(); // Fetch patients from patientManager
        } catch (IOException e) {
            System.out.println("Error fetching appointments or patient list: " + e.getMessage());
            return; // Exit if there's an issue fetching data
        }


        for (int i = 1; i < patientList.size(); i++) {
            String[] patient = patientList.get(i);
            System.out.println("Patient ID: " + patient[0] + ", Patient name: " + patient[3]);
        }


        System.out.println("Enter the patient's name to schedule follow-up appointment:");
        String patientName = scanner.nextLine();


        String patientID = null;
        try {
            patientID = patientManager.getPatientID(patientName);
        } catch (IOException e) {
            System.out.println("Error fetching patient ID: " + e.getMessage());
            return;
        }


        if (patientID == null) {
            System.out.println("Patient not found.");
            return;
        }


        System.out.println("Enter the follow-up appointment date (yyyy-MM-dd):");
        String followUpDate = scanner.nextLine();


        System.out.println("Enter the follow-up appointment time (hh:mm AM/PM):");
        String followUpTime = scanner.nextLine();


        String newAppId = appointmentManager.generateAppId();
        String doctorId = this.getUserID();


        String[] followUpAppointment = new String[] {
                newAppId,
                patientID,
                doctorId,
                followUpDate,
                followUpTime,
                "Confirmed"
        };


        try {
            appointmentManager.addAppointment(followUpAppointment);


            appointmentOutcomeManager.addToOutcome(newAppId);


            System.out.println("Follow-up appointment with " + patientName + " scheduled successfully for "
                    + followUpDate + " at " + followUpTime);
        } catch (IOException e) {
            System.out.println("Error scheduling follow-up appointment: " + e.getMessage());
        }
    }


private void updatePatientMedicalRecord() {
    Scanner scanner = new Scanner(System.in);


    System.out.println("Enter patient ID to update medical record:");
    String patientId = scanner.nextLine().trim();


    try {
        // Fetch the patient's medical record
        String[] medicalRecord = medicalRecordManager.getMedicalRecordByPatientID(patientId);


        if (medicalRecord != null) {
            // Prompt doctor for new diagnosis and treatment
            System.out.println("\nEnter new diagnosis:");
            String newDiagnosis = scanner.nextLine().trim();
            System.out.println("Enter new treatment:");
            String newTreatment = scanner.nextLine().trim();


            // Update the medical record
            medicalRecordManager.updateMedicalRecord(patientId, newDiagnosis, newTreatment);


            // Fetch the updated medical record
            String[] updatedMedicalRecord = medicalRecordManager.getMedicalRecordByPatientID(patientId);


            // Print the updated medical record summary
            System.out.println("\n=== Updated Medical Record Summary ===");
            printMedicalRecordSummary(updatedMedicalRecord);


            System.out.println("\nMedical record updated successfully.");
        } else {
            System.out.println("Patient not found or no medical record exists.");
        }
    } catch (IOException e) {
        System.out.println("Error updating medical record: " + e.getMessage());
    }
}

private void printMedicalRecordSummary(String[] medicalRecord) {


    System.out.println("Record ID: " + medicalRecord[0]);
    System.out.println("Patient ID: " + medicalRecord[1]);
    System.out.println("User ID: " + medicalRecord[2]);
    System.out.println("Name: " + medicalRecord[3]);
    System.out.println("Date of Birth: " + medicalRecord[4]);
    System.out.println("Gender: " + medicalRecord[5]);
    System.out.println("Contact Information: " + medicalRecord[6]);
    System.out.println("Email: " + medicalRecord[7]);
    System.out.println("Blood Type: " + medicalRecord[8]);


    System.out.println("Past Diagnoses and Treatments:");
    String[] historyEntries = medicalRecord[9].split(";");
    for (String entry : historyEntries) {
        System.out.println("  - " + entry.trim());
    }


    System.out.println("Doctor ID: " + medicalRecord[10]);
}


}





