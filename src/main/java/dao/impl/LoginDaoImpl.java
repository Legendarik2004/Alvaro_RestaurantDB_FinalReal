package dao.impl;

import common.Constants;
import common.SQLQueries;
import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.User;
import model.errors.Error;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log4j2
public class LoginDaoImpl implements LoginDAO {
    private final DBConnectionPool db;
    private User newUser;

    @Inject
    public LoginDaoImpl(DBConnectionPool db) {
        this.db = db;
    }

    @Override
    public Either<Error, Boolean> doLogin(User user) {
        Either<Error, Boolean> result;

        try (Connection myConnection = db.getConnection();
             PreparedStatement preparedStatement = myConnection.prepareStatement(SQLQueries.CHECK_CREDENTIALS)) {
            preparedStatement.setString(1, user.getNombre());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    newUser = new User(
                            rs.getInt(Constants.ID),
                            rs.getString(Constants.USER_NAME),
                            rs.getString(Constants.PASSWORD)
                    );
                    result = Either.right(true);
                } else {
                    result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ON_LOGIN));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(LoginDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            result = Either.left(new Error(Constants.NUM_ERROR, Constants.ERROR_ON_LOGIN));
        }
        return result;
    }

    @Override
    public User get() {
        return newUser;
    }

}
