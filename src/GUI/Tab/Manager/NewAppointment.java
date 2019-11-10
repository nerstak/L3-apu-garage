package GUI.Tab.Manager;

import Projet.Files;
import Projet.Main;
import Projet.Utilities;
import oo.Appointment;
import oo.Customer;
import oo.Technician;
import oo.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static Projet.Utilities.listNbToString;

public class NewAppointment extends GUI.TabBase implements ActionListener {
    // GUI
    private JLabel _customerLabel, _technicianLabel, _typeLabel, _priceLabel, _dateLabel;
    private JTextField _customerField, _priceField;
    private JComboBox _technicianComboBox, _typeComboBox, _yearComboBox, _monthComboBox, _dayComboBox, _timeComboBox;
    private JButton _submitButton;
    private DefaultComboBoxModel _modelCombobox;

    // Local variables
    private final LocalDateTime _actualDate;
    private Customer _customer;
    private Technician _technician;
    private LocalDateTime _daySelected;
    private String[] _typeString;


    public NewAppointment() {
        _actualDate = LocalDateTime.now();
        SetElements();
        DisplayElements(2);
    }

    /**
     * Set the characteristics and position of elements in the window
     */
    @Override
    protected void SetElements() {
        // Selecting customer
        listComponents.add(new JLabel("Customer Email"));
        _customerField = new JTextField(10);
        listComponents.add(_customerField);

        // Selecting technician
        listComponents.add(new JLabel("Technician"));
        _technicianComboBox = new JComboBox(getNamesTechnicians(Main.techniciansList).toArray());
        listComponents.add(_technicianComboBox);

        // Selecting type of appointment;
        listComponents.add(new JLabel("Type of appointment"));
        _typeString = new String[]{"Normal: $" + Main.parameters.getPriceNormal(),
                "Major: $" + Main.parameters.getPriceMajor()};

        _modelCombobox = new DefaultComboBoxModel(_typeString);
        _typeComboBox = new JComboBox(_modelCombobox);
        listComponents.add(_typeComboBox);

        // Selecting price
        listComponents.add(new JLabel("Special price"));
        _priceField = new JTextField(10);
        listComponents.add(_priceField);

        // Selecting day and time
        listComponents.add(new JLabel("Date: Year"));
        _yearComboBox = new JComboBox(listNbToString(_actualDate.getYear(),_actualDate.getYear() + 5));
        listComponents.add(_yearComboBox);
        listComponents.add(new JLabel("Date: Month"));
        _monthComboBox = new JComboBox(listNbToString(1,12));
        listComponents.add(_monthComboBox);
        listComponents.add(new JLabel("Date: Day"));
        _dayComboBox = new JComboBox(listNbToString(1,31));
        listComponents.add(_dayComboBox);
        listComponents.add(new JLabel("Time"));
        _timeComboBox = new JComboBox(listNbToString(0,23));
        listComponents.add(_timeComboBox);
        listComponents.add(new JLabel());

        // Button
        _submitButton = new JButton("Add appointment");
        _submitButton.addActionListener(this);
        listComponents.add(_submitButton);

    }

    public void Load() {
        _typeString = new String[] {"Normal: $" + Main.parameters.getPriceNormal(),
                "Major: $" + Main.parameters.getPriceMajor()};
        _typeComboBox.setModel(new DefaultComboBoxModel(_typeString));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == _submitButton) {
            if(checkValues()) {
                addAppointment();
            }
        }
    }

    /**
     * Set an array of string, each consisting of IDCard, Name and department of Technicians
     * @param list ArrayList of Technician
     * @return ArrayList of String
     */
    private ArrayList<String> getNamesTechnicians(ArrayList<Technician> list) {
        ArrayList<String> names = new ArrayList();
        for (Technician t: list) {
            names.add(t.getIdCard() + " " + t.getName() + ": " + t.getDepartment());
        }
        return names;
    }

    /**
     * Verify that the value entered by the user are usable
     * @return boolean confirming validity of data
     */
    private boolean checkValues() {
        // Verification that the user given exists
        _customer = User.searchListMail(Main.customersList,_customerField.getText());
        if(_customer == null) {
            JOptionPane.showMessageDialog(this, "No user corresponding to this email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        _technician = Main.techniciansList.get(_technicianComboBox.getSelectedIndex());

        // Verification of date
        String year = _yearComboBox.getSelectedItem().toString();
        String month = Utilities.convertIntDateToString(_monthComboBox.getSelectedItem(),2);
        String day = Utilities.convertIntDateToString(_dayComboBox.getSelectedItem(),2);
        String time = Utilities.convertIntDateToString(_timeComboBox.getSelectedItem(),2);

        // Verify that date is possible
        if(!Utilities.verifyDate(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day))) {
            JOptionPane.showMessageDialog(this, "Incorrect date", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        _daySelected = LocalDateTime.parse(year+"-"+month+"-"+day+"T"+time+":00:00.0");

        // Verify that date is not anterior to actual day
        if(_daySelected.compareTo(_actualDate)<0) {
            JOptionPane.showMessageDialog(this, "Incorrect date", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Check that technician selected is available
        if(!_technician.checkAvailability(_daySelected,_typeComboBox.getSelectedItem().toString())) {
            JOptionPane.showMessageDialog(this, _technician.getName()+ " is not available", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(this, "Appointment added", "Information", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    /**
     * Add an appointment
     */
    private void addAppointment() {
        // Price selection
        Double price = Main.parameters.getPriceNormal();
        String type = "Normal";
        if(_typeComboBox.getSelectedIndex() == 1) {
            price = Main.parameters.getPriceMajor();
            type = "Major";
        }
        if(!_priceField.getText().isEmpty()) {
            try {
                price = Double.valueOf(_priceField.getText());
            } catch (Exception e) {}
        }

        price = Utilities.round(price,2);

        // Data gathering
        Integer appointmentID = Main.parameters.newAppointmentIndex();
        Integer customerID = _customer.getUserID();
        Integer technicianID = _technician.getUserID();

        // New appointment and writing in file
        Main.appointmentsList.add(new Appointment(appointmentID,customerID,technicianID,type,price,false," "," ",_daySelected, false));
        Files.writeAppointments(Main.appointmentsList);
        Main.managerWindow.dispose();
        Main.managerWindow.Load();
    }
}
