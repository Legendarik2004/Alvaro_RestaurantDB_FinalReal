package common;

public class Constants {


    public static final String SEGURO_QUE_QUIERES_ELIMINAR = "¿Seguro que quieres eliminar?";

    private Constants() {}

    public static final int NUM_ERROR = 1;


    //Login
    public static final String INCORRECT_USER_OR_PASSWORD = "Incorrect user or password";
    public static final String ERROR_ADDING_CREDENTIALS = "Error adding credentials";
    public static final String ERROR_ON_LOGIN = "An error occurred during login";
    public static final String ERROR_DELETING_CREDENTIALS = "Error deleting credentials";


    //Empty List Errors
    public static final String EMPTY_FIELD = "There is an empty field";
    public static final String NO_CUSTOMER_FOUND = "No customer found";
    public static final String NO_CUSTOMERS_FOUND = "No customers found";
    public static final String NO_ORDERS_FOUND = "No orders found";
    public static final String NO_ORDER_ITEMS_FOUND = "No order items found";
    public static final String NO_MENU_ITEMS_FOUND = "No menu items found";


    //Customer Success
    public static final String CUSTOMER_ADDED_SUCCESSFULLY = "Customer added successfully.";
    public static final String CUSTOMER_UPDATED_SUCCESSFULLY = "Customer updated successfully";
    public static final String CUSTOMER_DELETED_SUCCESSFULLY = "Customer deleted successfully";

    //Customer Errors
    public static final String ERROR_ADDING_CUSTOMER = "Error adding customer";
    public static final String ERROR_UPDATING_CUSTOMER = "Error updating customer";
    public static final String ERROR_DELETING_CUSTOMER = "Error deleting customer";


    //Order Success
    public static final String ORDER_ADDED_SUCCESSFULLY = "Order added successfully";
    public static final String ORDER_UPDATED_SUCCESSFULLY = "Order updated successfully";
    public static final String ORDER_DELETED_SUCCESSFULLY = "Order deleted successfully";

    //Order Errors
    public static final String ERROR_ADDING_ORDER = "Error adding order";
    public static final String ERROR_UPDATING_ORDER = "Error updating order";
    public static final String ERROR_DELETING_ORDER = "Error deleting order";


    //Item Success
    public static final String ITEM_ADDED_SUCCESSFULLY = "Item added successfully";
    public static final String ITEM_REMOVED_SUCCESSFULLY = "Item removed successfully";

    //Item Errors
    public static final String ERROR_ADDING_ITEM = "Error adding item";
    public static final String ERROR_DELETING_ITEM = "Error deleting item";


    //Nothing selected
    public static final String SELECT_CUSTOMER_FIRST = "To delete a customer select it first";
    public static final String SELECT_ORDER_FIRST = "To delete an order select it first";



    //Variables

    //Login
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";

    //Customers
    public static final String ID = "id";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String DATE_OF_BIRTH = "date_of_birth";

    //Orders
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_DATE = "order_date";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String TABLE_ID = "table_id";

    //Items_MenuItems
    public static final String ORDER_ITEM_ID = "order_item_id";
    public static final String QUANTITY = "quantity";
    public static final String MENU_ITEM_ID = "menu_item_id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PRICE = "price";

    //Tables
    public static final String TABLE_NUMBER_ID = "table_number_id";
    public static final String NUMBER_OF_SEATS = "number_of_seats";


}