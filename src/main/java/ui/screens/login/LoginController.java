package ui.screens.login;

import common.Constants;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.User;
import services.LoginService;
import ui.screens.common.BaseScreenController;

public class LoginController extends BaseScreenController {
    @FXML
    public BorderPane loginPane;
    @FXML
    private TextField userTextField;
    @FXML
    private TextField passTextField;
    private final LoginService loginService;

    @Inject
    LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @FXML
    private void doLogin() {
        User user = new User(0, userTextField.getText(), passTextField.getText());

        loginService.doLogin(user).peek(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        getPrincipalController().onLoginDone(loginService.get());
                    } else {
                        getPrincipalController().showErrorAlert(Constants.INCORRECT_USER_OR_PASSWORD);
                    }
                })
                .peekLeft(loginError -> getPrincipalController().showErrorAlert(Constants.INCORRECT_USER_OR_PASSWORD));

    }
}
