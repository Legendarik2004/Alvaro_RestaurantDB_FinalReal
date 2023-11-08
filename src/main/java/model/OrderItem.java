package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItem {
    private int orderItemId;
    private int orderId;
    private int menuItemId;
    private int quantity;
    private MenuItem menuItem;
}
