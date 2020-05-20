package com.hanu.ims.view;

import com.hanu.ims.controller.BatchController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Product;
import com.hanu.ims.model.domain.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

import static com.hanu.ims.util.modal.ModalService.showAlertDialog;
import static com.hanu.ims.util.modal.ModalService.showLoadingDialog;

public class BatchCreateView extends Stage {

    // constants
    private static final String FXML_FILE_NAME = "batch_create_view.fxml";
    private static final UnaryOperator<TextFormatter.Change> TEXT_FILTER = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*")) {
            return change;
        }
        return null;
    };

    // static fields
    private static ObservableList<Product> products;
    private static ObservableList<Supplier> suppliers;
    private static BatchController controller;

    // FXML fields
    @FXML
    private TextField skuInput;
    private AutoCompletionBinding<Product> skuAutoComplete;
    @FXML
    private TextField supplierInput;
    private AutoCompletionBinding<Supplier> supplierAutoComplete;
    @FXML
    private Spinner<Integer> quantityInput;
    @FXML
    private TextField importPriceInput;
    @FXML
    private TextField retailPriceInput;
    @FXML
    private VBox container;
    @FXML
    private Button saveButton;
    @FXML
    private Button resetButton;

    private Product selectedProduct;
    private Supplier selectedSupplier;

    public BatchCreateView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        setScene(scene);
        controller = new BatchController();
        bindAutoCompleteFields();
        updateProductSuggestions();
        updateSupplierSuggestions();
        addInputEventHandlers();
        prepareTextFormattedFields();
        disableButtonsIfNotCompletedForm();
    }

    private void addInputEventHandlers() {
        skuInput.setOnKeyTyped(event -> {
            disableButtonsIfNotCompletedForm();
        });
        supplierInput.setOnKeyTyped(event -> {
            disableButtonsIfNotCompletedForm();
        });
        importPriceInput.setOnKeyTyped(event -> {
            disableButtonsIfNotCompletedForm();
        });
        retailPriceInput.setOnKeyTyped(event -> {
            disableButtonsIfNotCompletedForm();
        });
//        skuAutoComplete.setOnAutoCompleted(event -> {
//            selectedProduct = event.getCompletion();
//        });
//        supplierAutoComplete.setOnAutoCompleted(event -> {
//            selectedSupplier = event.getCompletion();
//        });
    }

    private void bindAutoCompleteFields() {
        if (products == null) {
            products = FXCollections.observableList(new ArrayList<>());
        }
        if (suppliers == null) {
            suppliers = FXCollections.observableList(new ArrayList<>());
        }
        skuInput.setOnKeyTyped(event -> {
            selectedProduct = null;
        });
        supplierInput.setOnKeyTyped(event -> {
            selectedSupplier = null;
        });
        skuAutoComplete = TextFields.bindAutoCompletion(skuInput, products);
        supplierAutoComplete = TextFields.bindAutoCompletion(supplierInput, suppliers);
        skuAutoComplete.setPrefWidth(Double.MAX_VALUE);
        supplierAutoComplete.setPrefWidth(Double.MAX_VALUE);
    }

    private void prepareTextFormattedFields() {
        TextFormatter<String> textFormatterOne = new TextFormatter<>(TEXT_FILTER);
        TextFormatter<String> textFormatterTwo = new TextFormatter<>(TEXT_FILTER);
        importPriceInput.setTextFormatter(textFormatterOne);
        retailPriceInput.setTextFormatter(textFormatterTwo);
    }

    void updateProductSuggestions() {
        products.clear();
        var productSuggestions = controller.getAllProductSuggestions();
        productSuggestions.addListener((ListChangeListener<? super Product>) c -> {
            products.setAll(productSuggestions);
            skuAutoComplete = TextFields.bindAutoCompletion(skuInput, products);

            skuAutoComplete.setOnAutoCompleted(event -> {
                selectedProduct = event.getCompletion();
            });
        });
    }

    void updateSupplierSuggestions() {
        suppliers.clear();
        var supplierSuggestions = controller.getAllSupplierSuggestions();
        supplierSuggestions.addListener((ListChangeListener<? super Supplier>) c -> {
            suppliers.setAll(supplierSuggestions);
            supplierAutoComplete = TextFields.bindAutoCompletion(supplierInput, suppliers);

            supplierAutoComplete.setOnAutoCompleted(event -> {
                selectedSupplier = event.getCompletion();
            });
        });
    }

    private void disableButtonsIfNotCompletedForm() {
        String skuValue = skuInput.getText();
        String supplierIdValue = supplierInput.getText();
        String importPriceValue = importPriceInput.getText();
        String retailPriceValue = retailPriceInput.getText();
        if (skuValue.isEmpty() && supplierIdValue.isEmpty()
                && importPriceValue.isEmpty() && retailPriceValue.isEmpty()) {
            resetButton.setDisable(true);
        } else {
            resetButton.setDisable(false);
        }
        if (skuValue.isEmpty() || supplierIdValue.isEmpty()
                || importPriceValue.isEmpty() || retailPriceValue.isEmpty()) {
            saveButton.setDisable(true);
        } else {
            saveButton.setDisable(false);
        }
    }

    public void onReset() {
        selectedProduct = null;
        skuInput.setText("");
        supplierInput.setText("");
        importPriceInput.setText("");
        retailPriceInput.setText("");

        disableButtonsIfNotCompletedForm();
    }

    public void onSave() throws IOException {
        if (selectedProduct == null) {
            // show create product dialog
            var createProductDialog = new ProductCreateView(skuInput.getText());
            createProductDialog.initModality(Modality.WINDOW_MODAL);
            createProductDialog.initOwner(getScene().getWindow());
            createProductDialog.setOnCloseRequest(event -> {
                updateProductSuggestions();
            });
            createProductDialog.show();
            return;
        }
        if (selectedSupplier == null) {
            showAlertDialog("The entered supplier is not present in database!",
                    "An error occurred!", Alert.AlertType.WARNING);
            return;
        }
        Dialog<?> loadingDialog = showLoadingDialog(getOwner());
        String sku = selectedProduct.getSku();
        Integer supplierId = selectedSupplier.getId();
        Integer quantity = quantityInput.getValue();
        Long importPrice = Long.parseLong(importPriceInput.getText());
        Long retailPrice = Long.parseLong(retailPriceInput.getText());
        Date importDate = new Date(Instant.now().getEpochSecond() * 1000);
        Batch batch = new Batch(sku, quantity, importDate, importPrice, retailPrice, supplierId);
        try {
            controller.addBatch(batch);
            BatchListView.updateDataSource(true);
            loadingDialog.close();
            showAlertDialog("Operation completed successfully!", "Successful!", Alert.AlertType.INFORMATION);
            close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage(), "An error occurred!", Alert.AlertType.ERROR);
        }
    }
}
