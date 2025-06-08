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
    private Datenbank db;

    @Override
    public void start(Stage primaryStage) {
        ViewManager.getInstance().setStage(primaryStage);
        db = new Datenbank("username", "password");
        db.updateMoney(10D);
        CasinoController controller = new CasinoController(db.getMoney());
        ViewManager.getInstance().setController(controller);
    }

    @Override
    public void stop() {
        db.updateMoney(ViewManager.getInstance().getController().getMoney().get());
    }
}
