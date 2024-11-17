
package DataManagement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class doctorManager {


    private final String staffFilePath;
    private final String patientFilePath;
    private final String medicalRecordsFilePath;
    private final String userFilePath;


    public doctorManager(String staffFilePath, String patientFilePath, String medicalRecordsFilePath,
            String userFilePath) {
        this.staffFilePath = staffFilePath;
        this.patientFilePath = patientFilePath;
        this.medicalRecordsFilePath = medicalRecordsFilePath;
        this.userFilePath = userFilePath;
    }


    /**
     * @param userID
     * @return String[]
     * @throws IOException
     */
    public String[] getDoctorByUserID(String userID) throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);
        for (String[] staff : staffList) {
            if (staff[1].equalsIgnoreCase(userID) && "Doctor".equalsIgnoreCase(staff[3])) {
                return staff;
            }
        }
        return null;
    }


    /**
     * @param doctorID
     * @return List<String[]>
     * @throws IOException
     */
    public List<String[]> getPatientsByDoctorID(String doctorID) throws IOException {
        List<String[]> allPatients = CSVReader.readCSV(patientFilePath);
        List<String[]> filteredPatients = new ArrayList<>();


        for (String[] patient : allPatients) {
            if (patient[8].equalsIgnoreCase(doctorID)) {
                filteredPatients.add(patient);
            }
        }
        return filteredPatients;
    }


    public String getDoctorName(String doctorID) throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);


        for (String[] staff : staffList) {
            if (staff[1].equalsIgnoreCase(doctorID)) {
                return staff[2];
            }
        }


        return "not found";
    }


    public List<String[]> getMedicalRecordsByDoctorID(String doctorID) throws IOException {
        List<String[]> medicalRecords = CSVReader.readCSV(medicalRecordsFilePath);
        List<String[]> doctorRecords = new ArrayList<>();


        for (String[] record : medicalRecords) {
            if (record.length > 10 && record[10].trim().equalsIgnoreCase(doctorID.trim())) {
                doctorRecords.add(record);
            }
        }


        return doctorRecords;
    }


    public String getDoctorIDFromUserList(String userID) throws IOException {
        List<String[]> userList = CSVReader.readCSV(userFilePath);


        for (String[] user : userList) {
            if (user.length > 3 && user[0].equalsIgnoreCase(userID) && "Doctor".equalsIgnoreCase(user[3])) {
                return user[0].trim();
            }
        }


        return null;
    }


    public List<String[]> getAllDoctors() throws IOException {
        List<String[]> staffList = CSVReader.readCSV(staffFilePath);
        List<String[]> doctorsList = new ArrayList<>();


        for (String[] staff : staffList) {
            if ("Doctor".equalsIgnoreCase(staff[3])) {
                doctorsList.add(staff);
            }
        }


        return doctorsList;
    }


}



