package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class orderRequestManager {

    private final String orderRequestFilePath;

    public orderRequestManager(String orderRequestFilePath) {
        this.orderRequestFilePath = orderRequestFilePath;
    }

    public void addReplenishmentRequest(String medID, String medicationName, int quantity) throws IOException {
        List<String[]> existingRequests = CSVReader.readCSV(orderRequestFilePath);

        int maxID = 0;
        for (String[] request : existingRequests) {
            try {
                maxID = Math.max(maxID, Integer.parseInt(request[0])); 
            } catch (NumberFormatException e) {
                
            }
        }

        String newRequestID = String.valueOf(maxID + 1);
        String status = "Pending";

        String[] newRequest = {newRequestID, medID, medicationName, String.valueOf(quantity), status};

        CSVWriter.appendToCSV(orderRequestFilePath, newRequest);
        System.out.println("Replenishment request added successfully: " + String.join(", ", newRequest));
    }

    public String[] getReplenishmentRequestByID(String requestID) throws IOException {
        List<String[]> requests = CSVReader.readCSV(orderRequestFilePath);
        for (String[] request : requests) {
            if (request[0].equals(requestID)) {
                return request;
            }
        }
        return null; 
    }

    public void updateReplenishmentRequestStatus(String requestID, String status) throws IOException {
        List<String[]> requests = CSVReader.readCSV(orderRequestFilePath);

        boolean updated = false;
        for (String[] request : requests) {
            if (request[0].equals(requestID)) {
                request[4] = status; 
                updated = true;
            }
        }

        if (updated) {
            CSVWriter.writeCSV(orderRequestFilePath, requests);
            System.out.println("Replenishment request status updated successfully.");
        } else {
            System.out.println("Request ID not found: " + requestID);
        }
    }

    public List<String[]> getOrderRequests() throws IOException {
        return CSVReader.readCSV(orderRequestFilePath);
    }

    public List<String[]> getPendingReplenishmentRequests() throws IOException {
        List<String[]> pendingRequests = new ArrayList<>();
        List<String[]> allRequests = CSVReader.readCSV(orderRequestFilePath);

        for (String[] fields : allRequests) {
            if (fields[4].trim().equalsIgnoreCase("Pending")) {
                pendingRequests.add(fields);
            }
        }
        return pendingRequests;
    }
}
