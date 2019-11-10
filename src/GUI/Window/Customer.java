package GUI.Window;

import GUI.Tab.User.ScheduledAppointment;
import GUI.TabBase;

import java.awt.event.KeyEvent;

/**
 * Class that holds information of Customer Window
 */
public class Customer extends GUI.PanelBase {
    private final TabBase _editPanel;
    private final TabBase _historyPanel;
    private final TabBase _schedulePanel;
    public Customer() {
        int i = 0;
        _schedulePanel = new ScheduledAppointment();
        tabbedPane.addTab("Scheduled appointment",_schedulePanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        _historyPanel = new GUI.Tab.User.History();
        tabbedPane.addTab("History",_historyPanel);
        tabbedPane.setMnemonicAt(i++,KeyEvent.VK_1);

        _editPanel = new GUI.Tab.User.Edit();
        tabbedPane.addTab("Edit account",_editPanel);
        tabbedPane.setMnemonicAt(i++, KeyEvent.VK_1);

        addLogOutButtonToTab();
    }

    /**
     * Load elements and tabs
     */
    public void Load() {
       _editPanel.Load();
       _historyPanel.Load();
       _schedulePanel.Load();
       setFrame("Customer Panel");
    }
}
