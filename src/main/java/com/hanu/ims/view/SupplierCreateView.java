package com.hanu.ims.view;

import com.hanu.ims.controller.ProductController;
import com.hanu.ims.controller.SupplierController;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.ArrayList;

public class SupplierCreateView extends Stage {
    private static final String FXML_FILE_NAME = "supplier_create_view.fxml";

    private static SupplierController controller;
    private static ProductController productController;
    private static ObservableList<Category> categories;

    @FXML
    private TextField supplierNameInput;
    @FXML
    private TextField supplierPhoneInput;
    @FXML
    private TextArea supplierAddressInput;
    @FXML
    private ComboBox<Category> categoryInput;

    public SupplierCreateView() throws IOException {
        // data init
        controller = new SupplierController();
        productController = new ProductController();
        categories = FXCollections.observableList(new ArrayList<>());
        updateSuggestions(true);

        // view init
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Parent root = loader.load();
        setScene(new Scene(root));
    }

    public void onSubmit() {
        String name = supplierNameInput.getText().trim();
        String phone = supplierPhoneInput.getText().trim();
        String address = supplierAddressInput.getText().trim();
        Supplier supplier = new Supplier(name, phone, address, true);

        var loadingDialog = showLoadingDialog();
        try {
            controller.createSupplier(supplier);
            updateSuggestions(false);
            SupplierListView.updateDataSource(true);
            loadingDialog.close();
            close();
            showAlertDialog("Operation completed!", "Successful!", Alert.AlertType.INFORMATION);
        } catch (RuntimeException e) {
            loadingDialog.close();
            showAlertDialog(e.getMessage(), "An error occurred!", Alert.AlertType.WARNING);
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
        loadingDialog.initOwner(getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }

    static void updateSuggestions(boolean forceUpdate) {
        boolean shouldUpdate = categories == null || forceUpdate;
        if (shouldUpdate) {
            var updatedCategories = productController.getAllCategories();
            updatedCategories.addListener((ListChangeListener<? super Category>) c -> {
                categories.setAll(updatedCategories);
            });
        }
    }
}
