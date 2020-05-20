package com.hanu.ims.view;

import com.hanu.ims.controller.OrderController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Order;
import com.hanu.ims.model.domain.OrderLine;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import com.hanu.ims.util.configuration.Configuration;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.*;

import static com.hanu.ims.util.modal.ModalService.showAlertDialog;
import static com.hanu.ims.util.modal.ModalService.showLoadingDialog;

public class OrderCreateView extends Stage {

    private static final int DEFAULT_QTY = 1;
    private static final String FXML_FILE_NAME = "order_create.fxml";
    private static final String TITLE = Configuration.get("window.title.order.create");
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
    @FXML
    private Label cashierLabel;
    @FXML
    private Label total;

    private AutoCompletionBinding<Product> autoCompletionBinding;

    private OrderController controller;
    private ObservableList<OrderLine> orderLineData;
    private ObservableValue<Order> orderData;

    public OrderCreateView() throws IOException {
        controller = new OrderController();
        orderLineData = FXCollections.observableList(new ArrayList<>());
        orderData = new SimpleObjectProperty<>();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Parent sceneRoot = loader.load();
        Scene scene = new Scene(sceneRoot);
        setScene(scene);
        setTitle(TITLE);
        setInitialTotal();
    }

    private void setInitialTotal() {
        total.setText("$0");
    }

    @FXML
    public void initialize() {
        ((SimpleObjectProperty<Order>) orderData)
                .set(new Order(AuthenticationProvider.getInstance().getCurrentAccount().getId()));
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
        setupAutoCompletion();

        resetBtn.setDisable(true);
        submitBtn.setDisable(true);
        initializeTable();
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
        resetBtn.setDisable(true);
        submitBtn.setDisable(true);
        cashierLabel.setText(AuthenticationProvider.getInstance().getCurrentAccount().getUsername());
        initializeTable();
        skuSuggestions.addListener((ListChangeListener<? super Product>) c -> {
            autoCompletionBinding = TextFields.bindAutoCompletion(skuTextField, skuSuggestions);
        });
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
        Map<Integer, List<OrderLine>> skuQuantities = new HashMap<>();
        for (OrderLine ol : orderLineData) {
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
        orderLineData.setAll(orderLines);
        orderData.getValue().setOrderLines(orderLines);
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
        orderData.getValue().addOrderLines(orderLinesToAdd); // defect here
        ((SimpleObjectProperty<Order>) orderData).set(orderData.getValue());

        reduceOrderLineData();
        setTotal();
    }

    private void setTotal() {
        total.setText("$" + calculateOrderTotal());
    }

    private long calculateOrderTotal() {
        if (orderLineData.isEmpty()) return 0;
        return orderLineData.stream().map(line -> line.getLineSum()).reduce((l1, l2) -> l1 + l2).get();
    }

    public void onSubmit(ActionEvent actionEvent) {
        Dialog<?> loadingDialog = showLoadingDialog(getOwner());
        try {
            Order order = orderData.getValue();
            order.setTimestampAsCurrent();
            controller.createOrder(order);
            OrderListView.updateDataSource(true);
            loadingDialog.close();
            onSuccessfulAdd();
        } catch (RuntimeException e) {
            loadingDialog.close();
            e.printStackTrace();
            showAlertDialog(e.getMessage());
        }
    }

    private void onSuccessfulAdd() {
        orderLineData.clear();
        setTotal();
    }

    public void onReset(ActionEvent actionEvent) {
        orderLineData.clear();
        orderData.getValue().setOrderLines(new ArrayList<>());
        controller.invalidateCache();
        setInitialTotal();
    }

    public void updateSuggestions() {
        if (skuSuggestions.isEmpty())
            skuSuggestions = controller.getAllProductSuggestions();
    }
}
