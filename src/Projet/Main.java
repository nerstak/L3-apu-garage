package Projet;

import GUI.Window.Login;
import oo.*;

import java.util.ArrayList;

import static Projet.Files.*;

public class Main {
    // Parameters
    public static Parameters parameters;

    // Windows
    public static Login loginWindow;
    public static GUI.Window.CustomerTechnician customerWindow;
    public static GUI.Window.CustomerTechnician technicianWindow;
    public static GUI.Window.Manager managerWindow;

    // Data
    public static ArrayList<Customer> customersList;
    public static ArrayList<Technician> techniciansList;
    public static ArrayList<Manager> managersList;
    public static ArrayList<Appointment> appointmentsList;

    public static User user;
    public static void main(String[] args) {
        readData();
        createWindows();
    }

    /**
     * Load every data
     */
    public static void readData() {
        parameters = new Parameters();
        customersList = readUsers("Customer");
        techniciansList = readUsers("Technician");
        managersList = readUsers("Manager");
        appointmentsList = readAppointments();
    }

    /**
     * Load the login window (and restart the others)
     */
    public static void loadLogin() {
        disposeWindows();
        createWindows();
    }

    /**
     * Remove windows
     */
    private static void disposeWindows() {
        managerWindow.dispose();
        technicianWindow.dispose();
        customerWindow.dispose();
        user = null;
    }

    /**
     * Create windows such as they should at first launch
     */
    private static void createWindows() {
        technicianWindow = new GUI.Window.CustomerTechnician();
        customerWindow = new GUI.Window.CustomerTechnician();
        managerWindow = new GUI.Window.Manager();
        loginWindow = new Login();
    }
}
