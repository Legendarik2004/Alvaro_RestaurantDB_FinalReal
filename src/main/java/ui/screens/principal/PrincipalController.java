package ui.screens.principal;


import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import model.Credentials;
import ui.screens.common.BaseScreenController;
import ui.screens.common.Screens;

import java.io.IOException;

@Data
@Log4j2
public class PrincipalController {
    private Alert alert;
    private Credentials actualCredentials;
    @FXML
    public BorderPane root;
    Instance<Object> instance;
    private Stage primaryStage;
    @FXML
    private MenuBar menuPrincipal;
    @FXML
    private Menu menuCustomers;
    @FXML
    private MenuItem addOrder;
    @FXML
    private MenuItem deleteOrder;


    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
    }

    private void cargarPantalla(Screens pantalla) {
        cambioPantalla(cargarPantalla(pantalla.getRuta()));
    }

    private Pane cargarPantalla(String ruta) {
        Pane panePantalla = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            panePantalla = fxmlLoader.load(getClass().getResourceAsStream(ruta));
            BaseScreenController pantallaController = fxmlLoader.getController();
            pantallaController.setPrincipalController(this);
            pantallaController.principalCargado();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return panePantalla;
    }

    public void showErrorAlert(String mensaje) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.getDialogPane().setId("alert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("btn-ok");
        alert.showAndWait();
    }

    public void showConfirmationAlert(String mensaje) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.setHeaderText(mensaje);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("btn-ok");
        alert.showAndWait();
    }

    public void onLoginDone(Credentials credentials) {
        actualCredentials = credentials;
        menuPrincipal.setVisible(true);
        cargarPantalla(Screens.WELCOME);

        if (actualCredentials.getId() < 0) {
            menuCustomers.setVisible(true);
            addOrder.setVisible(false);
            deleteOrder.setVisible(true);
        } else {
            menuCustomers.setVisible(false);
            addOrder.setVisible(true);
            deleteOrder.setVisible(false);
        }
    }


    public void logout() {
        actualCredentials = null;
        menuPrincipal.setVisible(false);
        cargarPantalla(Screens.LOGIN);
    }

    private void cambioPantalla(Pane pantallaNueva) {
        root.setCenter(pantallaNueva);
    }


    public void initialize() {
        menuPrincipal.setVisible(false);
        cargarPantalla(Screens.LOGIN);
    }

    public void exit() {
        primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    public void menuCustomers(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case "listCustomers" -> cargarPantalla(Screens.CUSTOMERS);
            case "addCustomer" -> cargarPantalla(Screens.ADDCUSTOMERS);
            case "updateCustomer" -> cargarPantalla(Screens.UPDATECUSTOMERS);
            case "deleteCustomer" -> cargarPantalla(Screens.DELETECUSTOMERS);
            default ->
                    throw new IllegalStateException("Unexpected value: " + ((MenuItem) actionEvent.getSource()).getId());
        }
    }

    public void menuOrders(ActionEvent actionEvent) {
        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case "listOrders" -> cargarPantalla(Screens.ORDERS);
            case "addOrder" -> cargarPantalla(Screens.ADDORDERS);
            case "updateOrder" -> cargarPantalla(Screens.UPDATEORDERS);
            case "deleteOrder" -> cargarPantalla(Screens.DELETEORDERS);
            default ->
                    throw new IllegalStateException("Unexpected value: " + ((MenuItem) actionEvent.getSource()).getId());
        }
    }
}
