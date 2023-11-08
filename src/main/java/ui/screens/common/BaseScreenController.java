package ui.screens.common;

import ui.screens.principal.PrincipalController;

import java.io.IOException;


public class BaseScreenController {

    private PrincipalController principalController;

    public PrincipalController getPrincipalController() {
        return principalController;
    }

    public void setPrincipalController(PrincipalController principalController) {
        this.principalController = principalController;
    }

    public void principalCargado() throws IOException {
        //NOP
    }
}
