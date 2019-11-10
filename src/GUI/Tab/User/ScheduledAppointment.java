package GUI.Tab.User;

import Projet.Main;
import oo.Appointment;
import oo.Customer;
import oo.Technician;
import oo.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;

public class ScheduledAppointment extends GUI.TabBase {
    private JTable _futureTable;
    private DefaultTableModel _modelTable;

    @SuppressWarnings("FieldCanBeLocal")
    private ArrayList<Appointment> _listAppointment;
    
    public ScheduledAppointment() {
        SetElements();
        DisplayElements(1);
    }

    @Override
    protected void SetElements() {
        // Creating scrollable table
        _modelTable = new DefaultTableModel();
        // Non editable table
        _futureTable = new JTable(_modelTable){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        _futureTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        _futureTable.getTableHeader().setReorderingAllowed(false);
        listComponents.add(new JScrollPane(_futureTable));
    }

    public <T extends User> void Load() {
        // Creating table
        ArrayList<String[]> values = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        // Header
        columns.add("ID");
        columns.add("Type");
        columns.add("Date");
        columns.add("Time");
        if(Main.user.getType().equals("Technician")) {
            columns.add("Customer");
        } else {
            columns.add("Technician");
        }
        columns.add("Price");

        // List of past appointments of user
        if (Main.user.getType().equals("Technician")) {
            _listAppointment = oo.Appointment.getAppointmentTechnician(Main.appointmentsList, (Technician) Main.user,"planned");
        } else {
            _listAppointment = oo.Appointment.getAppointmentCustomer(Main.appointmentsList, (Customer) Main.user,"planned");
        }
        _listAppointment.sort(Collections.reverseOrder());

        // Formatting appointments
        for (Appointment a : _listAppointment) {
            T u;
            if(Main.user.getType().equals("Technician")) {
                u = (T) User.searchListIDUser(Main.customersList,a.getCustomerID());
            } else {
                u = (T) User.searchListIDUser(Main.techniciansList,a.getTechnicianID());
            }
            values.add(new String[] {
                    a.getAppointmentID().toString(), // Appointment ID
                    a.getType(), // Appointment Type
                    Main.parameters.getDateFormat().format(a.getDate()), // Date
                    a.getDate().getHour() + "H", // Time
                    u.getName(), // Name of other party involved
                    a.getPrice().toString() + "$" // Price
            });
        }

        _modelTable = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
        _futureTable.setModel(_modelTable);
    }
}
