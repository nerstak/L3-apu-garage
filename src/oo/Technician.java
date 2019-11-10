package oo;

import Projet.Main;
import Projet.Utilities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Technician extends Staff {
    public Technician(String name, String idCard, String email, String phoneNumber, String address, String password, String department, Integer userID) {
        super(name, idCard, email, phoneNumber, address, password, userID, department);
        _type = "Technician";
    }

    /**
     * Check if a technician is available at a certain time
     * @param date        LocalDateTime of intervention
     * @param typeService String type of service ("Major", "Normal")
     * @return boolean of availability
     */
    public boolean checkAvailability(LocalDateTime date, String typeService) {
        ArrayList<Appointment> appointments = Appointment.getAppointmentTechnician(Main.appointmentsList, this, "planned");

        // Defining period to check
        LocalDateTime end = date.plus(Duration.ofHours(1));
        if (typeService.equals("Major")) {
            end = end.plus(Duration.ofHours(2));
        }

        if (!appointments.isEmpty()) {
            for (Appointment a : appointments) {
                // Defining period of appointment to compare
                LocalDateTime endA = a.getDate().plus(Duration.ofHours(1));
                if (a.getType().equals("Major")) {
                    endA = endA.plus(Duration.ofHours(2));
                }

                // Comparison
                if (Utilities.isOverlapping(date, end, a.getDate(), endA)) {
                    return false;
                }
            }
        }

        return true;
    }
}
