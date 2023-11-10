package ui.screens.orders;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Credentials;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.Table;
import services.OrderItemService;
import services.OrderService;
import services.TablesService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class AddOrderController extends BaseScreenController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @FXML
    public TableView<OrderItem> itemsTable;
    @FXML
    public TableColumn<OrderItem, String> menuItemColumn;
    @FXML
    public TableColumn<Integer, OrderItem> quantityItemColumn;
    @FXML
    public ComboBox<String> itemsComboBox;
    @FXML
    public TextField quantityItemField;
    @FXML
    public ComboBox<Integer> tableComboBox;
    private OrderItem selectedOrderItem;
    private Credentials actualCredentials;
    private TablesService tablesService;

    @Inject
    public AddOrderController(OrderService orderService, OrderItemService orderItemService, TablesService tablesService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.tablesService = tablesService;
    }

    public void initialize() {
        menuItemColumn.setCellValueFactory((cellData -> new SimpleStringProperty(cellData.getValue().getMenuItem().getName())));
        quantityItemColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        itemsTable.setOnMouseClicked(this::handleTableClick);

        tableComboBox.getItems().addAll(tablesService.getAll().get().stream().map(Table::getTableId).toList());
        itemsComboBox.getItems().addAll(orderItemService.getAllMenuItems().get().stream().map(MenuItem::getName).toList());
    }

    private void handleTableClick(MouseEvent event) {
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
        setTable();
    }

    private void setTable() {
        itemsTable.getItems().clear();
    }

    @FXML
    public void addOrder() {
        if (tableComboBox.getValue() == null || itemsTable.getItems().isEmpty()) {
            getPrincipalController().showErrorAlert(Constants.EMPTY_FIELD);
        } else {
            List<OrderItem> orderItemList = itemsTable.getItems().stream().toList();
            orderService.save(new Order(0, Timestamp.from(Instant.now()), actualCredentials.getId(), tableComboBox.getValue(), orderItemList)).peek(success -> {

                        if (success == 0) {
                            setTable();
                            getPrincipalController().showConfirmationAlert(Constants.ORDER_ADDED_SUCCESSFULLY);
                        }
                    })
                    .peekLeft(customerError -> getPrincipalController().showErrorAlert(Constants.ERROR_ADDING_ORDER));
        }
    }

    @FXML
    public void addItem() {
        if (quantityItemField.getText().isEmpty() || itemsComboBox.getValue().equals("Menu items")) {
            getPrincipalController().showErrorAlert(Constants.EMPTY_FIELD);
        } else {
            MenuItem add = orderItemService.getAllMenuItems().get().stream()
                    .filter(menuItem -> menuItem.getName().equals(itemsComboBox.getValue()))
                    .findFirst().orElse(null);
            if (add != null) {
                itemsTable.getItems().add(new OrderItem(0, 0, Integer.parseInt(quantityItemField.getText()), add));
                quantityItemField.clear();
                getPrincipalController().showConfirmationAlert(Constants.ITEM_ADDED_SUCCESSFULLY);
            } else {
                getPrincipalController().showErrorAlert(Constants.ERROR_ADDING_ITEM);
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
