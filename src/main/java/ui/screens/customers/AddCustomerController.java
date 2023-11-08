package ui.screens.customers;

import common.Constants;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.User;
import services.CustomerService;
import ui.screens.common.BaseScreenController;

import java.io.IOException;
import java.time.LocalDate;

public class AddCustomerController extends BaseScreenController {
    private final CustomerService customerService;
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
    public TextField userNameField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField fnameField;
    @FXML
    public TextField lnameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public DatePicker dobField;

    @Inject
    public AddCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void initialize() {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        dobCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
    }

    @Override
    public void principalCargado() throws IOException {
        setTable();
    }

    private void setTable() {
        customersTable.getItems().clear();
        customerService.getAll().peek(customers -> customersTable.getItems().addAll(customers))
                .peekLeft(customerError -> getPrincipalController().showErrorAlert(customerError.getMessage()));
    }

    public void addCustomer() {
        if (fnameField.getText().isEmpty() || lnameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty() || dobField.getValue() == null) {
            getPrincipalController().showErrorAlert(Constants.EMPTY_FIELD);
        } else {

            customerService.save(new Customer(0, fnameField.getText(), lnameField.getText(), emailField.getText(), phoneField.getText(), dobField.getValue()),new User(0,userNameField.getText(),passwordField.getText())).peek(success -> {
                        if (success == 0) {

                            setTable();
                            getPrincipalController().showConfirmationAlert(Constants.CUSTOMER_ADDED_SUCCESSFULLY);

                        }
                    })
                    .peekLeft(customerError -> getPrincipalController().showErrorAlert(Constants.ERROR_ADDING_CUSTOMER));
        }
    }
}