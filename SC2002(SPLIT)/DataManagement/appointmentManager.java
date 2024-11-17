package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class appointmentManager {
    private final String appointmentsFilePath;

    // Constructor
    public appointmentManager(String appointmentsFilePath) {
        this.appointmentsFilePath = appointmentsFilePath;
    }

    // Generate random app id when doctor schedule follow-up appointments
    public String generateAppId() {
        /*Random rand = new Random();
        int rand_int1 = rand.nextInt(100000);
        return "A" + rand_int1; */
        return "A" + UUID.randomUUID().toString();
    }

    // Get all appointments
    public List<String[]> getAppointments() throws IOException {
        return CSVReader.readCSV(appointmentsFilePath); // Static method access
    }

    // Add a new appointment
    public void addAppointment(String[] newAppointment) throws IOException {
        CSVWriter.appendToCSV(appointmentsFilePath, newAppointment); // Static method access
    }

    // Delete an appointment by ID
    public void deleteAppointment(String appointmentID) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); // Static method access
        appointments.removeIf(appointment -> appointment[0].equals(appointmentID));
        CSVWriter.writeCSV(appointmentsFilePath, appointments); // Static method access
    }

    // Get appointments by doctor ID
    public List<String[]> getAppointmentsByDoctor(String doctorID) throws IOException {
        List<String[]> appointments = getAppointments();
        List<String[]> filtered = new ArrayList<>();
        for (String[] appointment : appointments) {
            if (appointment[2].equals(doctorID)) { // Assuming DoctorID is at index 2
                filtered.add(appointment);
            }
        }
        return filtered;
    }

    // Get appointments by patient ID
    public List<String[]> getAppointmentsByPatient(String patientID) throws IOException {
        List<String[]> appointments = getAppointments();
        List<String[]> filtered = new ArrayList<>();
        for (String[] appointment : appointments) {
            if (appointment[1].equals(patientID)) { // Assuming PatientID is at index 1
                filtered.add(appointment);
            }
        }
        return filtered;
    }

    // Update an appointment by ID
    public void updateAppointment(String appointmentID, String[] updatedAppointment) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); // Static method access
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i)[0].equals(appointmentID)) { // Assuming AppointmentID is at index 0
                appointments.set(i, updatedAppointment);
                break;
            }
        }
        CSVWriter.writeCSV(appointmentsFilePath, appointments); // Static method access
    }

    // Set availability for a fresh appointment (status = null)
    public void setAvailability(String appointmentID, String availabilityStatus) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); // Static method access
        boolean appointmentFound = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                appointment[5] = availabilityStatus; // Assuming Status is at index 5
                appointmentFound = true;
                break;
            }
        }

        if (appointmentFound) {
            CSVWriter.writeCSV(appointmentsFilePath, appointments); // Static method access
            System.out.println("Appointment availability updated to " + availabilityStatus);
        } else {
            System.out.println("Appointment not found or already has a status.");
        }
    }

    // Set appointment status to "Accepted" or "Declined"
    public void setAppointmentStatus(String appointmentID, String status) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); // Static method access
        boolean appointmentFound = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                appointment[5] = status; // Assuming Status is at index 5
                appointmentFound = true;
                break;
            }
        }

        if (appointmentFound) {
            CSVWriter.writeCSV(appointmentsFilePath, appointments); // Static method access
            System.out.println("Appointment status updated to " + status);
        } else {
            System.out.println("Appointment not found or it doesn't have a Pending status.");
        }
    }
}
