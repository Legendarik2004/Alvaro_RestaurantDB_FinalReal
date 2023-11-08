package services.impl;

import dao.LoginDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.User;
import model.errors.Error;
import services.LoginService;


public class LoginServiceImpl implements LoginService {

    private final LoginDAO dao;

    @Inject
    public LoginServiceImpl(LoginDAO dao) {
        this.dao = dao;
    }

    @Override
    public Either<Error, Boolean> doLogin(User user) {
        return dao.doLogin(user);
    }

    @Override
    public User get() {
        return dao.get();
    }
}
