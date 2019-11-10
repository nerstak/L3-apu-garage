package GUI.Tab.User;

import Projet.Files;
import Projet.Main;
import oo.Appointment;
import oo.Customer;
import oo.Technician;
import oo.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class History extends GUI.TabBase implements ActionListener, ListSelectionListener {
    private JTable _historyTable;
    private DefaultTableModel _modelTable;
    private JLabel _commentLabel, _feedbackLabel;
    private JTextArea _commentTextArea, _feedbackTextArea;
    private JButton _submitButton, _doneButton;

    // Local variables
    private ArrayList<Appointment> _listAppointment;
    private Appointment _selectedAppointment;


    public History() {
        SetElements();
        DisplayElements(1);
    }

    @Override
    protected void SetElements() {
        // Creating scrollable table
        _modelTable = new DefaultTableModel();
        // Non editable table
        _historyTable = new JTable(_modelTable) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        _historyTable.getSelectionModel().addListSelectionListener(this);
        _historyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        _historyTable.getTableHeader().setReorderingAllowed(false);
        // GUI
        JScrollPane _tableScrollPane = new JScrollPane(_historyTable);
        listComponents.add(_tableScrollPane);

        // Done Button
        _doneButton = new JButton("Mark as done");
        _doneButton.setEnabled(false);
        _doneButton.addActionListener(this);
        listComponents.add(_doneButton);

        // Feedback
        _feedbackLabel = new JLabel("Feedback from technician:");
        listComponents.add(_feedbackLabel);

        _feedbackTextArea = new JTextArea(5, 10);
        listComponents.add(_feedbackTextArea);

        // Comment
        _commentLabel = new JLabel("Comment from customer:");
        listComponents.add(_commentLabel);
        _commentTextArea = new JTextArea(5, 10);
        listComponents.add(_commentTextArea);

        //Button
        _submitButton = new JButton("Submit");
        _submitButton.addActionListener(this);
        listComponents.add(_submitButton);

        setFieldsAvailability(false);
    }

    public void Load() {
        // Creating table
        ArrayList<String[]> values = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        // Header
        // We set the name and number of columns
        columns.add("ID");
        columns.add("Type");
        columns.add("Date");
        columns.add("Time");
        switch (Main.user.getType()) {
            case "Technician":
                // Text and column of Technician
                columns.add("Customer");
                _feedbackLabel.setText("Your feedback");
                break;
            case "Customer":
                // Text and column of Customer
                _commentLabel.setText("Your comment");
                columns.add("Technician");
                break;
            case "Manager":
                // Columns of Manager
                columns.add("Customer");
                columns.add("Technician");
                break;
        }
        columns.add("Price");
        columns.add("Status");
        columns.add("Receipt");

        // List of past appointments of the user (or all appointments for Manager)
        switch (Main.user.getType()) {
            case "Technician":
                _listAppointment = Appointment.getAppointmentTechnician(Main.appointmentsList, (Technician) Main.user, "history");
                break;
            case "Customer":
                _listAppointment = Appointment.getAppointmentCustomer(Main.appointmentsList, (Customer) Main.user, "history");
                break;
            case "Manager":
                _listAppointment = Main.appointmentsList;
                break;
        }
        _listAppointment.sort(Collections.reverseOrder());

        // We put the values in the table
        if (Main.user.getType().equals("Manager")) {
            fillTableManager(values);
        } else {
            fillTableCustomerTechnician(values);
        }

        _modelTable = new DefaultTableModel(values.toArray(new Object[][]{}), columns.toArray());
        _historyTable.setModel(_modelTable);

        // If the user is not a member of the staff, he cannot set the appointment as Done
        if (Main.user.getType().equals("Customer")) {
            _doneButton.setVisible(false);
        }
        // Manager cannot submit remarks
        if(Main.user.getType().equals("Manager")) {
            _submitButton.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _submitButton) {
            // Submit button for comment and feedback
            if (checkValues()) {
                updateValues();
                displayFields();
            }
        }
        if (e.getSource() == _doneButton) {
            // Done button to set appointment as done
            _selectedAppointment.setDone(true);
            _doneButton.setEnabled(false);


            Files.writeAppointments(Main.appointmentsList);
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        // If a line is selected
        if (!_historyTable.getSelectionModel().isSelectionEmpty()) {
            // We get the line and the appointment
            int rowIndex = _historyTable.getSelectionModel().getMinSelectionIndex();
            Integer idAppointment = Integer.valueOf(String.valueOf(_historyTable.getValueAt(rowIndex, 0)));
            _selectedAppointment = Appointment.getAppointmentID(_listAppointment, idAppointment);

            // We display the fields
            displayFields();
        } else {
            // We set the fields as non available
            setFieldsAvailability(false);
            _doneButton.setEnabled(false);
            _commentTextArea.setText("");
            _feedbackTextArea.setText("");
            JOptionPane.showMessageDialog(this, "Set appointment done", "Information", JOptionPane.INFORMATION_MESSAGE);
            _selectedAppointment = null;
        }
    }

    /**
     * Set availability of fields
     * @param b boolean of availability
     */
    private void setFieldsAvailability(boolean b) {
        _feedbackLabel.setEnabled(b);
        _feedbackTextArea.setEnabled(b);
        _commentLabel.setEnabled(b);
        _commentTextArea.setEnabled(b);
        _submitButton.setEnabled(b);
    }

    /**
     * Check that values submitted are correct
     * @return boolean of validity
     */
    private boolean checkValues() {
        // Checking if comment is not null
        if (_commentTextArea.getText().isBlank() && Main.user.getType().equals("Customer")) {
            JOptionPane.showMessageDialog(this, "Empty comment", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Checking if feedback is not null
        if (_feedbackTextArea.getText().isBlank() && Main.user.getType().equals("Technician")) {
            JOptionPane.showMessageDialog(this, "Empty feedback", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Checking if there is an appointment selected
        if (_selectedAppointment == null) {
            JOptionPane.showMessageDialog(this, "No appointment selected", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(this, "Comment added", "Information", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    /**
     * Update feedback or comment
     */
    private void updateValues() {
        _selectedAppointment.setComment(_commentTextArea.getText());
        _selectedAppointment.setFeedback(_feedbackTextArea.getText());
        Files.writeAppointments(Main.appointmentsList);
    }

    /**
     * Display fields as needed
     */
    private void displayFields() {
        setFieldsAvailability(true);
        if (!Main.user.getType().equals("Customer")) {
            // Done button
            if (!_selectedAppointment.isDone()) {
                _doneButton.setEnabled(true);
            } else {
                _doneButton.setEnabled(false);
            }


            // Fields for feedback of technician
            if (_selectedAppointment.getFeedback().isBlank()) {
                // No feedback
                _feedbackTextArea.setText("");
                if(Main.user.getType().equals("Technician")) {
                    _feedbackTextArea.setEditable(true);
                }
            } else {
                // Feedback already present
                _feedbackTextArea.setText(_selectedAppointment.getFeedback());
                _feedbackTextArea.setEditable(false);
                _submitButton.setEnabled(false);
            }

            // Comment area
            _commentTextArea.setText(_selectedAppointment.getComment());
            _commentTextArea.setEditable(false);
        } else {
            // Fields for comment of customer
            if (_selectedAppointment.getComment().isBlank()) {
                // No comment
                _commentTextArea.setText("");
                _commentTextArea.setEditable(true);
            } else {
                // Comment already present
                _commentTextArea.setText(_selectedAppointment.getComment());
                _commentTextArea.setEditable(false);
                _submitButton.setEnabled(false);
            }

            // Feedback area
            _feedbackTextArea.setText(_selectedAppointment.getFeedback());
            _feedbackTextArea.setEditable(false);
        }
    }

    /**
     * Fill a table for a customer or a technician
     * @param values ArrayList of String[] that will be filled
     */
    private <T extends User> void fillTableCustomerTechnician(ArrayList<String[]> values) {
        if (Main.user.getType().equals("Customer") || Main.user.getType().equals("Technician")) {
            // Formatting appointments
            for (Appointment a : _listAppointment) {
                T u;
                if (Main.user.getType().equals("Technician")) {
                    u = (T) User.searchListIDUser(Main.customersList, a.getCustomerID());
                } else {
                    u = (T) User.searchListIDUser(Main.techniciansList, a.getTechnicianID());
                }
                // Status and payed
                String status = "Pending";
                if (a.isDone()) status = "Done";
                String payed = "Not payed";
                if(a.isPayed()) payed = "Payed";

                values.add(new String[]{
                        a.getAppointmentID().toString(), // Appointment ID
                        a.getType(), // Appointment Type
                        Main.parameters.getDateFormat().format(a.getDate()), // Date
                        a.getDate().getHour() + "H", // Time
                        u.getName(), // Name of other party involved
                        a.getPrice().toString() + "$", // Price
                        status, // Status
                        payed // Payed
                });
            }
        }
    }

    private void fillTableManager(ArrayList<String[]> values) {
        // Formatting appointments
        for (Appointment a : _listAppointment) {
            Customer c = User.searchListIDUser(Main.customersList, a.getCustomerID());
            Technician t = User.searchListIDUser(Main.techniciansList, a.getTechnicianID());

            // Status and payed
            String status = "Pending";
            if (a.isDone()) status = "Done";
            String payed = "Not payed";
            if(a.isPayed()) payed = "Payed";

            values.add(new String[]{
                    a.getAppointmentID().toString(), // Appointment ID
                    a.getType(), // Appointment Type
                    Main.parameters.getDateFormat().format(a.getDate()), // Date
                    a.getDate().getHour() + "H", // Time
                    c.getName(), // Name of customer
                    t.getName(), // Name of technician
                    a.getPrice().toString() + "$", // Price
                    status, // Status
                    payed // Payed
            });
        }
    }
}
