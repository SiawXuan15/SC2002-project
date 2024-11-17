package DataManagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class appointmentManager {
    private final String appointmentsFilePath;

    public appointmentManager(String appointmentsFilePath) {
        this.appointmentsFilePath = appointmentsFilePath;
    }

    public String generateAppId() {
        return "AP" + UUID.randomUUID().toString();
    }

    public List<String[]> getAppointments() throws IOException {
        return CSVReader.readCSV(appointmentsFilePath); 
    }

    public void addAppointment(String[] newAppointment) throws IOException {
        CSVWriter.appendToCSV(appointmentsFilePath, newAppointment); 
    }

    public void deleteAppointment(String appointmentID) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); 
        appointments.removeIf(appointment -> appointment[0].equals(appointmentID));
        CSVWriter.writeCSV(appointmentsFilePath, appointments); 
    }

    public List<String[]> getAppointmentsByDoctor(String doctorID) throws IOException {
        List<String[]> appointments = getAppointments();
        List<String[]> filtered = new ArrayList<>();
        for (String[] appointment : appointments) {
            if (appointment[2].equals(doctorID)) { 
                filtered.add(appointment);
            }
        }
        return filtered;
    }

    public List<String[]> getAppointmentsByPatient(String patientID) throws IOException {
        List<String[]> appointments = getAppointments();
        List<String[]> filtered = new ArrayList<>();
        for (String[] appointment : appointments) {
            if (appointment[1].equals(patientID)) { 
                filtered.add(appointment);
            }
        }
        return filtered;
    }

    public void updateAppointment(String appointmentID, String[] updatedAppointment) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); // Static method access
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i)[0].equals(appointmentID)) { 
                appointments.set(i, updatedAppointment);
                break;
            }
        }
        CSVWriter.writeCSV(appointmentsFilePath, appointments); 
    }

    public void setAvailability(String appointmentID, String availabilityStatus) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); 
        boolean appointmentFound = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                appointment[5] = availabilityStatus;
                appointmentFound = true;
                break;
            }
        }

        if (appointmentFound) {
            CSVWriter.writeCSV(appointmentsFilePath, appointments); 
            System.out.println("Appointment availability updated to " + availabilityStatus);
        } else {
            System.out.println("Appointment not found or already has a status.");
        }
    }

    public void setAppointmentStatus(String appointmentID, String status) throws IOException {
        List<String[]> appointments = CSVReader.readCSV(appointmentsFilePath); 
        boolean appointmentFound = false;

        for (String[] appointment : appointments) {
            if (appointment[0].equals(appointmentID)) {
                appointment[5] = status; 
                appointmentFound = true;
                break;
            }
        }

        if (appointmentFound) {
            CSVWriter.writeCSV(appointmentsFilePath, appointments); 
            System.out.println("Appointment status updated to " + status);
        } else {
            System.out.println("Appointment not found or it doesn't have a Pending status.");
        }
    }
}
