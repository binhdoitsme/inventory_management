package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import com.hanu.ims.util.configuration.Configuration;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static com.hanu.ims.util.modal.ModalService.showLoadingDialog;

public class LoginView extends Stage {

    private static final Logger logger = Logger.getLogger(LoginView.class.getName());
    private static final Map<Role, Class<? extends DashboardView>> landingViews
            = new HashMap<>() {
        {
            put(Role.Admin, AdminDashboardView.class);
            put(Role.Salesperson, SalespersonDashboardView.class);
            put(Role.InventoryManager, InventoryManagerDashboardView.class);
        }
    };
    private static final Pattern ACCEPTABLE_INPUT_REGEX = Pattern.compile("^[A-Za-z0-9!@#%_]{0,64}$");
    private static final String TITLE = Configuration.get("window.title.main");

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private AccountController accountController;

    public LoginView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        loader.setController(this);
        Parent sceneRoot = loader.load();
        Scene scene = new Scene(sceneRoot);
        setScene(scene);
        setTitle(TITLE);
        accountController = new AccountController();
    }

    @FXML
    public void initialize() {
        usernameField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // Your validation rules, anything you like
                    if (!ACCEPTABLE_INPUT_REGEX.asMatchPredicate().test(newValue))
                        // If newValue is not valid for your rules
                        ((StringProperty) observable).setValue(oldValue);
                }
        );
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
        Dialog<?> loadingDialog = showLoadingDialog(getOwner());

        String username = usernameField.getText();
        String password = passwordField.getText();

        logger.info("Executed login");
        logger.info(username + " " + password);

        Account account = validateAccount(username, password);

        loadingDialog.close();

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
        loginFailedAlert.setHeaderText("");
        loginFailedAlert.setContentText(longMessage);
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
