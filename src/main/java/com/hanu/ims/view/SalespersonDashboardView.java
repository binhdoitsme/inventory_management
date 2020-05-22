package com.hanu.ims.view;

import com.hanu.ims.base.DashboardView;
import com.hanu.ims.util.authentication.AuthenticationProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import static com.hanu.ims.util.modal.ModalService.showLoadingDialog;

public class SalespersonDashboardView extends DashboardView {
    private static final String LAYOUT_FILE = "salesperson_dashboard.fxml";

    @FXML
    private AnchorPane container;
    @FXML
    private Label welcomeLabel;

    public SalespersonDashboardView() {
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

    public void initialize() {
        setTitle();
        setWelcomeLabel(welcomeLabel);
    }

    public void onCreateOrderClicked(ActionEvent actionEvent) throws IOException {
        Stage createOrderView = new OrderCreateView();
        container.getScene().getWindow().hide();
        createOrderView.setOnCloseRequest(event -> {
            Stage salesDashboard = new SalespersonDashboardView();
            ((Window) event.getTarget()).hide();
            salesDashboard.show();
        });
        createOrderView.show();
    }

    public void onManageOrdersClicked(ActionEvent actionEvent) throws IOException {
        try {
            var loadingDialog = showLoadingDialog(this);
            Stage createOrderView = new OrderListView();
            createOrderView.setOnCloseRequest(event -> {
                Stage salesDashboard = new SalespersonDashboardView();
                ((Window) event.getTarget()).hide();
                salesDashboard.show();
            });
            createOrderView.show();
            container.getScene().getWindow().hide();
            loadingDialog.close();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    protected void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("");
        alert.setContentText(msg);
        alert.setTitle("Unexpected error");
        alert.setOnCloseRequest(event -> {
            Platform.exit();
        });
        alert.show();
    }
}
