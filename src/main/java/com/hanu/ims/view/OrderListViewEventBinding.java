package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Date;

public class OrderListViewEventBinding {

    private static ObservableList<Order> dataSource;

    @FXML
    private TableView<Order> orderListTable;
    @FXML
    private TableColumn<Order, Integer> orderColId;
    @FXML
    private TableColumn<Order, String> orderColCashier;
    @FXML
    private TableColumn<Order, String> orderColTimestamp;
    @FXML
    private TableColumn<Order, Boolean> orderColCheckbox;
    @FXML
    private TableColumn<Order, Long> orderColTotalPrice;

    private static OrderController controller;

    public OrderListViewEventBinding() {
        controller = new OrderController();
    }

    public void initialize() {
        updateDataSource();
        dataSource.addListener((ListChangeListener<? super Order>) c -> {
            System.out.println("Changed!");
        });

        orderColId.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getId()).asObject());
        orderColCashier.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getCashierName()));
        orderColTimestamp.setCellValueFactory(param ->
                new SimpleStringProperty(new Date(param.getValue().getTimestamp()).toString()));
        orderColCheckbox.setCellFactory(CheckBoxTableCell.forTableColumn(orderColCheckbox));

        orderColTotalPrice.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue()
                        .getOrderLines().stream()
                        .map(OrderLine::getLineSum)
                        .reduce((x, y) -> x + y)
                        .get()).asObject());
        orderListTable.setItems(dataSource);
        orderListTable.setRowFactory(tv -> {
            TableRow<Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    Order clickedRow = row.getItem();
                    createOrderDetailsView(clickedRow);
                }
            });
            return row;
        });
    }

    public void createOrderDetailsView(Order order) {
        try {
            Stage detailsWindow = new OrderDetailsView(order);
            detailsWindow.initModality(Modality.WINDOW_MODAL);
            detailsWindow.initOwner(orderListTable.getScene().getWindow());
            detailsWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCreateOrderClicked(ActionEvent actionEvent) {
        createOrderCreateView();
    }

    private void createOrderCreateView() {
        try {
            Stage orderCreateWindow = new OrderCreateView();
            orderCreateWindow.initOwner(orderListTable.getScene().getWindow());
            orderCreateWindow.initModality(Modality.WINDOW_MODAL);
            orderCreateWindow.show();
            orderCreateWindow.setOnCloseRequest(event -> {
                // update data of this
                updateDataSource();
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    static void updateDataSource() {
        if (dataSource == null)
            dataSource = controller.getOrderList();
    }
}
