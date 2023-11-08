package services.impl;

import dao.OrderDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.errors.Error;
import services.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private final OrderDAO dao;

    @Inject
    public OrderServiceImpl(OrderDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<Error, List<Order>> getAll() {
        return dao.getAll();
    }

    @Override
    public Either<Error, List<Order>> getOrderOfCustomer(int id) {
        return dao.getOrderOfCustomer(id);
    }

    @Override
    public Either<Error, Integer> save(Order o) {
        return dao.save(o);
    }

    @Override
    public Either<Error, Integer> update(Order o) {
        return dao.update(o);
    }

    @Override
    public Either<Error, Integer> delete(Order o) {
        return dao.delete(o);
    }

    @Override
    public int getAddedOrderId() {
        return dao.getAddedOrderId();
    }
}
