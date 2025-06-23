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
    public static final StringProperty error = new SimpleStringProperty("");

    public static void login(String username, String password) {
        Datenbank datenbank = Datenbank.getInstance();
        ViewManager vm = ViewManager.getInstance();
        if (username.trim().isEmpty() || password.trim().isEmpty()) {// entfernt Müll Leerzeichen
            error.set("Bruder falsche Eingabe");
        }
        else {
            if (anmeldung.get()) {
                if (datenbank.signIn(username, password)) {
                    vm.getController().setMoney(datenbank.getMoney());
                    error.set("");
                    vm.setView(1);
                } else {
                    error.set("Bruder du hast keinen Account!!!");
                }
            } else {
                if(is18.get()){
                if (datenbank.signUp(username, password)) {
                    vm.getController().setMoney(datenbank.getMoney());
                    error.set("");
                    vm.getInstance().setView(1);
                } else {
                    error.set("BRuder du hast einen Account!!!");
                }}
                else{
                    error.set("Bist du nicht 18????");
                }
            }
        }
    }
}
