package com.hanu.ims.view;


import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

import static com.hanu.ims.util.modal.ModalService.*;


public class AccountCreateView extends Stage {
    private static final String TITLE = "Create New User";
    private static final String DEFAULT_PASSWORD = "1";

    private Label userCountLabel;
    private TableView<Account> userTable;

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public ChoiceBox roleChoiceBox;
    @FXML
    public Label statusLabel;

    private final AccountController ac = new AccountController();

    public AccountCreateView(TableView<Account> tableView, Label label) throws IOException {
        this.userTable = tableView;
        this.userCountLabel = label;

        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("account_create_view.fxml"));
        popupLoader.setController(this);
        Parent firstPane = popupLoader.load();
        Scene firstScene = new Scene(firstPane);
        setTitle(TITLE);
        setScene(firstScene);
    }

    public void onCreateAccount(ActionEvent actionEvent) throws Exception {
        String username = usernameField.getText();
//        String password = passwordField.getText();
//        String confirmPassword = confirmPasswordField.getText();
        String role = (String) roleChoiceBox.getValue();
        String password = DEFAULT_PASSWORD;

//        if (validateNewAccount(username, password, confirmPassword, role).equals("OK")) {
            Account account = new Account(username, password, Role.valueOf(role));
            var loadingDialog = showLoadingDialog(this);
            try {
                ac.createAccount(account);
//                statusLabel.setText("Added account successfully");
                if (account.getRole() != Role.Admin) {
                    Account doppelgangerAccount = new Account(ac.validate(username, password).getId()
                            , username, password, Role.valueOf(role), 0);
                    AccountListView.staticAccountList.add(doppelgangerAccount);
                }
                loadingDialog.close();
                showSuccessfulDialog();
                close();
            } catch (Exception ex) {
                ex.printStackTrace();
//                statusLabel.setText("Failed to add account to database");
                loadingDialog.close();
                showAlertDialog(ex.getMessage());
            }

//        } else statusLabel.setText(validateNewAccount(username, password, confirmPassword, role));
    }

//    private String validateNewAccount(String username, String password, String confirmPassword, String role) {
//        if (username.length() < 4) {
//            return "Username must be at least 4 characters";
//        } else if (password.length() < 4) {
//            return "Password must be at least 4 characters";
//        } else if (!password.equals(confirmPassword)) {
//            return "Password and repeated password does not match";
//        } else if (!role.equals("Admin")
//                && !role.equals("InventoryManager")
//                && !role.equals("Salesperson")) {
//            return "Impressive. How did you even mismatch the role?";
//        } else return "OK";
//    }

}