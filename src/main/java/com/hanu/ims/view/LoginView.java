package com.hanu.ims.view;

import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.logging.Logger;

public class LoginView {

    private static final Logger logger = Logger.getLogger(LoginView.class.getName());

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    private Scene dashboardPage;

    private AccountController accountController;

    public LoginView() {
        accountController = new AccountController();
    }

    @FXML
    public void createDashboardView(ActionEvent event) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        logger.info("Executed login");
        logger.info(username + " " + password);

        Account account = accountController.validate(username, password);

        if (account != null) {
            if (account.getRole() == Role.Admin) {
                URL resourceUrl = getClass().getResource("admindashboardview.fxml");
                FXMLLoader secondPageLoader = new FXMLLoader(resourceUrl);
                Parent secondPane = secondPageLoader.load();
                Scene secondScene = new Scene(secondPane, 1200, 800);
                setDashboardPage(secondScene);
                toDashboardPage(event);
            }
        } else {
            Alert loginFailedAlert = new Alert(Alert.AlertType.WARNING);
            String msg = "Login failed!";
            String longMessage = "Username or password is incorrect, please try again!";
            loginFailedAlert.setTitle(msg);
            loginFailedAlert.setHeaderText(longMessage);
            loginFailedAlert.show();
            logger.info("Login failed");
        }
    }


    @FXML
    public void setDashboardPage(Scene scene) {
        dashboardPage = scene;
    }

    @FXML
    private void toDashboardPage(ActionEvent event) {
        //label.setText("Hello World!");
        logger.info("Switch to dashboard page");
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(dashboardPage);
    }
}
