package com.hanu.ims.base;

import com.hanu.ims.util.authentication.AuthenticationProvider;
import com.hanu.ims.util.configuration.Configuration;
import com.hanu.ims.view.LoginView;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class DashboardView extends Stage {
    private static final String TITLE = Configuration.get("window.title.main");

    protected void setTitle() {
        setTitle(TITLE);
    }

    public void onLogoutClicked(ActionEvent actionEvent) throws IOException {
        close();
        new LoginView().show();
        AuthenticationProvider.getInstance().setCurrentAccount(null);
    }

    public void onExit(ActionEvent actionEvent) {
        showExitAlert();
    }

    private void showExitAlert() {
        String msg = "Goodbye!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(msg);
        alert.setHeaderText("");
        alert.setContentText(msg);
        alert.setOnCloseRequest(event -> {
            getScene().getWindow().hide();
        });
        alert.show();
    }


    protected Dialog<?> showLoadingDialog() {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        loadingDialog.show();
        return loadingDialog;
    }
}
