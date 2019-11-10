package GUI.Tab.User;

import Projet.Files;
import Projet.Main;
import Projet.Utilities;
import oo.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class that holds Edit User information
 */
public class Edit extends GUI.TabBase implements ActionListener {
    // GUI
    private JTextField _nameField, _idCardField, _emailField, _phoneNumberField, _addressField;
    private JPasswordField _passwordField;
    private JButton _updateButton;

    /**
     * Set the characteristics and position of elements in the window
     */
    @Override
    protected void SetElements() {
        // Name
        listComponents.add(new JLabel("Name"));
        _nameField = new JTextField(10);
        listComponents.add(_nameField);

        // ID Card
        listComponents.add(new JLabel("ID Card"));
        _idCardField = new JTextField(10);
        listComponents.add(_idCardField);

        // Email
        listComponents.add(new JLabel("Email address"));
        _emailField = new JTextField(10);
        listComponents.add(_emailField);

        // Phone number
        listComponents.add(new JLabel("Phone number"));
        _phoneNumberField = new JTextField(10);
        listComponents.add(_phoneNumberField);

        // Address
        listComponents.add(new JLabel("Address"));
        _addressField = new JTextField(10);
        listComponents.add(_addressField);

        // Password
        listComponents.add(new JLabel("Password"));
        _passwordField = new JPasswordField(10);
        listComponents.add(_passwordField);

        // Button
        listComponents.add(new JLabel());
        _updateButton = new JButton("Update");
        _updateButton.addActionListener(this);
        listComponents.add(_updateButton);
    }

    public Edit() {
        SetElements();

        DisplayElements(2);
    }

    /**
     * Load data into fields
     */
    public <T extends User> void Load() {
        T u = (T) Main.user;
        _nameField.setText(u.getName());
        _idCardField.setText(u.getIdCard());
        _emailField.setText(u.getEmail());
        _phoneNumberField.setText(u.getPhoneNumber());
        _addressField.setText(u.getAddress());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _updateButton) {
            if(checkValues(Main.user,_nameField.getText(),_addressField.getText(),_idCardField.getText(),_emailField.getText(), _phoneNumberField.getText(),this)) {
                updateValues();
            }
        }
    }

    /**
     * Update values of user
     */
    private <T extends User> void updateValues() {
        T u = (T) Main.user;

        u.setName(_nameField.getText());
        u.setIdCard(_idCardField.getText());
        u.setEmail(_emailField.getText().toLowerCase().toLowerCase());
        u.setPhoneNumber(_phoneNumberField.getText());
        u.setAddress(_addressField.getText());

        // Is the password going to change
        String password = String.valueOf(_passwordField.getPassword());
        if(!password.isEmpty()) {
            u.setPassword(password);
        }

        Files.writeUsers();
    }

    /**
     * Verify that values entered by the user are usable
     * @return boolean confirming validity of data
     */
    public static <T extends User> boolean checkValues(T u, String name, String address, String IDcard, String email, String phone, JPanel panel) {
        ArrayList listUser = null;

        // Selecting correct list of user
        switch (Main.user.getType()) {
            case "Manager": listUser = Main.managersList; break;
            case "Technician": listUser = Main.techniciansList; break;
            case "Customer": listUser = Main.customersList; break;
        }

        // Validity of name
        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Incorrect name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(address.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Incorrect address", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of IDCard
        //noinspection unchecked
        if(IDcard.isEmpty() || !(User.searchListIDCard(listUser, IDcard) == null || User.searchListIDCard(listUser, IDcard) == u)) {
            JOptionPane.showMessageDialog(panel, "Incorrect ID Card", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of email
        //noinspection unchecked
        if(email.isEmpty() || !(User.searchListMail(listUser, email.toLowerCase()) == null || User.searchListMail(listUser, email.toLowerCase()) == u) || !Utilities.isValidMail(email)) {
            JOptionPane.showMessageDialog(panel, "Incorrect email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of phone number
        if(phone.length() != 10) {
            JOptionPane.showMessageDialog(panel, "Incorrect phone number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(panel, "Information updated", "Information", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}
