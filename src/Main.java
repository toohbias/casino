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
        System.out.println(Datenbank.getInstance().signIn("username", "password"));
        CasinoController controller = new CasinoController();//Datenbank.getInstance().getMoney()
        //controller.setMoney(200000D);
        ViewManager.getInstance().setController(controller);
        //ViewManager.getInstance().setShowMoney(true); // TODO rausmachen wenn Login fertig ist
        ViewManager.getInstance().setView(0);
    }

    @Override
    public void stop() {
        Datenbank.getInstance().updateMoney(ViewManager.getInstance().getController().getMoney().get());
    }
}
