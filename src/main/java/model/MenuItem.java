package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuItem {
    private int menuItemId;
    private String name;
    private String description;
    private double price;

    public MenuItem(int menuItemId) {
        this.menuItemId = menuItemId;
    }
}
