package com.hanu.ims.view;


import com.hanu.ims.model.domain.Account;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class CreateAccountPopup {

    private Label userCountLabel;
    private TableView<Account> userTable;

    CreateAccountPopup() {
        super();
    }

    CreateAccountPopup(TableView<Account> tableView, Label label) {
        this.userTable = tableView;
        this.userCountLabel = label;
    }

    public void display() throws Exception {
        Stage popupWindow = new Stage();

        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("newaccountpopup.fxml"));
        Parent firstPane = popupLoader.load();
        Scene firstScene = new Scene(firstPane, 400, 800);

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Create New User");

        popupWindow.setScene(firstScene);

        popupWindow.setOnHiding(event -> {
            System.out.println("Closing Stage");
            userTable.getItems().clear();
            userTable.getItems().addAll(AdminDashboardViewEventBinding.staticAccountList);
            userCountLabel.setText(Integer.toString(AdminDashboardViewEventBinding.staticAccountList.size()));
        });

        popupWindow.showAndWait();

    }

}