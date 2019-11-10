package GUI.Window;

import GUI.TabBase;

import java.awt.event.KeyEvent;

public class Manager extends GUI.PanelBase {
    private final TabBase _editPanel;
    private final TabBase _newAppointmentPanel;
    private final TabBase _pricingPanel;
    @SuppressWarnings("FieldCanBeLocal")
    private final TabBase _newUserPanel;
    private final TabBase _paymentPanel;
    private final TabBase _userManagementPanel;
    private final TabBase _allAppointments;

    public Manager() {
        int i = 0;
        _allAppointments = new GUI.Tab.User.History();
        tabbedPane.addTab("All appointments",_allAppointments);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _newAppointmentPanel = new GUI.Tab.Manager.NewAppointment();
        tabbedPane.addTab("New appointment", _newAppointmentPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _pricingPanel = new GUI.Tab.Manager.Pricing();
        tabbedPane.addTab("Edit prices", _pricingPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _paymentPanel = new GUI.Tab.Manager.Payment();
        tabbedPane.addTab("Payment",_paymentPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _newUserPanel = new GUI.Tab.Manager.NewUser();
        tabbedPane.addTab("New account", _newUserPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _editPanel = new GUI.Tab.User.Edit();
        tabbedPane.addTab("Edit account",_editPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _userManagementPanel = new GUI.Tab.Manager.UserManagement();
        tabbedPane.addTab("Manage users",_userManagementPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        addLogOutButtonToTab();
    }

    public void Load() {
        _allAppointments.Load();
        _pricingPanel.Load();
        _newAppointmentPanel.Load();
        _editPanel.Load();
        _paymentPanel.Load();
        _userManagementPanel.Load();
        setFrame("Manager Panel");
    }
}
