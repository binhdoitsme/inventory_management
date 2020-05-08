package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LoginViewEventBinding {

    private static final Logger logger = Logger.getLogger(LoginViewEventBinding.class.getName());

    private static final Map<Role, Class<? extends DashboardView>> landingViews
            = new HashMap<Role, Class<? extends DashboardView>>() {
        {
            put(Role.Admin, AdminDashboardView.class);
            put(Role.Salesperson, SalespersonDashboardView.class);
        }
    };

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    private Scene dashboardPage;

    private AccountController accountController;

    public LoginViewEventBinding() {
        accountController = new AccountController();
    }

    @FXML
    public void onLoginClicked(ActionEvent event) throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        logger.info("Executed login");
        logger.info(username + " " + password);

        Account account = validateAccount(username, password);

        if (account != null) {
            DashboardView dashboardView = createDashboardView(account);
            dashboardView.show();
        } else {
            showErrorDialog();
        }
    }

    private void showErrorDialog() {
        Alert loginFailedAlert = new Alert(Alert.AlertType.WARNING);
        String msg = "Login failed!";
        String longMessage = "Username or password is incorrect, please try again!";
        loginFailedAlert.setTitle(msg);
        loginFailedAlert.setHeaderText(longMessage);
        loginFailedAlert.show();
        logger.info("Login failed");
    }

    private Account validateAccount(String username, String password) {
        return accountController.validate(username, password);
    }

    private DashboardView createDashboardView(Account account) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Role accountRole = account.getRole();
        return landingViews.get(accountRole).getConstructor().newInstance();
    }
}
