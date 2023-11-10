package services;

import io.vavr.control.Either;
import model.Customer;
import model.errors.Error;

import java.util.List;

public interface CustomerService {
    Either<Error, List<Customer>> getAll();

    Either<Error, Customer> getCustomerById(int id);

    Either<Error, Integer> save(Customer c);

    Either<Error, Integer> update(Customer c);

    Either<Error, Integer> delete(Customer c);
}
