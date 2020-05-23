package com.hanu.ims.view;

import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.time.Instant;

import static com.hanu.ims.util.modal.ModalService.*;

public class ChangePasswordView extends Stage {
    private static final String FXML_FILE_NAME = "change_password.fxml";
    private static final String TITLE = "Change password";

    // data fields
    private final AccountController controller;

    // FXML fields
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField newPasswordConfirm;

    public ChangePasswordView() throws IOException {
        // init data
        controller = new AccountController();

        // init view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_NAME));
        loader.setController(this);
        setScene(new Scene(loader.load()));
        setTitle(TITLE);
    }

    @FXML
    public void onSubmit(ActionEvent actionEvent) {
        String currentPasswordValue = currentPassword.getText();
        String newPasswordValue = newPassword.getText();
        String newPasswordConfirmValue = newPasswordConfirm.getText();

        Account currentAccount = AuthenticationProvider.getInstance().getCurrentAccount();

        if (currentPasswordValue.isEmpty()
                || newPasswordValue.isEmpty()
                || newPasswordConfirmValue.isEmpty()) {
            showAlertDialog("You must fill into every fields!");
            return;
        }

        String hashedCurrentPass = DigestUtils.sha256Hex(currentPasswordValue);
        if (!hashedCurrentPass.equals(currentAccount.getPassword())) {
            showAlertDialog("Current password is incorrect!");
            return;
        }

        if (!newPasswordConfirmValue.equals(newPasswordValue)) {
            showAlertDialog("New passwords do not match!");
            return;
        }

        if (newPasswordValue.equals(currentPasswordValue)) {
            showAlertDialog("Why on earth do you change for the same password?");
            return;
        }

        if (newPasswordValue.length() <= 6) {
            showAlertDialog("Password is too short, please make it more than 6 characters!");
            return;
        }

        currentAccount.setPassword(DigestUtils.sha256Hex(newPasswordValue));
        currentAccount.setLastLogin(Instant.now().getEpochSecond() * 1000);
        try {
            showLoadingDialog(this);
            controller.updateAccount(currentAccount);
            showSuccessfulDialog();
            close();
        } catch (RuntimeException e) {
            e.printStackTrace();
            showAlertDialog(e.getMessage());
        }
    }
}
