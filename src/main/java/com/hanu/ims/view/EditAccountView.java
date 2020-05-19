package com.hanu.ims.view;

import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditAccountView {
    private AccountController ac= new AccountController();

//    public TextField passwordField;
//    public TextField confirmPasswordField;
    Account account= AdminDashboardViewEventBinding.accountToEdit;

    public TextField usernameField;



    public Label statusLabel;

    public void initialize() {
        System.out.println(account);
        usernameField.setText(account.getUsername());
        usernameField.setDisable(true);
//        passwordField.setText(account.getPassword());


    }

    public void attemptEditAccount(ActionEvent actionEvent) {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//        String confirmPassword = confirmPasswordField.getText();
//        String role= account.getRole().name();

        Account updatedAccount = new Account(account.getId(), account.getUsername(), account.getUsername(), account.getRole());
        try {
            if (ac.updateAccount(updatedAccount)) {
                statusLabel.setText("Reset account's password to match username successfully");
            } else {
                statusLabel.setText("Failed to reset password??????");
            }

//                Account doppelgangerAccount = new Account(ac.validate(username, password).getId()
//                        , username, password, Role.valueOf(role));
//                AdminDashboardView.staticAccountList.add(doppelgangerAccount);


        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Failed to reset password!?!??!?!?!");
        }

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
