package DataManagement;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class medicineManager {


    private final String medicineFilePath;


    public medicineManager(String medicineFilePath) {
        this.medicineFilePath = medicineFilePath;
    }


    /**
     * @return List<String[]>
     * @throws IOException
     */
    public List<String[]> getMedicineList() throws IOException {
        return CSVReader.readCSV(medicineFilePath);
    }


    /**
     * @return List<String[]>
     * @throws IOException
     */
    public List<String[]> getLowStockMedications() throws IOException {
        List<String[]> medicines = getMedicineList();
        List<String[]> lowStock = new ArrayList<>();


        for (String[] medicine : medicines) {
            int currentStock = Integer.parseInt(medicine[2]);
            int lowStockAlert = Integer.parseInt(medicine[3]);


            if (currentStock <= lowStockAlert) {
                lowStock.add(medicine);
            }
        }


        return lowStock;
    }


    public void updateLowStockLevel(String medID, int newLowStockLevel) throws IOException {
        List<String[]> medicines = getMedicineList();


        for (String[] medicine : medicines) {
            if (medicine[0].equalsIgnoreCase(medID)) {
                medicine[3] = String.valueOf(newLowStockLevel);
                System.out.println("Low Stock Level Alert updated for Medication ID: " + medID);
            }
        }


        CSVWriter.writeCSV(medicineFilePath, medicines);
    }


    public void addMedicine(String[] newMedicine) throws IOException {
        CSVWriter.appendToCSV(medicineFilePath, newMedicine);
    }


    public void updateMedicineList(List<String[]> updatedMedicine) throws IOException {
        CSVWriter.writeCSV(medicineFilePath, updatedMedicine);
    }


    public void updateMedicationStock(String medID, int quantity) throws IOException {
        List<String[]> medicines = getMedicineList();
        boolean updated = false;


        for (String[] med : medicines) {
            if (med[0].equalsIgnoreCase(medID)) {
                int currentStock = Integer.parseInt(med[2]);
                med[2] = String.valueOf(currentStock + quantity);
                updated = true;
                break;
            }
        }


        if (!updated) {
            System.out.println("Medication with ID " + medID + " not found.");
        } else {
            CSVWriter.writeCSV(medicineFilePath, medicines);
            System.out.println("Stock level updated successfully for Medication ID: " + medID);
        }
    }


    public String getMedicationField(String medID, String fieldName) throws IOException {
        List<String[]> medicines = getMedicineList();


        for (String[] med : medicines) {
            if (med[0].equalsIgnoreCase(medID)) {
                switch (fieldName.toLowerCase()) {
                    case "medicine id":
                        return med[0];
                    case "medicine name":
                        return med[1];
                    case "initial stock":
                        return med[2];
                    case "low stock level alert":
                        return med[3];
                    case "dosage information":
                        return med[4];
                    case "expiry date":
                        return med[5];
                    default:
                        throw new IllegalArgumentException("Invalid field name: " + fieldName);
                }
            }
        }


        System.out.println("Medication with ID " + medID + " not found.");
        return null;
    }


    public String[] getMedicationDetails(String medID) throws IOException {
        List<String[]> medicines = getMedicineList();


        for (String[] med : medicines) {
            if (med[0].equalsIgnoreCase(medID)) {
                return med;
            }
        }


        System.out.println("Medication with ID " + medID + " not found.");
        return null;
    }


    public void removeStockLevel(String medID, int removeQuantity) throws IOException {
        List<String[]> medicines = getMedicineList();
        boolean found = false;


        for (String[] med : medicines) {
            if (med[0].equalsIgnoreCase(medID)) {
                int currentStock = Integer.parseInt(med[2]);
                if (currentStock >= removeQuantity) {
                    med[2] = String.valueOf(currentStock - removeQuantity);
                    found = true;
                } else {
                    throw new IllegalArgumentException("Insufficient stock to remove the specified quantity.");
                }
                break;
            }
        }


        if (!found) {
            throw new IllegalArgumentException("Medication with ID " + medID + " not found.");
        }


        CSVWriter.writeCSV(medicineFilePath, medicines);
        System.out.println("Stock level updated successfully for Medication ID: " + medID);
    }


    public void addStockLevels(String medID, int addQuantity) throws IOException {
        updateMedicationStock(medID, addQuantity);
    }


    public List<String[]> loadMedicationList() {
        List<String[]> medications = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                medications.add(line.split(","));
            }
        } catch (IOException e) {
            System.err.println("Error reading medication file: " + e.getMessage());
        }
        return medications;
    }
}



