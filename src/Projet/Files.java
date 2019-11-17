package Projet;

import oo.*;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class that holds information on file access
 */
public class Files {
    // Path to files
    private static final String _customerFile = "users/customers.txt";
    private static final String _technicianFile = "users/technicians.txt";
    private static final String _managerFile = "users/managers.txt";
    private static final String _appointmentFile = "appointments/appointments.txt";

    /**
     * Jump lines during reading
     * @param s Scanner
     * @param l Integer number of lines to jump
     */
    private static void jumpLines(Scanner s, int l) {
        int nextBuffer = l;
        while (s.hasNext() && nextBuffer != 0) {
            s.nextLine();
            nextBuffer--;
        }
    }

    /**
     * Select file to open
     * @param type String of class ("Manager", "Technician", "Customer")
     * @return String of path of file
     */
    private static String fileToUse(String type) {
        switch (type) {
            case "Manager":
                return _managerFile;
            case "Technician":
                return _technicianFile;
            case "Customer":
                return _customerFile;
        }
        return null;
    }

    /**
     * Load users from file
     * @param type String Type of user to load ("Manager", "Technician", "Customer")
     * @param <T> Inherited of user
     * @return ArrayList of T
     */
    public static <T extends User> ArrayList<T> readUsers(String type) {
        ArrayList<T> allUser = new ArrayList<>();
        try {
            // Open correct file
            Scanner s = new Scanner(new File(fileToUse(type)));
            while (s.hasNext()) {
                // Reading values
                Integer userID = Integer.valueOf(s.nextLine());
                String emailFile = s.nextLine();
                String name = s.nextLine();
                String idCard = s.nextLine();
                String phone = s.nextLine();
                String address = s.nextLine();
                String password = s.nextLine();
                String dept = null;
                if (!type.equals("Customer")) {
                    dept = s.nextLine();
                }

                // Creating user
                T u = null;
                switch (type) {
                    case "Manager": {
                        u = (T) new Manager(name, idCard, emailFile, phone, address, password, dept, userID);
                        break;
                    }
                    case "Technician": {
                        u = (T) new Technician(name, idCard, emailFile, phone, address, password, dept, userID);
                        break;
                    }
                    case "Customer": {
                        u = (T) new Customer(name, idCard, emailFile, phone, address, password, userID);
                        break;
                    }
                }

                // Adding user
                allUser.add(u);
                jumpLines(s, 1);
            }
        } catch (Exception e) {
        }
        return allUser;

    }

    /**
     * Load appointments from file
     * @return ArrayList of Appointment loaded
     */
    public static ArrayList<Appointment> readAppointments() {
        ArrayList<Appointment> allAppointment = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File(_appointmentFile));
            while (s.hasNext()) {
                Integer idApp = Integer.valueOf(s.nextLine());
                LocalDateTime date = LocalDateTime.parse(s.nextLine());
                Integer idCustomer = Integer.valueOf(s.nextLine());
                Integer idTechnician = Integer.valueOf(s.nextLine());
                String type = s.nextLine();
                Double price = Double.valueOf(s.nextLine());
                String comment = s.nextLine();
                String feedback = s.nextLine();
                boolean payed = Boolean.parseBoolean(s.nextLine());
                boolean done = Boolean.parseBoolean(s.nextLine());

                Appointment a = new Appointment(idApp, idCustomer, idTechnician, type, price, payed, comment, feedback, date,done);
                allAppointment.add(a);
                jumpLines(s, 1);
            }
        } catch (Exception e) {
        }
        return allAppointment;
    }

    /**
     * Write all users to files
     */
    public static void writeUsers() {
        writeUsers(Main.customersList,"Customer");
        writeUsers(Main.techniciansList, "Technician");
        writeUsers(Main.managersList, "Manager");
    }

    /**
     * Write users into file
     * @param list ArrayList of user T
     * @param type String Type of user to load ("Manager", "Technician", "Customer")
     * @param <T> Inherited of user
     */
    private static <T extends User> void writeUsers(ArrayList<T> list, String type) {
        try {
            PrintWriter p = new PrintWriter(fileToUse(type));
            for (T u : list) {
                p.println(u.getUserID());
                p.println(u.getEmail());
                p.println(u.getName());
                p.println(u.getIdCard());
                p.println(u.getPhoneNumber());
                p.println(u.getAddress());
                p.println(u.getPassword());
                if(!type.equals("Customer")) {
                    p.println(((Staff) u).getDepartment());
                }
                p.println();
            }
            p.close();
        } catch (Exception ex) {
        }
    }

    /**
     * Write in file every appointment
     * @param list ArrayList<Appointment> List of appointment
     */
    public static void writeAppointments(ArrayList<Appointment> list) {
        try {
            PrintWriter p = new PrintWriter(_appointmentFile);
            for (Appointment a : list) {
                p.println(a.getAppointmentID());
                p.println(a.getDate());
                p.println(a.getCustomerID());
                p.println(a.getTechnicianID());
                p.println(a.getType());
                p.println(a.getPrice());
                p.println(a.getComment());
                p.println(a.getFeedback());
                p.println(a.isPayed());
                p.println(a.isDone());
                p.println();
            }
            p.close();
        } catch (Exception e) {
        }
    }
}
