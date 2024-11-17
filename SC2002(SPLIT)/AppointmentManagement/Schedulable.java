package AppointmentManagement;
import java.util.List;
public interface Schedulable {
     
    void rescheduleAppointment();
    List<TimeSlot> cancel(Appointment appointment); 
    void viewAvailableSlots();
}
