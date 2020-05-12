package com.hanu.ims.view;

import com.hanu.ims.controller.BatchController;
import com.hanu.ims.model.domain.Category;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BatchCreateView extends Stage {

    // constants
    private static final String FXML_FILE_NAME = "batch_create_view.fxml";

    // static fields
    private static ObservableList<Category> categories;
    private static BatchController controller;

    // FXML fields
    @FXML
    private ComboBox<Category> categoryInput;

    public BatchCreateView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        setScene(scene);
        controller = new BatchController();
        updateCategorySuggestions();
        bindCategoryDropdown();
    }

    private void bindCategoryDropdown() {
        categoryInput.setItems(categories);
    }

    public void onReset() {
        System.out.println("reset!");
    }

    public void onSave() {
        System.out.println("saved!");
    }

    static void updateCategorySuggestions() {
        if (categories == null) {
            categories = controller.getCategorySuggestions();
        } else {
            var data = controller.getCategorySuggestions();
            data.addListener((ListChangeListener<? super Category>) c -> {
                categories.setAll(data);
            });
        }
    }
}
