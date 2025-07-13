package src.Logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.Datenbank;
import src.View_GUI.ViewManager;

public class Login {

    public static final BooleanProperty anmeldung = new SimpleBooleanProperty(true);
    public static final BooleanProperty canSign = new SimpleBooleanProperty(false);
    public static final BooleanProperty is18 = new SimpleBooleanProperty(false);
    public static final StringProperty username = new SimpleStringProperty();
    public static final StringProperty password = new SimpleStringProperty();

    public static void login(String username, String password) {
        Datenbank datenbank = Datenbank.getInstance();
        if (username.trim().isEmpty() || password.trim().isEmpty()) {// entfernt Müll Leerzeichen
            ViewManager.getInstance().displayErrorMessage("Bruder falsche Eingabe!");
        }
        else {
            if (anmeldung.get()) {
                if (datenbank.signIn(username, password)) {
                    ViewManager.getInstance().getController().setMoney(datenbank.getMoney());
                    ViewManager.getInstance().setView(ViewManager.MAIN_MENU);
                } else {
                    ViewManager.getInstance().displayErrorMessage("Bruder du hast keinen Account!!!");
                }
            } else {
                if(is18.get()){
                if (datenbank.signUp(username, password)) {
                    ViewManager.getInstance().getController().setMoney(datenbank.getMoney());
                    ViewManager.getInstance().setView(ViewManager.MAIN_MENU);
                } else {
                    ViewManager.getInstance().displayErrorMessage("Bruder du hast einen Account!!!");
                }}
                else{
                    ViewManager.getInstance().displayErrorMessage("Bist du nicht 18????");
                }
            }
        }
    }
}
