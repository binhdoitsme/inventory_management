package com.hanu.ims.view;

import com.hanu.ims.model.domain.Batch;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;

public class BatchDetailsView extends Stage {

    private static final String FXML_FILE_NAME = "batch_details_view.fxml";
    private static final String TITLE = "Batch Details";

    private final Batch initialState;
    private ObservableValue<Batch> currentState;

    // FXML
    @FXML
    private Label batchIdField;
    @FXML
    private TextField skuInput;
    @FXML
    private TextField productNameInput;
    @FXML
    private TextField supplierInput;
    @FXML
    private TextField quantityInput;
    @FXML
    private TextField importPriceInput;
    @FXML
    private TextField retailPriceInput;
    @FXML
    private TextField status;
    @FXML
    private TextField importDateInput;
    @FXML
    private Button revertButton;
    @FXML
    private Button saveButton;

    public BatchDetailsView(Batch batch) throws IOException {
        initialState = batch;
        currentState = new SimpleObjectProperty<>(
                new Batch(
                        batch.getId(), batch.getSku(),
                        batch.getImportQuantity(), batch.getQuantity(), batch.getImportDate(),
                        batch.getImportPrice(), batch.getRetailPrice(), batch.getProductName()
                )
        );
        currentState.getValue().setStatus(batch.getStatus());
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        setScene(scene);
        setTitle(TITLE);
        disableInputIfExpired();
    }

    private void setFieldValues(Batch newValue) {
        batchIdField.setText("#" + newValue.getId());
        skuInput.setText(newValue.getSku());
        importPriceInput.setText(String.valueOf(newValue.getImportPrice()));
        retailPriceInput.setText(String.valueOf(newValue.getRetailPrice()));
        productNameInput.setText(newValue.getProductName());
        productNameInput.setEditable(false);
        importDateInput.setText(newValue.getImportDate().toString().substring(0, 10));
        status.setText(newValue.getStatus().toString());
        if (newValue.getQuantity() < newValue.getImportQuantity()) {
            quantityInput.setEditable(false);
            importPriceInput.setEditable(false);
            retailPriceInput.setEditable(false);
            quantityInput.setText(newValue.getQuantity() + "/" + newValue.getImportQuantity());
        } else {
            quantityInput.setEditable(true);
            quantityInput.setText(String.valueOf(newValue.getImportQuantity()));
        }
    }

    private void disableInputIfExpired() {
        if (currentState.getValue().getStatus() == Batch.Status.EXPIRED) {
            quantityInput.setDisable(true);
            importPriceInput.setDisable(true);
            retailPriceInput.setDisable(true);
            quantityInput.setDisable(true);
            skuInput.setDisable(true);
            productNameInput.setDisable(true);
            importDateInput.setDisable(true);
            status.setDisable(true);
        }
    }

    private void disableButtonsIfNoChangeDetected() {
        if (currentState.getValue().equals(initialState)) {
            revertButton.setDisable(true);
            saveButton.setDisable(true);
        } else {
            revertButton.setDisable(false);
            saveButton.setDisable(false);
        }
    }

    @FXML
    public void initialize() {
        setFieldValues(currentState.getValue());
        disableButtonsIfNoChangeDetected();
        currentState.addListener((observable, oldValue, newValue) -> {
            setFieldValues(newValue);
            disableButtonsIfNoChangeDetected();
            disableInputIfExpired();
        });
    }
}
