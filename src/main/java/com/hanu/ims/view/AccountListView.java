package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.hanu.ims.util.modal.ModalService.*;

public class AccountListView extends Stage {
    private static final String LAYOUT_FILE = "account_list_view.fxml";
    private static final String DEFAULT_PASSWORD = "1";

    private AccountController controller;

    @FXML
    public Label userCountLabel;
    @FXML
    public Label deleteStatusLabel;
    @FXML
    public TableView<Account> userTable;
    @FXML
    public TableColumn<Account, Integer> idColumn;
    @FXML
    public TableColumn<Account, String> usernameColumn;
    @FXML
    public TableColumn<Account, Role> roleColumn;

    public static ObservableList<Account> staticAccountList = null;
    private static List<Account> accountList = null;
    static Account accountToEdit = null;

    private final Callback<TableColumn<Account, Void>, TableCell<Account, Void>> resetPasswordCellFatory
            = param -> new TableCell<>() {

        private final Button btn = new Button("Reset password");

        private final EventHandler<ActionEvent> clickHandler = (event) -> {
            Account data = getTableView().getItems().get(getIndex());
            resetPassword(data);
        };

        @Override
        public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(btn);
            }

        }

        {
            btn.setOnAction(clickHandler);
        }
    };

    private final Callback<TableColumn<Account, Void>, TableCell<Account, Void>> deleteCellFactory
            = param -> new TableCell<>() {
        private final Button btn = new Button("Delete");
        private final EventHandler<ActionEvent> clickHandler = (event) -> {
            Optional<ButtonType> confirmationResult = showDeleteConfirmationDialog();
            if (!confirmationResult.isPresent() || confirmationResult.get().equals(ButtonType.NO)) {
                return;
            }
            Account data = getTableView().getItems().get(getIndex());
            System.out.println("selectedData: " + data);
            if (controller.deleteAccount(data)) {
                deleteStatusLabel.setText("Deleted user " + data.toString() + " successfully");
                refreshTable();
            } else {
                deleteStatusLabel.setText("Delete user failed??? Wow! Probably it's due to FK constraints");
            }
        };

        @Override
        public void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(btn);
            }

        }

        {
            btn.setOnAction(clickHandler);
        }
    };

    public AccountListView() throws IOException {
        // init data
        controller = new AccountController();

        // init view
        FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_FILE));
        loader.setController(this);
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        setScene(scene);
    }

    private Optional<ButtonType> showDeleteConfirmationDialog() {
        return showConfirmationDialog("Are you sure to delete this account?", this);
    }

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Account, Integer>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<Account, Role>("role"));
        userTable.getItems().setAll(parseUserList());
        addEditButtonToTable();
        addDeleteButtonToTable();
    }

    private void addEditButtonToTable() {
        TableColumn<Account, Void> colBtn = new TableColumn("Reset password");
        colBtn.setCellFactory(resetPasswordCellFatory);
        userTable.getColumns().add(colBtn);
    }

    private void addDeleteButtonToTable() {
        TableColumn<Account, Void> colBtn = new TableColumn("Delete");
        colBtn.setCellFactory(deleteCellFactory);
        userTable.getColumns().add(colBtn);

    }

    @FXML
    public void showCreateAccountView(ActionEvent event) throws Exception {
        AccountCreateView pop = new AccountCreateView(userTable, userCountLabel);
        pop.setOnCloseRequest(ev -> refreshTable());
        pop.setOnHiding(ev -> refreshTable());
        pop.initModality(Modality.APPLICATION_MODAL);
        pop.initOwner(this);
        pop.show();
    }

    private ObservableList<Account> parseUserList() {
        accountList = controller.getAccountList();
        userCountLabel.setText(Integer.toString(accountList.size()));

        staticAccountList = FXCollections.observableArrayList(accountList);
        return staticAccountList;
    }

    public void refreshTable() {
        userTable.getItems().clear();
        userTable.getItems().addAll(parseUserList());
        userCountLabel.setText(Integer.toString(accountList.size()));
    }

    private void resetPassword(Account account) {
        String username = account.getUsername();
        String password = DigestUtils.sha256Hex(DEFAULT_PASSWORD);
        String role = account.getRole().name();

        Account updatedAccount = new Account(account.getId(), username, password, Role.valueOf(role), -1);
        Dialog<?> loading = showLoadingDialog(this);
        try {
            controller.updateAccount(updatedAccount);
            loading.close();
            showSuccessfulDialog();
            refreshTable();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlertDialog(ex.getMessage());
            loading.close();
        }
    }
}
