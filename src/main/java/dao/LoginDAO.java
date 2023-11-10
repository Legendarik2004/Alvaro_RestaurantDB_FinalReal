package dao;

import io.vavr.control.Either;
import model.Credentials;
 import model.errors.Error;

public interface LoginDAO {
    Either<Error,Boolean> doLogin(Credentials credentials);
    Credentials get();
}
