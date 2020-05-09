package com.hanu.ims.view;

import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.util.Date;
import java.util.List;

public class OrderDetailsViewEventBinding {

    private ObservableValue<Order> observableOrder;

    private final Order initialState;
    private ObservableList<OrderLine> orderLines;

    @FXML
    private Label orderId;
    @FXML
    private Label cashierName;
    @FXML
    private Label timestamp;
    @FXML
    private TableView<OrderLine> orderLinesTable;
    @FXML
    private TableColumn<OrderLine, String> orderLineSku;
    @FXML
    private TableColumn<OrderLine, String> orderLineProductName;
    @FXML
    private TableColumn<OrderLine, Long> orderLineListPrice;
    @FXML
    private TableColumn<OrderLine, Integer> orderLineQuantity;
    @FXML
    private TableColumn<OrderLine, Long> orderLineSum;
    @FXML
    private TableColumn<OrderLine, Boolean> orderLineCheckbox;
    @FXML
    private Button revertButton;
    @FXML
    private Button saveButton;

    public OrderDetailsViewEventBinding(Order order) {
        observableOrder = new SimpleObjectProperty<>(order);
        initialState = order;
        orderLines = FXCollections.observableList(order.getOrderLines());
    }

    public void initialize() {
        Order order = observableOrder.getValue();
        orderId.setText("#" + order.getId());
        cashierName.setText(order.getCashierName());
        timestamp.setText(new Date(order.getTimestamp()).toString());
        bindTable();
        disableButtonsIfNoChangeDetected();
    }

    private void bindTable() {
        orderLinesTable.setItems(orderLines);
        orderLineSku.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getSku()));
        orderLineProductName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductName()));
        orderLineListPrice.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue().getListPrice()).asObject());
        orderLineQuantity.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getQuantity()).asObject());
        orderLineSum.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue().getLineSum()).asObject());
        orderLineCheckbox.setCellFactory(CheckBoxTableCell.forTableColumn(orderLineCheckbox));
    }

    private void disableButtonsIfNoChangeDetected() {
        if (observableOrder.getValue().equals(initialState)) {
            revertButton.setDisable(true);
            saveButton.setDisable(true);
            return;
        }
        revertButton.setDisable(false);
        saveButton.setDisable(false);
    }

    /**
     * Event handler
     */
    public void onRevertButtonClicked() {
        restoreOriginalState();
    }

    private void restoreOriginalState() {
        ((SimpleObjectProperty<Order>)observableOrder).set(initialState);
        List<OrderLine> initialOrderLines = initialState.getOrderLines();
        System.out.println(initialOrderLines);
        orderLinesTable.getItems().setAll(initialOrderLines);
        bindTable();
    }

    /**
     * Event handler
     */
    public void onSaveButtonClicked() {

    }
}
