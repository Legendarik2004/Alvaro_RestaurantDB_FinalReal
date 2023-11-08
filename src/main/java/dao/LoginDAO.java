package dao;

import io.vavr.control.Either;
import model.User;
 import model.errors.Error;

public interface LoginDAO {
    Either<Error,Boolean> doLogin(User user);
    User get();
}
