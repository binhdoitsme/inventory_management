package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SalespersonDashboardView extends DashboardView {
    private static final String LAYOUT_FILE = "salesperson_dashboard.fxml";

    public SalespersonDashboardView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(LAYOUT_FILE));
            Scene scene = new Scene(parent);
            setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
