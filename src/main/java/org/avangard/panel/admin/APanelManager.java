package org.avangard.panel.admin;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class APanelManager {
    @Getter
    private static List<AdminPanel> editSessions = new ArrayList<>();

    public static AdminPanel editor(long id) {
        for(AdminPanel adminPanel : editSessions) {
            if(adminPanel.getAdmin() == id) return adminPanel;
        }
        return null;
    }

    public static void quit(long id) {
        editSessions.removeIf(adminPanel -> adminPanel.getAdmin() == id);
    }
}
