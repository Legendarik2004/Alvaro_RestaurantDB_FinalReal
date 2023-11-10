package dao.impl;

import common.Constants;
import common.SQLQueries;
import dao.CustomersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.Credentials;
import model.errors.Error;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public class CustomerDaoImpl implements CustomersDAO {
    private final DBConnectionPool db;

    @Inject
    public CustomerDaoImpl(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<Error, List<Customer>> getAll() {

        Either<Error, List<Customer>> result;

        try (Connection myConnection = db.getConnection();
             Statement statement = myConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            ResultSet rs = statement.executeQuery(SQLQueries.GETALL_CUSTOMERS);


            result = Either.right(readRSGetAll(rs).get());
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_CUSTOMERS_FOUND));
        }
        return result;
    }

    private Either<Error, List<Customer>> readRSGetAll(ResultSet rs) {
        Either<Error, List<Customer>> either;
        try {
            List<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer resultCustomer = new Customer(
                        rs.getInt(Constants.ID),
                        rs.getString(Constants.FIRST_NAME),
                        rs.getString(Constants.LAST_NAME),
                        rs.getString(Constants.EMAIL),
                        rs.getString(Constants.PHONE),
                        rs.getDate(Constants.DATE_OF_BIRTH).toLocalDate(),
                        new Credentials(0,null,null)
                );
                customers.add(resultCustomer);
            }
            either = Either.right(customers);
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);

            either = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_CUSTOMERS_FOUND));
        }
        return either;
    }

    @Override
    public Either<Error, Customer> getCustomerById(int id) {
        List<Customer> customersList = getAll().get();
        Either<Error, Customer> result;
        try {
            Customer customer = customersList.stream().filter(customers -> customers.getId() == id).findFirst().orElse(null);
            if (customer != null) {
                result = Either.right(customer);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_CUSTOMER_FOUND));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.NO_CUSTOMER_FOUND));
        }
        return result;
    }

    @Override
    public Either<Error, Integer> save(Customer customer) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatementCredentials = myConnection.prepareStatement(SQLQueries.ADD_CREDENTIALS, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatementCredentials.setString(1, customer.getCredentials().getNombre());
            preparedStatementCredentials.setString(2, customer.getCredentials().getPassword());

            int credentialsAdded = preparedStatementCredentials.executeUpdate();
            if (credentialsAdded == 0) {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_CREDENTIALS));
            } else {
                ResultSet generatedKeys = preparedStatementCredentials.getGeneratedKeys();


                if (generatedKeys.next()) {
                    result = readRSSave(generatedKeys, myConnection, customer);
                } else {
                    result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_CUSTOMER));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_CREDENTIALS));
        }
        return result;
    }

    public Either<Error, Integer> readRSSave(ResultSet generatedKeys, Connection myConnection, Customer customer) {
        Either<Error, Integer> result;

        try (PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.ADD_CUSTOMER)) {
            preparedStatement.setInt(1, generatedKeys.getInt(1));
            preparedStatement.setString(2, customer.getFirstName());
            preparedStatement.setString(3, customer.getLastName());
            preparedStatement.setString(4, customer.getEmail());
            preparedStatement.setString(5, customer.getPhone());
            preparedStatement.setDate(6, Date.valueOf(customer.getDob()));

            int rowsAdded = preparedStatement.executeUpdate();

            if (rowsAdded > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_CUSTOMER));
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ADDING_CUSTOMER));
        }
        return result;
    }

    @Override
    public Either<Error, Integer> update(Customer customer) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();

             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.UPDATE_CUSTOMER)) {
            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setDate(5, Date.valueOf(customer.getDob()));
            preparedStatement.setInt(6, customer.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_UPDATING_CUSTOMER));
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_UPDATING_CUSTOMER));
        }
        return result;
    }


    @Override
    public Either<Error, Integer> delete(Customer customer) {
        Either<Error, Integer> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.DELETE_CUSTOMER)) {

            preparedStatement.setInt(1, customer.getId());

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {

                result = deleteCredential(myConnection, customer);

            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_DELETING_CUSTOMER));
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_UPDATING_CUSTOMER));
        }
        return result;
    }

    public Either<Error, Integer> deleteCredential(Connection myConnection, Customer customer) {
        Either<Error, Integer> result;
        try (PreparedStatement preparedStatementCredentials = myConnection.prepareStatement(SQLQueries.DELETE_CREDENTIALS)) {
            preparedStatementCredentials.setInt(1, customer.getId());

            int rowsDeletedCredentials = preparedStatementCredentials.executeUpdate();

            if (rowsDeletedCredentials > 0) {
                result = Either.right(0);
            } else {
                result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_DELETING_CREDENTIALS));
            }
        } catch (SQLException e) {
            Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_DELETING_CREDENTIALS));
        }

        return result;
    }

}
