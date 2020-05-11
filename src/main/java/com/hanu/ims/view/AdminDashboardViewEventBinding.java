package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import com.hanu.ims.controller.AccountController;
import com.hanu.ims.model.domain.Account;
import com.hanu.ims.model.domain.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;

public class AdminDashboardViewEventBinding {
    private AccountController controller;

    public AdminDashboardViewEventBinding() throws ClassNotFoundException {
        controller = new AccountController();
    }

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
    public TableColumn<Account, String> passwordColumn;
    @FXML
    public TableColumn<Account, Role> roleColumn;



    public static ObservableList<Account> staticAccountList= null;
    private static List<Account> accountList= null;
    public static Account accountToEdit= null;

//    private final List<Account> data =
//            FXCollections.observableArrayList(
//                    new Account(1,"Jacob", "Smith", Role.Admin),
//                    new Account(2,"Isabella", "Johnson", Role.InventoryManager),
//                    new Account(3,"Ethan", "Williams", Role.Salesperson)
//            );



    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Account, Integer>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<Account, Role>("role"));
        userTable.getItems().setAll(parseUserList());

        addEditButtonToTable();

        addDeleteButtonToTable();

    }

    private void addEditButtonToTable() {
        TableColumn<Account, Void> colBtn = new TableColumn("Edit");

        Callback<TableColumn<Account, Void>, TableCell<Account, Void>> cellFactory = new Callback<TableColumn<Account, Void>, TableCell<Account, Void>>() {
            @Override
            public TableCell<Account, Void> call(final TableColumn<Account, Void> param) {
                final TableCell<Account, Void> cell = new TableCell<Account, Void>() {

                    private final Button btn = new Button("Edit");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Account data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedEditData: " + data);
                            accountToEdit= data;
                            EditAccountPopup edit= new EditAccountPopup(userTable, data);
                            try {
                                edit.display();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        userTable.getColumns().add(colBtn);

    }

    private void addDeleteButtonToTable() {
        TableColumn<Account, Void> colBtn = new TableColumn("Delete");

        Callback<TableColumn<Account, Void>, TableCell<Account, Void>> cellFactory = new Callback<TableColumn<Account, Void>, TableCell<Account, Void>>() {
            @Override
            public TableCell<Account, Void> call(final TableColumn<Account, Void> param) {
                final TableCell<Account, Void> cell = new TableCell<Account, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Account data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                            if(controller.deleteAccount(data)){
                                deleteStatusLabel.setText("Deleted user "+ data.toString()+ " successfully");
                                refreshTable(event);
                            }
                            else deleteStatusLabel.setText("Delete user failed??? Wow! Probably it's due to FK constraints");
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        userTable.getColumns().add(colBtn);

    }

    @FXML
    public void showCreateAccountView(ActionEvent event) throws Exception{
        CreateAccountPopup pop= new CreateAccountPopup(userTable, userCountLabel);
        pop.display();
    }

    private ObservableList<Account> parseUserList() {
        accountList= controller.getAccountList();
        userCountLabel.setText(Integer.toString(accountList.size()));

        staticAccountList= FXCollections.observableArrayList(accountList);
        return staticAccountList;
    }

    public void refreshTable(ActionEvent actionEvent) {
        System.out.println("Refresh");
        userTable.getItems().clear();
        userTable.getItems().addAll(parseUserList());
        userCountLabel.setText(Integer.toString(accountList.size()));
    }
}


