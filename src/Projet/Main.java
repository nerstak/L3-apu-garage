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

    public static void readData() {
        parameters = new Parameters();
        customersList = readUsers("Customer");
        techniciansList = readUsers("Technician");
        managersList = readUsers("Manager");
        appointmentsList = readAppointments();
    }

    public static void loadLogin() {
        disposeData();
        createWindows();
    }

    private static void disposeData() {
        managerWindow.dispose();
        technicianWindow.dispose();
        customerWindow.dispose();
        user = null;
    }

    private static void createWindows() {
        technicianWindow = new GUI.Window.CustomerTechnician();
        customerWindow = new GUI.Window.CustomerTechnician();
        managerWindow = new GUI.Window.Manager();
        loginWindow = new Login();
    }
}
