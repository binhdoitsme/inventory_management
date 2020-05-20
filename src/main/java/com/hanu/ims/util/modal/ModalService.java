package com.hanu.ims.util.modal;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Window;

import java.util.Optional;

public class ModalService {
    public static void showAlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occurred!");
        alert.setHeaderText(message);
        alert.show();
    }

    public static void showSuccessfulDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText("The operation completed successfully!");
        alert.show();
    }

    public static Dialog<?> showLoadingDialog(Window window) {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(window);
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }

    public static <T> Optional<T> showConfirmationDialog(String message, Window ownerWindow) {
        Dialog<T> confirmationDialog = new Dialog<>();
        confirmationDialog.setTitle("Confirm delete");
        confirmationDialog.setHeaderText(message);
        confirmationDialog.initOwner(ownerWindow);
        confirmationDialog.initModality(Modality.WINDOW_MODAL);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        confirmationDialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        return confirmationDialog.showAndWait();
    }

    public static void showAlertDialog(String message, String title, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.show();
    }
}
