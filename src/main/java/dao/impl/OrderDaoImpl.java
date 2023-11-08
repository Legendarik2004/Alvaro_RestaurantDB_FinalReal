package dao.impl;

import common.Constants;
import common.SQLQueries;
import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import model.Order;
import model.errors.Error;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@Log4j2
public class OrderDaoImpl implements OrderDAO {
    private final DBConnectionPool db;
    private int addedOrderId;

    @Inject
    public OrderDaoImpl(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<Error, List<Order>> getAll() {

        Either<Error, List<Order>> result;

        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(SQLQueries.GETALL_ORDERS);

            result = Either.right(readRS(rs).get());
        } catch (SQLException e) {
            Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_ORDERS_FOUND));
        }
        return result;
    }

    private Either<Error, List<Order>> readRS(ResultSet rs) {
        Either<Error, List<Order>> either;
        try {
            List<Order> orders = new ArrayList<>();
            while (rs.next()) {
                Order resultOrder = new Order(
                        rs.getInt(Constants.ORDER_ID),
                        rs.getTimestamp(Constants.ORDER_DATE),
                        rs.getInt(Constants.CUSTOMER_ID),
                        rs.getInt(Constants.TABLE_ID));
                orders.add(resultOrder);
            }
            either = Either.right(orders);
        } catch (SQLException e) {
            Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);

            either = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_ORDERS_FOUND));
        }
        return either;
    }

    @Override
    public Either<Error, List<Order>> getOrderOfCustomer(int id) {

        return Either.right(getAll().get().stream().filter(order -> order.getCustomerId() == id).toList());
    }

    @Override
    public Either<Error, Integer> save(Order order) {

        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.ADD_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setTimestamp(1, order.getDate());
            preparedStatement.setInt(2, order.getCustomerId());
            preparedStatement.setInt(3, order.getTableId());


            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    addedOrderId = rs.getInt(1);
                }
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_ORDER));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_ORDER));
        }
        return result;
    }

    @Override
    public Either<Error, Integer> update(Order order) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();

             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.UPDATE_ORDER)) {


            preparedStatement.setInt(1, order.getTableId());
            preparedStatement.setInt(2, order.getOrderId());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_UPDATING_CUSTOMER));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_UPDATING_CUSTOMER));
        }
        return result;
    }

    @Override
    public Either<Error, Integer> delete(Order order) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.DELETE_ORDER)) {

            preparedStatement.setInt(1, order.getOrderId());

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_DELETING_CUSTOMER));
            }
        } catch (SQLException e) {
            Logger.getLogger(OrderDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_UPDATING_CUSTOMER));
        }
        return result;
    }

    @Override
    public int getAddedOrderId() {
        return addedOrderId;
    }
}
