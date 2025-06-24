package src;

import javafx.application.Application;
import javafx.stage.Stage;
import src.Logic.CasinoController;
import src.View_GUI.ViewManager;

/**
 * Starte das Spiel
 * Auswahl des Spiels
 * Lobby
 * Startwert werden festgelegt
 * Passwort und Username festlegen
 * -------------------------------
 * Laut JavaFX muss in das Fenster in
 * einer Klasse namens Main initialisiert werden
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ViewManager.getInstance().setStage(primaryStage);
    }

    @Override
    public void stop() {
        Datenbank.getInstance().updateMoney(ViewManager.getInstance().getController().getMoney().get());
    }
}
