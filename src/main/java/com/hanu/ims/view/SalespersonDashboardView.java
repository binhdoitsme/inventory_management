package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SalespersonDashboardView extends DashboardView {
    private static final String LAYOUT_FILE = "salesperson_dashboard.fxml";

    public SalespersonDashboardView() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(LAYOUT_FILE));
        Scene scene = new Scene(parent);
        setScene(scene);
    }
}
