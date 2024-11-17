package AppointmentManagement;

/**
 * The Schedulable interface defines the contract for classes that manage
 * appointments. It provides methods for scheduling, rescheduling, viewing
 * available slots,
 * and canceling appointments.
 * 
 * <p>
 * Any class that implements this interface is expected to provide concrete
 * implementations
 * for all the methods defined here, thereby ensuring that appointment
 * management functionalities
 * are consistently available.
 * </p>
 */

public interface Schedulable {

    // Reschedules an existing appointment to a new time or date.
    void rescheduleAppointment();

    // Displays the available time slots for scheduling appointments.
    void viewAvailableSlots();

    // Cancels an existing appointment.
    void cancelAppointment();

    // Schedules a new appointment at an available time slot.
    void scheduleAppointment();
}
