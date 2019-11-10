package GUI.Tab.Manager;

import Projet.Main;
import Projet.Utilities;
import oo.Customer;
import oo.Manager;
import oo.Technician;
import oo.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NewUser extends GUI.TabBase implements ActionListener {
    // GUI
    private JTextField _nameField, _idCardField, _emailField, _phoneNumberField, _addressField, _departmentField;
    private JPasswordField _passwordField;
    private JComboBox _userComboBox;
    private JLabel _departmentLabel;
    private JButton _createButton;

    public NewUser() {
        SetElements();
        DisplayElements(2);
    }

    /**
     * Set the characteristics and position of elements in the window
     */
    @Override
    protected void SetElements() {
        // User type
        listComponents.add(new JLabel("Type"));
        String[] users = {"Customer", "Technician", "Manager"};
        _userComboBox = new JComboBox(users);
        _userComboBox.addActionListener(this);
        listComponents.add(_userComboBox);

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

        // Department
        _departmentLabel = new JLabel("Department");
        _departmentLabel.setVisible(false);
        listComponents.add(_departmentLabel);
        _departmentField = new JTextField();
        _departmentField.setVisible(false);
        listComponents.add(_departmentField);

        // Button
        listComponents.add(new JLabel());
        _createButton = new JButton("Create");
        _createButton.addActionListener(this);
        listComponents.add(_createButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _userComboBox) {
            if (!_userComboBox.getSelectedItem().toString().equals("Customer")) {
                _departmentField.setVisible(true);
                _departmentLabel.setVisible(true);
            } else {
                _departmentField.setVisible(false);
                _departmentLabel.setVisible(false);
            }
        }

        if (e.getSource() == _createButton) {
            if(checkValues()) {
                addUser();
            }
        }
    }

    /**
     * Verify that values entered by the user are usable
     *
     * @return boolean confirming validity of data
     */
    private boolean checkValues() {
        ArrayList listUser = null;

        // Selecting correct list of user
        switch (_userComboBox.getSelectedItem().toString()) {
            case "Manager":
                listUser = Main.managersList;
                break;
            case "Technician":
                listUser = Main.techniciansList;
                break;
            case "Customer":
                listUser = Main.customersList;
                break;
        }

        // Validity of name
        if (_nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Incorrect name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (_addressField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Incorrect address", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of IDCard
        if (_idCardField.getText().isEmpty() || !(User.searchListIDCard(listUser, _idCardField.getText()) == null)) {
            JOptionPane.showMessageDialog(this, "Incorrect ID Card", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of email
        if (_emailField.getText().isEmpty() || !(User.searchListMail(listUser, _emailField.getText()) == null) || !Utilities.isValidMail(_emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Incorrect email", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of phone number
        if (_phoneNumberField.getText().isEmpty() || _phoneNumberField.getText().length() != 10) {
            JOptionPane.showMessageDialog(this, "Incorrect phone number", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of department
        if (!_userComboBox.getSelectedItem().toString().equals("Customer") && _departmentField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Incorrect department", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validity of password
        if (_passwordField.getPassword().toString().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(this, "User created", "Information", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    /**
     * Add an user
     */
    private void addUser() {
        Integer userID = Main.parameters.newUserIndex();
        String name = _nameField.getText();
        String cardID = _idCardField.getText();
        String email = _emailField.getText();
        String phoneNumber = _phoneNumberField.getText();
        String address = _addressField.getText();
        String password = new String(_passwordField.getPassword());
        String dept = _departmentField.getText();

        // Selecting correct list of user
        switch (_userComboBox.getSelectedItem().toString()) {
            case "Manager": {
                Main.managersList.add(new Manager(name, cardID, email, phoneNumber, address, password, dept, userID));
                break;
            }
            case "Technician": {
                Main.techniciansList.add(new Technician(name, cardID, email, phoneNumber, address, password, dept,userID));
                break;}
            case "Customer":{
                Main.customersList.add(new Customer(name, cardID, email, phoneNumber, address, password, userID));
                break;
            }
        }

        Projet.Files.writeUsers();
    }
}
