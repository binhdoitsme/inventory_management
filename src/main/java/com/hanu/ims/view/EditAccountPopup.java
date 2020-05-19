package com.hanu.ims.view;


import com.hanu.ims.model.domain.Account;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EditAccountPopup {

    private Account account;
    private TableView<Account> userTable;

    EditAccountPopup(){
        super();
    }

    EditAccountPopup(TableView<Account> tableView, Account account) {
        this.userTable = tableView;
        this.account= account;
    }

    public void display() throws Exception {
        Stage popupWindow = new Stage();

        FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("editaccountpopup.fxml"));
        Parent firstPane = popupLoader.load();
        Scene firstScene = new Scene(firstPane, 400, 800);

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle("Reset Password Confirmation");

        popupWindow.setScene(firstScene);

        popupWindow.setOnHiding( event -> {
            System.out.println("Closing Edit Stage");
            userTable.refresh();
        } );

        popupWindow.showAndWait();

    }

}