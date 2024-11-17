// package MedicalRecords;

// import UserManagement.Patient;
// import UserManagement.Doctor;
// import PharmacyManagement.Medication;
// import java.util.List;

// public class Prescription {

// 	private final String prescriptionID;
// 	private final Patient patient;
// 	private final Doctor doctor;
// 	private final List<Medication> medication;
// 	private final String dosage;
// 	private String status;

// 	public Prescription(String prescriptionID, Patient patient, Doctor doctor, List<Medication> medications, String dosage, String status) {
//         this.prescriptionID = prescriptionID;
//         this.patient = patient;
//         this.doctor = doctor;
//         this.medication = medications;
//         this.dosage = dosage;
//         this.status = status;
//     }

//     public String getPrescriptionID() {
//         return prescriptionID;
//     }

//     public Patient getPatient() {
//         return patient;
//     }

//     public Doctor getDoctor() {
//         return doctor;
//     }

//     public List<Medication> getMedications() {
//         return medication;
//     }

//     public String getDosage() {
//         return dosage;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public void updateStatus(String newStatus) {
//         this.status = newStatus;
//     }

//     // Method to get a detailed description of the prescription
//     public String getPrescriptionDetails() {
//         StringBuilder details = new StringBuilder();
//         details.append("Prescription ID: ").append(prescriptionID).append("\n");
//         details.append("Patient: ").append(patient.getName()).append("\n");
//         details.append("Doctor: ").append(doctor.getName()).append("\n");
//         details.append("Medications: \n");
//         for (Medication med : medication) {
//             details.append("- ").append(med.getMedicationName()).append("\n");
//         }
//         details.append("Dosage: ").append(dosage).append("\n");
//         details.append("Status: ").append(status).append("\n");
//         return details.toString();
//     }

// }