package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class Order {
    private int orderId;
    private Timestamp date;
    private int customerId;
    private int tableId;
    private List<OrderItem> orderItems;
}
