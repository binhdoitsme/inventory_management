package com.hanu.ims.view;

import com.hanu.ims.model.domain.Order;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class OrderDetailsView extends Stage {
    private static final String FXML_FILE_NAME = "order_details.fxml";

    public OrderDetailsView(Order order) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        OrderDetailsViewEventBinding orderDetailsViewEventBinding = new OrderDetailsViewEventBinding(order);
        loader.setController(orderDetailsViewEventBinding);

        Scene scene = new Scene(loader.load());
        setScene(scene);
    }
}
