package src;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import src.View_GUI.ViewManager;

/**
 * Starte das Spiel
 * Auswahl des Spiels
 * Lobby
 * Startwert werden fesst gelegt
 * Passwort und Username festlegen
 * -------------------------------
 * Laut JavaFX muss in das Fenster in
 * einer Klasse namens Main initialisiert werden
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(ViewManager.getInstance().getDefaultScene());
        primaryStage.setTitle("Casino");
        primaryStage.getIcons().add(new Image("src/assets/Sevensign.png"));
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
