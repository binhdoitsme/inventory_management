package com.hanu.ims.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HomepageView {

    private Scene loginPage;

    @FXML
    public void setLoginPage(Scene scene) {
        loginPage = scene;
    }

    @FXML
    private void toLoginPage(ActionEvent event) {
        //label.setText("Hello World!");
        System.out.println("Switch to login page");
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(loginPage);
    }
}