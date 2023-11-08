package dao.impl;

import common.Constants;
import common.SQLQueries;
import dao.OrderItemsDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MenuItem;
import model.OrderItem;
import model.errors.Error;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public class OrderItemsDaoImpl implements OrderItemsDAO {
    private final DBConnectionPool db;

    @Inject
    public OrderItemsDaoImpl(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<Error, List<OrderItem>> getAllOrderItems(int id) {
        Either<Error, List<OrderItem>> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.GETALL_ORDERITEMS)) {
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            result = Either.right(readRS(rs).get());
        } catch (SQLException e) {
            Logger.getLogger(OrderItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_ORDER_ITEMS_FOUND));
        }
        return result;
    }


    private Either<Error, List<OrderItem>> readRS(ResultSet rs) {
        Either<Error, List<OrderItem>> result;
        try {
            List<OrderItem> orderItems = new ArrayList<>();
            while (rs.next()) {
                int orderItemId = rs.getInt(Constants.ORDER_ITEM_ID);
                int orderId = rs.getInt(Constants.ORDER_ID);
                int menuItemId = rs.getInt(Constants.MENU_ITEM_ID);
                int quantity = rs.getInt(Constants.QUANTITY);
                String name = rs.getString(Constants.NAME);
                String description = rs.getString(Constants.DESCRIPTION);
                double price = rs.getDouble(Constants.PRICE);

                orderItems.add(new OrderItem(orderItemId, orderId, menuItemId, quantity, new MenuItem(menuItemId, name, description, price)));
            }
            result = Either.right(orderItems);
        } catch (SQLException e) {
            Logger.getLogger(OrderItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_ORDER_ITEMS_FOUND));
        }
        return result;
    }


    @Override
    public Either<Error, List<OrderItem>> get(int id) {
        Either<Error, List<OrderItem>> result;
        result = Either.right(getAllOrderItems(id).get().stream().filter(orderItem -> orderItem.getOrderId() == id).toList());
        return result;
    }


    @Override
    public Either<Error, Integer> save(OrderItem orderItem) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.ADD_ORDERITEM)) {

            preparedStatement.setInt(1, orderItem.getOrderId());
            preparedStatement.setInt(2, orderItem.getMenuItemId());
            preparedStatement.setInt(3, orderItem.getQuantity());

            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_ITEM));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_ITEM));
        }
        return result;
    }


    @Override
    public Either<Error, Integer> update(OrderItem orderItem) {
        getAllOrderItems(orderItem.getOrderId());






        return Either.right(0);
    }

    @Override
    public Either<Error, Integer> delete(OrderItem orderItem) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.DELETE_ORDERITEM)) {

            preparedStatement.setInt(1, orderItem.getOrderItemId());


            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_ITEM));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_ITEM));
        }
        return result;
    }
}
