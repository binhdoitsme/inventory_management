package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.util.date.EpochSecondConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.Modality;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailsViewEventBinding {

    private ObservableValue<Order> observableOrder;
    private final Order initialState;
    private ObservableList<OrderLine> orderLines;
    private ObservableList<OrderLine> selectedOrderLines;
    private ObservableList<OrderLine> orderLinesToDelete;
    private OrderController controller;

    @FXML
    private Label orderId;
    @FXML
    private Label cashierName;
    @FXML
    private Label timestamp;
    @FXML
    private Label orderTotal;
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
//    @FXML
//    private TableColumn<OrderLine, Boolean> orderLineCheckbox;
    @FXML
    private Button revertButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private CheckBox selectAll;

    public OrderDetailsViewEventBinding(Order order) {
        Order clonedOrder = new Order(order.getId(),
                order.getCashierId(), order.getCashierName(),
                order.getOrderLines(), order.getTimestamp());
        observableOrder = new SimpleObjectProperty<>(clonedOrder);
        initialState = order;
        List<OrderLine> orderLines = new ArrayList<>();
        orderLines.addAll(clonedOrder.getOrderLines());
        this.orderLines = FXCollections.observableList(orderLines);
        selectedOrderLines = FXCollections.observableList(new ArrayList<>());
        orderLinesToDelete = FXCollections.observableList(new ArrayList<>());
    }

    public void initialize() {
        controller = new OrderController();
        Order order = observableOrder.getValue();
        orderId.setText("#" + order.getId());
        cashierName.setText(order.getCashierName());
        timestamp.setText(EpochSecondConverter.epochSecondToString(order.getTimestamp()));
        updateTotal(initialState.getOrderLines());
        deleteButton.setDisable(true);
        revertButton.setDisable(true);
        saveButton.setDisable(true);
        bindTable();
        disableButtonsIfNoChangeDetected();
        addOrderLineSelectedListener();
        orderLinesTable.getItems().addListener((ListChangeListener<? super OrderLine>) c -> {
            updateTotal(orderLinesTable.getItems());
        });
    }

    private void updateTotal(List<OrderLine> orderLines) {
        if (orderLines.isEmpty()) {
            orderTotal.setText("0");
        } else {
            orderTotal.setText(String.valueOf(observableOrder.getValue().getTotalPrice()));
        }
    }

    private void addOrderLineSelectedListener() {
        ObservableList<TablePosition> selectedCells = orderLinesTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<? super TablePosition>) c -> {
            if (selectedCells.isEmpty()) {
                deleteButton.setDisable(true);
            } else {
                deleteButton.setDisable(false);
            }
        });
        orderLines.addListener((ListChangeListener<? super OrderLine>) c -> {
            disableButtonsIfNoChangeDetected();
        });
    }

    public void onSelectAll() {
        if (selectedOrderLines.isEmpty()) {
            orderLinesTable.getSelectionModel().selectAll();

        } else {
            selectedOrderLines.clear();
        }
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
//        orderLineCheckbox.setCellFactory(CheckBoxTableCell.forTableColumn(orderLineCheckbox));
    }

    private void disableButtonsIfNoChangeDetected() {
        var initialOrderLines = observableOrder.getValue().getOrderLines();
        System.out.println(orderLines.size() == initialOrderLines.size());
        System.out.println(observableOrder.getValue().equals(initialState));
        if (observableOrder.getValue().equals(initialState)
                && orderLines.size() == initialOrderLines.size()) {
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
        ((SimpleObjectProperty<Order>) observableOrder).set(initialState);
        List<OrderLine> initialOrderLines = initialState.getOrderLines();
        System.out.println(initialOrderLines);
        orderLinesTable.getItems().setAll(initialOrderLines);
        bindTable();
    }

    private void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occurred!");
        alert.setHeaderText(message);
        alert.show();
    }

    private void showSuccessfulDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("The operation completed successfully!");
        alert.show();
    }

    private Dialog<?> showLoadingDialog() {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(orderLinesTable.getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }

    /**
     * Event handler
     */
    public void onSaveButtonClicked() {
        Dialog<?> loadingDialog = showLoadingDialog();
        try {
            controller.updateOrder(observableOrder.getValue(), orderLinesToDelete, new ArrayList<>());
            showSuccessfulDialog();
        } catch (RuntimeException e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage());
        }
        loadingDialog.close();
        OrderListViewEventBinding.updateDataSource(true);
    }

    /**
     * Event handler
     */
    public void onDeleteButtonClicked() {
        var selectedItem = orderLinesTable.getSelectionModel().getSelectedItem();
        orderLinesToDelete.add(selectedItem);
        orderLines.remove(selectedItem);
    }
}
