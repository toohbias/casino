package src.View_GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import src.Logic.Shop;

/**
 * wrappt die {@code getPane()}-Methode für den Shop
 */
public class ShopView {

    /**
     * Shop-Szene
     * hier kann der Spieler sein Geld aufladen
     * @return Szene
     */
    public static Node getPane() {
        BorderPane root = new BorderPane();

        // Zentrum: Frage + Eingabe
        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);

        Label frage = new Label("Wie viel Geld brauchst du?");
        frage.setStyle("-fx-font-size: 24pt; -fx-text-fill: white;");

        TextField eingabe = new TextField();  // KORREKTUR: nicht 'extField'
        eingabe.setPromptText("Gib den Betrag ein");
        eingabe.setMaxWidth(200);

        Label fehlerLabel = new Label();
        fehlerLabel.setStyle("-fx-text-fill: red;");
        fehlerLabel.textProperty().bind(Shop.errorMessage); // Fehlerbindung

        eingabe.setOnAction(e -> {
            String eingabeText = eingabe.getText();
            if (eingabeText.matches("\\d+")) {
                int betrag = Integer.parseInt(eingabeText);
                ViewManager.getInstance().getController().addMoney(betrag);
                Shop.errorMessage.set(""); // Fehler löschen
                eingabe.clear();
            } else {
                Shop.errorMessage.set("Bruder gib eine gültige Zahl ein.");
            }
        });

        centerBox.getChildren().addAll(frage, eingabe, fehlerLabel);
        root.setCenter(centerBox);

        // Animationen
        StackPane animationLayer = new StackPane();

        ImageView anim1 = new ImageView(new Image("src/assets/GewonnenAnimation1make-it-rain-coin.gif"));
        anim1.setTranslateX(-300);
        anim1.setTranslateY(-200);

        ImageView anim2 = new ImageView(new Image("src/assets/GewonnenAnimationbag-bags.gif"));
        anim2.setTranslateX(200);
        anim2.setTranslateY(-150);

        ImageView anim3 = new ImageView(new Image("src/assets/GewonnenAnimation1make-it-rain-coin.gif"));
        anim3.setTranslateX(-250);
        anim3.setTranslateY(150);

        ImageView anim4 = new ImageView(new Image("src/assets/GewonnenAnimationdollar-bill.gif"));
        anim4.setTranslateX(250);
        anim4.setTranslateY(200);

        animationLayer.getChildren().addAll(anim1, anim2, anim3, anim4);

        // Gesamtansicht
        StackPane fullView = new StackPane(animationLayer, root);
        StackPane.setAlignment(root, Pos.CENTER);
        StackPane.setMargin(root, new Insets(50));

        return fullView;
    }
}