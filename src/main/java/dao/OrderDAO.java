package dao;

import io.vavr.control.Either;
import model.Order;
import model.errors.Error;

import java.util.List;

public interface OrderDAO {
    Either<Error, List<Order>> getAll();

    Either<Error, List<Order>> getOrderOfCustomer(int id);

    Either<Error, Integer> save(Order order);

    Either<Error, Integer> update(Order order);

    Either<Error, Integer> delete(Order order);

    int getAddedOrderId();
}
