package com.hanu.ims.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderCreateView extends Stage {

    public OrderCreateView() throws IOException {
        Parent sceneRoot = FXMLLoader.load(OrderCreateView.class.getResource("order_create.fxml"));
        Scene scene = new Scene(sceneRoot);
        setScene(scene);
    }
}
