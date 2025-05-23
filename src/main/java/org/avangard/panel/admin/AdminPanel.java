package org.avangard.panel.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AdminPanel {
    private final long admin;

    @Setter
    private EditDirection editDirection = EditDirection.NOTHING;

    public AdminPanel(long admin) {
        this.admin = admin;
    }
}
