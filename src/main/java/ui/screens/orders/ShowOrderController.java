package ui.screens.orders;


import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.User;
import services.CustomerService;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.security.Timestamp;

public class ShowOrderController extends BaseScreenController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final CustomerService customerService;

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
    @FXML
    public TableColumn<Integer, OrderItem> quantityItemColumn;
    @FXML
    public TableColumn<OrderItem, String> nameItemColumn;

    @FXML
    public ComboBox<String> filterComboBox;
    @FXML
    public ComboBox<Integer> customerOrderComboBox;
    @FXML
    public DatePicker dateField;
    @FXML
    public Button filterButton;
    @FXML
    public Label customernameLabel;
    @FXML
    public TableView<OrderItem> itemsTable;

    private Order selectedOrder;
    private User actualUser;


    @Inject
    public ShowOrderController(OrderService orderService, OrderItemService orderItemService, CustomerService customerService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.customerService = customerService;
    }


    public void initialize() {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));
        nameItemColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().getMenuItem().getName())));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ordersTable.setOnMouseClicked(this::handleTableClick);

        customerOrderComboBox.getItems().addAll(customerService.getAll().get().stream().map(Customer::getId).toList());

        customerOrderComboBox.setVisible(false);
        dateField.setVisible(false);
        filterButton.setVisible(false);
    }

    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Order newSelectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (newSelectedOrder != null) {
                this.selectedOrder = newSelectedOrder;
            }
        }
        setOrderItemTable();
    }

    @Override
    public void principalCargado() throws IOException {
        actualUser = getPrincipalController().getActualUser();

        if (actualUser.getId() < 0) {
            filterComboBox.getItems().addAll("Default", "Date", "Customer");
            customernameLabel.setVisible(true);
        } else {
            filterComboBox.getItems().addAll("Default", "Date");
            customernameLabel.setVisible(false);
        }
        setOrderTable();
    }

    private void setOrderTable() {
        ordersTable.getItems().clear();

        if (actualUser.getId() < 0) {
            orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                    .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));

        } else {
            orderService.getOrderOfCustomer(actualUser.getId()).peek(orders -> ordersTable.getItems().addAll(orders))
                    .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));
        }
    }


    private void setOrderItemTable() {
        if (selectedOrder != null) {
            orderItemService.getAllOrderItems(selectedOrder.getOrderId())
                    .peek(orderItems -> itemsTable.getItems().setAll(orderItems))
                    .peekLeft(noItems -> itemsTable.getItems().clear());
            customerService.getCustomerById(selectedOrder.getCustomerId())
                    .peek(customer -> customernameLabel.setText(customer.toStringSimplified()))
                    .peekLeft(noCustomer -> customernameLabel.setText(noCustomer.getMessage()));
        } else {
            itemsTable.getItems().clear();
        }
    }

    @FXML
    public void filter() {
        String selectedFilter = filterComboBox.getValue();

        if ("Date".equals(selectedFilter)) {
            dateField.setVisible(true);
            customerOrderComboBox.setVisible(false);
            filterButton.setVisible(true);

            filterButton.setOnAction(event -> {
                itemsTable.getItems().clear();
                if (dateField.getValue() != null) {
                    ordersTable.getItems().clear();
                    orderService.getAll()
                            .map(orders -> orders.stream()
                                    .filter(order -> order.getDate().toLocalDateTime().toLocalDate().equals(dateField.getValue()))
                                    .toList())
                            .peek(ordersTable.getItems()::addAll)
                            .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));
                    dateField.setValue(null);
                }

            });
        } else if ("Customer".equals(selectedFilter)) {
            dateField.setVisible(false);
            customerOrderComboBox.setVisible(true);
            filterButton.setVisible(true);

            filterButton.setOnAction(event -> {
                itemsTable.getItems().clear();
                if (customerOrderComboBox.getValue() != null) {

                    ordersTable.getItems().clear();
                    orderService.getAll()
                            .map(orders -> orders.stream()
                                    .filter(order -> {
                                        String customerIdAsString = String.valueOf(order.getCustomerId());
                                        return customerIdAsString.equals(customerOrderComboBox.getValue().toString());
                                    })
                                    .toList())
                            .peek(ordersTable.getItems()::addAll)
                            .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));
                }
            });
        } else {
            itemsTable.getItems().clear();
            setOrderTable();
        }
    }


}
