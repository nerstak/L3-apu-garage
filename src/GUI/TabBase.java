package GUI;

import oo.User;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class TabBase extends JPanel
{
    private GridBagConstraints gbc;
    protected ArrayList<JComponent> listComponents;

    public TabBase() {
        gbc = new GridBagConstraints();
        setLayout(new GridBagLayout());
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        setBackground(Color.LIGHT_GRAY);

        listComponents = new ArrayList<>();
    }

    /**
     * Virtual method to load data into different panels
     */
    public <T extends User> void Load() {
    }

    protected void SetElements(){}

    /**
     * Add every elements of listComponents to the JPanel on a grid
     * @param modulo int Number of columns
     */
    protected void DisplayElements(Integer modulo) {
        for(Integer i = 0; i < listComponents.size(); i++) {
            gbc.gridx = i % modulo;
            gbc.gridy = i / modulo;
            this.add(listComponents.get(i),gbc);
        }
    }
}
