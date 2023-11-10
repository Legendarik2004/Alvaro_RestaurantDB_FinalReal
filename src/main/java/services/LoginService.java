package services;

import io.vavr.control.Either;
import model.Credentials;
import model.errors.Error;

public interface LoginService {
    Either<Error,Boolean> doLogin(Credentials credentials);

    Credentials get();
}
