package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Order;
import model.OrderItem;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.security.Timestamp;


public class DeleteOrderController extends BaseScreenController {
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
    public TableColumn<Integer, OrderItem> idItemColumn;
    @FXML
    public TableColumn<OrderItem, String> nameItemColumn;
    @FXML
    public TableColumn<Integer, OrderItem> quantityItemColumn;

    private Order selectedOrder;


    @Inject
    public DeleteOrderController(OrderService orderService, OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    public void initialize() {
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateOrderColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerOrderColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tableOrderColumn.setCellValueFactory(new PropertyValueFactory<>("tableId"));

        idItemColumn.setCellValueFactory(new PropertyValueFactory<>("orderItemId"));
        nameItemColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().getMenuItem().getName())));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        ordersTable.setOnMouseClicked(this::handleTableClick);
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
        setOrderTable();
    }

    private void setOrderTable() {
        ordersTable.getItems().clear();
        orderService.getAll().peek(orders -> ordersTable.getItems().addAll(orders))
                .peekLeft(orderError -> getPrincipalController().showErrorAlert(orderError.getMessage()));

    }

    private void setOrderItemTable() {
        if (selectedOrder != null) {
            orderItemService.getAllOrderItems(selectedOrder.getOrderId())
                    .peek(orderItems -> itemsTable.getItems().setAll(orderItems))
                    .peekLeft(noItems -> itemsTable.getItems().clear());
        } else {
            itemsTable.getItems().clear();
        }
    }

    public void deleteOrder() {
        if (selectedOrder == null) {
            getPrincipalController().showErrorAlert(Constants.SELECT_ORDER_FIRST);
        } else {

            orderItemService.getAllOrderItems(selectedOrder.getOrderId())
                    .peek(orderItemsList -> orderItemsList.forEach(orderItemService::delete))
                    .peekLeft(noItems -> itemsTable.getItems().clear());

            orderService.delete(selectedOrder).peek(success -> {
                        if (success == 0) {
                            setOrderTable();
                            getPrincipalController().showConfirmationAlert(Constants.ORDER_DELETED_SUCCESSFULLY);
                        }
                    })
                    .peekLeft(customerError -> getPrincipalController().showErrorAlert(Constants.ERROR_DELETING_ORDER));
        }
    }
}
