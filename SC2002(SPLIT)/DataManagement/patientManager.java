package DataManagement;


import java.io.IOException;
import java.util.List;


public class patientManager {
    private final String patientFilePath;
    private final String medicalRecordsFilePath;
    private final String userFilePath;


    public patientManager(String patientFilePath, String medicalRecordsFilePath, String userFilePath) {
        this.patientFilePath = patientFilePath;
        this.medicalRecordsFilePath = medicalRecordsFilePath;
        this.userFilePath = userFilePath;
    }


    /**
     * @param userID
     * @return String[]
     * @throws IOException
     */
    public String[] getPatientByUserID(String userID) throws IOException {
        List<String[]> patients = CSVReader.readCSV(medicalRecordsFilePath);
        for (String[] patient : patients) {
            if (patient[1].equals(userID)) {
                return patient;
            }
        }
        return null;
    }


    /**
     * @return List<String[]>
     * @throws IOException
     */
    public List<String[]> getPatientList() throws IOException {
        return CSVReader.readCSV(patientFilePath);
    }


    public void addPatient(String[] newPatient) throws IOException {
        CSVWriter.appendToCSV(patientFilePath, newPatient);
    }


    public void updatePatientList(List<String[]> updatedPatients) throws IOException {
        CSVWriter.writeCSV(patientFilePath, updatedPatients);
    }


    public String getPatientDOB(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[4];
        }
        return null;
    }


    public String getPatientBloodType(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[8];
        }
        return null;
    }


    public int getPatientEmergencyContact(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return Integer.parseInt(patientDetails[6]);
        }
        return 0;
    }


    public String getPatientName(String userID) throws IOException {
        List<String[]> patients = CSVReader.readCSV(patientFilePath);
        for (String[] patient : patients) {
            if (patient[1].equals(userID)) {
                return patient[3];
            }
        }
        return null;
    }


    public String getPatientID(String name) throws IOException {
        List<String[]> patients = CSVReader.readCSV(patientFilePath);
        for (String[] patient : patients) {
            if (patient[3].equals(name)) {
                return patient[1];
            }
        }
        return null;
    }


    public String getPatientGender(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[5];
        }
        return null;
    }


    public String getPastDiagnosesAndTreatments(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[9];
        }
        return null;
    }


    public String getPatientEmailAddress(String userID) throws IOException {
        String[] patientDetails = getPatientByUserID(userID);
        if (patientDetails != null) {
            return patientDetails[7];
        }
        return null;
    }


    public String getPatientPayment(String userID) throws IOException {
        List<String[]> patients = getPatientList();
        for (String[] patient : patients) {
            if (patient[1].equalsIgnoreCase(userID)) {
                return patient[9];
            }
        }
        return "Payment method not found for user ID: " + userID;
    }


    // public boolean updatePatientContactInfo(String userID, String newEmail,
    // String newContact) throws IOException {
    // boolean updated = false;


    // List<String[]> patientList = CSVReader.readCSV(patientFilePath);
    // for (String[] patient : patientList) {
    // if (patient[1].equals(userID)) {
    // patient[7] = newContact;
    // patient[8] = newEmail;
    // updated = true;
    // break;
    // }
    // }
    // if (updated) {
    // CSVWriter.writeCSV(patientFilePath, patientList);
    // }


    // List<String[]> userList = CSVReader.readCSV(userFilePath);
    // for (String[] user : userList) {
    // if (user[0].equals(userID)) {
    // user[5] = newContact;
    // user[4] = newEmail;
    // break;
    // }
    // }
    // CSVWriter.writeCSV(userFilePath, userList);


    // List<String[]> medicalRecords = CSVReader.readCSV(medicalRecordsFilePath);
    // for (String[] record : medicalRecords) {
    // if (record[2].equals(userID)) {
    // record[6] = newContact;
    // record[7] = newEmail;
    // break;
    // }
    // }
    // CSVWriter.writeCSV(medicalRecordsFilePath, medicalRecords);


    // return updated;
    // }


    public boolean updatePatientData(String userID, String name, String email,
            String contact, String gender, String dob, String paymentMethod) throws IOException {
        boolean updated = false;


        // Update User_List.csv
        List<String[]> users = CSVReader.readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(userID)) { // Match by User ID
                if (name != null)
                    user[1] = name; // Update name
                if (email != null)
                    user[4] = email; // Update email
                if (contact != null)
                    user[5] = contact; // Update contact number
                updated = true;
                break;
            }
        }
        if (updated)
            CSVWriter.writeCSV(userFilePath, users);


        // Update Patient_List.csv
        updated = false; // Reset flag for next update
        List<String[]> patients = CSVReader.readCSV(patientFilePath);
        for (String[] patient : patients) {
            if (patient[1].equalsIgnoreCase(userID)) { // Match by User ID
                if (name != null)
                    patient[3] = name;
                if (dob != null)
                    patient[4] = dob;
                if (gender != null)
                    patient[5] = gender;
                if (contact != null)
                    patient[7] = contact;
                if (email != null)
                    patient[8] = email;
                if (paymentMethod != null)
                    patient[9] = paymentMethod;
                updated = true;
                break;
            }
        }
        if (updated)
            CSVWriter.writeCSV(patientFilePath, patients);


        // Update MedicalRecords.csv
        updated = false; // Reset flag for next update
        List<String[]> medicalRecords = CSVReader.readCSV(medicalRecordsFilePath);
        for (String[] record : medicalRecords) {
            if (record[2].equalsIgnoreCase(userID)) { // Match by User ID
                if (name != null)
                    record[3] = name;
                if (dob != null)
                    record[4] = dob;
                if (gender != null)
                    record[5] = gender;
                if (contact != null)
                    record[6] = contact;
                if (email != null)
                    record[7] = email;
                updated = true;
                break;
            }
        }
        if (updated)
            CSVWriter.writeCSV(medicalRecordsFilePath, medicalRecords);


        return updated;
    }


    public void reloadPatientData() throws IOException {
        // Reload patients list from CSV
        List<String[]> patients = CSVReader.readCSV(patientFilePath);
        System.out.println("Reloaded patient data: " + patients.size() + " entries found.");
    }


}



