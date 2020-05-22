package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.io.IOException;

import static com.hanu.ims.util.modal.ModalService.showLoadingDialog;

public class AdminDashboardView extends DashboardView {
    private static final String LAYOUT_FILE = "admin_dashboard.fxml";

    @FXML
    private Label welcomeLabel;

    public AdminDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_FILE));
            loader.setController(this);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        setTitle();
        setWelcomeLabel(welcomeLabel);
    }

    public void onManageAccountsClicked() throws IOException {
        var stage = new AccountListView();
        var dialog = showLoadingDialog(this);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this);
        stage.show();
        dialog.close();
    }
}
