package com.hanu.ims.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class SalespersonDashboardViewEventBinding {
    @FXML
    private AnchorPane container;

    public void onCreateOrderClicked(ActionEvent actionEvent) throws IOException {
        Stage createOrderView = new OrderCreateView();
        container.getScene().getWindow().hide();
        createOrderView.setOnCloseRequest(event -> {
            Stage salesDashboard = new SalespersonDashboardView();
            ((Window)event.getTarget()).hide();
            salesDashboard.show();
        });
        createOrderView.show();
    }

    public void onManageOrdersClicked(ActionEvent actionEvent) throws IOException {
        try {
            Stage createOrderView = new OrderListView();
            createOrderView.setOnCloseRequest(event -> {
                Stage salesDashboard = new SalespersonDashboardView();
                ((Window) event.getTarget()).hide();
                salesDashboard.show();
            });
            createOrderView.show();
            container.getScene().getWindow().hide();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(msg);
        alert.setTitle("Unexpected error");
        alert.setOnCloseRequest(event -> {
            Platform.exit();
        });
        alert.show();
    }

    public void onLogoutClicked(ActionEvent actionEvent) {
    }

    public void onExit(ActionEvent actionEvent) {
        showExitAlert();
    }

    private void showExitAlert() {
        String msg = "Goodbye!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(msg);
        alert.setHeaderText(msg);
        alert.setOnCloseRequest(event -> {
            container.getScene().getWindow().hide();
        });
        alert.show();
    }
}
