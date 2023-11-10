package dao.impl;


import common.Constants;
import common.SQLQueries;
import dao.TableDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Table;
import model.errors.Error;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableDaoImpl implements TableDAO {

    private final DBConnectionPool db;

    @Inject
    public TableDaoImpl(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<Error, List<Table>> getAll() {

        Either<Error, List<Table>> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.GETALL_TABLES)
        ) {
            ResultSet rs = preparedStatement.executeQuery();
            result = Either.right(readRS(rs).get());
        } catch (SQLException e) {
            Logger.getLogger(TableDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, "No tables found"));
        }
        return result;


    }

    private Either<Error, List<Table>> readRS(ResultSet rs) {
        Either<Error, List<Table>> result;
        try {
            List<Table> tables = new ArrayList<>();
            while (rs.next()) {
                int tableId = rs.getInt(Constants.TABLE_NUMBER_ID);
                int numberOfSeats = rs.getInt(Constants.NUMBER_OF_SEATS);
                Table table = new Table(tableId,numberOfSeats);
                tables.add(table);
            }
            result = Either.right(tables);
        } catch (SQLException e) {
            Logger.getLogger(MenuItemsDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, "No tables found"));
        }
        return result;
    }
}