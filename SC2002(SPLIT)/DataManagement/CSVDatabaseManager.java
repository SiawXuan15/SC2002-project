// package DataManagement;

// import java.io.IOException;
// import java.util.List;

// public class CSVDatabaseManager {
//     private final String staffFilePath;
//     private final String medicineFilePath;
//     private final String patientFilePath;
//     private final String medicalRecordsFilePath;
//     private final String appointmentsFilePath;
//     private final String userFilePath;
//     private final String appointmentOutcomeFilePath; 
//     private final String orderRequestFilePath; 
//     private final String prescriptionFilePath; 

//     public CSVDatabaseManager(String staffFilePath, String medicineFilePath, String patientFilePath,
//                               String medicalRecordsFilePath, String appointmentsFilePath, String userFilePath, String appointmentOutcomeFilePath, String orderRequestFilePath, String prescriptionFilePath) {
//         this.staffFilePath = staffFilePath;
//         this.medicineFilePath = medicineFilePath;
//         this.patientFilePath = patientFilePath;
//         this.medicalRecordsFilePath = medicalRecordsFilePath;
//         this.appointmentsFilePath = appointmentsFilePath;
//         this.userFilePath = userFilePath;
//         this.appointmentOutcomeFilePath = appointmentOutcomeFilePath; 
//         this.orderRequestFilePath = orderRequestFilePath;
//         this.prescriptionFilePath = prescriptionFilePath;
//     }


//     // Generic CSV Read
//     public List<String[]> readCSV(String filePath) throws IOException {
//         return CSVReader.readCSV(filePath);
//     }

//     // Generic CSV Write
//     public void writeCSV(String filePath, List<String[]> data) throws IOException {
//         CSVWriter.writeCSV(filePath, data);
//     }

//     // Generic CSV Append
//     public void appendToCSV(String filePath, String[] data) throws IOException {
//         CSVWriter.appendToCSV(filePath, data);
//     }
// }
//     // =========================== User Management ===========================
//     public String[] getUserById(String userID) throws IOException {
//         List<String[]> users = readCSV(userFilePath);
//         for (String[] user : users) {
//             if (user[0].equalsIgnoreCase(userID)) { // Assuming UserID is in the first column
//                 return user;
//             }
//         }
//         return null; // User not found
//     }
//    public String getUserName(String userID) throws IOException {
//     List<String[]> users = readCSV(userFilePath);  // Assuming this reads the CSV correctly
//     for (String[] user : users) {
//         if (user[0].trim().equalsIgnoreCase(userID.trim())) {  // Trim and compare case-insensitively
//             return user[1].trim();  // Trim the name to remove any leading/trailing spaces
//         }
//     }
//     return null;  // Return null if no match is found
// }


//     // Verify login credentials
//     public boolean login(String userID, String password) throws IOException {
//         String[] user = getUserById(userID);
//         return user != null && user[2].equals(password); // Assuming password is at index 2
//     }

//     // Update user password
//     public boolean updateUserPassword(String userID, String newPassword) throws IOException {
//         List<String[]> users = readCSV(userFilePath);
//         for (String[] user : users) {
//             if (user[0].trim().equals(userID.trim())) {
//                 user[2] = newPassword; // Assuming password is at index 2
//                 writeCSV(userFilePath, users);
//                 return true;
//             }
//         }
//         return false; // User not found
//     }

//     // Check if user exists
//     public boolean isUserExists(String userID) throws IOException {
//         return getUserById(userID) != null;
//     }

//     public List<String[]> getUsersList() throws IOException {
//         return CSVReader.readCSV(userFilePath);
//     }

//     public void addUser(String[] newUser) throws IOException {
//         CSVWriter.appendToCSV(userFilePath, newUser);
//     }

//     public void updateUsersList(List<String[]> updatedUsers) throws IOException {
//         CSVWriter.writeCSV(userFilePath, updatedUsers);
//     }

//     public String getRoleByUserID(String userID) throws IOException {
//         List<String[]> users = readCSV(userFilePath);
//         for (String[] user : users) {
//             if (user[0].trim().equalsIgnoreCase(userID.trim())) { // Assuming UserID is at index 0
//                 return user[3].trim(); // Assuming Role is at index 3
//             }
//         }
//         return null; // Return null if user not found
//     }
    
    
//     // =========================== Staff Management ===========================

//     public String[] getStaffByUserID(String userID) throws IOException {
//         List<String[]> staffList = readCSV(staffFilePath);
//         for (String[] staff : staffList) {
//             if (staff[1].equals(userID)) { // Assuming UserID is at index 1
//                 return staff;
//             }
//         }
//         return null; // Staff not found
//     }
    
//     // Method to generate the next Staff ID
//     private String generateNextStaffID(List<String[]> staffList) {
//         int maxID = 0;
//         for (String[] staff : staffList) {
//             String staffID = staff[0]; // Assuming Staff ID is at index 0
//             if (staffID.startsWith("S")) {
//                 try {
//                     int idNumber = Integer.parseInt(staffID.substring(1)); // Extract numeric part
//                     if (idNumber > maxID) {
//                         maxID = idNumber; // Keep track of the largest ID
//                     }
//                 } catch (NumberFormatException e) {
//                     // Ignore any invalid ID formats
//                 }
//             }
//         }
//         return "S" + String.format("%03d", maxID + 1); // Generate next ID (e.g., S005)
//     }

//     // Helper function to generate UserID
//     private String generateUserID(String rolePrefix, String filePath) throws IOException {
//         List<String[]> records = readCSV(filePath);
//         int maxNumber = 0;
//         for (String[] record : records) {
//             if (record.length > 0 && record[1].startsWith(rolePrefix)) {
//                 try {
//                     int num = Integer.parseInt(record[1].substring(rolePrefix.length()));
//                     maxNumber = Math.max(maxNumber, num);
//                 } catch (NumberFormatException ignored) {
//                     // Ignore invalid IDs
//                 }
//             }
//         }
//         return rolePrefix + String.format("%03d", maxNumber + 1);
//     }

//     private String getRolePrefix(String role) {
//         switch (role.toLowerCase()) {
//             case "doctor": return "D";
//             case "administrator": return "A";
//             case "pharmacist": return "P";
//             default: return "U"; // Default prefix for unrecognized roles
//     }
// }

// public List<String[]> getStaffList() throws IOException {
//     return readCSV(staffFilePath);
// }

// public void addStaff() throws IOException {
//     Scanner sc = new Scanner(System.in);
//     List<String[]> staffList = getStaffList();

//     // Generate the next Staff ID
//     String nextStaffID = generateNextStaffID(staffList);

//     System.out.println("Enter details for new staff.");


//     // Input Name
//     System.out.print("Name: ");
//     String name = sc.nextLine();
    
//     // Input Role
//     System.out.print("Role: ");
//     String role = sc.nextLine();
//     String rolePrefix = getRolePrefix(role);
//     String userID = generateUserID(rolePrefix, staffFilePath);
//     System.out.println("Generated User ID: " + userID);


//     // Input and validate Gender
//     String gender;
//     while (true) {
//         System.out.print("Gender: ");
//         gender = sc.nextLine();
//         if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female")) {
//             gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase(); // Capitalize
//             break;
//         } else {
//             System.out.println("Invalid Gender. Please enter Male or Female.");
//         }
//     }

//     // Input and validate Age
//     String age;
//     while (true) {
//         System.out.print("Age: ");
//         age = sc.nextLine();
//         try {
//             int ageInt = Integer.parseInt(age);
//             if (ageInt >= 0 && ageInt <= 100) {
//                 break;
//             } else {
//                 System.out.println("Invalid Age. It must be between 0 and 100.");
//             }
//         } catch (NumberFormatException e) {
//             System.out.println("Invalid Age. Please enter a valid number.");
//         }
//     }

//     // Input Specialization
//     System.out.print("Specialization: ");
//     String specialization = sc.nextLine();

//     // Input and validate Contact
//     String contact;
//     while (true) {
//         System.out.print("Contact (8 digits, starts with 8 or 9): ");
//         contact = sc.nextLine();
//         if (contact.matches("^[89]\\d{7}$")) {
//             break;
//         } else {
//             System.out.println("Invalid Contact. It must start with 8 or 9 and have 8 digits.");
//         }
//     }

//     // Input and validate Email
//     String email;
//     while (true) {
//         System.out.print("Email: ");
//         email = sc.nextLine();
//         if (email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) { // Basic email validation regex
//             break;
//         } else {
//             System.out.println("Invalid Email. Please enter a valid email address.");
//         }
//     }

//     // Define newStaff array with email at the last index
//     String[] newStaff = {nextStaffID, userID, name, role, gender, age, specialization, contact, email};
    
//     // Define newUser array, replacing "" with email
//     String[] newUser = {userID, name, "password", role, email, contact};

//     try {
//         CSVWriter.appendToCSV(staffFilePath, newStaff);
//         addUser(newUser);
//         System.out.println("New staff added successfully with Staff ID: " + nextStaffID);
//     } catch (IOException e) {
//         System.err.println("Error adding new staff: " + e.getMessage());
//     }
// }



//     // remove staff
//     public boolean removeStaff(String userID) throws IOException {
//         List<String[]> staffList = getStaffList(); // Get the current list of staff
    
//         // Try to remove the staff with the given userID
//         boolean removed = staffList.removeIf(staff -> staff[0].equals(userID)); // Assuming userID is at index 0
    
//         if (removed) {
//             // Update the CSV file to reflect the removal
//             updateStaffList(staffList); // This method should write the updated list back to the CSV
//         }
    
//         return removed; // Return true if removal was successful, otherwise false
//     }


//     public void updateStaffList(List<String[]> updatedStaff) throws IOException {
//         writeCSV(staffFilePath, updatedStaff);
//     }

//     public void updateStaffInfo(String userID, String[] updatedStaff) throws IOException {
//         List<String[]> staffList = getStaffList();
//         boolean found = false;
    
//         for (int i = 0; i < staffList.size(); i++) {
//             String[] staff = staffList.get(i);
//             if (staff[0].equals(userID)) { // Assuming UserID is at index 0
//                 staffList.set(i, updatedStaff); // Replace the old data with updated data
//                 found = true;
//                 break;
//             }
//         }
    
//         if (found) {
//             updateStaffList(staffList); // Write the updated list back to the CSV
//         } else {
//             System.out.println("Staff with UserID " + userID + " not found.");
//         }
//     }
    

//     public List<String[]> filterStaff(String criterion, String value) throws IOException {
//         List<String[]> staffList = getStaffList();
//         List<String[]> filteredList = new ArrayList<>();
    
//         // Assuming the indexes are:
//         // Index 2: Role, Index 3: Gender, Index 4: Age
//         for (String[] staff : staffList) {
//             switch (criterion.toLowerCase()) {
//                 case "role":
//                     if (staff[2].equalsIgnoreCase(value)) {
//                         filteredList.add(staff);
//                     }
//                     break;
//                 case "gender":
//                     if (staff[3].equalsIgnoreCase(value)) {
//                         filteredList.add(staff);
//                     }
//                     break;
//                 case "age":
//                     if (staff[4].equals(value)) { // Age should be an exact match
//                         filteredList.add(staff);
//                     }
//                     break;
//             }
//         }
//         return filteredList;
//     } 
 
    
//     // =========================== Patient Management ===========================
    
//     public String[] getPatientByUserID(String userID) throws IOException {
//         List<String[]> patients = readCSV(medicalRecordsFilePath);
//         for (String[] patient : patients) {
//             if (patient[1].equals(userID)) { // Assuming UserID is at index 1
//                 return patient;
//             }
//         }
//         return null; // Patient not found
//     }

//     public List<String[]> getPatientList() throws IOException {
//         return readCSV(patientFilePath);
//     }



//     public void addPatient(String[] newPatient) throws IOException {
//         appendToCSV(patientFilePath, newPatient);
//     }

//     public void updatePatientList(List<String[]> updatedPatients) throws IOException {
//         writeCSV(patientFilePath, updatedPatients);
//     }
//     public String getPatientDOB(String userID) throws IOException {
//         String[] patientDetails = getMedicalRecordByUserID(userID);
//         if (patientDetails != null) {
//             return patientDetails[4]; 
//         }
//         return null;
//     }

//     public String getPatientBloodType(String userID) throws IOException {
//         String[] patientDetails = getMedicalRecordByUserID(userID);
//         if (patientDetails != null) {
//             return patientDetails[8]; 
//         }
//         return null;
//     }


//     public int getPatientEmergencyContact(String userID) throws IOException {
//         String[] patientDetails = getMedicalRecordByUserID(userID);
//         if (patientDetails != null) {
//             return Integer.parseInt(patientDetails[6]); 
//         }
//         return 0; 
//     }
    
//     // public String getUserName(String userID) throws IOException {
//     //     List<String[]> patients = readCSV(userFilePath); 
//     //     for (String[] patient : patients) {
//     //         if (patient[0].equals(userID)) { 
//     //             return patient[1]; 
//     //         }
//     //     }
//     //     return null; // Return null if no match is found
//     // }

    
//    public String getPatientName(String userID) throws IOException {
//        List<String[]> patients = readCSV(patientFilePath); // Read the patient list CSV
//        for (String[] patient : patients) {
//            if (patient[1].equals(userID)) { // Assuming the first column is userID
//                return patient[3]; // Assuming the second column is the patient's name
//            }
//        }
//        return null; // Return null if no match is found
//    }
    
        
//     public String getPatientGender(String userID) throws IOException {
//         String[] patientDetails = getMedicalRecordByUserID(userID);
//         if (patientDetails != null) {
//             return patientDetails[5]; 
//         }
//         return null;
//     }

    
//     public String getPastDiagnosesAndTreatments(String userID) throws IOException {
//         String[] patientDetails = getMedicalRecordByUserID(userID);
//         if (patientDetails != null) {
//             return patientDetails[9]; 
//         }
//         return null;
//     }

    
//     public String getPatientEmailAddress(String userID) throws IOException {
//         String[] patientDetails = getMedicalRecordByUserID(userID);
//         if (patientDetails != null) {
//             return patientDetails[7]; 
//         }
//         return null;
//     }

//     public String getPatientPayment(String userID) throws IOException {
//         // Fetch the list of patients from the CSV file
//         List<String[]> patients = getPatientList();
        
//         // Iterate through each patient record to find the matching user ID
//         for (String[] patient : patients) {
//             if (patient[1].equalsIgnoreCase(userID)) { // Assuming User ID is at index 1
//                 return patient[9]; // Payment method is at index 9
//             }
//         }
        
//         // If no matching record is found, return a message or null
//         return "Payment method not found for user ID: " + userID;
//     }

    
    

    
//     public String getPatientInsuranceDetails(String userID) {
//         // If insurance details are part of Patient_List.csv or another file, retrieve them here.
//         // For now, return a default value
//         return "Default Insurance";
//     }
//     public boolean updatePatientContactInfo(String userID, String newEmail, String newContact) throws IOException {
//         boolean updated = false;
    
//         // Update in Patient_List.csv
//         List<String[]> patientList = readCSV(patientFilePath);
//         for (String[] patient : patientList) {
//             if (patient[1].equals(userID)) { // Assuming UserID is at index 1
//                 patient[7] = newContact; // Assuming Contact Number is at index 7
//                 patient[8] = newEmail;   // Assuming Email Address is at index 8
//                 updated = true;
//                 break;
//             }
//         }
//         if (updated) {
//             writeCSV(patientFilePath, patientList);
//         }
    
//         // Update in User_List.csv
//         List<String[]> userList = readCSV(userFilePath);
//         for (String[] user : userList) {
//             if (user[0].equals(userID)) { // Assuming UserID is at index 0
//                 user[5] = newContact; // Assuming Contact Number is at index 5
//                 user[4] = newEmail;   // Assuming Email Address is at index 4
//                 break;
//             }
//         }
//         writeCSV(userFilePath, userList);
    
//         // Update in MedicalRecords.csv
//         List<String[]> medicalRecords = readCSV(medicalRecordsFilePath);
//         for (String[] record : medicalRecords) {
//             if (record[2].equals(userID)) { // Assuming UserID is at index 2
//                 record[6] = newContact; // Assuming Contact Number is at index 6
//                 record[7] = newEmail;   // Assuming Email Address is at index 7
//                 break;
//             }
//         }
//         writeCSV(medicalRecordsFilePath, medicalRecords);
    
//         return updated;
//         }    


//     // =========================== Doctor Management ===========================


//     public String[] getDoctorByUserID(String userID) throws IOException {
//         List<String[]> staffList = getStaffList(); // Fetch staff list
//         for (String[] staff : staffList) {
//             if (staff[1].equals(userID) && "Doctor".equalsIgnoreCase(staff[3])) { // Assuming Role is at index 3
//                 return staff;
//             }
//         }
//         return null; // Doctor not found
//     }

//     // Get all patients by Doctor ID (same as UserID for doctors)
//     public List<String[]> getPatientsByDoctorID(String doctorID) throws IOException {
//         List<String[]> allPatients = readCSV(patientFilePath); // Read the Patient_List.csv
//         List<String[]> filteredPatients = new ArrayList<>();

//         for (String[] patient : allPatients) {
//             if (patient[8].equals(doctorID)) { // Assuming DoctorID is at index 8
//                 filteredPatients.add(patient);
//             }
//         }
//         return filteredPatients;
//     }

//       // Get doctor name using Doctor ID
//      public String getDoctorName(String doctorID) throws IOException {
//         List<String[]> staffList = CSVReader.readCSV(staffFilePath);
//         String doctorName="";
        
//         for (String[] staff : staffList) {
//             if (staff[1].equals(doctorID)) {
//                 doctorName = staff[2]; // tally again in case any changes to StaffList.csv format
//                 return doctorName;
//             }
//         }
        
//         return "not found";
//     }
//     public List<String[]> getMedicalRecordsByDoctorID(String doctorID) throws IOException {
//         List<String[]> medicalRecords = readCSV(medicalRecordsFilePath); // Read the CSV file
//         List<String[]> doctorRecords = new ArrayList<>();
    
//         for (String[] record : medicalRecords) {
//             if (record.length > 10 && record[10].trim().equalsIgnoreCase(doctorID.trim())) { // Trim whitespace
//                 doctorRecords.add(record);
//             }
//         }
    
//         return doctorRecords;
//     }
    
//     public String getDoctorIDFromUserList(String userID) throws IOException {
//         List<String[]> userList = readCSV(userFilePath); // Read User_List.csv
    
//         for (String[] user : userList) {
//             if (user.length > 3) { // Ensure the row has enough columns
//                 // Check if the User ID matches and Role is Doctor
//                 if (user[0].equals(userID) && "Doctor".equalsIgnoreCase(user[3])) {
//                     return user[0].trim(); // Return User ID as Doctor ID
//                 }
//             }
//         }
    
//         return null; // Doctor ID not found
//     }
    
//     // =========================== Medical Records Management ===========================
//     public String[] getMedicalRecordByUserID(String userID) throws IOException {
//         List<String[]> patients = readCSV(medicalRecordsFilePath);
//         for (String[] patient : patients) {
//             if (patient[1].equals(userID)) { // Assuming UserID is at index 1
//                 return patient;
//             }
//         }
//         return null; // Patient not found
//     }

//     public void updateMedicalRecordList(List<String[]> updatedRecords) throws IOException {
//         // Use a BufferedWriter to overwrite the medical record CSV file with the updated list
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(medicalRecordsFilePath))) {
//             // Write each record from the updated list
//             for (String[] record : updatedRecords) {
//                 StringBuilder recordLine = new StringBuilder();

//                 // Convert each field in the record to a line of comma-separated values
//                 for (int i = 0; i < record.length; i++) {
//                     recordLine.append(record[i]);

//                     // Add a comma after each field except the last one
//                     if (i < record.length - 1) {
//                         recordLine.append(",");
//                     }
//                 }

//                 // Write the record line to the CSV file
//                 writer.write(recordLine.toString());
//                 writer.newLine();
//             }

//             System.out.println("Medical record list updated successfully.");
//         } catch (IOException e) {
//             throw new IOException("Error updating the medical record file: " + e.getMessage(), e);
//         }
//     }  

    
//     public List<String[]> getMedicalRecords() throws IOException {
//         return CSVReader.readCSV(medicalRecordsFilePath);
//     }

//     public void addMedicalRecord(String[] newRecord) throws IOException {
//         CSVWriter.appendToCSV(medicalRecordsFilePath, newRecord);
//     }

//     public void updateMedicalRecord(String patientID, String[] updatedRecord) throws IOException {
//         List<String[]> records = CSVReader.readCSV(medicalRecordsFilePath);
//         for (int i = 0; i < records.size(); i++) {
//             if (records.get(i)[0].equals(patientID)) { // Assuming column 0 is RecordID
//                 records.set(i, updatedRecord);
//                 break;
//             }
//         }
//         CSVWriter.writeCSV(medicalRecordsFilePath, records);
//     }

//     public void deleteMedicalRecord(String recordID) throws IOException {
//         List<String[]> records = CSVReader.readCSV(medicalRecordsFilePath);
//         records.removeIf(record -> record[0].equals(recordID));
//         CSVWriter.writeCSV(medicalRecordsFilePath, records);
//     }

    


//     // =========================== Appointment Management ===========================
//     public List<String[]> getAppointments() throws IOException {
//         List<String[]> records = new ArrayList<>();
//         try (BufferedReader br = new BufferedReader(new FileReader(appointmentsFilePath))) {
//             String line;
//             br.readLine(); // Skip header line
//             while ((line = br.readLine()) != null) {
//                 String[] fields = line.split(",");
//                 records.add(fields);
//             }
//         }
//         return records;
//     }

//     public void addAppointment(String[] newAppointment) throws IOException {
//         appendToCSV(appointmentsFilePath, newAppointment);
//     }

//     public void deleteAppointment(String appointmentID) throws IOException {
//         List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath);
//         appointments.removeIf(appointment -> appointment[0].equals(appointmentID));
//         CSVWriter.writeCSV(appointmentsFilePath, appointments);
//     }


//     public List<String[]> getAppointmentsByDoctor(String doctorID) throws IOException {
//         List<String[]> appointments = getAppointments();
//         List<String[]> filtered = new ArrayList<>();
//         for (String[] appointment : appointments) {
//             if (appointment[2].equals(doctorID)) { // Assuming DoctorID is at index 2
//                 filtered.add(appointment);
//             }
//         }
//         return filtered;
//     }

//     public List<String[]> getAppointmentsByPatient(String patientID) throws IOException {
//         List<String[]> appointments = getAppointments();
//         List<String[]> filtered = new ArrayList<>();
//         for (String[] appointment : appointments) {
//             if (appointment[1].equals(patientID)) { // Assuming PatientID is at index 1
//                 filtered.add(appointment);
//             }
//         }
//         return filtered;
//     }

//     public void updateAppointment(String appointmentID, String[] updatedAppointment) throws IOException {
//         List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath);
//         for (int i = 0; i < appointments.size(); i++) {
//             if (appointments.get(i)[0].equals(appointmentID)) { // Assuming AppointmentID is at index 0
//                 appointments.set(i, updatedAppointment);
//                 break;
//             }
//         }
//         writeCSV(appointmentsFilePath, appointments);
//     }


//       // for doctor to set available/unavailable on fresh appointments (status = null)
//       public void setAvailability(String appointmentID, String availabilityStatus) throws IOException {
//         // Read all appointments from the CSV file
//         List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath);

//         // Flag to check if appointment is found
//         boolean appointmentFound = false;

//         // Iterate through the appointments to find the one with the given appointmentID
//         for (String[] appointment : appointments) {
//             // Check if appointment ID matches and the status is null
//             if (appointment[0].equals(appointmentID)) {
//                 // Update the appointment status to the desired availability status
//                 appointment[5] = availabilityStatus;  // Column 5 is the Status
//                 appointmentFound = true;
//                 break;  // Exit loop once the appointment is updated
//             }
//         }

//         // If the appointment is found and updated, write the changes back to the CSV
//         if (appointmentFound) {
//             CSVWriter.writeCSV(appointmentsFilePath, appointments); // Write the updated list back to CSV
//             System.out.println("Appointment availability updated to " + availabilityStatus);
//         } else {
//             System.out.println("Appointment not found or already has a status.");
//         }
//     }


//     // for doctor to accept/decline appointments
// // for doctor to accept/decline appointments
// public void setAppointmentStatus(String appointmentID, String status) throws IOException {
//     // Read all appointments from the CSV file
//     List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath);

//     // Flag to check if appointment is found
//     boolean appointmentFound = false;

//     // Iterate through the appointments to find the one with the given appointmentID
//     for (String[] appointment : appointments) {
//         // Check if appointment ID matches and the status is "Pending"
//         if (appointment[0].equals(appointmentID)) {
//         //if (appointment[0].equals(appointmentID) && "Pending".equals(appointment[5])) {
//             // Update the appointment status to the desired status (e.g., Accepted or Declined)
//             appointment[5] = status;  // Column 5 is the Status
//             appointmentFound = true;
//             break;  // Exit loop once the appointment is updated
//         }
//     }
    
//     // If the appointment is found and updated, write the changes back to the CSV
//     if (appointmentFound) {
//         CSVWriter.writeCSV(appointmentsFilePath, appointments); // Write the updated list back to CSV
//         System.out.println("Appointment status updated to " + status);
//     } else {
//         System.out.println("Appointment not found or it doesn't have a Pending status.");
//     }

// }





//     // =========================== Medicine Management ===========================

//     public List<String[]> getMedicineList() throws IOException {
//         return readCSV(medicineFilePath);
//     }

//     // Fetch medications below the low-stock level
//     public List<String[]> getLowStockMedications() throws IOException {
//         List<String[]> medicines = getMedicineList();
//         List<String[]> lowStock = new ArrayList<>();

//         for (String[] medicine : medicines) {
//             int currentStock = Integer.parseInt(medicine[2]); // Index 2: Initial Stock
//             int lowStockAlert = Integer.parseInt(medicine[3]); // Index 3: Low Stock Level Alert

//             if (currentStock <= lowStockAlert) {
//                 lowStock.add(medicine);
//             }
//         }

//         return lowStock;
//     }

//     // update threshold only 
//     public void updateLowStockLevel(String medID, int newLowStockLevel) throws IOException {
//         List<String[]> medicines = new ArrayList<>();
    
//         // Read all records from the CSV
//         try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
//             String line;
    
//             // Read each line and modify if the Medicine ID matches
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 if (data[0].equalsIgnoreCase(medID)) { // Check if the Medicine ID matches
//                     data[3] = String.valueOf(newLowStockLevel); // Update the Low Stock Level Alert (Index 3)
//                 }
//                 medicines.add(data); // Add the line (modified or unmodified) to the list
//             }
//             System.out.println("Low Stock Level Alert updated");
//         }
    
//         // Write updated records back to the CSV
//         try (PrintWriter writer = new PrintWriter(new FileWriter(medicineFilePath))) {
//             for (String[] medData : medicines) {
//                 writer.println(String.join(",", medData));
//             }
//         }
    
//         System.out.println("Low Stock Level Alert updated for Medication ID: " + medID);
//     }
    
//     // adds new entry to the CSV 
//     public void addMedicine(String[] newMedicine) throws IOException {
//         CSVWriter.appendToCSV(medicineFilePath, newMedicine);
//     } 

//     // rewrites the entire CSV file with new data 
//     public void updateMedicineList(List<String[]> updatedMedicine) throws IOException {
//         CSVWriter.writeCSV(medicineFilePath, updatedMedicine);
//     } 

//     // only update the stock 
//     public void updateMedicationStock(String medID, int quantity) throws IOException {
//         List<String[]> medications = new ArrayList<>();
        
//         // Read all medications from the file
//         try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 medications.add(line.split(","));
//             }
//     } 
        
//         // Update the stock for the specified MedID
//         boolean updated = false;
//         for (String[] med : medications) {
//             if (med[0].equals(medID)) {
//                 int currentStock = Integer.parseInt(med[2].trim());
//                 med[2] = String.valueOf(currentStock + quantity); // Update the stock level
//                 updated = true;
//                 break;
//             }
//         }
        
//         if (!updated) {
//             System.out.println("Medication with ID " + medID + " not found.");
//             return;
//         }
        
//         // Write the updated list back to the file
//         try (BufferedWriter writer = new BufferedWriter(new FileWriter(medicineFilePath))) {
//             for (String[] med : medications) {
//                 writer.write(String.join(",", med));
//                 writer.newLine();
//             }
//         }
//         }

    
//     public String getMedicationField(String medID, String fieldName) throws IOException {
//         List<String[]> medicines = getMedicineList(); // Read all medicines from the CSV

//         for (String[] medData : medicines) {
//             if (medData[0].equalsIgnoreCase(medID)) { // Check if Medicine ID matches
//                 // Based on the requested field, return the specific data
//                 switch (fieldName.toLowerCase()) {
//                     case "medicine id":
//                         return medData[0]; // Medicine ID
//                     case "medicine name":
//                         return medData[1]; // Medicine Name
//                     case "initial stock":
//                         return medData[2]; // Initial Stock as String
//                     case "low stock level alert":
//                         return medData[3]; // Low Stock Level Alert as String
//                     case "dosage information":
//                         return medData[4]; // Dosage Information
//                     case "expiry date":
//                         return medData[5]; // Expiry Date as String
//                     default:
//                         throw new IllegalArgumentException("Invalid field name: " + fieldName);
//                 }
//             }
//         }
        
//         System.out.println("Medication with ID " + medID + " not found.");
//         return null; // Return null if no matching Medication is found
//     }

    

//     // Method to retrieve medication details by MedID
//     public String[] getMedicationDetails(String medID) throws IOException {
//         try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] fields = line.split(",");
//                 if (fields[0].trim().equals(medID)) {
//                     return fields; // Return the entire row as an array
//                 }
//             }
//         }
//         return null; // Return null if medication not found
//     }

//     public void removeStockLevel(String medID, int removeQuantity) throws IOException {
//         List<String[]> medicines = new ArrayList<>();
//         boolean found = false;
    
//         // Read all records from the CSV
//         try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
//             String line;
    
//             // Read and modify each record
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
    
//                 if (data[0].equalsIgnoreCase(medID)) { // Check if the Medicine ID matches
//                     int currentStock = Integer.parseInt(data[2]); // Current stock is at index 2
//                     if (currentStock >= removeQuantity) {
//                         data[2] = String.valueOf(currentStock - removeQuantity); // Deduct stock
//                         found = true;
//                     } else {
//                         throw new IllegalArgumentException("Insufficient stock to remove the specified quantity.");
//                     }
//                 }
//                 medicines.add(data); // Add modified or unmodified record to the list
//             }
//         }
    
//         // Check if the medication was found
//         if (!found) {
//             throw new IllegalArgumentException("Medication with ID " + medID + " not found.");
//         }
    
//         // Write updated records back to the CSV
//         try (PrintWriter writer = new PrintWriter(new FileWriter(medicineFilePath))) {
//             for (String[] medData : medicines) {
//                 writer.println(String.join(",", medData));
//             }
//         }
    
//         System.out.println("Stock level updated successfully for Medication ID: " + medID);
//     }

//     public void addStockLevels(String medID, int addQuantity) throws IOException {
//         List<String[]> medicines = new ArrayList<>();
//         boolean found = false;
    
//         // Read all records from the CSV
//         try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
//             String line;
    
//             // Read and modify each record
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
    
//                 if (data[0].equalsIgnoreCase(medID)) { // Check if the Medicine ID matches
//                     int currentStock = Integer.parseInt(data[2]); // Current stock is at index 2
//                     data[2] = String.valueOf(currentStock + addQuantity); // Add stock
//                     found = true;
//                 }
//                 medicines.add(data); // Add modified or unmodified record to the list
//             }
//         }
    
//         // Check if the medication was found
//         if (!found) {
//             throw new IllegalArgumentException("Medication with ID " + medID + " not found.");
//         }
    
//         // Write updated records back to the CSV
//         try (PrintWriter writer = new PrintWriter(new FileWriter(medicineFilePath))) {
//             for (String[] medData : medicines) {
//                 writer.println(String.join(",", medData));
//             }
//         }
    
//         System.out.println("Stock level updated successfully for Medication ID: " + medID);
//     }
    
//     public List<String[]> loadMedicationList() {
//         List<String[]> medications = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(medicineFilePath))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 medications.add(line.split(","));
//             }
//         } catch (IOException e) {
//             System.err.println("Error reading medication file: " + e.getMessage());
//         }
//         return medications;
//     }

    
//     // ===========================Appointment Outcome Record File ===========================
//     public List<String[]> getAllAppointmentOutcomeRecords() throws IOException {
//         List<String[]> records = new ArrayList<>();
//         try (BufferedReader br = new BufferedReader(new FileReader(appointmentOutcomeFilePath))) {
//             String line;
//             br.readLine(); // Skip header line
//             while ((line = br.readLine()) != null) {
//                 String[] fields = line.split(",");
//                 records.add(fields);
//             }
//         }
//         return records;
//     }

//     public List<String[]> getAppointmentOutcomeRecordsByAppointmentId(String appointmentId) throws IOException {
//         List<String[]> filteredRecords = new ArrayList<>();
//         List<String[]> allRecords = getAllAppointmentOutcomeRecords();

//         for (String[] record : allRecords) {
//             if (record[0].equalsIgnoreCase(appointmentId)) { // Assuming Appointment ID is the first column
//                 filteredRecords.add(record);
//             }
//         }
//         return filteredRecords;
//     }

//     public List<String[]> getAppointmentOutcomeRecordsByPatientId(String patientId) throws IOException {
//         List<String[]> filteredRecords = new ArrayList<>();
//         List<String[]> allRecords = getAllAppointmentOutcomeRecords();

//         for (String[] record : allRecords) {
//             if (record[1].equalsIgnoreCase(patientId)) { // Assuming Patient ID is the second column
//                 filteredRecords.add(record);
//             }
//         }
//         return filteredRecords;
//     }
//     public Map<String, String[]> mapAppointmentOutcomes() throws IOException {
//         List<String[]> outcomeRecords = getAllAppointmentOutcomeRecords();
//         Map<String, String[]> outcomeMap = new HashMap<>();

//         for (String[] outcome : outcomeRecords) {
//             String appointmentID = outcome[0]; // Assuming appointmentID is at index 0
//             outcomeMap.put(appointmentID, outcome);
//         }

//         return outcomeMap;
//     }

//     public void addToOutcome(String appointmentID) throws IOException {
//         List<String[]> appointmentsData = CSVReader.readCSV(appointmentsFilePath);
//         String[] appointmentData = null;

//         for (String[] row : appointmentsData) {
//             if (row[0].equals(appointmentID)) {
//                 appointmentData = row;
//                 break;
//             }
//         }

//         if (appointmentData == null) {
//             throw new IOException("Appointment ID not found.");
//         }

//         String patientID = appointmentData[1]; 
//         String doctorID = appointmentData[2];    
//         String appointmentDate = appointmentData[3]; 

//         String[] outcomeData = {
//             appointmentID,             // Appointment ID
//             patientID,                 // Patient ID
//             doctorID,                  // Doctor ID
//             appointmentDate,           // Date of appointment
//             "-",                       // Type of service
//             "-",                       // Prescription name 
//             "-",                       // Prescription status 
//             "-"                        // Consultation notes 
//         };

//         CSVWriter.appendToCSV(appointmentOutcomeFilePath, outcomeData);
//     }

//     public void removeFromOutcome(String appointmentID) throws IOException {
//         List<String[]> allOutcomes = CSVReader.readCSV(appointmentOutcomeFilePath);
//         List<String[]> updatedOutcomes = new ArrayList<>(); // create a new list to store those don't want to remove
    
//         // add rows to the new arraylist if != the cancelled appointment
//         for (String[] outcome : allOutcomes) {
//             if (!outcome[0].equals(appointmentID)) {
//                 updatedOutcomes.add(outcome);
//             }
//         }
    
//         CSVWriter.writeCSV(appointmentOutcomeFilePath, updatedOutcomes);
//         }
        
//         public void updateAppointmentOutcome(String appointmentID, String typeOfService, String prescriptionName, String consultationNotes) throws IOException {
//             List<String[]> outcomeRecords = CSVReader.readCSV(appointmentOutcomeFilePath);
//             boolean recordFound = false;
    
//             for (String[] outcome : outcomeRecords) {
//                 if (outcome[0].equals(appointmentID)) {
//                     outcome[4] = typeOfService;            // Type of service
//                     outcome[5] = prescriptionName;         // Prescription name
//                     outcome[6] = "Pending";                // Prescription status
//                     outcome[7] = consultationNotes;        // Consultation notes
//                     recordFound = true;
//                     break;
//                 }
//             }
//             if (!recordFound) {
//                 throw new IOException("Appointment ID not found in the outcome records.");
//             }
    
//             CSVWriter.writeCSV(appointmentOutcomeFilePath, outcomeRecords);
//         }
    
    
    
//     // ===========================Prescription management ==============================================

//     public void updatePrescriptionStatus(String appointmentID, String status) throws IOException {
//         List<String[]> records = new ArrayList<>();
//         boolean updated = false;
    
//         // Read all records from the CSV
//         try (BufferedReader reader = new BufferedReader(new FileReader(appointmentOutcomeFilePath))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] data = line.split(",");
//                 // Check if the record matches the specified appointment ID
//                 if (data[0].equals(appointmentID)) {
//                     if (data[6].equalsIgnoreCase("Dispensed") && status.equalsIgnoreCase("Dispensed")) {
//                         System.out.println("Prescription is already dispensed.");
//                     } else {
//                         data[6] = status; // Update status to the specified status
//                         updated = true;
//                     }
//                 }
//                 records.add(data);
//             }
//         }
    
//         if (!updated) {
//             System.out.println("No records found for the given appointment ID or no changes were made.");
//             return;
//         }
    
//         // Write updated records back to the CSV file
//         try (PrintWriter writer = new PrintWriter(new FileWriter(appointmentOutcomeFilePath))) {
//             for (String[] record : records) {
//                 writer.println(String.join(",", record));
//             }
//         }
    
//         System.out.println("Prescription status updated successfully.");
//     }
   
//     public void addPrescriptionToCSV(String patientID, String doctorID, String date, 
//                                  String medicationDetails, String instructions) throws IOException {
//     // Read the current prescriptions from the CSV
//         List<String[]> prescriptions = readCSV(prescriptionFilePath);

//         // Generate the next Prescription ID
//         String lastPrescriptionID = prescriptions.isEmpty() ? "RX000" : prescriptions.get(prescriptions.size() - 1)[0];
//         int nextID = Integer.parseInt(lastPrescriptionID.substring(2)) + 1;
//         String prescriptionID = String.format("RX%03d", nextID);

//         // Ensure instructions are quoted for consistent formatting
//         instructions = "\"" + instructions + "\"";

//         // Build the prescription record
//         String[] prescriptionRecord = {
//             prescriptionID,
//             patientID,
//             doctorID,
//             date,
//             medicationDetails,
//             instructions
//         };

//         // Append the new record to the CSV file
//         appendToCSV(prescriptionFilePath, prescriptionRecord);

//         System.out.println("Prescription added successfully with ID: " + prescriptionID);
// }

// public void dispensePrescription(String appointmentID) throws IOException {
//     boolean found = false;

//     // Read all appointment outcome records
//     List<String[]> outcomeRecords = readCSV(appointmentOutcomeFilePath);

//     for (String[] record : outcomeRecords) {
//         if (record.length < 7) continue; // Skip malformed rows

//         if (record[0].equalsIgnoreCase(appointmentID)) { // Match by Appointment ID

//             if (!"Pending".equalsIgnoreCase(record[6].trim())) { // Check status
//                 System.out.println("Prescription for Appointment ID " + appointmentID + " is already dispensed.");
//                 return;
//             }

//             // Update status to "Dispensed"
//             record[6] = "Dispensed";
//             found = true;
//             break;
//         }
//     }

//     if (!found) {
//         System.out.println("No pending prescription found for Appointment ID: " + appointmentID);
//         return;
//     }

//     // Write updated outcome records back to the file
//     writeCSV(appointmentOutcomeFilePath, outcomeRecords);

//     // Remove the prescription from the Prescription.csv based on the Appointment ID
//     List<String[]> prescriptionRecords = readCSV(prescriptionFilePath);

//     boolean prescriptionRemoved = prescriptionRecords.removeIf(record -> {
//         if (record.length < 2) return false; // Skip malformed rows
//         return record[1].equalsIgnoreCase(appointmentID); // Match by Appointment ID
//     });

//     if (!prescriptionRemoved) {
//         System.out.println("No prescription found in Prescription.csv for Appointment ID: " + appointmentID);
//         return;
//     }

//     // Write updated prescription records back to the file
//     writeCSV(prescriptionFilePath, prescriptionRecords);

//     System.out.println("Prescription for Appointment ID " + appointmentID + " dispensed and removed successfully.");
// }

// public List<String[]> getPendingPrescriptions() throws IOException {
//     List<String[]> prescriptions = new ArrayList<>();

//     // Read all records from the prescription file
//     List<String[]> records = readCSV(prescriptionFilePath);

//     for (String[] record : records) {
//         // Add all prescriptions (assuming prescriptions don't have statuses in this file)
//         prescriptions.add(record);
//     }

//     return prescriptions;
// }

// public boolean removePrescriptionByID(String prescriptionID) throws IOException {
//     List<String[]> prescriptions = readCSV(prescriptionFilePath);
//     boolean removed = prescriptions.removeIf(prescription -> prescription[0].equals(prescriptionID));
//     if (removed) {
//         writeCSV(prescriptionFilePath, prescriptions);
//     }
//     return removed;
// }
    

// public void updateAppointmentOutcomePrescriptionStatus(String prescriptionID, String newStatus) throws IOException {
//     List<String[]> records = readCSV(appointmentOutcomeFilePath);
//     for (String[] record : records) {
//         if (record[5].equals(prescriptionID)) { // Match Prescription Name
//             record[6] = newStatus; // Update Prescription Status
//             break;
//         }
//     }
//     writeCSV(appointmentOutcomeFilePath, records);
// }

// public String getPrescriptionNameByAppointmentID(String appointmentID) {
//     try {
//         // Read all appointment records to get the Patient ID from the Appointment ID
//         List<String[]> appointments = readCSV(appointmentsFilePath); // Read the appointments.csv
//         String patientID = null;
//         for (String[] record : appointments) {
//             if (record[0].equals(appointmentID)) { // Match the Appointment ID
//                 patientID = record[1]; // Patient ID is in the second column
//                 break;
//             }
//         }

//         if (patientID == null) {
//             return null; // Return null if no matching appointment ID is found
//         }

//         // Read all prescription records to get Medication Details using the Patient ID
//         List<String[]> prescriptions = readCSV(prescriptionFilePath); // Read the prescriptions.csv
//         for (String[] record : prescriptions) {
//             if (record[1].equals(patientID)) { // Match the Patient ID
//                 return record[4]; // Medication Details are in the fifth column
//             }
//         }
//     } catch (IOException e) {
//         System.err.println("Error retrieving prescription: " + e.getMessage());
//     }

//     return null; // Return null if no prescription is found for the given Appointment ID
// }


// //public void addAppointmentOutcomeRecordToCSV(
// //        String appointmentID,
// //        String patientID,
// //        String doctorID,
// //        String dateOfAppointment,
// //        String typeOfService,
// //        String prescriptionName,
// //        String prescriptionStatus,
// //        String consultationNotes
// //) throws IOException {
// //    // Read the current records from the CSV
// //    List<String[]> records = readCSV(appointmentOutcomeFilePath);
// //
// //    // Generate the new record
// //    String[] newRecord = {
// //        appointmentID,
// //        patientID,
// //        doctorID,
// //        dateOfAppointment,
// //        typeOfService,
// //        prescriptionName,
// //        prescriptionStatus,
// //        consultationNotes
// //    };
// //
// //    // Add the new record to the list
// //    records.add(newRecord);
// //
// //    // Write all records back to the CSV file
// //    try (PrintWriter writer = new PrintWriter(new FileWriter(appointmentOutcomeFilePath))) {
// //        for (String[] record : records) {
// //            writer.println(String.join(",", record));
// //        }
// //    }
// //
// //    System.out.println("Appointment outcome record added successfully.");
// //}


// public Appointment getAppointmentByID(String appointmentID) {
//     try {
//         List<String[]> appointments = readCSV(appointmentsFilePath); // Read the appointments.csv
//         for (String[] record : appointments) {
//             if (record[0].equals(appointmentID)) { // Match the Appointment ID
//                 // Retrieve Patient and Doctor objects using their IDs
//                 Patient patient = new Patient(record[1], this); // Assuming Patient constructor is compatible
//                 Doctor doctor = new Doctor(record[2], this);    // Assuming Doctor constructor is compatible

//                 // Parse the Date and Time
//                 Date date = new SimpleDateFormat("yyyy-MM-dd").parse(record[3]);
//                 String time = record[4];

//                 // Create a new Appointment object
//                 Appointment appointment = new Appointment(patient, doctor, date, time);
//                 appointment.setStatus(record[5]); // Set the status if it's different from the default

//                 return appointment; // Return the matching Appointment object
//             }
//         }
//     } catch (IOException | ParseException e) {
//         System.err.println("Error retrieving appointment: " + e.getMessage());
//     }
//     return null; // Return null if no match is found
// }




//     // ===========================Order Request management ==============================================

// // Method to add a replenishment request
// public void addReplenishmentRequest(String medID, String medicationName, int quantity) throws IOException {
//     // Retrieve all existing requests
//     List<String[]> existingRequests = getOrderRequests();

//     // Determine the next Request ID
//     int maxID = 0;
//     for (String[] request : existingRequests) {
//         try {
//             maxID = Math.max(maxID, Integer.parseInt(request[0])); // Parse numeric Request IDs
//         } catch (NumberFormatException e) {
//             // Skip non-numeric IDs
//         }
//     }

//     // Generate the new Request ID
//     String newRequestID = String.valueOf(maxID + 1);
//     String status = "Pending";

//     // Create the new request record
//     String[] newRequest = {newRequestID, medID, medicationName, String.valueOf(quantity), status};

//     // Append the new request to the CSV file
//     try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRequestFilePath, true))) {
//         writer.write(String.join(",", newRequest));
//         writer.newLine();
//         System.out.println("Replenishment request added successfully: " + String.join(", ", newRequest));
//     } catch (IOException e) {
//         System.err.println("Error writing to OrderRequest.csv: " + e.getMessage());
//     }
// }

// public String[] getReplenishmentRequestByID(String requestID) throws IOException {
// try (BufferedReader reader = new BufferedReader(new FileReader(orderRequestFilePath))) {
//     String line;
//     reader.readLine(); // Skip the header row
//     while ((line = reader.readLine()) != null) {
//         String[] request = line.split(",");
//         if (request[0].equals(requestID)) {
//             return request;
//         }
//     }
// }
// return null; // Return null if no matching request is found
// }

// public void updateReplenishmentRequestStatus(String requestID, String status) throws IOException {
// List<String[]> requests = new ArrayList<>();

// // Read all requests from the file
// try (BufferedReader reader = new BufferedReader(new FileReader(orderRequestFilePath))) {
//     String line;
//     while ((line = reader.readLine()) != null) {
//         String[] request = line.split(",");
//         if (request[0].equals(requestID)) {
//             request[4] = status; // Update the status
//         }
//         requests.add(request);
//     }
// }

// // Write the updated list back to the file
// try (BufferedWriter writer = new BufferedWriter(new FileWriter(orderRequestFilePath))) {
//     for (String[] request : requests) {
//         writer.write(String.join(",", request));
//        writer.newLine();
//         }
//     }
//     }

// // Method to retrieve all order requests from the CSV
//     public List<String[]> getOrderRequests() throws IOException {
//         List<String[]> requests = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(orderRequestFilePath))) {
//             String line;
//             // Skip the header row
//             reader.readLine();
//             while ((line = reader.readLine()) != null) {
//                 String[] request = line.split(",");
//                 requests.add(request);
//             }
//         }
//         return requests;
//     }

// public List<String[]> getPendingReplenishmentRequests() throws IOException {
//         List<String[]> pendingRequests = new ArrayList<>();
//         try (BufferedReader reader = new BufferedReader(new FileReader(orderRequestFilePath))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] fields = line.split(",");
//                 if (fields[4].trim().equalsIgnoreCase("Pending")) {
//                     pendingRequests.add(fields);
//                 }
//             }
//         }
//         return pendingRequests;
//     }
    


// }


