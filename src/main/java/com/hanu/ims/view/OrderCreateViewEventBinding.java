package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class OrderCreateViewEventBinding {
    @FXML
    private Button resetBtn;

    @FXML
    private Button submitBtn;

    private OrderController controller;

    public void onSubmit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Submitted!");
        alert.show();
    }

    public void onReset(ActionEvent actionEvent) {

    }
}
