package com.hanu.ims.view;

import com.hanu.ims.controller.ProductController;
import com.hanu.ims.controller.SupplierController;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Supplier;
import com.hanu.ims.util.modal.ModalService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

import static com.hanu.ims.util.modal.ModalService.showAlertDialog;
import static com.hanu.ims.util.modal.ModalService.showLoadingDialog;

public class SupplierCreateView extends Stage {
    private static final String FXML_FILE_NAME = "supplier_create_view.fxml";
    private static final UnaryOperator<TextFormatter.Change> TEXT_FILTER = change -> {
        String text = change.getText();
        if (text.matches("[0-9]*")) {
            return change;
        }
        return null;
    };

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
        supplierPhoneInput.setTextFormatter(new TextFormatter<>(TEXT_FILTER));
        supplierPhoneInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 10) {
                supplierPhoneInput.setText(oldValue);
            }
        });
    }

    public void onSubmit() {
        String name = supplierNameInput.getText().trim();
        String phone = supplierPhoneInput.getText().trim();
        String address = supplierAddressInput.getText().trim();

        // validation
        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            ModalService.showAlertDialog("All fields must be filled!");
            return;
        }

        Supplier supplier = new Supplier(name, phone, address, true);

        var loadingDialog = showLoadingDialog(getOwner());
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
