package dao.impl;

import common.Constants;
import common.SQLQueries;
import dao.MenuItemsDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MenuItem;
import model.errors.Error;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public class MenuItemsDaoImpl implements MenuItemsDAO {
    private final DBConnectionPool db;

    @Inject
    public MenuItemsDaoImpl(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<Error, List<MenuItem>> getAll() {
        Either<Error, List<MenuItem>> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.GETALL_MENUITEMS)
        ) {


            ResultSet rs = preparedStatement.executeQuery();
            result = Either.right(readRS(rs).get());
        } catch (SQLException e) {
            Logger.getLogger(MenuItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_MENU_ITEMS_FOUND));
        }
        return result;
    }

    private Either<Error, List<MenuItem>> readRS(ResultSet rs) {
        Either<Error, List<MenuItem>> result;
        try {
            List<MenuItem> menuItems = new ArrayList<>();
            while (rs.next()) {
                int menuItemId = rs.getInt(Constants.MENU_ITEM_ID);
                String name = rs.getString(Constants.NAME);
                String description = rs.getString(Constants.DESCRIPTION);
                double price = rs.getDouble(Constants.PRICE);
                MenuItem menuItem = new MenuItem(menuItemId, name, description, price);
                menuItems.add(menuItem);
            }
            result = Either.right(menuItems);
        } catch (SQLException e) {
            Logger.getLogger(MenuItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_MENU_ITEMS_FOUND));
        }
        return result;
    }
}
