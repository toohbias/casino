package src;

import javafx.application.Application;
import javafx.stage.Stage;
import src.View_GUI.CasionView;

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
        primaryStage.setTitle("Casino");
        CasionView view = new CasionView();
        primaryStage.setScene(view);
        primaryStage.show();
    }
}
