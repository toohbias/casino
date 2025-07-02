package src.View_GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import src.Logic.Blackjack;
import src.Logic.CasinoController;

public class BlackjackView {

    public static Node getPane() {
        Blackjack game = new Blackjack();

        BorderPane root = new BorderPane();
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(20));
        centerBox.setAlignment(Pos.CENTER);

        // Labels für Karten und Punkte
        Label dealerLabel = new Label("Dealer-Karten:");
        Label dealerHandLabel = new Label();
        Label dealerValueLabel = new Label();

        Label playerLabel = new Label("Deine Karten:");
        Label playerHandLabel = new Label();
        Label playerValueLabel = new Label();

        // Gewinn-/Verloren-Label
        Label gewonnenLabel = new Label();
        gewonnenLabel.textProperty().bind(game.GewonnenText);
        Label verlorenLabel = new Label();
        verlorenLabel.textProperty().bind(game.VerlorenText);

        // Buttons
        Button hitButton = new Button("Karte nehmen");
        Button standButton = new Button("Bleiben");
        Button newGameButton = new Button("Neues Spiel");

        hitButton.setDisable(true);
        standButton.setDisable(true);

        // Einsatzfeld
        HBox einsatzBox = new HBox(10);
        einsatzBox.setAlignment(Pos.CENTER);
        Label einsatzFrage = new Label("Einsatz:");
        TextField einsatzFeld = new TextField();
        Label einsatzFehler = new Label();
        einsatzFehler.setStyle("-fx-text-fill: red;");

        einsatzBox.getChildren().addAll(einsatzFrage, einsatzFeld, newGameButton);

        // Neues Spiel starten
        newGameButton.setOnAction(e -> {
            String eingabe = einsatzFeld.getText();
            if (!eingabe.matches("\\d+")) {
                einsatzFehler.setText("Bitte gib eine gültige Zahl ein.");
                return;
            }

            int betrag = Integer.parseInt(eingabe);
            if (betrag <= 0) {
                einsatzFehler.setText("Der Einsatz muss größer als 0 sein.");
                return;
            }

            if (ViewManager.getInstance().getController().getMoney().get() < betrag) {
                einsatzFehler.setText("Nicht genügend Geld für diesen Einsatz.");
                return;
            }

            einsatzFehler.setText("");
            einsatzFeld.clear();
            game.newGame(betrag);

            hitButton.setDisable(false);
            standButton.setDisable(false);

            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        // Karte nehmen
        hitButton.setOnAction(e -> {
            game.playerHit();
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);

            if (game.isGameOver()) {
                hitButton.setDisable(true);
                standButton.setDisable(true);
            }
        });

        // Bleiben
        standButton.setOnAction(e -> {
            game.playerStand();
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);

            hitButton.setDisable(true);
            standButton.setDisable(true);
        });

        // Layout zusammenbauen
        centerBox.getChildren().addAll(
                dealerLabel, dealerHandLabel, dealerValueLabel,
                playerLabel, playerHandLabel, playerValueLabel,
                einsatzBox, einsatzFehler,
                new HBox(10, hitButton, standButton),
                gewonnenLabel, verlorenLabel
        );

        root.setCenter(centerBox);
        return root;
    }

    private static void updateCardLabels(Blackjack game, Label playerHandLabel, Label dealerHandLabel,
                                         Label playerValueLabel, Label dealerValueLabel) {
        playerHandLabel.setText(game.getPlayerHand().toString());
        dealerHandLabel.setText(game.getDealerHand().toString());

        playerValueLabel.setText("Punkte: " + game.getHandValue(game.getPlayerHand()));
        dealerValueLabel.setText("Punkte: " + game.getHandValue(game.getDealerHand()));
    }


}
