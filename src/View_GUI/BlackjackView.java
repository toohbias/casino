package src.View_GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import src.Logic.Blackjack;

public class BlackjackView {

    public static Node getPane() {
        BorderPane root = new BorderPane();
        Blackjack game = new Blackjack();

        Label title = new Label("Blackjack");
        title.setStyle("-fx-font-size: 28pt; -fx-text-fill: white;");

        HBox dealerCards = new HBox(10);
        HBox playerCards = new HBox(10);
        dealerCards.setAlignment(Pos.CENTER);
        playerCards.setAlignment(Pos.CENTER);

        // Labels für Status
        Label infoLabel = new Label("Spiel starten...");
        infoLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16pt;");
        Label dealerLabel = new Label("Dealer");
        Label playerLabel = new Label("Spieler");

        // Buttons
        Button hitButton = new Button("Karte nehmen");
        Button standButton = new Button("Passen");
        Button newGameButton = new Button("Neues Spiel");

        hitButton.setOnAction(e -> {
            game.playerHit();
            updateCards(playerCards, game.getPlayerHand());
            infoLabel.setText(game.getStatus());
            if (game.isGameOver()) {
                hitButton.setDisable(true);
                standButton.setDisable(true);
                updateCards(dealerCards, game.getDealerHand());
            }
        });

        standButton.setOnAction(e -> {
            game.playerStand();
            updateCards(dealerCards, game.getDealerHand());
            infoLabel.setText(game.getStatus());
            hitButton.setDisable(true);
            standButton.setDisable(true);
        });

        newGameButton.setOnAction(e -> {
            game.newGame();
            updateCards(playerCards, game.getPlayerHand());
            dealerCards.getChildren().clear(); // Dealer zeigt anfangs nur eine Karte
            dealerCards.getChildren().add(getCardBackImage()); // verdeckte Karte
            infoLabel.setText("Karte nehmen oder passen?");
            hitButton.setDisable(false);
            standButton.setDisable(false);
        });

        // Layout
        VBox vbox = new VBox(20, title, dealerLabel, dealerCards, playerLabel, playerCards, infoLabel, new HBox(10, hitButton, standButton, newGameButton));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        StackPane background = new StackPane();
        // ToDo HIER BILD ALS HINTERGRUND EINSETZEN (z.B. Spieltisch)
        // background.getChildren().add(new ImageView(new Image("src/assets/BlackjackBackground.png")));

        background.getChildren().add(vbox);

        root.setCenter(background);
        return root;
    }

    private static void updateCards(HBox box, java.util.List<Integer> cards) {
        box.getChildren().clear();
        for (int card : cards) {
            box.getChildren().add(getCardImage(card));
        }
    }

    private static ImageView getCardImage(int cardId) {
        // TODO HIER BILD ZUR KARTE cardId EINSETZEN
        // z.B. "src/assets/cards/2H.png" für Zwei Herz
        String path = "src/assets/cards/" + cardId + ".png"; // Platzhalter
        ImageView imageView = new ImageView(new Image(path));
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private static ImageView getCardBackImage() {
        // ToDO HIER BILD FÜR RÜCKSEITE DER KARTE EINSETZEN
        ImageView imageView = new ImageView(new Image("src/assets/cards/back.png"));
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}
