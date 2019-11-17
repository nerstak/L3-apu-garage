package GUI.Tab.Manager;

import Projet.Files;
import Projet.Main;
import oo.Staff;
import oo.User;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class UserManagement<T extends User> extends GUI.TabBase implements ActionListener, ListSelectionListener, DocumentListener {
    private JTable _userTable;
    private DefaultTableModel _modelTable;

    private JTextField _searchField;
    private JButton _updateButton, _deleteButton;

    private ArrayList<T> _users, _searchList;
    private T _selectedUser;
    private int _selectedRow;

    public UserManagement() {
        SetElements();
        fillUserList();
        DisplayElements(1);
    }

    @Override
    protected void SetElements() {
        // Search field
        listComponents.add(new JLabel("Search by email address"));
        _searchField = new JTextField();
        _searchField.getDocument().addDocumentListener(this);
        listComponents.add(_searchField);

        // Creating scrollable table
        _modelTable = new DefaultTableModel();
        _userTable = new JTable(_modelTable);
        _userTable.getSelectionModel().addListSelectionListener(this);
        _userTable.getTableHeader().setReorderingAllowed(false);
        _userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        listComponents.add(new JScrollPane(_userTable));

        // Update button
        listComponents.add(new JLabel());
        _updateButton = new JButton("Update");
        _updateButton.addActionListener(this);
        listComponents.add(_updateButton);

        // Delete button
        listComponents.add(new JLabel());
        _deleteButton = new JButton("Delete");
        _deleteButton.addActionListener(this);
        listComponents.add(_deleteButton);

        setFieldsAvailability(false);
    }

    @Override
    public void Load() {
        fillUserList();
        loadTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == _updateButton) {
            if (_userTable.isEditing())
                _userTable.getCellEditor().stopCellEditing();
            if (checkValues()) {
                updateValues();
            }
        } else if (e.getSource() == _deleteButton) {
            // We remove the user from lists
            Main.managersList.remove(_selectedUser);
            Main.techniciansList.remove(_selectedUser);
            Main.customersList.remove(_selectedUser);
            // We write the changement
            Files.writeUsers();

            // We reload lists
            fillUserList();
            search();
            Load();

            setFieldsAvailability(false);
            JOptionPane.showMessageDialog(this, "User deleted", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        search();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        search();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        search();
    }

    /**
     * Update list of user according to query
     */
    private void search() {
        if (_searchField.getText().isEmpty()) {
            _searchList = _users;
        } else {
            _searchList = User.searchOccurrenceStringMail(_users,_searchField.getText());
        }
        loadTable();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!_userTable.getSelectionModel().isSelectionEmpty()) {
            // We get the line and the user
            _selectedRow = _userTable.getSelectionModel().getMinSelectionIndex();
            Integer IdUser = Integer.valueOf(String.valueOf(_userTable.getValueAt(_selectedRow, 0)));
            _selectedUser = User.searchListIDUser(_users, IdUser);

            setFieldsAvailability(true);
            if (_selectedUser.getUserID().equals(Main.user.getUserID())) {
                _deleteButton.setEnabled(false);
            }
        } else {
            setFieldsAvailability(false);
        }
    }


    /**
     * Change availability of button
     * @param b boolean
     */
    private void setFieldsAvailability(boolean b) {
        _deleteButton.setEnabled(b);
        _updateButton.setEnabled(b);
    }

    /**
     * Verify that the values given are correct
     * @return boolean of validity
     */
    private boolean checkValues() {
        // Department
        if (!_selectedUser.getType().equals("Customer") && String.valueOf(_userTable.getValueAt(_selectedRow, 7)).isBlank()) {
            JOptionPane.showMessageDialog(this, "Incorrect department", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Other values
        return GUI.Tab.User.Edit.checkValues(_selectedUser,
                String.valueOf(_userTable.getValueAt(_selectedRow, 3)), // Name
                String.valueOf(_userTable.getValueAt(_selectedRow, 6)), // Address
                String.valueOf(_userTable.getValueAt(_selectedRow, 1)), // ID card
                String.valueOf(_userTable.getValueAt(_selectedRow, 4)), // Email
                String.valueOf(_userTable.getValueAt(_selectedRow, 5)), // Phone
                this);
    }

    /**
     * Update data of user
     */
    private void updateValues() {
        // Name
        _selectedUser.setName(String.valueOf(_userTable.getValueAt(_selectedRow, 3)));
        // Address
        _selectedUser.setAddress(String.valueOf(_userTable.getValueAt(_selectedRow, 6)));
        // ID Card
        _selectedUser.setIdCard(String.valueOf(_userTable.getValueAt(_selectedRow, 1)));
        // Email
        _selectedUser.setEmail(String.valueOf(_userTable.getValueAt(_selectedRow, 4)).toLowerCase());
        // Phone
        _selectedUser.setPhoneNumber(String.valueOf(_userTable.getValueAt(_selectedRow, 5)));

        if (!_selectedUser.getType().equals("Customer")) {
            ((Staff) _selectedUser).setDepartment(String.valueOf(_userTable.getValueAt(_selectedRow, 7)));
        }

        Files.writeUsers();
    }

    /**
     * Fill the list of all users
     */
    private void fillUserList() {
        _users = new ArrayList<>();
        _users.addAll((ArrayList<T>) Main.techniciansList);
        _users.addAll((ArrayList<T>) Main.customersList);
        _users.addAll((ArrayList<T>) Main.managersList);
        _searchList = _users;
    }

    /**
     * Load the table and its information
     */
    private void loadTable() {
        // Creating table
        ArrayList<String[]> values = new ArrayList<>();
        ArrayList<String> columns = new ArrayList<>();

        Collections.sort(_searchList);

        // Header
        columns.add("ID"); // 0
        columns.add("ID card"); // 1
        columns.add("Role"); // 2
        columns.add("Name"); // 3
        columns.add("Mail"); // 4
        columns.add("Phone number"); // 5
        columns.add("Address"); // 6
        columns.add("Department"); // 7

        for (T u : _searchList) {
            String dept = "";
            if (!u.getType().equals("Customer")) {
                dept = ((Staff) u).getDepartment();
            }
            values.add(new String[]{
                    u.getUserID().toString(),
                    u.getIdCard(),
                    u.getType(),
                    u.getName(),
                    u.getEmail(),
                    u.getPhoneNumber(),
                    u.getAddress(),
                    dept
            });
        }

        _modelTable = new DefaultTableModel(values.toArray(new Object[][]{}), columns.toArray());
        _userTable.setModel(_modelTable);
    }
}
