package Application;

import DataManagement.*;
import UserManagement.*;
import InventoryManagement.*;
import java.io.IOException;
import java.util.*;

public class UserMenus {
    public static void main(String args[]) throws IOException {
        // File paths for CSV files
        String staffFilePath = "src/Data/Staff_List.csv";
        String medicineFilePath = "src/Data/Medicine_List.csv";
        String patientFilePath = "src/Data/Patient_List.csv";
        String medicalRecordsFilePath = "src/Data/Medical_Records.csv";
        String appointmentsFilePath = "src/Data/Appointments.csv";
        String userFilePath = "src/Data/User_List.csv";
        String appointmentOutcomeFilePath = "src/Data/Appointment_Outcome.csv";
        String orderRequestFilePath = "src/Data/OrderRequest.csv";
        String prescriptionFilePath = "src/Data/Prescription.csv";
    

        // Initialize CSVReader and CSVWriter
        CSVReader csvReader = new CSVReader();
        CSVWriter csvWriter = new CSVWriter();

        // Initialize the separate manager classes
        appointmentManager appointmentManager = new appointmentManager(appointmentsFilePath, csvReader, csvWriter);
        medicalRecordManager medicalRecordManager = new medicalRecordManager(medicalRecordsFilePath, csvReader, csvWriter);
        PrescriptionManager prescriptionManager = new PrescriptionManager(appointmentOutcomeFilePath, prescriptionFilePath, appointmentsFilePath, csvReader, csvWriter, doctorManager, patientManager);
        userManager userManager = new userManager(userFilePath, csvReader, csvWriter);
        medicineManager medicineManager = new medicineManager(medicineFilePath, csvReader, csvWriter);
        appointmentOutcomeManager appointmentOutcomeManager = new appointmentOutcomeManager(appointmentOutcomeFilePath, appointmentsFilePath, csvReader, csvWriter);
        staffManager staffManager = new staffManager(staffFilePath, csvReader, csvWriter);
        patientManager patientManager = new patientManager(patientFilePath, medicalRecordsFilePath, userFilePath, csvReader, csvWriter);
        orderRequestManager orderRequestManager = new orderRequestManager(orderRequestFilePath, csvReader, csvWriter);
        doctorManager doctorManager = new doctorManager(staffFilePath, patientFilePath, medicalRecordsFilePath, userFilePath, csvReader, csvWriter);

        // User input
        Scanner sc = new Scanner(System.in);
        String userID;
        String password;

        System.out.println("========Welcome to the Hospital Management System!=========");
        System.out.print("To begin, enter your User ID: ");
        userID = sc.nextLine();

        System.out.print("Please enter your password: ");
        password = sc.nextLine();

        // User login validation
        boolean isAuthenticated = userManager.login(userID, password);
        while (!isAuthenticated) {
            System.out.println("Invalid login credentials, please try again");
            System.out.print("User ID: ");
            userID = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();
            isAuthenticated = userManager.login(userID, password);
        }

        // Check if it's the user's first login and prompt to change password
        if (password.equals("password")) {
            System.out.println("This is your first log in. Would you like to change your password? (Yes/No)");
            String pwChange = sc.nextLine().trim();
            while (!pwChange.equalsIgnoreCase("yes") && !pwChange.equalsIgnoreCase("no")) {
                System.out.println("Please enter either Yes or No");
                pwChange = sc.nextLine().trim();
            }
            if (pwChange.equalsIgnoreCase("yes")) {
                System.out.print("Please enter new password: ");
                String newPassword = sc.nextLine();
                if (userManager.updateUserPassword(userID, newPassword)) {
                    System.out.println("Password updated successfully!");
                } else {
                    System.out.println("Password update failed.");
                }
            }
        }

        // Determine the user's role and display the appropriate menu
        String role = userManager.getRoleByUserID(userID);
        switch (role) {
            case "Patient":
                Users existingPatient = new Patient(userID, userManager, patientManager, staffManager, appointmentManager, medicalRecordManager, prescriptionManager, doctorManager, appointmentOutcomeManager);
                System.out.println("Welcome to HMS, Patient " + existingPatient.getName());
                existingPatient.displayMenu();
                break;

            case "Doctor":
                Users existingDoctor = new Doctor(userID, appointmentManager, appointmentOutcomeManager, medicalRecordManager, prescriptionManager, userManager);
                System.out.println("Welcome to HMS, Doctor " + existingDoctor.getName());
                existingDoctor.displayMenu();
                break;

                case "Pharmacist":
                // Instantiate a Pharmacist using the correct parameter types
                Users existingPharmacist = new Pharmacist(userID, userManager, appointmentOutcomeManager, prescriptionManager, medicineManager, orderRequestManager);
                System.out.println("Welcome to HMS, Pharmacist " + existingPharmacist.getName());
                existingPharmacist.displayMenu();
                break;
            
            case "Administrator":
                Users existingAdmin = new Administrator(userID, staffManager, medicineManager, orderRequestManager, appointmentManager, appointmentOutcomeManager, userManager);
                System.out.println("Welcome to HMS, Administrator " + existingAdmin.getName());
                existingAdmin.displayMenu();
                break;

            default:
                System.out.println("Invalid role detected. Please contact the system administrator.");
                break;
        }
    }
}
