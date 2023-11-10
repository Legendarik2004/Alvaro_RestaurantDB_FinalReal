package services.impl;

import dao.MenuItemsDAO;
import dao.OrderItemsDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.MenuItem;
import model.OrderItem;
import model.errors.Error;
import services.OrderItemService;

import java.util.List;

public class OrderItemsServiceImpl implements OrderItemService {

    private final OrderItemsDAO dao;

    private final MenuItemsDAO menuItemsDao;
    @Inject
    public OrderItemsServiceImpl(OrderItemsDAO dao, MenuItemsDAO menuItemsDao) {
        this.dao = dao;
        this.menuItemsDao = menuItemsDao;
    }

    @Override
    public Either<Error, List<OrderItem>> getAll(int id) {
        return dao.getAll(id);
    }



    @Override
    public Either<Error, Integer> save(OrderItem orderItem) {
        return dao.save(orderItem);
    }


    @Override
    public Either<Error, Integer> delete(OrderItem orderItem) {
        return dao.delete(orderItem);
    }

    @Override
    public Either<Error, List<MenuItem>> getAllMenuItems() {
        return menuItemsDao.getAll();
    }

}
