package ui.main;


import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.screens.principal.PrincipalController;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFX {


    FXMLLoader fxmlLoader;

    @Inject
    public MainFX(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public void start(@Observes @StartupScene Stage stage) {
        try {
            Parent fxmlParent = fxmlLoader.load(getClass().getResourceAsStream("/fxml/principal.fxml"));
            PrincipalController controller = fxmlLoader.getController();
            controller.setStage(stage);
            stage.setScene(new Scene(fxmlParent));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(MainFX.class.getName()).log(Level.SEVERE, null, e);
            System.exit(0);
        }
    }

}
