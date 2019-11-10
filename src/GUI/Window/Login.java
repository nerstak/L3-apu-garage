package GUI.Window;

import Projet.Main;
import oo.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class that holds window information
 */
public class Login<T extends User> extends JFrame implements ActionListener {
    // GUI
    private JLabel loginLabel, passwordLabel;
    private JButton loginButton, quitButton;
    private JTextField mailField;
    private JPasswordField passwordField;
    private JComboBox userComboBox;
    private final String[] comboBoxItems = {"Customer", "Technician", "Manager"};

    /**
     * Set the characteristics and position of elements in the window
     */
    private void SetElements() {
        setTitle("Login Page");
        setSize(300, 200);
        setLocation(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        loginLabel = new JLabel("Email");
        passwordLabel = new JLabel("Password");

        mailField = new JTextField();
        mailField.setColumns(10);
        passwordField = new JPasswordField();
        passwordField.setColumns(10);

        userComboBox = new JComboBox(comboBoxItems);
        userComboBox.setEditable(false);

        loginButton = new JButton("Login");
        quitButton = new JButton("Quit");
        this.getRootPane().setDefaultButton(loginButton);
    }

    public Login() {
        SetElements();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        loginButton.addActionListener(this);
        quitButton.addActionListener(this);


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(loginLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(mailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(quitButton, gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        add(loginButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(userComboBox, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            T u;
            String mail = mailField.getText().toLowerCase();
            switch (String.valueOf(userComboBox.getSelectedItem())) {
                case ("Manager"): {
                    u = (T) User.searchListMail(Main.managersList,mail);
                    break;
                }
                case ("Technician"): {
                    u = (T) User.searchListMail(Main.techniciansList,mail);
                    break;
                }
                default:
                case ("Customer"): {
                    u = (T) User.searchListMail(Main.customersList,mail);
                    break;
                }
            }


            if (u != null) {
                if (u.checkPassword(String.valueOf(passwordField.getPassword()))) {
                    // Correct password
                    Main.user = u;
                    loadWindow(u);
                    setVisible(false);
                    dispose();
                } else {
                    // Incorrect password
                    JOptionPane.showMessageDialog(this, "Incorrect password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // No email corresponding
                JOptionPane.showMessageDialog(this, "No user corresponding to this email", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if(e.getSource() == quitButton) {
            dispose();
        }
    }

    private void loadWindow(T u) {
        switch (u.getType()) {
            case "Manager": Main.managerWindow.Load();
                break;
            case "Customer": Main.customerWindow.Load();
            break;
            case "Technician": Main.technicianWindow.Load();
                break;
        }
    }
}
