package dao;

import io.vavr.control.Either;
import model.OrderItem;
import model.errors.Error;

import java.util.List;

public interface OrderItemsDAO {
    Either<Error, List<OrderItem>> getAllOrderItems(int id);

    Either<Error, List<OrderItem>> get(int id);

    Either<Error, Integer> save(OrderItem orderItem);

    Either<Error, Integer> update(OrderItem orderItem);

    Either<Error, Integer> delete(OrderItem orderItem);


}
