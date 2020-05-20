package com.hanu.ims.view;

import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountView {
    //    private Scene dashboardPage;
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
    AccountController ac = new AccountController();

    public CreateAccountView() throws ClassNotFoundException {
    }


    public void attemptCreateAccount(ActionEvent actionEvent) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = (String) roleChoiceBox.getValue();
//
//        System.out.println(username);
//        System.out.println(password);
//        System.out.println(confirmPassword);
//        System.out.println(role);

        if (validateNewAccount(username, password, confirmPassword, role).equals("OK")) {
            Account account = new Account(username, password, Role.valueOf(role));
            try {
                ac.createAccount(account);

                statusLabel.setText("Added account successfully");
//                LoginViewEventBinding lv = new LoginViewEventBinding();
//                lv.createDashboardView(actionEvent);
//                AdminDashboardView adw= new AdminDashboardView();
//                FXMLLoader secondPageLoader = new FXMLLoader(getClass().getResource("/admin_dashboard.fxml"));
//                Parent secondPane = secondPageLoader.load();
//                Scene secondScene = new Scene(secondPane, 1200, 800);
//                setDashboardPage(secondScene);
//                toDashboardPage(actionEvent);
                if(account.getRole()!=Role.Admin) {
                    Account doppelgangerAccount= new Account(ac.validate(username,password).getId()
                            , username, password, Role.valueOf(role), 0);
                    AdminDashboardViewEventBinding.staticAccountList.add(doppelgangerAccount);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("Failed to add account to database");
            }

        } else statusLabel.setText(validateNewAccount(username, password, confirmPassword, role));
    }

    private String validateNewAccount(String username, String password, String confirmPassword, String role) {
        if (username.length() < 4) {
            return "Username must be at least 4 characters";
        } else if (password.length() < 4) {
            return "Password must be at least 4 characters";
        } else if (!password.equals(confirmPassword)) {
            return "Password and repeated password does not match";
        } else if (!role.equals("Admin")
                && !role.equals("InventoryManager")
                && !role.equals("Salesperson")) {
            return "Impressive. How did you even mismatch the role?";
        } else return "OK";
    }

//    @FXML
//    public void setDashboardPage(Scene scene) {
//        dashboardPage = scene;
//    }
//
//    @FXML
//    private void toDashboardPage(ActionEvent event) {
//        //label.setText("Hello World!");
//        System.out.println("Switch to dashboard page");
//        Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//        primaryStage.setScene(dashboardPage);
//    }
}
