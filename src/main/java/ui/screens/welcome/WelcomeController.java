package ui.screens.welcome;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ui.screens.common.BaseScreenController;

public class WelcomeController extends BaseScreenController {

    @FXML
    public Label welcomeLabel;

    @Override
    public void principalCargado() {
        welcomeLabel.setText("Welcome " + getPrincipalController().getActualUser().getNombre());
    }
}
