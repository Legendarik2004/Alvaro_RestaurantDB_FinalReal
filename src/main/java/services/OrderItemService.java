package services;

import io.vavr.control.Either;
import model.MenuItem;
import model.OrderItem;
import model.errors.Error;

import java.util.List;

public interface OrderItemService {
    Either<Error, List<OrderItem>> getAllOrderItems(int id);

    Either<Error, List<OrderItem>> get(int id);

    Either<Error, Integer> save(OrderItem orderItem);

    Either<Error, Integer> update(OrderItem orderItem);

    Either<Error, Integer> delete(OrderItem orderItem);

    Either<Error, List<MenuItem>> getAllMenuItems();

}
