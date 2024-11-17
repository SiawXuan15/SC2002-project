package UserManagement;

import AppointmentManagement.Schedulable;
import DataManagement.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Patient extends Users implements Schedulable, PaymentProcessor {

    private final appointmentManager appointmentManager;
    private final userManager userManager;
    private final prescriptionManager prescriptionManager;
    private String userID;
    private Scanner scanner = new Scanner(System.in);
    private final doctorManager doctorManager;
    private final patientManager patientManager;
    private final medicalRecordManager medicalRecordManager;
    private final staffManager staffManager;
    private final appointmentOutcomeManager appointmentOutcomeManager;

    public Patient(String userID, userManager userManager, patientManager patientManager, staffManager staffManager,
            appointmentManager appointmentManager, medicalRecordManager medicalRecordManager,
            prescriptionManager prescriptionManager, doctorManager doctorManager,
            appointmentOutcomeManager appointmentOutcomeManager) throws IOException {
        super(userID, userManager);
        this.userID = userID;
        this.userManager = userManager;
        this.appointmentManager = appointmentManager;
        this.medicalRecordManager = medicalRecordManager;
        this.prescriptionManager = prescriptionManager;
        this.doctorManager = doctorManager;
        this.appointmentOutcomeManager = appointmentOutcomeManager;
        this.staffManager = staffManager;
        this.patientManager = patientManager;
    }

    @Override
    public void displayMenu() {
        while (true) {
            System.out.println("=========================================================");
            System.out.println("Patient Menu:");
            System.out.println("1. View Medical Record");
            System.out.println("2. Update Personal Information");
            System.out.println("3. View Available Appointment Slots");
            System.out.println("4. Schedule an Appointment");
            System.out.println("5. Reschedule an Appointment");
            System.out.println("6. Cancel an Appointment");
            System.out.println("7. View Scheduled Appointments");
            System.out.println("8. View Past Appointment Outcome Records");
            System.out.println("9. Make payment");
            System.out.println("10. Logout");
            System.out.println("Please select an option (1-10): ");
            System.out.println("=========================================================");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewMedicalRecord();
                    break;
                case 2:
                    updatePatientInformation();
                    break;
                case 3:
                    viewAvailableSlots();
                    break;
                case 4:
                    scheduleAppointment();
                    break;
                case 5:
                    rescheduleAppointment();
                    break;
                case 6:
                    cancelAppointment();
                    break;
                case 7:
                    viewScheduledAppointments();
                    break;
                case 8:
                    viewPastAppointmentOutcomeRecords();
                    break;
                case 9:
                    promptPayment();
                    break;
                case 10:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public void viewPastAppointmentOutcomeRecords() {
        List<String[]> pastOutcomes = null;

        try {
            pastOutcomes = appointmentOutcomeManager.getAppointmentOutcomeRecordsByPatientId(this.getUserID());
        } catch (IOException e) {
            System.out.println("Error fetching appointment outcomes: " + e.getMessage());
            return;
        }

        if (pastOutcomes.isEmpty()) {
            System.out.println("No past appointment outcomes found for this patient.");
        } else {
            System.out.println("Past appointment outcomes for Patient ID: " + this.getUserID());

            for (String[] record : pastOutcomes) {
                String typeOfService = record[4];

                if (!typeOfService.equals("-")) {
                    String doctorName = null;

                    try {
                        doctorName = doctorManager.getDoctorName(record[2]);
                    } catch (IOException e) {
                        System.out.println(
                                "Error fetching doctor name for Doctor ID " + record[2] + ": " + e.getMessage());
                        doctorName = "Unknown Doctor";
                    }

                    System.out.println("Appointment date: " + record[3]);
                    System.out.println("Type of service: " + record[4]);
                    System.out.println("Prescription name: " + record[5]);
                    System.out.println("Prescription status: " + record[6]);
                    System.out.println("Consultation notes: " + record[7]);
                    System.out.println("Doctor: " + doctorName);
                    System.out.println("======================================");
                }
            }
        }
    }

    public void promptPayment() {
        System.out.println("You have an outstanding payment of $25");
        System.out.println("Would you like to make payment now? (Y/N)");
        String response = scanner.next().trim().toUpperCase();
        if (response.equals("Y")) {
            makePayment();
        } else {
            System.out.println("Payment postponed.");
            return;
        }
    }

    @Override
    public void makePayment() {
        try {
            String paymentMethod = patientManager.getPatientPayment(userID);

            if (paymentMethod != null) {
                PaymentProcessor paymentProcessor = getPaymentProcessor(paymentMethod);

                if (paymentProcessor == null) {
                    System.out.println("Unknown payment method: " + paymentMethod);
                    return;
                }

                paymentProcessor.makePayment();
                System.out.println("Payment of $25 is made successfully using " + paymentMethod + ".");
            } else {
                System.out.println("Payment method not found for user ID: " + userID);
            }

        } catch (IOException e) {
            System.err.println("Error retrieving payment method: " + e.getMessage());
        }
    }

    private PaymentProcessor getPaymentProcessor(String paymentMethod) {
        switch (paymentMethod.toLowerCase()) {
            case "cash":
                return new Cash();
            case "bank transfer":
                return new BankTransfer();
            default:
                return null;
        }
    }

    public void updatePatientInformation() {
        try {
            List<String[]> patients = patientManager.getPatientList();

            String[] patientRecord = null;
            for (String[] patient : patients) {
                if (patient[1].equalsIgnoreCase(userID)) {
                    patientRecord = patient;
                    break;
                }
            }
<<<<<<< Updated upstream
        }
    
        public void updatePatientInformation() {
            try {
                List<String[]> patients = patientManager.getPatientList();
                String[] patientRecord = null;
                for (String[] patient : patients) {
                    if (patient[1].equalsIgnoreCase(userID)) {
                        patientRecord = patient;
                        break;
                    }
                }
        
                if (patientRecord == null) {
                    System.out.println("No patient record found for User ID: " + userID);
                    return;
                }
        
                Scanner scanner = new Scanner(System.in);
                boolean updating = true;
        
                boolean nameUpdated = false;
                boolean emailUpdated = false;
                boolean contactUpdated = false;
        
                while (updating) {
                    System.out.println("Select what you want to update:");
                    System.out.println("1. Name");
                    System.out.println("2. Gender");
                    System.out.println("3. Contact Number");
                    System.out.println("4. Payment Method");
                    System.out.println("5. Email");
                    System.out.println("6. Date of Birth");
                    System.out.println("7. Quit to go back to main menu");
        
                    int choice = scanner.nextInt();
                    scanner.nextLine();
        
                    switch (choice) {
                        case 1:
                            System.out.print("Enter new name: ");
                            patientRecord[3] = scanner.nextLine();
                            nameUpdated = true;
                            break;
                        case 2:
                            System.out.print("Enter new gender (Male/Female): ");
                            String gender = scanner.nextLine().trim();
                            while (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
                                System.out.print("Invalid gender. Please enter 'Male' or 'Female': ");
                                gender = scanner.nextLine().trim();
                            }
                            patientRecord[5] = gender;
                            break;
                        case 3:
                            System.out.print("Enter new contact number: ");
                            String contact = scanner.nextLine().trim();
                            while (!contact.matches("[89]\\d{7}")) {
                                System.out.print("Invalid contact number. It should start with 8 or 9 and have 8 digits. Please try again: ");
                                contact = scanner.nextLine().trim();
                            }
                            patientRecord[7] = contact;
                            contactUpdated = true;
                            break;
                        case 4:
                            System.out.print("Enter new payment method: ");
                            patientRecord[9] = scanner.nextLine();
                            break;
                        case 5:
                            System.out.print("Enter new email address: ");
                            patientRecord[8] = scanner.nextLine();
                            emailUpdated = true;
                            break;
                        case 6:
                            System.out.print("Enter new date of birth (yyyy-mm-dd): ");
                            String dobString = scanner.nextLine().trim();
                            try {
                                LocalDate.parse(dobString);
                                patientRecord[4] = dobString;
                            } catch (Exception e) {
                                System.out.println("Invalid date format. Please enter in yyyy-mm-dd format.");
                            }
                            break;
                        case 7:
                            updating = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                }
        
                patientManager.updatePatientList(patients);
        
                if (nameUpdated || emailUpdated || contactUpdated) {
                    List<String[]> users = userManager.getUsersList();
                    for (String[] user : users) {
                        if (user[0].equalsIgnoreCase(userID)) {
                            if (nameUpdated) {
                                user[1] = patientRecord[3];
                            }
                            if (emailUpdated) {
                                user[4] = patientRecord[8];
                            }
                            if (contactUpdated) {
                                user[5] = patientRecord[7];
                            }
                            break;
                        }
                    }
                    userManager.updateUsersList(users);
                }
        
                List<String[]> medicalRecords = medicalRecordManager.getMedicalRecords();
                for (String[] record : medicalRecords) {
                    if (record[2].equalsIgnoreCase(userID)) {
                        if (nameUpdated) {
                            record[3] = patientRecord[3];
                        }
                        record[4] = patientRecord[4];
                        record[5] = patientRecord[5];
                        record[6] = patientRecord[7];
                        record[7] = patientRecord[8];
                        break;
                    }
                }
                medicalRecordManager.updateMedicalRecordList(medicalRecords);
        
                System.out.println("Patient information updated successfully!");
        
            } catch (IOException e) {
                System.err.println("Error updating patient information: " + e.getMessage());
=======

            if (patientRecord == null) {
                System.out.println("No patient record found for User ID: " + userID);
                return;
            }

            Scanner scanner = new Scanner(System.in);
            boolean updating = true;

            boolean nameUpdated = false;
            boolean emailUpdated = false;
            boolean contactUpdated = false;

            while (updating) {
                System.out.println("Select what you want to update:");
                System.out.println("1. Name");
                System.out.println("2. Gender");
                System.out.println("3. Contact Number");
                System.out.println("4. Payment Method");
                System.out.println("5. Email");
                System.out.println("6. Date of Birth");
                System.out.println("7. Quit to go back to main menu");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter new name: ");
                        patientRecord[3] = scanner.nextLine();
                        nameUpdated = true;
                        break;
                    case 2:
                        System.out.print("Enter new gender (Male/Female): ");
                        String gender = scanner.nextLine().trim();
                        while (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
                            System.out.print("Invalid gender. Please enter 'Male' or 'Female': ");
                            gender = scanner.nextLine().trim();
                        }
                        patientRecord[5] = gender;
                        break;
                    case 3:
                        System.out.print("Enter new contact number: ");
                        String contact = scanner.nextLine().trim();
                        while (!contact.matches("[89]\\d{7}")) {
                            System.out.print(
                                    "Invalid contact number. It should start with 8 or 9 and have 8 digits. Please try again: ");
                            contact = scanner.nextLine().trim();
                        }
                        patientRecord[7] = contact;
                        contactUpdated = true;
                        break;
                    case 4:
                        System.out.print("Enter new payment method: ");
                        patientRecord[9] = scanner.nextLine();
                        break;
                    case 5:
                        System.out.print("Enter new email address: ");
                        patientRecord[8] = scanner.nextLine();
                        emailUpdated = true;
                        break;
                    case 6:
                        System.out.print("Enter new date of birth (yyyy-mm-dd): ");
                        String dobString = scanner.nextLine().trim();
                        try {
                            LocalDate.parse(dobString);
                            patientRecord[4] = dobString;
                        } catch (Exception e) {
                            System.out.println("Invalid date format. Please enter in yyyy-mm-dd format.");
                        }
                        break;
                    case 7:
                        updating = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            }

            patientManager.updatePatientList(patients);

            if (nameUpdated || emailUpdated || contactUpdated) {
                List<String[]> users = userManager.getUsersList();
                for (String[] user : users) {
                    if (user[0].equalsIgnoreCase(userID)) {
                        if (nameUpdated) {
                            user[1] = patientRecord[3];
                        }
                        if (emailUpdated) {
                            user[4] = patientRecord[8];
                        }
                        if (contactUpdated) {
                            user[5] = patientRecord[7];
                        }
                        break;
                    }
                }
                userManager.updateUsersList(users);
>>>>>>> Stashed changes
            }

        } catch (IOException e) {
            System.err.println("Error updating patient information: " + e.getMessage());
        }
<<<<<<< Updated upstream
        
                
            @Override
            public void scheduleAppointment() {
                Scanner scanner = new Scanner(System.in);
                try {
                    List<String[]> appointments = appointmentManager.getAppointments(); 
                    List<String[]> staffList = staffManager.getStaffList(); 
                    List<String[]> availableSlots = new ArrayList<>();
        
                    System.out.println("Available Appointment Slots:");
                    for (String[] appointment : appointments) {        
                        if ("Available".equals(appointment[5]) && !appointment[5].equals("-")) {  
                            String doctorName = doctorManager.getDoctorName(appointment[2]);
        
                            availableSlots.add(appointment);
                            System.out.println("Doctor: " + doctorName + ", Date: " + appointment[3] + ", Time: " + appointment[4]);
                        }
                    }
        
                    if (availableSlots.isEmpty()) {
                        System.out.println("No available slots.");
                        return;
                    }
        
                    System.out.println("Enter the Doctor's Name (as displayed above):");
                    String doctorName = scanner.nextLine().trim();  
        
                    String doctorID = "";
                    for (String[] staff : staffList) {
                        if (staff[2].equalsIgnoreCase(doctorName)) {  
                            doctorID = staff[1];  
                            break;
                        }
                    }
        
                    if (doctorID.isEmpty()) {
                        System.out.println("Doctor not found.");
                        return;
                    }
        
                    System.out.println("Enter Date (yyyy-MM-dd):");
                    String date = scanner.next();
                    scanner.nextLine(); 
        
                    System.out.println("Enter Time (hh:mm AM/PM):");
                    String time = scanner.nextLine();
        
                    for (String[] appointment : availableSlots) {
                        if (appointment[2].equals(doctorID) && appointment[3].equals(date) && appointment[4].equals(time)) {
                            appointment[5] = "Pending";
                            appointment[1] = this.getUserID();  
        
                            appointmentManager.updateAppointment(appointment[0], appointment);
        
                            System.out.println("Appointment with Dr. " + doctorName + " on " + date + " at " + time + " is pending Doctor's approval. Please check back for \"Confirmed\" status");
                            break;
                        }
                    }
        
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
            @Override
            public void viewAvailableSlots() {
                try {
                    List<String[]> appointments = appointmentManager.getAppointments(); 
                    List<String[]> availableSlots = new ArrayList<>();
        
                    // Display available slots
                    System.out.println("Available Appointment Slots:");
                    for (String[] appointment : appointments) {
                        if ("Available".equals(appointment[5])) {  
                            String doctorName = doctorManager.getDoctorName(appointment[2]);
                            availableSlots.add(appointment);
                            System.out.println("Date: " + appointment[3] + ", Time: " + appointment[4] + ", Doctor: " + doctorName);
                        }
                    }
                    if (availableSlots.isEmpty()) {
                        System.out.println("No available slots.");
                    }
        
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        
            @Override
            public void rescheduleAppointment() {
                try {
                    Scanner scanner = new Scanner(System.in);
                    List<String[]> appointments = appointmentManager.getAppointments(); 
                    List<String[]> currentAppointments = new ArrayList<>();
                    String doctorName;
        
                    System.out.println("Your Appointments:");
                    for (String[] appointment : appointments) {
                        if (appointment[1].equals(this.getUserID()) && !("Completed".equals(appointment[5]))) { 
                            doctorName = doctorManager.getDoctorName(appointment[2]);
=======
    }
>>>>>>> Stashed changes

    public void updateMedicalRecordIfNecessary(String[] patientRecord, boolean nameUpdated) {
        try {
            List<String[]> medicalRecords = medicalRecordManager.getMedicalRecords();
            for (String[] record : medicalRecords) {
                if (record[2].equalsIgnoreCase(userID)) {
                    if (nameUpdated) {
                        record[3] = patientRecord[3];
                    }
                    record[4] = patientRecord[4];
                    record[5] = patientRecord[5];
                    record[6] = patientRecord[7];
                    record[7] = patientRecord[8];
                    break;
                }
            }
            medicalRecordManager.updateMedicalRecordList(medicalRecords);

            System.out.println("Patient information updated successfully!");
        } catch (IOException e) {
            System.err.println("Error updating patient information: " + e.getMessage());
        }
    }

    @Override
    public void scheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
        try {
            List<String[]> appointments = appointmentManager.getAppointments();
            List<String[]> staffList = staffManager.getStaffList();
            List<String[]> availableSlots = new ArrayList<>();

            System.out.println("Available Appointment Slots:");
            for (String[] appointment : appointments) {
                if ("Available".equals(appointment[5]) && !appointment[5].equals("-")) {
                    String doctorName = doctorManager.getDoctorName(appointment[2]);

                    availableSlots.add(appointment);
                    System.out.println(
                            "Doctor: " + doctorName + ", Date: " + appointment[3] + ", Time: " + appointment[4]);
                }
            }

            if (availableSlots.isEmpty()) {
                System.out.println("No available slots.");
                return;
            }

            System.out.println("Enter the Doctor's Name (as displayed above):");
            String doctorName = scanner.nextLine().trim();

            String doctorID = "";
            for (String[] staff : staffList) {
                if (staff[2].equalsIgnoreCase(doctorName)) {
                    doctorID = staff[1];
                    break;
                }
            }

            if (doctorID.isEmpty()) {
                System.out.println("Doctor not found.");
                return;
            }

            System.out.println("Enter Date (yyyy-MM-dd):");
            String date = scanner.next();
            scanner.nextLine();

            System.out.println("Enter Time (hh:mm AM/PM):");
            String time = scanner.nextLine();

            for (String[] appointment : availableSlots) {
                if (appointment[2].equals(doctorID) && appointment[3].equals(date) && appointment[4].equals(time)) {
                    appointment[5] = "Pending";
                    appointment[1] = this.getUserID();

                    appointmentManager.updateAppointment(appointment[0], appointment);

                    System.out.println("Appointment with Dr. " + doctorName + " on " + date + " at " + time
                            + " is pending Doctor's approval. Please check back for \"Confirmed\" status");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void viewAvailableSlots() {
        try {
            List<String[]> appointments = appointmentManager.getAppointments();
            List<String[]> availableSlots = new ArrayList<>();

            // Display available slots
            System.out.println("Available Appointment Slots:");
            for (String[] appointment : appointments) {
                if ("Available".equals(appointment[5])) {
                    String doctorName = doctorManager.getDoctorName(appointment[2]);
                    availableSlots.add(appointment);
                    System.out.println(
                            "Date: " + appointment[3] + ", Time: " + appointment[4] + ", Doctor: " + doctorName);
                }
            }
            if (availableSlots.isEmpty()) {
                System.out.println("No available slots.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rescheduleAppointment() {
        try {
            Scanner scanner = new Scanner(System.in);
            List<String[]> appointments = appointmentManager.getAppointments();
            List<String[]> currentAppointments = new ArrayList<>();
            String doctorName;

            System.out.println("Your Appointments:");
            for (String[] appointment : appointments) {
                if (appointment[1].equals(this.getUserID()) && !("Completed".equals(appointment[5]))) {
                    doctorName = doctorManager.getDoctorName(appointment[2]);

                    currentAppointments.add(appointment);

                    System.out.println("Appointment ID: " + appointment[0]);
                    System.out.println("Doctor: " + doctorName);
                    System.out.println("Date: " + appointment[3]);
                    System.out.println("Time: " + appointment[4]);
                    System.out.println("Status: " + appointment[5]);
                    System.out.println("-------------------------");
                }
            }

            if (currentAppointments.isEmpty()) {
                System.out.println("You have no appointments to reschedule.");
                return;
            }

            System.out.println("Enter Appointment ID to reschedule:");
            String appointmentID = scanner.next();

            for (String[] appointment : currentAppointments) {
                if (appointment[0].equals(appointmentID)) {
                    appointment[1] = "";
                    appointment[5] = "Available";

                    scheduleAppointment();

                    appointmentOutcomeManager.removeFromOutcome(appointmentID);

                    appointmentManager.updateAppointment(appointment[0], appointment);

                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelAppointment() {
        try {
            Scanner scanner = new Scanner(System.in);
            List<String[]> appointments = appointmentManager.getAppointments();
            List<String[]> patientAppointments = new ArrayList<>();
            String doctorName = "";

            System.out.println("Your Appointments:");
            for (String[] appointment : appointments) {
                if (appointment[1].equals(this.getUserID()) && !("Cancelled".equals(appointment[5]))
                        && !("Completed".equals(appointment[5]))) {
                    doctorName = doctorManager.getDoctorName(appointment[2]);

                    patientAppointments.add(appointment);
                    System.out.println("Appointment ID: " + appointment[0]);
                    System.out.println("Doctor: " + doctorName);
                    System.out.println("Date: " + appointment[3]);
                    System.out.println("Time: " + appointment[4]);
                    System.out.println("Status: " + appointment[5]);
                    System.out.println("-------------------------");
                }
            }

            if (patientAppointments.isEmpty()) {
                System.out.println("You have no appointments to cancel.");
                return;
            }

            System.out.println("Enter Appointment ID to cancel:");
            String appointmentID = scanner.next();

            for (String[] appointment : patientAppointments) {
                if (appointment[0].equals(appointmentID)) {
                    appointment[1] = "";
                    appointment[5] = "Available";
                    String apptDoctor = doctorManager.getDoctorName(appointment[2]);

                    appointmentManager.updateAppointment(appointment[0], appointment);
                    appointmentOutcomeManager.removeFromOutcome(appointmentID);

                    System.out.println("Appointment with Dr. " + apptDoctor + " on " + appointment[3] + " at "
                            + appointment[4] + " has been cancelled.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewScheduledAppointments() {
        try {
            List<String[]> patientAppointments = appointmentManager.getAppointmentsByPatient(this.getUserID());
            List<String[]> scheduledAppointments = new ArrayList<>();

            System.out.println("Your Scheduled Appointments:");
            for (String[] appointment : patientAppointments) {
                if ("Pending".equals(appointment[5]) || "Cancelled".equals(appointment[5])
                        || "Confirmed".equals(appointment[5]) || "Completed".equals(appointment[5])) { // Only display
                                                                                                       // "Scheduled"
                                                                                                       // appointments
                    String doctorName = doctorManager.getDoctorName(appointment[2]);

                    scheduledAppointments.add(appointment);
                    System.out.println("Appointment ID: " + appointment[0]);
                    System.out.println("Doctor: " + doctorName);
                    System.out.println("Date: " + appointment[3]);
                    System.out.println("Time: " + appointment[4]);
                    System.out.println("Status: " + appointment[5]);
                    System.out.println("-------------------------");
                }
            }

            if (scheduledAppointments.isEmpty()) {
                System.out.println("You have no scheduled appointments.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUserID() {
        return super.getUserID();
    }

    public void viewMedicalRecord() {
        try {
            String userID = getUserID();
            String[] medicalRecord = medicalRecordManager.getMedicalRecordByUserID(userID);

            if (medicalRecord != null) {
                System.out.println("=== Medical Record Summary ===");
                System.out.println("Record ID: " + medicalRecord[0]);
                System.out.println("Patient ID: " + medicalRecord[1]);
                System.out.println("User ID: " + medicalRecord[2]);
                System.out.println("Name: " + medicalRecord[3]);
                System.out.println("Date of Birth: " + medicalRecord[4]);
                System.out.println("Gender: " + medicalRecord[5]);
                System.out.println("Contact Information: " + medicalRecord[6]);
                System.out.println("Email: " + medicalRecord[7]);
                System.out.println("Blood Type: " + medicalRecord[8]);
                System.out.println("Past Diagnoses and Treatments: " + medicalRecord[9]);
                System.out.println("Doctor ID: " + medicalRecord[10]);
            } else {
                System.out.println("No medical record found for the user.");
            }
        } catch (IOException e) {
            System.err.println("Error accessing medical records: " + e.getMessage());
        }
    }

    public boolean updateContactInfo(String newEmail, String newContact) {
        try {
            boolean updated = patientManager.updatePatientContactInfo(getUserID(), newEmail, newContact);
            if (updated) {
                System.out.println("Contact information updated successfully in all records.");
            } else {
                System.out.println("Failed to update contact information.");
            }
            return updated;
        } catch (IOException e) {
            System.err.println("Error updating contact information: " + e.getMessage());
            return false;
        }
    }

    public void verifyUpdatedContactInfo() {
        try {
            System.out.println("===== Updated Contact Information =====");
            System.out.println("Email Address: " + patientManager.getPatientEmailAddress(getUserID()));
            System.out.println("Contact Number: " + patientManager.getPatientEmergencyContact(getUserID()));
        } catch (IOException e) {
            System.err.println("Error verifying updated contact information: " + e.getMessage());
        }
    }

    public String getName() {
        try {
            List<String[]> patients = patientManager.getPatientList();

            if (patients == null || patients.isEmpty()) {
                System.out.println("No patients found in the system.");
                return null;
            }

            for (String[] patient : patients) {
                if (patient[1].equalsIgnoreCase(userID)) {
                    return patient[3];
                }
            }

            System.out.println("Patient with User ID: " + userID + " not found.");
            return null;

        } catch (IOException e) {
            System.err.println("Error fetching patient list: " + e.getMessage());
            return null;
        }
    }
}
