package com.hanu.ims.view;

import com.hanu.ims.model.domain.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

public class ProductCreatePopupViewEventBinding {
    @FXML
    private TextField skuInput;
    @FXML
    private TextField productNameInput;
    @FXML
    private TextArea productDescriptionInput;
    @FXML
    private TextField supplierInput;

    private AutoCompletionBinding<Supplier> supplierAutoCompletionBinding;

    private static ObservableList<Supplier> supplierObservableList;

    public ProductCreatePopupViewEventBinding() { }

    public void initialize() {
        updateSuggestions();
        supplierAutoCompletionBinding = TextFields.bindAutoCompletion(supplierInput, supplierObservableList);
    }

    static void updateSuggestions() {

    }
}
