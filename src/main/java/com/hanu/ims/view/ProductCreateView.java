package com.hanu.ims.view;

import com.hanu.ims.controller.ProductController;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Product;
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

public class ProductCreateView extends Stage {
    private static final String FXML_FILE_NAME = "product_create_view.fxml";

    private static ProductController controller;
    private static ObservableList<Category> categories;

    @FXML
    private TextField skuInput;
    @FXML
    private TextField productNameInput;
    @FXML
    private TextArea productDescriptionInput;
    @FXML
    private ComboBox<Category> categoryInput;

    public ProductCreateView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Parent root = loader.load();
        setScene(new Scene(root));
        controller = new ProductController();
        categories = FXCollections.observableList(new ArrayList<>());
        categoryInput.setItems(categories);
        categoryInput.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category object) {
                if (object == null) return "";
                return object.getName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });
        updateSuggestions(true);
    }

    public ProductCreateView(String skuToCreate) throws IOException {
        this();
        skuInput.setText(skuToCreate);
    }

    public void onSubmit() {
        String sku = skuInput.getText().trim();
        String name = productNameInput.getText().trim();
        String desc = productDescriptionInput.getText().trim();
        Category category = categoryInput.getValue();
        Product product = new Product(sku, name, desc);
        product.setCategory(category);
        var loadingDialog = showLoadingDialog();
        try {
            controller.addProduct(product);
            updateSuggestions(true);
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
        loadingDialog.initOwner(skuInput.getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }

    static void updateSuggestions(boolean forceUpdate) {
        boolean shouldUpdate = categories == null || forceUpdate;
        if (shouldUpdate) {
            var updatedCategories = controller.getAllCategories();
            updatedCategories.addListener((ListChangeListener<? super Category>) c -> {
                categories.setAll(updatedCategories);
            });
        }
    }
}
