package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class LoginViewEventBinding {

    private static final Logger logger = Logger.getLogger(LoginViewEventBinding.class.getName());

    private static final Map<Role, Class<? extends DashboardView>> landingViews
            = new HashMap<>() {
        {
            put(Role.Admin, AdminDashboardView.class);
            put(Role.Salesperson, SalespersonDashboardView.class);
        }
    };

    private static final Pattern ACCEPTABLE_INPUT_REGEX = Pattern.compile("^A-Za-z0-9!@#%_$");

    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;

    private AccountController accountController;

    public LoginViewEventBinding() {
        accountController = new AccountController();
    }

    @FXML
    void onKeyTyped(KeyEvent event) {
//        String inputChar = event.getCharacter();
//        System.out.println(inputChar);
//        if (!ACCEPTABLE_INPUT_REGEX.matcher(inputChar).find()) {
//            event.consume();
//        }
    }

    @FXML
    public void onKeyPress(KeyEvent event) throws Exception {
        switch (event.getCode()) {
            case ENTER:
                onLoginClicked();
        }
    }

    @FXML
    public void onLoginClicked() throws Exception {
        Dialog<?> loadingDialog = showLoadingDialog();

        String username = usernameField.getText();
        String password = passwordField.getText();

        logger.info("Executed login");
        logger.info(username + " " + password);

        Account account = validateAccount(username, password);

        loadingDialog.hide();

        if (account != null) {
            AuthenticationProvider.getInstance().setCurrentAccount(account);
            DashboardView dashboardView = createDashboardView(account);
            dashboardView.show();
            usernameField.getScene().getWindow().hide();
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

    private Dialog<?> showLoadingDialog() {
        Dialog<String> loadingDialog = new Dialog<>();
        loadingDialog.initModality(Modality.WINDOW_MODAL);
        loadingDialog.initOwner(usernameField.getScene().getWindow());
        loadingDialog.setHeaderText("Please wait...");
        loadingDialog.show();
        return loadingDialog;
    }

    private Account validateAccount(String username, String password) {
        return accountController.validate(username, password);
    }

    private DashboardView createDashboardView(Account account) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Role accountRole = account.getRole();
        return landingViews.get(accountRole).getConstructor().newInstance();
    }
}
