package com.hanu.ims.view;

import com.hanu.ims.util.configuration.Configuration;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderListView extends Stage {
    private static final String FXML_FILE_NAME = "order_list.fxml";
    private static final String WINDOW_TITLE = Configuration.get("window.title.order.manage");

    public OrderListView() throws IOException {
        Parent rootElement = FXMLLoader.load(getClass().getResource(FXML_FILE_NAME));
        Scene rootScene = new Scene(rootElement);
        setScene(rootScene);
        setTitle(WINDOW_TITLE);
    }
}
