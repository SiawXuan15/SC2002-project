package UserManagement;


import DataManagement.*;
import InventoryManagement.*;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Pharmacist extends Users implements InventoryMonitor {
    private final appointmentOutcomeManager appointmentOutcomeManager;
    private final prescriptionManager prescriptionManager;
    private final medicineManager medicineManager;
    private final orderRequestManager orderRequestManager;
    private final userManager userManager;
    private final String userID;


    public Pharmacist(String userID, userManager userManager, appointmentOutcomeManager appointmentOutcomeManager,
            prescriptionManager prescriptionManager, medicineManager medicineManager,
            orderRequestManager orderRequestManager) throws IOException {
        super(userID, userManager);
        this.userID = userID;
        this.appointmentOutcomeManager = appointmentOutcomeManager;
        this.prescriptionManager = prescriptionManager;
        this.userManager = userManager;
        this.medicineManager = medicineManager;
        this.orderRequestManager = orderRequestManager;
    }


    @Override
    public void displayMenu() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("=========================================================");
            System.out.println("Pharmacist Menu");
            System.out.println("1. View Appointment Outcome Record");
            System.out.println("2. Update Prescription Status");
            System.out.println("3. View Medication Inventory");
            System.out.println("4. Submit Replenishment Request");
            System.out.println("5. Logout");
            System.out.println("Please select an option (1-5): ");
            System.out.println("=========================================================");


            int choice = sc.nextInt();
            sc.nextLine();


            switch (choice) {
                case 1:
                    System.out.println("=== Viewing Appointment Outcome Summary ===");
                    viewAppointmentOutcomeRecord();
                    break;
                case 2:
                    System.out.println("1. View Prescription Requests");
                    System.out.println("2. Dispense Medicine");
                    System.out.print("Choose an option: ");
                    int subChoice = sc.nextInt();
                    sc.nextLine(); // Consume newline


                    if (subChoice == 1) {
                        System.out.println("=== Viewing Prescription Requests ===");
                        try {
                            viewPendingPrescriptions();
                        } catch (IOException e) {
                            System.err.println("Error retrieving pending prescriptions: " + e.getMessage());
                        }
                    } else if (subChoice == 2) {
                        dispensePrescriptionForAppointment();
                    } else {
                        System.out.println("Invalid option. Returning to main menu.");
                    }
                    break;


                case 3:
                    getMedicationList();
                    break;
                case 4:
                    System.out.print("Enter Medication ID for replenishment: ");
                    String medId = sc.nextLine();
                    System.out.print("Enter replenishment quantity: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();


                    submitReplenishmentRequest(medId, quantity);
                    break;


                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }
        }
    }


    public void viewAppointmentOutcomeRecord() {
        try {
            List<String[]> records = appointmentOutcomeManager.getAllAppointmentOutcomeRecords();
            //for (String[] record : records) {
         for (int i = 1; i < records.size(); i++) {
         String[] record = records.get(i);
                String appointmentId = record[0];
                String patientId = record[1];
                String doctorId = record[2];
                String dateOfAppointment = record[3];
                String typeOfService = record[4];
                String medicationName = record[5];
                String medicationStatus = record[6];
                String consultationNotes = record[7];
 
 
 
 
                System.out.println("Appointment ID: " + appointmentId);
                System.out.println("Patient ID: " + patientId);
                System.out.println("Doctor ID: " + doctorId);
                System.out.println("Date Of Appointment: " + dateOfAppointment);
                System.out.println("Type Of Service: " + typeOfService);
                System.out.println("Medication Name: " + medicationName);
                System.out.println("Medication Status: " + medicationStatus);
                System.out.println("Consultation Notes: " + consultationNotes);
                System.out.println("--------------------------------------");
            }
 
 
 
 
        } catch (IOException e) {
            System.err.println("Error accessing appointment outcome records: " + e.getMessage());
        }
    }
 
 


    /**
     * @param medID
     * @param quantity
     */
    @Override
    public void submitReplenishmentRequest(String medID, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }


        try {
            String[] medicationDetails = medicineManager.getMedicationDetails(medID);


            if (medicationDetails == null) {
                System.out.println("Medication with ID " + medID + " not found.");
                return;
            }


            String medicationName = medicationDetails[1].trim(); // Medication Name
            int currentStock = Integer.parseInt(medicationDetails[2].trim()); // Current Stock
            int lowStockThreshold = Integer.parseInt(medicationDetails[3].trim()); // Low Stock Threshold


            if (currentStock <= lowStockThreshold) {
                orderRequestManager.addReplenishmentRequest(medID, medicationName, quantity);
                System.out.println("Replenishment request submitted for " + medicationName +
                        " (ID: " + medID + ") with quantity: " + quantity);
            } else {
                System.out.println("Stock level for " + medicationName +
                        " (ID: " + medID + ") is sufficient. Replenishment not required.");
            }
        } catch (IOException e) {
            System.err.println("Error accessing medication data: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing stock data for MedID " + medID + ": " + e.getMessage());
        }
    }


    @Override
    public void getMedicationList() {
        try {
            List<String[]> inventoryData = medicineManager.getMedicineList();


            if (!inventoryData.isEmpty()) {
                System.out.println("=== Medication Inventory ===");
                for (int i = 1; i < inventoryData.size(); i++) {
                    String[] medData = inventoryData.get(i);
                    System.out.println("Medicine ID: " + medData[0]);
                    System.out.println("Medicine Name: " + medData[1]);
                    System.out.println("Initial Stock: " + medData[2]);
                    System.out.println("Low Stock Level Alert: " + medData[3]);
                    System.out.println("Dosage Information: " + medData[4]);
                    System.out.println("Expiry Date: " + medData[5]);
                    System.out.println("--------------------------------------");
                }
            } else {
                System.out.println("No medication inventory data found.");
            }
        } catch (IOException e) {
            System.err.println("Error retrieving medicine inventory: " + e.getMessage());
        }
    }


    /**
     * @throws IOException
     */
    public void viewPendingPrescriptions() throws IOException {
        List<String[]> prescriptions = prescriptionManager.getPendingPrescriptions();


        if (prescriptions.isEmpty()) {
            System.out.println("No prescriptions available.");
            return;
        }


        System.out.println("=== Prescription Requests ===");


        for (String[] record : prescriptions) {
            if (record == null || record.length < 7 || String.join("", record).trim().isEmpty()) {
                continue;
            }


            for (int i = 0; i < record.length; i++) {
                record[i] = record[i].trim();
            }


            System.out.println("Prescription ID: " + record[0]);
            System.out.println("Appointment ID: " + record[1]);
            System.out.println("Patient ID: " + record[2]);
            System.out.println("Doctor ID: " + record[3]);
            System.out.println("Date: " + record[4]);
            System.out.println("Medication Details: " + record[5]);
            System.out.println("Instructions: " + record[6]);
            System.out.println("Dispensed Quantity: " + record[7]);
            System.out.println("-------------------------");
        }
    }


    public void dispensePrescriptionForAppointment() {
        Scanner scanner = new Scanner(System.in);


        System.out.println("=== Dispense Prescription ===");
        System.out.print("Enter Appointment ID to dispense prescription: ");
        String appointmentID = scanner.nextLine();


        try {
            prescriptionManager.dispensePrescription(appointmentID);
        } catch (IOException e) {
            System.err.println("Error dispensing prescription: " + e.getMessage());
        }
    }


}



