package AppointmentManagement;

import java.util.Date;

public class TimeSlot {
    private Date date;
    private String time;

    public TimeSlot(Date date, String time) {
        this.date = date;
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Time: " + time;
    }
    
}