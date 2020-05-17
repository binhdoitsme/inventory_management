package com.hanu.ims.view;

import com.hanu.ims.controller.BatchController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Category;
import com.hanu.ims.model.domain.Order;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class BatchListView extends Stage {

    // constants
    private static final String FXML_FILE_NAME = "batch_list_view.fxml";
    private static final String TITLE = "View Batch List";

    // static
    private static ObservableList<Batch> dataSource;
    private static BatchController controller;
    private static ObservableList<Category> categories;

    // FXML
    @FXML
    private Button batchDeleteButton;
    @FXML
    private TableView<Batch> batchTable;
    @FXML
    private TableColumn<Batch, String> batchColSku;
    @FXML
    private TableColumn<Batch, String> batchColImpDate;
    @FXML
    private TableColumn<Batch, Long> batchColImpPrice;
    @FXML
    private TableColumn<Batch, Long> batchColRetail;
    @FXML
    private TableColumn<Batch, Integer> batchColQty;
    @FXML
    private TableColumn<Batch, Batch.Status> batchColStatus;

    // others

    public BatchListView() throws IOException {
        controller = new BatchController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Scene scene = new Scene(loader.load());
        setScene(scene);
        setTitle(TITLE);
        updateDataSource(true);
        addBatchTableSelectedEvent();
        batchDeleteButton.setDisable(true);
        batchTable.setItems(dataSource);
        bindTableData();
    }

    private void addBatchTableSelectedEvent() {
        var selection = batchTable.getSelectionModel().getSelectedItems();

        selection.addListener((ListChangeListener<? super Batch>) c -> {
            System.out.println("Selection changed!");
            if (selection.isEmpty()) {
                batchDeleteButton.setDisable(true);
            } else {
                Batch item = batchTable.getSelectionModel().getSelectedItem();
//                System.out.println(item.getQuantity() < item.getImportQuantity() ||
//                        item.getStatus() == Batch.Status.EXPIRED ||
//                        item.getStatus() == Batch.Status.ORDERED);
                if (item.getQuantity() < item.getImportQuantity() ||
                        item.getStatus() == Batch.Status.EXPIRED ||
                        item.getStatus() == Batch.Status.ORDERED) {
                    batchDeleteButton.setDisable(true);
                    return;
                }
                batchDeleteButton.setDisable(false);
            }
        });
    }

    private void bindTableData() {
        batchColSku.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getSku()));
        batchColImpDate.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getImportDate().toString()));
        batchColImpPrice.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue().getImportPrice()).asObject());
        batchColRetail.setCellValueFactory(param ->
                new SimpleLongProperty(param.getValue().getRetailPrice()).asObject());
        batchColQty.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getQuantity()).asObject());
        batchColStatus.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getStatus()));
        batchTable.setRowFactory(tv -> {
            TableRow<Batch> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    Batch clickedRow = row.getItem();
                    createBatchDetailsView(clickedRow);
                }
            });
            return row;
        });
    }

    @FXML
    public void onAddButtonClicked(ActionEvent event) {
        createBatchCreateView();
    }

    private void createBatchCreateView() {
        try {
            Stage batchCreateWindow = new BatchCreateView();
            batchCreateWindow.initOwner(batchTable.getScene().getWindow());
            batchCreateWindow.initModality(Modality.WINDOW_MODAL);
            batchCreateWindow.show();
            batchCreateWindow.setOnCloseRequest(event -> {
                // update data of this
                updateDataSource(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage());
        }
    }

    @FXML
    public void onDeleteButtonClicked() {
        Dialog<ButtonType> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle("Confirm delete");
        confirmationDialog.setHeaderText("Are you sure want to delete the selected batch?");
        confirmationDialog.initOwner(batchTable.getScene().getWindow());
        confirmationDialog.initModality(Modality.WINDOW_MODAL);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (!result.isPresent() || result.get().equals(ButtonType.NO)) {
            return;
        }

        Dialog<?> loadingDialog = showLoadingDialog();
        Batch batch = batchTable.getSelectionModel().getSelectedItem();
        batchTable.getSelectionModel().clearSelection();
        try {
            controller.removeBatch(batch.getId());
            updateDataSource(true);
        } catch (RuntimeException e) {
            showAlertDialog(e.getMessage());
        }
        loadingDialog.close();
    }

    public void createBatchDetailsView(Batch batch) {
        try {
            Stage batchDetailsWindow = new BatchDetailsView(batch);
            batchDetailsWindow.initModality(Modality.WINDOW_MODAL);
            batchDetailsWindow.initOwner(batchTable.getScene().getWindow());
            batchDetailsWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occurred!");
        alert.setHeaderText(message);
        alert.show();
    }

    static void updateDataSource(boolean forceUpdate) {
        if (dataSource == null) {
            dataSource = controller.getBatchList();
            return;
        }
        if (forceUpdate) {
            ObservableList<Batch> batches = controller.getBatchList();
            batches.addListener((ListChangeListener<? super Batch>) c -> {
                dataSource.setAll(batches);
            });
        }
    }
}
