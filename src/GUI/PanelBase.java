package GUI;

import Projet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Abstract class that is the base of every window with tabs
 */
public abstract class PanelBase extends JPanel implements ActionListener {
//    private TabBase panel1;
    private JFrame _frame;
    protected JTabbedPane tabbedPane;
    private JButton _logoutButton;

    public PanelBase() {
        super(new GridLayout(1,1));
        tabbedPane = new JTabbedPane();
        //tabbedPane.setPreferredSize(new Dimension(600,800));
        add(tabbedPane);
    }

    /**
     * Virtual function to simplify inheritance
     */
    public void Load() {
    }

    /**
     * Display the window
     * @param nameWindow String name of the Window
     */
    protected void setFrame(String nameWindow) {
        _frame = new JFrame(nameWindow);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _frame.add(this,BorderLayout.WEST);

        // Display at correct dimension
        _frame.pack();
        _frame.setVisible(true);
        _frame.setResizable(false);
    }

    protected void addLogOutButtonToTab() {
        _logoutButton = new JButton("Logout");
        _logoutButton.addActionListener(this);
        JPanel tmp = makeTextPanel("Log out page");
        tabbedPane.addTab("",tmp);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, _logoutButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == _logoutButton) {
            Main.loadLogin();
        }
    }

    private JPanel makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }


    public void dispose() {
        if (_frame != null) {
            _frame.dispose();
        }
    }
}
