package DataManagement;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class prescriptionManager {
    private final String prescriptionFilePath;
    private final String appointmentsFilePath;
    private final String appointmentOutcomeFilePath;
    private final String medicineFilePath;
    private final appointmentManager appointmentManager;


    public prescriptionManager(
            String prescriptionFilePath,
            String appointmentsFilePath,
            String appointmentOutcomeFilePath,
            String medicineFilePath,
            appointmentManager appointmentManager) {


        this.prescriptionFilePath = prescriptionFilePath;
        this.appointmentsFilePath = appointmentsFilePath;
        this.appointmentOutcomeFilePath = appointmentOutcomeFilePath;
        this.medicineFilePath = medicineFilePath;
        this.appointmentManager = appointmentManager;
    }


    /**
     * @param appointmentOutcomeFilePath
     * @param appointmentID
     * @param status
     * @throws IOException
     */
    public void updatePrescriptionStatus(String appointmentOutcomeFilePath, String appointmentID, String status)
            throws IOException {
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


    // public void addPrescriptionToCSV(String patientID, String doctorID, String
    // date, String medicationDetails,
    // String instructions) throws IOException {
    // List<String[]> prescriptions = CSVReader.readCSV(prescriptionFilePath);


    // String lastPrescriptionID = prescriptions.isEmpty() ? "RX000" :
    // prescriptions.get(prescriptions.size() - 1)[0];
    // int nextID = Integer.parseInt(lastPrescriptionID.substring(2)) + 1;
    // String prescriptionID = String.format("RX%03d", nextID);


    // instructions = "\"" + instructions + "\"";


    // String[] prescriptionRecord = { prescriptionID, patientID, doctorID, date,
    // medicationDetails, instructions };


    // CSVWriter.appendToCSV(prescriptionFilePath, prescriptionRecord);


    // System.out.println("Prescription added successfully with ID: " +
    // prescriptionID);
    // }


    public void addPrescriptionToCSV(String appointmentID, String patientID, String doctorID, String date,
            String medicationDetails, String instructions) throws IOException {
        // Read the existing prescriptions from the file
        List<String[]> prescriptions = CSVReader.readCSV(prescriptionFilePath);


        // Generate the next Prescription ID
        String lastPrescriptionID = prescriptions.isEmpty() ? "RX000" : prescriptions.get(prescriptions.size() - 1)[0];
        if (!lastPrescriptionID.startsWith("RX") || lastPrescriptionID.length() != 5) {
            throw new IOException("Invalid Prescription ID format in CSV: " + lastPrescriptionID);
        }
        int nextID = Integer.parseInt(lastPrescriptionID.substring(2)) + 1;
        String prescriptionID = String.format("RX%03d", nextID);


        // Wrap instructions in quotes for CSV compatibility
        instructions = "\"" + instructions + "\"";


        // Create the prescription record
        String[] prescriptionRecord = { prescriptionID, appointmentID, patientID, doctorID, date, medicationDetails,
                instructions, "0" };


        // Append the new prescription to the CSV file
        CSVWriter.appendToCSV(prescriptionFilePath, prescriptionRecord);


        System.out.println("Prescription added successfully with ID: " + prescriptionID);
    }


    /**
     * @param appointmentID
     * @throws IOException
     */
    public void dispensePrescription(String appointmentID) throws IOException {
        boolean found = false;


        // Read prescription records
        List<String[]> prescriptionRecords = CSVReader.readCSV(prescriptionFilePath);
        String[] prescription = null;


        // Find the prescription for the given Appointment ID
        for (String[] record : prescriptionRecords) {
            if (record.length < 8)
                continue; // Ensure enough columns


            if (record[1].equalsIgnoreCase(appointmentID)) { // Matching Appointment ID
                found = true;
                prescription = record;
                break;
            }
        }


        if (!found) {
            System.out.println("No prescription found for Appointment ID: " + appointmentID);
            return;
        }


        // Parse medication details and dispensed quantities
        String[] medications = prescription[5].split("\\|");
        String[] dispensedQuantities = prescription[7].split("\\|");


        if (medications.length != dispensedQuantities.length) {
            System.out.println("Error: Medication details and dispensed quantities do not match for Appointment ID: "
                    + appointmentID);
            return;
        }


        // Dispense medications
        boolean allFullyDispensed = true;
        for (int i = 0; i < medications.length; i++) {
            String[] details = medications[i].split(":");
            if (details.length < 3)
                continue;


            String medicineID = details[0].trim();
            String dosage = details[1].trim();
            int prescribedQuantity = Integer.parseInt(details[2].trim());
            int alreadyDispensed = Integer.parseInt(dispensedQuantities[i].trim());


            int remainingQuantity = prescribedQuantity - alreadyDispensed;
            if (remainingQuantity <= 0) {
                System.out.println("Medication " + medicineID + " (" + dosage + ") already fully dispensed.");
                continue;
            }


            // Update stock in Medicine.csv
            if (!updateMedicineStock(medicineID, remainingQuantity)) {
                System.out.println(
                        "Unable to dispense " + medicineID + " due to insufficient stock or invalid Medicine ID.");
                allFullyDispensed = false;
                continue;
            }


            // Update dispensed quantity
            dispensedQuantities[i] = String.valueOf(alreadyDispensed + remainingQuantity);
        }


        // Check if all medications have been fully dispensed
        for (int i = 0; i < medications.length; i++) {
            String[] details = medications[i].split(":");
            if (details.length < 3)
                continue;


            int prescribedQuantity = Integer.parseInt(details[2].trim());
            int dispensedQuantity = Integer.parseInt(dispensedQuantities[i].trim());


            if (dispensedQuantity < prescribedQuantity) {
                allFullyDispensed = false;
                break;
            }
        }


        // Update the Prescription.csv file
        if (allFullyDispensed) {
            // Remove the prescription record
            prescriptionRecords.remove(prescription);
        } else {
            // Update the dispensed quantities in the prescription
            prescription[7] = String.join("|", dispensedQuantities);
        }
        CSVWriter.writeCSV(prescriptionFilePath, prescriptionRecords);


        // Update AppointmentOutcome.csv
        updateAppointmentOutcome(appointmentID, allFullyDispensed);


        if (allFullyDispensed) {
            System.out.println("Prescription for Appointment ID " + appointmentID + " fully dispensed and removed.");
        } else {
            System.out.println("Prescription for Appointment ID " + appointmentID + " partially dispensed.");
        }
    }


    /**
     * Updates the stock of medicines based on the Medicine ID.
     */
    private boolean updateMedicineStock(String medicineID, int quantityToDispense) throws IOException {
        // Read medicine records
        List<String[]> medicineRecords = CSVReader.readCSV(medicineFilePath);


        for (String[] medicineRecord : medicineRecords) {
            if (medicineRecord.length < 6)
                continue;


            if (medicineRecord[0].equalsIgnoreCase(medicineID)) { // Match by Medicine ID
                int currentStock = Integer.parseInt(medicineRecord[2]);
                int newStock = currentStock - quantityToDispense;


                if (newStock < 0) {
                    System.out.println("Insufficient stock for Medicine ID: " + medicineID);
                    return false;
                }


                medicineRecord[2] = String.valueOf(newStock);


                // Check low stock level
                int lowStockLevel = Integer.parseInt(medicineRecord[3]);
                if (newStock < lowStockLevel) {
                    System.out.println("Low stock alert for Medicine ID: " + medicineID);
                }


                // Write updated medicine records back to file
                CSVWriter.writeCSV(medicineFilePath, medicineRecords);
                return true;
            }
        }


        System.out.println("Medicine ID " + medicineID + " not found.");
        return false;
    }


    /**
     * Updates the appointment outcome record to reflect the prescription status.
     */
    private void updateAppointmentOutcome(String appointmentID, boolean fullyDispensed) throws IOException {
        // Read appointment outcome records
        List<String[]> outcomeRecords = CSVReader.readCSV(appointmentOutcomeFilePath);


        boolean recordUpdated = false;


        for (String[] record : outcomeRecords) {
            if (record.length < 8)
                continue; // Ensure there are enough columns


            if (record[0].equalsIgnoreCase(appointmentID)) { // Match Appointment ID
                // Check if the status is already "Dispensed"
                if ("Dispensed".equalsIgnoreCase(record[6]) && fullyDispensed) {
                    System.out.println(
                            "Prescription for Appointment ID " + appointmentID + " is already marked as Dispensed.");
                    return;
                }


                // Update the Prescription Status
                record[6] = fullyDispensed ? "Dispensed" : "Pending";
                recordUpdated = true; // Mark that the record has been updated
                break;
            }
        }


        if (!recordUpdated) {
            System.out.println("Appointment ID " + appointmentID + " not found in AppointmentOutcome.csv.");
            return;
        }


        // Write updated records back to the file
        CSVWriter.writeCSV(appointmentOutcomeFilePath, outcomeRecords);


        System.out.println("Updated Prescription Status for Appointment ID " + appointmentID + " to " +
                (fullyDispensed ? "Dispensed" : "Pending") + ".");
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


    public void updateAppointmentOutcomePrescriptionStatus(String appointmentOutcomeFilePath, String prescriptionID,
            String newStatus) throws IOException {
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



