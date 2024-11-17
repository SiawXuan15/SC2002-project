// package AppointmentManagement;

// import MedicalRecords.AppointmentOutcomeRecord;
// import UserManagement.Doctor;
// import UserManagement.Patient;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import java.util.UUID;


// public class Appointment {

//     private final String appointmentId;
//     private final Patient patient;
//     private final Doctor doctor;
//     private Date date;
//     private String time;
//     private String status;
//     private static final List<Appointment> appointmentList = new ArrayList<>();
//     private AppointmentOutcomeRecord outcomeRecord;

//     public Appointment(Patient patient, Doctor doctor, Date date, String time) {
//         this.appointmentId = generateAppId();
//         this.patient = patient;
//         this.doctor = doctor;
//         this.date = date;
//         this.time = time;
//         this.status = "Pending"; // Default status
//         this.outcomeRecord = null; // Default to no outcome record initially
//         appointmentList.add(this);
//     }

//     public void reschedule(Date newDate, String newTime) {
//         this.date = newDate;
//         this.time = newTime;
//         setStatus("Rescheduled");
//     }

//     public void cancel() {
//         setStatus("Canceled");
//     }

//     public boolean isConflictingWith(Appointment appointment) {
//         return this.date.equals(appointment.date) && this.time.equals(appointment.time) && this.doctor.equals(appointment.doctor);
//     }

//     public String getAppointmentId() {
//         return appointmentId;
//     }

//     public Patient getPatient() {
//         return patient;
//     }

//     public Doctor getDoctor() {
//         return doctor;
//     }

//     public Date getDate() {
//         return date;
//     }

//     public String getTime() {
//         return time;
//     }

//     public String getStatus() {
//         return status;
//     }

//     public static List<Appointment> getAppointmentList() {
//         return appointmentList;
//     }

//     public boolean completed() {
//         return "Completed".equalsIgnoreCase(this.status);
//     }

//     private String generateAppId() {
//         /*Random rand = new Random();
//         int rand_int1 = rand.nextInt(100000);
//         return "A" + rand_int1; */
//         return "A" + UUID.randomUUID().toString();
//     }

//     public AppointmentOutcomeRecord getOutcomeRecord() {
//         if (completed() && outcomeRecord != null) {
//             return outcomeRecord;
//         } else {
//             System.out.println("No outcome record available for appointment ID: " + appointmentId);
//             return null;
//         }
//     }

//     public void setOutcomeRecord(AppointmentOutcomeRecord outcomeRecord) {
//         if (completed()) {
//             this.outcomeRecord = outcomeRecord;
//         } else {
//             System.out.println("Cannot set outcome record unless appointment is completed.");
//         }
//     }

//     public void setStatus(String newStatus) {
//         List<String> validStatus = List.of("Pending", "Confirmed", "Completed", "Canceled", "Rescheduled");
//         if (validStatus.contains(newStatus)) {
//             this.status = newStatus;
//             System.out.println("Appointment status updated to: " + newStatus);
//             if ("Completed".equalsIgnoreCase(newStatus) && outcomeRecord == null) {
//                 System.out.println("Please add an outcome record for the completed appointment.");
//             }
//         } else {
//             System.out.println("Invalid status: " + newStatus);
//         }
//     }
// }
