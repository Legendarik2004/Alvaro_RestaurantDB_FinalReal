package services.impl;

import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Credentials;
import model.errors.Error;
import services.LoginService;


public class LoginServiceImpl implements LoginService {

    private final LoginDAO dao;

    @Inject
    public LoginServiceImpl(LoginDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<Error, Boolean> doLogin(Credentials credentials) {
        return dao.doLogin(credentials);
    }

    @Override
    public Credentials get() {
        return dao.get();
    }
}
