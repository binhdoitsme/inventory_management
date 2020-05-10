package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class AdminDashboardView extends DashboardView {
    private static final String LAYOUT_FILE = "admin_dashboard.fxml";

    public AdminDashboardView() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(LAYOUT_FILE));
        Scene scene = new Scene(parent);
        setScene(scene);
    }
}
