package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.util.date.EpochSecondConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Optional;

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
//    @FXML
//    private TableColumn<Order, Boolean> orderColCheckbox;
    @FXML
    private TableColumn<Order, Long> orderColTotalPrice;
    @FXML
    private Button deleteButton;

    private static OrderController controller;

    public OrderListViewEventBinding() {
        controller = new OrderController();
    }

    public void initialize() {
        updateDataSource(false);
        dataSource.addListener((ListChangeListener<? super Order>) c -> {
            System.out.println("Changed!");
        });

        orderColId.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getId()).asObject());
        orderColCashier.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getCashierName()));
        orderColTimestamp.setCellValueFactory(param ->
                new SimpleStringProperty(EpochSecondConverter.epochSecondToString(param.getValue().getTimestamp())));
//        orderColCheckbox.setCellFactory(CheckBoxTableCell.forTableColumn(orderColCheckbox));

        orderColTotalPrice.setCellValueFactory(param -> {
            if (param.getValue().getOrderLines().isEmpty())
                return new SimpleLongProperty(0).asObject();
            return new SimpleLongProperty(param.getValue()
                    .getOrderLines().stream()
                    .map(OrderLine::getLineSum)
                    .reduce((x, y) -> x + y)
                    .get()).asObject();
        });
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

        deleteButton.setDisable(true);
        addOrderLineSelectedListener();
    }

    private void addOrderLineSelectedListener() {
        ObservableList<TablePosition> selectedCells = orderListTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<? super TablePosition>) c -> {
            if (selectedCells.isEmpty()) {
                deleteButton.setDisable(true);
            } else {
                deleteButton.setDisable(false);
            }
        });
    }

    public void onDeleteButtonPressed() {
        Dialog<ButtonType> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle("Confirm delete");
        confirmationDialog.setHeaderText("Are you sure want to delete the selected order?");
        confirmationDialog.initOwner(orderListTable.getScene().getWindow());
        confirmationDialog.initModality(Modality.WINDOW_MODAL);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (!result.isPresent() || result.get().equals(ButtonType.NO)) {
            return;
        }

        Dialog<?> loadingDialog = showLoadingDialog();
        Order order = orderListTable.getSelectionModel().getSelectedItem();
        controller.removeOrder(order);
        updateDataSource(true);
        loadingDialog.close();
    }

    private Dialog<?> showLoadingDialog() {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(orderListTable.getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
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
                updateDataSource(false);
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    static void updateDataSource(boolean forceUpdate) {
        if (dataSource == null)
            dataSource = controller.getOrderList();
        if (forceUpdate) {
            ObservableList<Order> orders = controller.getOrderList();
            orders.addListener((ListChangeListener<? super Order>) c -> {
                dataSource.setAll(orders);
            });
        }
    }
}
