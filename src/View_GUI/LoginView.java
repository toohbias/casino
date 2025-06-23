package src.View_GUI;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import src.Logic.Login;

public class LoginView {

    public static final StringProperty username = new SimpleStringProperty();
    public static final StringProperty password = new SimpleStringProperty();
    public static final BooleanProperty canSign = new SimpleBooleanProperty(true);
    public static final BooleanProperty anmeldung = new SimpleBooleanProperty(true);
    public static final BooleanProperty is18 = new SimpleBooleanProperty(false);

    public static Node getPane() {
        // Aktion
        Label action = new Label();
        action.textProperty().bind(Bindings.when(anmeldung).then("Anmeldung").otherwise("Registrierung"));
        action.setPadding(new Insets(20, 0, 10, 0));

        // login/registrieren button
        Button sign = new Button();
        sign.textProperty().bind(Bindings.when(anmeldung).then("Anmelden").otherwise("Registrieren"));
        sign.disableProperty().bind(canSign.not());
        sign.setOnAction(e -> ViewManager.getInstance().sign(username.get(), password.get()));
        sign.setId("signButton");
        sign.styleProperty().bind(Bindings.format("-fx-text-fill: %s;", Bindings.when(canSign).then("rgb(200, 200, 200)").otherwise("rgb(150, 150, 150)").get()));
        HBox.setMargin(sign, new Insets(10, 20, 20, 20));

        // username field
        TextField user = new TextField();
        username.bind(user.textProperty());
        user.setPromptText("Benutzer");
        VBox.setMargin(user, new Insets(10, 20, 10, 20));

        // password field
        PasswordField pass = new PasswordField();
        password.bind(pass.textProperty());
        pass.setPromptText("Passwort");
        VBox.setMargin(pass, new Insets(10, 20, 10, 20));
        // anmeldung mit Enter
        pass.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                ViewManager.getInstance().sign(username.get(), password.get());
            }
        });
        pass.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                sign.pseudoClassStateChanged(PseudoClass.getPseudoClass("pressed"), false);
            }
        });

        // age check
        CheckBox ageCheck = new CheckBox("Über 18?");
        is18.bind(ageCheck.selectedProperty());
        ageCheck.visibleProperty().bind(Bindings.when(anmeldung).then(false).otherwise(true));
        HBox.setMargin(ageCheck, new Insets(10, 20, 10, 20));

        Region placeholder1 = new Region();
        HBox.setHgrow(placeholder1, Priority.ALWAYS);

        //Fehlermeldung
        Label fehler = new Label();
        fehler.textProperty().bindBidirectional(Login.error);
        fehler.visibleProperty().bind(fehler.textProperty().isNotEmpty());

        // andere Aktion (anmeldung -> registrieren / registrierung -> anmelden)
        Label altAction = new Label();
        altAction.textProperty().bind(Bindings.when(anmeldung).then("lieber registrieren").otherwise("lieber anmelden"));
        altAction.setOnMouseClicked(e -> {
            anmeldung.set(anmeldung.not().get());
            altAction.requestFocus(); // damit die Prompts aus beiden textfeldern angezeigt werden
            fehler.setText("");
        });
        altAction.setId("alternativeAction");
        altAction.setPadding(new Insets(0));
        HBox.setMargin(altAction, new Insets(10, 20, 10, 20));

        Region placeholder2 = new Region();
        HBox.setHgrow(placeholder2, Priority.ALWAYS);


        // Szene
        BorderPane root = new BorderPane(new VBox(user, pass, new HBox(ageCheck, placeholder1, altAction), new HBox(placeholder2, sign),fehler));
        root.setTop(new BorderPane(action));
        root.setId("floatingPane");
        root.maxHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(3));
        root.maxWidthProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(2));
        Platform.runLater(root::requestFocus); // damit die prompts aus beiden textfeldern angezeigt werden
        return root;
    }

}
