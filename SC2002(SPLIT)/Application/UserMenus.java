package Application;

import DataManagement.*;
import UserManagement.*;
import java.io.IOException;
import java.util.*;

/**
 * The UserMenus class serves as the entry point for the Hospital Management
 * System (HMS).
 * It handles user authentication, role-based access, and displays the
 * appropriate menu
 * based on the user's role (Patient, Doctor, Pharmacist, Administrator).
 * 
 * <p>
 * The main method initializes various managers for handling different aspects
 * of the system, such as user management, appointment management, and medical
 * records.
 * It prompts the user for their User ID and password, authenticates them, and
 * allows them to change
 * their password if it's their first login. Based on the user's role, it
 * creates the
 * appropriate user object
 * and displays the corresponding menu.
 * </p>
 * 
 * <p>
 * This class requires the following files to be present in the specified paths:
 * <ul>
 * <li>Staff_List.csv</li>
 * <li>Medicine_List.csv</li>
 * <li>Patient_List.csv</li>
 * <li>Medical_Records.csv</li>
 * <li>Appointments.csv</li>
 * <li>User_List.csv</li>
 * <li>Appointment_Outcome.csv</li>
 * <li>OrderRequest.csv</li>
 * <li>Prescription.csv</li>
 * </ul>
 * </p>
 */

public class UserMenus {
    public static void main(String args[]) throws IOException {
        String staffFilePath = "SC2002(SPLIT)/Data/Staff_List.csv";
        String medicineFilePath = "SC2002(SPLIT)/Data/Medicine_List.csv";
        String patientFilePath = "SC2002(SPLIT)/Data/Patient_List.csv";
        String medicalRecordsFilePath = "SC2002(SPLIT)/Data/Medical_Records.csv";
        String appointmentsFilePath = "SC2002(SPLIT)/Data/Appointments.csv";
        String userFilePath = "SC2002(SPLIT)/Data/User_List.csv";
        String appointmentOutcomeFilePath = "SC2002(SPLIT)/Data/Appointment_Outcome.csv";
        String orderRequestFilePath = "SC2002(SPLIT)/Data/OrderRequest.csv";
        String prescriptionFilePath = "SC2002(SPLIT)/Data/Prescription.csv";



        appointmentManager appointmentManager = new appointmentManager(appointmentsFilePath);
        medicalRecordManager medicalRecordManager = new medicalRecordManager(medicalRecordsFilePath);
        prescriptionManager prescriptionManager = new prescriptionManager(
                prescriptionFilePath,
                appointmentsFilePath,
                appointmentOutcomeFilePath,
                medicineFilePath,
                appointmentManager);
        userManager userManager = new userManager(userFilePath);
        medicineManager medicineManager = new medicineManager(medicineFilePath);
        appointmentOutcomeManager appointmentOutcomeManager = new appointmentOutcomeManager(appointmentOutcomeFilePath,
                appointmentsFilePath);
        staffManager staffManager = new staffManager(staffFilePath);
        patientManager patientManager = new patientManager(patientFilePath, medicalRecordsFilePath, userFilePath);
        orderRequestManager orderRequestManager = new orderRequestManager(orderRequestFilePath);
        doctorManager doctorManager = new doctorManager(staffFilePath, patientFilePath, medicalRecordsFilePath,
                userFilePath);

        Scanner sc = new Scanner(System.in);
        String userID;
        String password;

        System.out.println("========Welcome to the Hospital Management System!=========");
        System.out.print("To begin, enter your User ID: ");
        userID = sc.nextLine();

        System.out.print("Please enter your password: ");
        password = sc.nextLine();

        boolean isAuthenticated = userManager.login(userID, password);
        while (!isAuthenticated) {
            System.out.println("Invalid login credentials, please try again");
            System.out.print("User ID: ");
            userID = sc.nextLine();
            System.out.print("Password: ");
            password = sc.nextLine();
            isAuthenticated = userManager.login(userID, password);
        }

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

        String role = userManager.getRoleByUserID(userID);
        switch (role) {
            case "Patient":
                Users existingPatient = new Patient(userID, userManager, patientManager, staffManager,
                        appointmentManager, medicalRecordManager, prescriptionManager, doctorManager,
                        appointmentOutcomeManager);
                System.out.println("Welcome to HMS, Patient " + existingPatient.getName());
                existingPatient.displayMenu();
                break;

            case "Doctor":
                Users existingDoctor = new Doctor(userID, appointmentManager, appointmentOutcomeManager,
                        medicalRecordManager, prescriptionManager, userManager, patientManager, medicineManager);
                System.out.println("Welcome to HMS, Doctor " + existingDoctor.getName());
                existingDoctor.displayMenu();
                break;

            case "Pharmacist":
                Users existingPharmacist = new Pharmacist(userID, userManager, appointmentOutcomeManager,
                        prescriptionManager, medicineManager, orderRequestManager);
                System.out.println("Welcome to HMS, Pharmacist " + existingPharmacist.getName());
                existingPharmacist.displayMenu();
                break;

            case "Administrator":
                Users existingAdmin = new Administrator(userID, staffManager, medicineManager, orderRequestManager,
                        appointmentManager, appointmentOutcomeManager, userManager);
                System.out.println("Welcome to HMS, Administrator " + existingAdmin.getName());
                existingAdmin.displayMenu();
                break;

            default:
                System.out.println("Invalid role detected. Please contact the system administrator.");
                break;
        }
    }
}
