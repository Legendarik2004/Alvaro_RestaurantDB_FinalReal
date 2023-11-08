package ui.screens.customers;

import common.Constants;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.Order;
import services.CustomerService;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.security.Timestamp;
import java.time.LocalDate;

public class DeleteCustomerController extends BaseScreenController {
    public static final String SEGURO_QUE_QUIERES_ELIMINAR = "¿Seguro que quieres eliminar?";
    private final CustomerService customerService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Integer, Customer> idCustomerColumn;
    @FXML
    public TableColumn<String, Customer> firstnameCustomerColumn;
    @FXML
    public TableColumn<String, Customer> lastnameCustomerColumn;
    @FXML
    public TableColumn<String, Customer> emailCustomerColumn;
    @FXML
    public TableColumn<String, Customer> phoneCustomerColumn;
    @FXML
    public TableColumn<LocalDate, Customer> dobCustomerColumn;
    @FXML
    public TableView<Order> ordersTable;
    @FXML
    public TableColumn<Integer, Order> idOrderColumn;
    @FXML
    public TableColumn<Timestamp, Order> dateOrderColumn;
    @FXML
    public TableColumn<Integer, Order> customerOrderColumn;
    @FXML
    public TableColumn<Integer, Order> tableOrderColumn;
    private Customer selectedCustomer;

    @Inject
    public DeleteCustomerController(CustomerService customerService, OrderService orderService, OrderItemService orderItemService) {
        this.customerService = customerService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    public void initialize() {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        customersTable.setOnMouseClicked(this::handleTableClick);
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Customer newSelectedCustomer = customersTable.getSelectionModel().getSelectedItem();
            if (newSelectedCustomer != null) {
                this.selectedCustomer = newSelectedCustomer;
            }
        }
        setOrderTable();
    }

    @Override
    public void principalCargado() throws IOException {
        setCustomerTable();
    }

    private void setCustomerTable() {
        customersTable.getItems().clear();
        customerService.getAll().peek(customers -> customersTable.getItems().addAll(customers))
                .peekLeft(customerError -> getPrincipalController().showErrorAlert(customerError.getMessage()));
    }

    private void setOrderTable() {
        ordersTable.getItems().clear();
        if (selectedCustomer != null) {
            ordersTable.getItems().addAll(orderService.getOrderOfCustomer(selectedCustomer.getId()).get());
        }
    }

    public void deleteCustomerOrder() {
        if (selectedCustomer == null) {
            getPrincipalController().showErrorAlert(Constants.SELECT_CUSTOMER_FIRST);
        } else {
            if (!ordersTable.getItems().isEmpty()) {
                getPrincipalController().showConfirmationAlert(SEGURO_QUE_QUIERES_ELIMINAR);
                getPrincipalController().getAlert().showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        orderService.getOrderOfCustomer(selectedCustomer.getId()).peek(orders -> orders.forEach(
                                order -> orderItemService.getAllOrderItems(order.getOrderId())
                                        .peek(orderItemsList -> {
                                            orderItemsList.forEach(orderItemService::delete);
                                            orderService.delete(order).peek(orderDeleted -> {
                                                if (orderDeleted == 0) {
                                                    deleteCustomer(true);}
                                            });
                                        })
                        ));
                    }
                });
            } else {
                deleteCustomer(false);
            }
        }
    }

    public void deleteCustomer(boolean hayOrders) {

        customerService.delete(selectedCustomer).peek(success -> {
                    if (success == 0) {
                        setCustomerTable();
                        if (hayOrders){
                            setOrderTable();
                        }
                        getPrincipalController().showConfirmationAlert(Constants.CUSTOMER_DELETED_SUCCESSFULLY);
                    }
                })
                .peekLeft(customerError -> getPrincipalController().showErrorAlert(Constants.ERROR_DELETING_CUSTOMER));
    }

}
