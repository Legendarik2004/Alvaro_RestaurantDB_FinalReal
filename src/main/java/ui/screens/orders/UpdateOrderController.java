package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Credentials;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class UpdateOrderController extends BaseScreenController {

    public static final String MENU_ITEMS = "Menu items";
    private final OrderService orderService;
    private final OrderItemService orderItemService;
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
    public TableView<OrderItem> itemsTable;
    @FXML
    public TableColumn<OrderItem, String> nameItemColumn;
    @FXML
    public TableColumn<Integer, OrderItem> quantityItemColumn;
    @FXML
    public DatePicker dateField;
    @FXML
    public TextField tableOrderField;
    @FXML
    public TextField customerOrderField;
    @FXML
    public TextField quantityItemField;
    @FXML
    public ComboBox<String> itemsComboBox;
    private Order selectedOrder;
    private OrderItem selectedOrderItem;
    private Credentials actualCredentials;

    @Inject
    public UpdateOrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    public void initialize() {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));

        nameItemColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().getMenuItem().getName())));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ordersTable.setOnMouseClicked(this::handleOrderTableClick);
        itemsTable.setOnMouseClicked(this::handleItemTableClick);
        itemsComboBox.getItems().addAll(orderItemService.getAllMenuItems().get().stream().map(MenuItem::getName).toList());
        customerOrderField.setDisable(true);
        dateField.setDisable(true);
    }

    private void handleOrderTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            Order newSelectedOrder = ordersTable.getSelectionModel().getSelectedItem();
            this.selectedOrder = newSelectedOrder;

            if (newSelectedOrder != null) {
                tableOrderField.setText(String.valueOf(newSelectedOrder.getTableId()));
                customerOrderField.setText(String.valueOf(newSelectedOrder.getCustomerId()));
                dateField.setValue(newSelectedOrder.getDate().toLocalDateTime().toLocalDate());
            }
        }
        setOrderItemTable();
    }

    private void handleItemTableClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            OrderItem newSelectedOrderItem = itemsTable.getSelectionModel().getSelectedItem();
            if (newSelectedOrderItem != null) {
                this.selectedOrderItem = newSelectedOrderItem;

            }
        }

    }

    @Override
    public void principalCargado() throws IOException {
        actualCredentials = getPrincipalController().getActualCredentials();
        setTables();
    }

    private void setTables() {
        ordersTable.getItems().clear();
        if (actualCredentials.getId() < 0) {
            orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                    .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));

        } else {
            orderService.getOrderOfCustomer(actualCredentials.getId()).peek(orders -> ordersTable.getItems().addAll(orders))
                    .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));
        }
        itemsTable.getItems().clear();
        itemsComboBox.setDisable(true);
    }

    private void setOrderItemTable() {
        if (selectedOrder != null) {
            orderItemService.getAll(selectedOrder.getOrderId())
                    .peek(orderItems -> itemsTable.getItems().setAll(orderItems))
                    .peekLeft(noItems -> itemsTable.getItems().clear());
            itemsComboBox.setDisable(false);
        } else {
            itemsTable.getItems().clear();
        }
    }

    public void updateOrder() {
        if (tableOrderField.getText().isEmpty()) {
            getPrincipalController().showErrorAlert(Constants.EMPTY_FIELD);
        } else {
            List<OrderItem> orderItems = itemsTable.getItems().stream().toList();
            orderService.update(new Order(selectedOrder.getOrderId(), Timestamp.from(Instant.now()), 0, Integer.parseInt(tableOrderField.getText()),orderItems)).peek(success -> {
                        if (success == 0) {

                            orderItemService.getAll(selectedOrder.getOrderId())
                                    .peek(orderItemsList -> orderItemsList.forEach(orderItemService::delete))
                                    .peekLeft(noItems -> itemsTable.getItems().clear());

                            for (OrderItem orderItem : itemsTable.getItems().stream().toList()) {
                                orderItem.setOrderId(selectedOrder.getOrderId());
                                orderItemService.save(orderItem);
                            }
                            setTables();
                            getPrincipalController().showConfirmationAlert(Constants.ORDER_UPDATED_SUCCESSFULLY);

                        }
                    })
                    .peekLeft(customerError -> getPrincipalController().showErrorAlert(Constants.ERROR_UPDATING_ORDER));
        }
    }

    @FXML
    public void addItem() {
        if (selectedOrder == null) {
            getPrincipalController().showErrorAlert(Constants.SELECT_ORDER_FIRST);
        } else {
            if (quantityItemField.getText().isEmpty() || itemsComboBox.getValue().equals(MENU_ITEMS)) {
                getPrincipalController().showErrorAlert(Constants.EMPTY_FIELD);
            } else {
                MenuItem add = orderItemService.getAllMenuItems().get().stream()
                        .filter(menuItem -> menuItem.getName().equals(itemsComboBox.getValue()))
                        .findFirst().orElse(null);
                if (add != null) {
                    itemsTable.getItems().add(new OrderItem(0, selectedOrder.getOrderId(), Integer.parseInt(quantityItemField.getText()), add));
                    quantityItemField.clear();
                    getPrincipalController().showConfirmationAlert(Constants.ITEM_ADDED_SUCCESSFULLY);
                } else {
                    getPrincipalController().showErrorAlert(Constants.ERROR_ADDING_ITEM);
                }
            }
        }
    }

    @FXML
    public void removeItem() {
        if (itemsTable.getItems().remove(selectedOrderItem)) {
            getPrincipalController().showConfirmationAlert(Constants.ITEM_REMOVED_SUCCESSFULLY);
            selectedOrderItem = null;
        } else {
            getPrincipalController().showErrorAlert(Constants.ERROR_DELETING_ITEM);
        }
    }
}
