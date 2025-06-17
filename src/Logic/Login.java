package src.Logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Login {

    public static final BooleanProperty anmeldung = new SimpleBooleanProperty(true);
    public static final BooleanProperty canSign = new SimpleBooleanProperty(false);
    public static final BooleanProperty is18 = new SimpleBooleanProperty(false);
    public static final StringProperty username = new SimpleStringProperty();
    public static final StringProperty password = new SimpleStringProperty();

    public static void login(String username, String password) {
        // siehe issue#6
    }

}
