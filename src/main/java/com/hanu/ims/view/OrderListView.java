package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import com.hanu.ims.util.configuration.Configuration;
import com.hanu.ims.util.date.EpochSecondConverter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import static com.hanu.ims.util.modal.ModalService.*;

public class OrderListView extends Stage {

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
    private TableColumn<Order, Long> orderColTotalPrice;
    @FXML
    private TableColumn<Order, String> orderColStatus;
    @FXML
    private Button deleteButton;

    private static OrderController controller;

    private static final String FXML_FILE_NAME = "order_list.fxml";
    private static final String WINDOW_TITLE = Configuration.get("window.title.order.manage");

    public OrderListView() throws IOException {
        controller = new OrderController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Parent rootElement = loader.load();
        Scene rootScene = new Scene(rootElement);
        setScene(rootScene);
        setTitle(WINDOW_TITLE);
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
        orderColStatus.setCellValueFactory((param -> new SimpleStringProperty((
                param.getValue().isExpired() ? "EXPIRED" : "ORDERED")
        )));
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

    private void disableDeleteButtonIfNotCreatedByThisUser() {
        if (orderListTable.getSelectionModel().getSelectedItems().isEmpty()) return;
        if (orderListTable.getSelectionModel().getSelectedItem() == null) return;
        Order item = orderListTable.getSelectionModel().getSelectedItem();
        int cashierId = item.getCashierId();
        if (cashierId != AuthenticationProvider.getInstance().getCurrentAccount().getId() || item.isExpired()) {
            deleteButton.setDisable(true);
        }
    }

    private void addOrderLineSelectedListener() {
        ObservableList<TablePosition> selectedCells = orderListTable.getSelectionModel().getSelectedCells();
        selectedCells.addListener((ListChangeListener<? super TablePosition>) c -> {
            if (selectedCells.isEmpty()) {
                deleteButton.setDisable(true);
            } else {
                deleteButton.setDisable(false);
            }
            disableDeleteButtonIfNotCreatedByThisUser();
        });
    }

    public void onDeleteButtonPressed() {
        Optional<ButtonType> result = showConfirmationDialog("Are you sure want to delete the selected order?", getOwner());
        if (!result.isPresent() || result.get().equals(ButtonType.NO)) {
            return;
        }

        Dialog<?> loadingDialog = showLoadingDialog(getScene().getWindow());
        Order order = orderListTable.getSelectionModel().getSelectedItem();
        controller.removeOrder(order);
        updateDataSource(true);
        loadingDialog.close();
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
            showAlertDialog(e.getMessage());
        }
    }

    static void updateDataSource(boolean forceUpdate) {
        if (controller == null) return;
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
