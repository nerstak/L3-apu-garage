package GUI.Tab.Manager;

import Projet.Files;
import Projet.Main;
import oo.Appointment;
import oo.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class Payment extends GUI.TabBase implements ActionListener, ListSelectionListener {
    private JTable _paymentTable;
    private DefaultTableModel _modelTable;
    private JLabel _priceLabel;
    private JTextField _priceField;
    private JButton _paymentButton;

    private ArrayList<Appointment> _listAppointment;
    private Appointment _selectedAppointment;

    public Payment() {
        SetElements();
        DisplayElements(1);
    }

    @Override
    protected void SetElements() {
        // Creating scrollable table
        _modelTable = new DefaultTableModel();
        // Non editable table
        _paymentTable = new JTable(_modelTable){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        _paymentTable.getSelectionModel().addListSelectionListener(this);
        _paymentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        _paymentTable.getTableHeader().setReorderingAllowed(false);
        listComponents.add(new JScrollPane(_paymentTable));

        // Price label
        _priceLabel = new JLabel("Cost the appointment");
        listComponents.add(_priceLabel);

        // Price field
        _priceField = new JTextField();
        _priceField.setEditable(false);
        listComponents.add(_priceField);

        // Button
        _paymentButton = new JButton("Mark as payed");
        _paymentButton.addActionListener(this);
        listComponents.add(_paymentButton);

        setFieldsAvailability(false);
    }

    public void Load() {
        // Creating table
        ArrayList<String[]> values = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        // Header
        columns.add("ID");
        columns.add("Type");
        columns.add("Date");
        columns.add("Time");
        columns.add("Customer");
        columns.add("Technician");
        columns.add("Price");

        _listAppointment = oo.Appointment.getAppointments(Main.appointmentsList, "nonPayed");
        _listAppointment = oo.Appointment.getAppointments(_listAppointment,"done");
        Collections.sort(_listAppointment);

        // Formatting appointments
        for (Appointment a : _listAppointment) {
            User t,c;
            t = User.searchListIDUser(Main.techniciansList,a.getTechnicianID());
            c = User.searchListIDUser(Main.customersList,a.getCustomerID());

            values.add(new String[] {
                    a.getAppointmentID().toString(), // Appointment ID
                    a.getType(), // Appointment Type
                    Main.parameters.getDateFormat().format(a.getDate()), // Date
                    a.getDate().getHour() + "H", // Time
                    c.getName(), // Name of customer
                    t.getName(), // Name of technician
                    a.getPrice().toString() + "$" // Price
            });
        }

        _modelTable = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
        _paymentTable.setModel(_modelTable);
    }

    /**
     * Set availability of fields
     * @param b boolean of availability
     */
    private void setFieldsAvailability(boolean b) {
        _paymentButton.setEnabled(b);
        _priceField.setEnabled(b);
        _priceLabel.setEnabled(b);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == _paymentButton) {
            _selectedAppointment.setPayed(true);
            _selectedAppointment.generateReceipt(Main.user.getUserID());
            Files.writeAppointments(Main.appointmentsList);
            Load();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!_paymentTable.getSelectionModel().isSelectionEmpty()) {
            // We get the line and the appointment
            int rowIndex = _paymentTable.getSelectionModel().getMinSelectionIndex();
            Integer idAppointment = Integer.valueOf(String.valueOf(_paymentTable.getValueAt(rowIndex, 0)));
            _selectedAppointment = Appointment.getAppointmentID(_listAppointment, idAppointment);

            setFieldsAvailability(true);
            _priceField.setText(_selectedAppointment.getPrice().toString());
            if(_selectedAppointment.isPayed()) {
                // You cannot set done twice an appointment
                _paymentButton.setEnabled(false);
            }
        } else {
            _priceField.setText("");
            setFieldsAvailability(false);
        }
    }
}
