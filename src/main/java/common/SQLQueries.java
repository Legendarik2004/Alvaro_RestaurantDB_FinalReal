package common;

public class SQLQueries {


    private SQLQueries() {
    }

    //CREDENTIALS
    public static final String ADD_CREDENTIALS = "INSERT INTO credentials (user_name, password) VALUES (?, ?)";
    public static final String CHECK_CREDENTIALS = "SELECT * FROM credentials WHERE user_name = ?";
    public static final String DELETE_CREDENTIALS = "DELETE FROM credentials WHERE id = ?";


    //CUSTOMER
    public static final String GETALL_CUSTOMERS = "SELECT * FROM customers";
    public static final String ADD_CUSTOMER = "INSERT INTO customers (id, first_name, last_name, email, phone, date_of_birth) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_CUSTOMER = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone = ?, date_of_birth = ? WHERE id = ?";
    public static final String DELETE_CUSTOMER = "DELETE FROM customers WHERE id = ?";

    //ORDERS
    public static final String GETALL_ORDERS = "SELECT * FROM orders";
    public static final String ADD_ORDER = "INSERT INTO orders (order_date, customer_id, table_id) VALUES ( ?, ?, ?)";
    public static final String UPDATE_ORDER = "UPDATE orders SET table_id = ? WHERE order_id = ?";
    public static final String DELETE_ORDER = "DELETE FROM orders WHERE order_id = ?";

    //ORDERITEMS
    public static final String GETALL_ORDERITEMS = "SELECT oi.order_item_id, oi.order_id, oi.menu_item_id, oi.quantity, mi.name, mi.description, mi.price FROM order_items oi INNER JOIN menu_items mi ON oi.menu_item_id = mi.menu_item_id WHERE oi.order_id = ?";
    public static final String ADD_ORDERITEM = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES ( ?, ?, ?)";
    public static final String DELETE_ORDERITEM = "DELETE FROM order_items WHERE order_item_id = ?";

    //MENUITEMS
    public static final String GETALL_MENUITEMS = "SELECT * FROM menu_items";
}