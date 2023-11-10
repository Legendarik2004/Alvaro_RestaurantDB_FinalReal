package services.impl;

import dao.CustomersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Customer;
import model.errors.Error;
import services.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private final CustomersDAO dao;

    @Inject
    public CustomerServiceImpl(CustomersDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<Error, List<Customer>> getAll() {
        return dao.getAll();
    }

    @Override
    public Either<Error, Customer> getCustomerById(int id) {
        return dao.getCustomerById(id);
    }

    @Override
    public Either<Error, Integer> save(Customer c){
        return dao.save(c);
    }

    @Override
    public Either<Error, Integer> update(Customer c) {
        return dao.update(c);
    }

    @Override
    public Either<Error, Integer> delete(Customer c) {
        return dao.delete(c);
    }
}
