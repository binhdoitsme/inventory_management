package com.hanu.ims.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView extends Stage {
    public LoginView() throws IOException {
        Parent sceneRoot = FXMLLoader.load(LoginView.class.getResource("login.fxml"));
        Scene scene = new Scene(sceneRoot);
        setScene(scene);
    }
}
