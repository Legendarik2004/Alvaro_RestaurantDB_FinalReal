package ui.screens.common;

import lombok.Getter;

@Getter
public enum Screens {

    PRINCIPAL("/fxml/principal.fxml"),
    CUSTOMERS("/fxml/customers/showCustomers.fxml"),
    DELETECUSTOMERS("/fxml/customers/deleteCustomer.fxml"),
    ADDCUSTOMERS("/fxml/customers/addCustomer.fxml"),
    UPDATECUSTOMERS("/fxml/customers/updateCustomer.fxml"),
    ORDERS("/fxml/orders/showOrders.fxml"),
    ADDORDERS("/fxml/orders/addOrder.fxml"),
    UPDATEORDERS("/fxml/orders/updateOrder.fxml"),
    DELETEORDERS("/fxml/orders/deleteOrder.fxml"),
    LOGIN("/fxml/login.fxml"),
    WELCOME("/fxml/welcome.fxml");

    private final String ruta;

    Screens(String ruta) {
        this.ruta = ruta;
    }
}
