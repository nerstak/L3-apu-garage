package GUI.Tab.Manager;

import Projet.Main;
import Projet.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pricing extends GUI.TabBase implements ActionListener {
    // GUI
    private JTextField _priceNormalField, _priceMajorField;
    private JButton _updateButton;

    public Pricing() {
        SetElements();

        DisplayElements(2);
    }

    @Override
    protected void SetElements() {
        // Normal Price
        listComponents.add(new JLabel("Price (normal rate)"));
        _priceNormalField = new JTextField(10);
        listComponents.add(_priceNormalField);

        // Major Price
        listComponents.add(new JLabel("Price (major rate)"));
        _priceMajorField = new JTextField(10);
        listComponents.add(_priceMajorField);

        // Button
        listComponents.add(new JLabel());
        _updateButton = new JButton("Update");
        _updateButton.addActionListener(this);
        listComponents.add(_updateButton);
    }

    /**
     * Load data into field
     */
    public void Load() {
        _priceNormalField.setText(Main.parameters.getPriceNormal().toString());
        _priceMajorField.setText(Main.parameters.getPriceMajor().toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == _updateButton) {
            if(checkValues()) {
                updateValues();
            }
        }
    }

    /**
     * Verify that the value entered by the user is correct
     * @return boolean of integrity
     */
    private boolean checkValues() {
        if(_priceNormalField.getText().isEmpty() || !Utilities.isNumeric(_priceNormalField.getText()) || Double.parseDouble(_priceNormalField.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Incorrect normal price", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(_priceMajorField.getText().isEmpty() || !Utilities.isNumeric(_priceMajorField.getText()) || Double.parseDouble(_priceMajorField.getText()) <= 0) {
            JOptionPane.showMessageDialog(this, "Incorrect major price", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        JOptionPane.showMessageDialog(this, "Prices changed", "Information", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    /**
     * Update prices
     */
    private void updateValues() {
        Main.parameters.setPriceNormal(Double.parseDouble(_priceNormalField.getText()));
        Main.parameters.setPriceMajor(Double.parseDouble(_priceMajorField.getText()));
        Main.managerWindow.dispose();
        Main.managerWindow.Load();
    }
}
