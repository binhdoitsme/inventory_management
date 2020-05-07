package com.hanu.ims.view;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class CreateAccountPopup {


    public void display() throws Exception {
        Stage popupWindow = new Stage();

        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("newaccountpopup.fxml"));
        Parent firstPane = popupLoader.load();
        Scene firstScene = new Scene(firstPane, 400, 800);

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("This is a pop up window");

        popupWindow.setScene(firstScene);

        popupWindow.showAndWait();

    }

}