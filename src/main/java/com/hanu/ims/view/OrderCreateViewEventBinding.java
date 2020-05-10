package com.hanu.ims.view;

import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.*;

public class OrderCreateViewEventBinding {

    private static final int DEFAULT_QTY = 1;

    private static ObservableList<Product> skuSuggestions = FXCollections.observableList(new ArrayList<>());

    @FXML
    private Button resetBtn;
    @FXML
    private Button submitBtn;
    @FXML
    private TextField skuTextField;
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

    private AutoCompletionBinding<Product> autoCompletionBinding;

    private OrderController controller = new OrderController();
    private ObservableList<OrderLine> orderLineData = FXCollections.observableList(new ArrayList<>());
    private ObservableValue<Order> orderData = new SimpleObjectProperty<>();

    @FXML
    public void initialize() {
        ((SimpleObjectProperty<Order>)orderData).set(new Order(3));
        skuSuggestions.addListener((ListChangeListener<? super Product>) p -> {
            System.out.println("Changed!");
        });
        orderLineData.addListener((ListChangeListener<? super OrderLine>) orderLine -> {
            System.out.println("Added: " + orderLine);
            if (orderLineData.isEmpty()) {
                resetBtn.setDisable(true);
                submitBtn.setDisable(true);
            } else {
                resetBtn.setDisable(false);
                submitBtn.setDisable(false);
            }
        });
        orderData.addListener(observable -> {
            System.out.println("order has changed!");
        });
        updateSuggestions();
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
        resetBtn.setDisable(true);
        submitBtn.setDisable(true);
        initializeTable();
    }

    private void initializeTable() {
        orderLinesTable.setItems(orderLineData);
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
    }

    private void reduceOrderLineData() {
        Map<String, OrderLine> skuQuantities = new HashMap<>();
        for (OrderLine ol : orderLineData) {
            String sku = ol.getSku();
            if (!skuQuantities.containsKey(sku)) {
                skuQuantities.put(sku, ol);
                continue;
            }
            OrderLine orderLine = skuQuantities.get(sku);
            orderLine.setQuantity(orderLine.getQuantity() + ol.getQuantity());
        }
        List<OrderLine> orderLines = new ArrayList<>();
        for (String sku : skuQuantities.keySet()) {
            orderLines.add(skuQuantities.get(sku));
        }
        orderLineData.setAll(orderLines);
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
            OrderLine orderLine = new OrderLine(sku, productName, retailPrice, qty, batchId);
            orderLinesToAdd.add(orderLine);
        });
        orderLineData.addAll(orderLinesToAdd);
        orderData.getValue().addOrderLines(orderLinesToAdd);
        ((SimpleObjectProperty<Order>)orderData).set(orderData.getValue());

        reduceOrderLineData();
    }

    public void onSubmit(ActionEvent actionEvent) {
        Dialog<?> loadingDialog = showLoadingDialog();
        try {
            Order order = orderData.getValue();
            order.setTimestampAsCurrent();
            controller.createOrder(order);
            OrderListViewEventBinding.updateDataSource(true);
            loadingDialog.close();
            onSuccessfulAdd();
        } catch (RuntimeException e) {
            loadingDialog.close();
            showAlertDialog(e.getMessage());
        }
    }

    private void onSuccessfulAdd() {
        orderLineData.clear();
    }

    private void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occurred!");
        alert.setHeaderText(message);
        alert.show();
    }

    private Dialog<?> showLoadingDialog() {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(skuTextField.getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }

    public void onReset(ActionEvent actionEvent) {
        orderLineData.clear();
        orderData.getValue().setOrderLines(new ArrayList<>());
        controller.invalidateCache();
    }

    private void reset() {
        controller.invalidateCache();

    }

    public void updateSuggestions() {
        if (skuSuggestions.isEmpty())
            skuSuggestions = controller.getAllProductSuggestions();
    }
}
