package services;

import io.vavr.control.Either;
import model.User;
import model.errors.Error;

public interface LoginService {
    Either<Error,Boolean> doLogin(User user);

    User get();
}
