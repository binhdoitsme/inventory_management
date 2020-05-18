package com.hanu.ims.view;

import com.hanu.ims.controller.ProductController;
import com.hanu.ims.controller.SupplierController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SupplierDetailsView extends Stage {
    private static final String FXML_FILE_NAME = "supplier_details_view.fxml";
    private static final String TITLE = "Batch Details";

    private static SupplierController controller;
    private static ProductController productController;

    private final Supplier initialState;
    private Supplier currentState;

    @FXML
    private TextField supplierNameInput;
    @FXML
    private TextField supplierPhoneInput;
    @FXML
    private TextArea supplierAddressInput;
    @FXML
    private Button revertButton;
    @FXML
    private Button saveButton;

    public SupplierDetailsView(Supplier supplier) throws IOException {
        controller = new SupplierController();
        productController = new ProductController();
        initialState = supplier;
        initializeCurrentState(supplier);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        setScene(scene);
        setTitle(TITLE);
        disableInputIfNotAvailable();
        addEventListeners();
        setFieldValues(currentState);
        disableButtonsIfNoChangeDetected();
    }

    private void setFieldValues(Supplier supplier) {
        supplierNameInput.setText(supplier.getName());
        supplierPhoneInput.setText(supplier.getPhone());
        supplierAddressInput.setText(supplier.getAddress());

    }

    private void initializeCurrentState(Supplier supplier) {
        currentState = new Supplier(
                supplier.getId(), supplier.getName(), supplier.getPhone(),
                supplier.getAddress(), supplier.isAvailable()
        );
    }

    private void addEventListeners() {
        supplierNameInput.setOnKeyTyped(event -> {
            currentState.setName(supplierNameInput.getText());
            disableButtonsIfNoChangeDetected();
        });
        supplierPhoneInput.setOnKeyTyped(event -> {
            currentState.setPhone(supplierPhoneInput.getText());
            disableButtonsIfNoChangeDetected();
        });
        supplierAddressInput.setOnKeyTyped(event -> {
            currentState.setAddress(supplierAddressInput.getText());
            disableButtonsIfNoChangeDetected();
        });
    }

    private void disableButtonsIfNoChangeDetected() {
        if (currentState.equals(initialState)) {
            revertButton.setDisable(true);
            saveButton.setDisable(true);
        } else {
            revertButton.setDisable(false);
            saveButton.setDisable(false);
        }
    }

    private void disableInputIfNotAvailable() {
        if (!currentState.isAvailable()) {
            supplierAddressInput.setDisable(true);
            supplierNameInput.setDisable(true);
            supplierPhoneInput.setDisable(true);
        }
    }

    private void showAlertDialog(String message, String title, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.show();
    }

    private Dialog<?> showLoadingDialog() {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(saveButton.getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }

    public void onSave() {
        System.out.println("Saved!");
        var loadingDialog = showLoadingDialog();
        try {
            Supplier batch = controller.updateSupplier(currentState);
            loadingDialog.close();
            BatchListView.updateDataSource(true);
            showAlertDialog("Operation completed!", "Successful!", Alert.AlertType.INFORMATION);
            SupplierListView.updateDataSource(true);
            close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            loadingDialog.close();
            showAlertDialog(e.getMessage(), "An error occurred!", Alert.AlertType.ERROR);
        }
    }
}
