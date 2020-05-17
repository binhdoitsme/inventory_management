package com.hanu.ims.view;

import com.hanu.ims.controller.SupplierController;
import com.hanu.ims.model.domain.Batch;
import com.hanu.ims.model.domain.Supplier;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SupplierListView extends Stage {
    // constants
    private static final String FXML_FILE_NAME = "supplier_list_view.fxml";
    private static final String TITLE = "View Suppliers";

    // static
    private static ObservableList<Supplier> dataSource;
    private static SupplierController controller;

    // fxml
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, Integer> supplierColId;
    @FXML
    private TableColumn<Supplier, String> supplierColName;
    @FXML
    private TableColumn<Supplier, String> supplierColPhone;
    @FXML
    private TableColumn<Supplier, String> supplierColAddress;
    @FXML
    private TableColumn<Supplier, String> supplierColStatus;
    @FXML
    private Button supplierDeleteButton;
    @FXML
    private Button supplierAddButton;

    // data

    public SupplierListView() throws IOException {
        // init data
        controller = new SupplierController();
        updateDataSource(true);

        // init view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        Parent root = loader.load();
        setScene(new Scene(root));
        setTitle(TITLE);
        addTableSelectedEvent();
        supplierDeleteButton.setDisable(true);
        supplierTable.setItems(dataSource);
        bindTableData();
    }

    private void addTableSelectedEvent() {
        var selection = supplierTable.getSelectionModel().getSelectedItems();

        selection.addListener((ListChangeListener<? super Supplier>) c -> {
            if (selection.isEmpty()) {
                supplierDeleteButton.setDisable(true);
            } else {
                if (!supplierTable.getSelectionModel().getSelectedItem().isAvailable()) {
                    supplierDeleteButton.setDisable(true);
                    return;
                }
                supplierDeleteButton.setDisable(false);
            }
        });
    }

    private void bindTableData() {
        supplierColId.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getId()).asObject());
        supplierColName.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getName()));
        supplierColPhone.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getPhone()));
        supplierColAddress.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getAddress()));
        supplierColStatus.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().isAvailable() ? "AVAILABLE" : "UNAVAILABLE"));
        supplierTable.setRowFactory(tv -> {
            TableRow<Supplier> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    Supplier clickedRow = row.getItem();
                    createSupplierDetailsView(clickedRow);
                }
            });
            return row;
        });
    }

    public void createSupplierDetailsView(Supplier supplier) {
        try {
            Stage supplierDetailsWindow = new SupplierDetailsView(supplier);
            supplierDetailsWindow.initModality(Modality.WINDOW_MODAL);
            supplierDetailsWindow.initOwner(getScene().getWindow());
            supplierDetailsWindow.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDeleteButtonClicked() {
        Dialog<ButtonType> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle("Confirm delete");
        confirmationDialog.setHeaderText("Are you sure want to delete the selected supplier?");
        confirmationDialog.initOwner(getScene().getWindow());
        confirmationDialog.initModality(Modality.WINDOW_MODAL);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        Optional<ButtonType> result = confirmationDialog.showAndWait();
        if (!result.isPresent() || result.get().equals(ButtonType.NO)) {
            return;
        }

        Dialog<?> loadingDialog = showLoadingDialog();
        Supplier supplier = supplierTable.getSelectionModel().getSelectedItem();
        supplierTable.getSelectionModel().clearSelection();
        try {
            controller.invalidateSupplier(supplier);
            updateDataSource(true);
        } catch (RuntimeException e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage());
        }
        loadingDialog.close();
    }

    public void onAddButtonClicked() {
        createSupplierCreateView();
    }

    private void createSupplierCreateView() {
        try {
            Stage supplierCreateWindow = new SupplierCreateView();
            supplierCreateWindow.initOwner(getScene().getWindow());
            supplierCreateWindow.initModality(Modality.WINDOW_MODAL);
            supplierCreateWindow.show();
            supplierCreateWindow.setOnCloseRequest(event -> {
                // update data of this
                updateDataSource(false);
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage());
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
            dataSource = controller.getSupplierList();
            return;
        }
        if (forceUpdate) {
            ObservableList<Supplier> suppliers = controller.getSupplierList();
            suppliers.addListener((ListChangeListener<? super Supplier>) c -> {
                dataSource.setAll(suppliers);
            });
        }
    }
}
