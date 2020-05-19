package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import com.hanu.ims.util.configuration.Configuration;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.*;

public class OrderDetailsView extends Stage {
    private static final int DEFAULT_QTY = 1;
    private static final String FXML_FILE_NAME = "order_details.fxml";
    private static final String TITLE = Configuration.get("window.title.order.details");

    private static ObservableList<Product> skuSuggestions = FXCollections.observableList(new ArrayList<>());
    private static OrderController controller;

    private ObservableValue<Order> observableOrder;
    private final Order initialState;
    private ObservableList<OrderLine> orderLines;
    private ObservableList<OrderLine> selectedOrderLines;
    private ObservableList<OrderLine> orderLinesToDelete;
    private ObservableList<OrderLine> orderLinesToAdd;

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
    @FXML
    private TableColumn<OrderLine, Integer> orderLineBatchId;
    @FXML
    private Button revertButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private CheckBox selectAll;
    @FXML
    private TextField skuTextField;
    private AutoCompletionBinding<Product> autoCompletionBinding;


    public OrderDetailsView(Order order) throws IOException {
        // init data
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
        orderLinesToAdd = FXCollections.observableList(new ArrayList<>());

        // init view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        setScene(scene);
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
        disableIfNotEditable();
        updateSuggestions();
        setupAutoCompletion();
    }

    private void disableIfNotEditable() {
        if (initialState.getCashierId() != AuthenticationProvider.getInstance().getCurrentAccount().getId()) {
            orderLinesTable.setDisable(true);
            skuTextField.setDisable(true);
        }
    }

    private void updateTotal(List<OrderLine> orderLines) {
        if (orderLines.isEmpty()) {
            orderTotal.setText("0");
        } else {
            orderTotal.setText(String.valueOf(this.orderLines.stream().map(line -> line.getLineSum()).reduce((l1, l2) -> l1 + l2).get()));
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
        orderLineBatchId.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getBatchId()).asObject());
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
        orderLinesToDelete.clear();
        orderLinesToAdd.clear();
        bindTable();
        controller.invalidateCache();
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
            controller.updateOrder(observableOrder.getValue(), orderLinesToDelete, orderLinesToAdd);
            showSuccessfulDialog();
            close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage());
        }
        loadingDialog.close();
        OrderListView.updateDataSource(true);
    }

    /**
     * Event handler
     */
    public void onDeleteButtonClicked() {
        var selectedItem = orderLinesTable.getSelectionModel().getSelectedItem();
        orderLinesToDelete.add(selectedItem);
        orderLines.remove(selectedItem);
        updateTotal(orderLines);
    }

    private void setupAutoCompletion() {
        autoCompletionBinding = TextFields.bindAutoCompletion(skuTextField, skuSuggestions);
        autoCompletionBinding.setOnAutoCompleted(event -> {
            Product productToAdd = event.getCompletion();
            try {
                addOrderLinesWithProductAndQuantity(productToAdd, DEFAULT_QTY);
            } catch (RuntimeException e) {
                e.printStackTrace();
                showAlertDialog(e.getMessage());
            }
            skuTextField.clear();
        });
        skuSuggestions.addListener((ListChangeListener<? super Product>) c -> {
            autoCompletionBinding = TextFields.bindAutoCompletion(skuTextField, skuSuggestions);
        });
    }

    private void reduceOrderLineData() {
        Map<Integer, List<OrderLine>> skuQuantities = new HashMap<>();
        for (OrderLine ol : orderLinesToAdd) {
            int batchId = ol.getBatchId();
            skuQuantities.putIfAbsent(batchId, new ArrayList<>());

            List<OrderLine> orderLines = skuQuantities.get(batchId);
            Optional<OrderLine> orderLineFromSameBatch = orderLines.stream()
                    .filter(o -> o.getBatchId() == ol.getBatchId()).findFirst();
            if (orderLineFromSameBatch.isPresent()) {
                OrderLine orderLine = orderLineFromSameBatch.get();
                orderLine.setQuantity(orderLine.getQuantity() + ol.getQuantity());
            } else {
                skuQuantities.get(batchId).add(ol);
            }
        }
        List<OrderLine> orderLines = new ArrayList<>();
        for (int batchId : skuQuantities.keySet()) {
            orderLines.addAll(skuQuantities.get(batchId));
        }
        orderLinesToAdd.setAll(orderLines);
        this.orderLines.setAll(initialState.getOrderLines());
        this.orderLines.removeAll(orderLinesToDelete);
        this.orderLines.addAll(orderLinesToAdd);
//        observableOrder.getValue().setOrderLines(orderLines);
    }

    private void addOrderLinesWithProductAndQuantity(Product productToAdd, int quantity) {
        // add to order line list with quantity 1
        String sku = productToAdd.getSku();
        Map<Batch, Integer> batchSelections = controller.getBatches(sku, quantity);
        List<OrderLine> orderLinesToAdd = new ArrayList<>();
        Collection<Batch> batches = batchSelections.keySet();
        batches.forEach(batch -> {
            String productName = productToAdd.getName();
            long retailPrice = batch.getRetailPrice();
            Integer qty = batchSelections.get(batch);
            int batchId = batch.getId();
            OrderLine orderLine = new OrderLine(sku, productName, retailPrice, qty, batchId, initialState.getId());
            orderLinesToAdd.add(orderLine);
        });
        this.orderLinesToAdd.addAll(orderLinesToAdd);
//        observableOrder.getValue().addOrderLines(orderLinesToAdd); // defect here
//        ((SimpleObjectProperty<Order>) observableOrder).set(observableOrder.getValue());

        reduceOrderLineData();
        updateTotal(orderLines);
//        setTotal();
    }

    public void updateSuggestions() {
        if (skuSuggestions.isEmpty())
            skuSuggestions = controller.getAllProductSuggestions();
    }
}
