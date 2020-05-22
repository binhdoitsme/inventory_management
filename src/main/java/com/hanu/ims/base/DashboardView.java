package com.hanu.ims.base;

import com.hanu.ims.util.authentication.AuthenticationProvider;
import com.hanu.ims.util.configuration.Configuration;
import com.hanu.ims.view.ChangePasswordView;
import com.hanu.ims.view.LoginView;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
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

    public void onChangePasswordChosen(ActionEvent event) throws IOException {
        Stage changePasswordWindow = new ChangePasswordView();
        changePasswordWindow.initModality(Modality.WINDOW_MODAL);
        changePasswordWindow.initOwner(this);
        changePasswordWindow.show();
    }

    protected void setWelcomeLabel(Label welcomeLabel) {
        welcomeLabel.setText("Welcome, "
                + AuthenticationProvider.getInstance().getCurrentAccount().getUsername() + "!");
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
}
