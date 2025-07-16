package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import src.Logic.Blackjack;
import src.Logic.CasinoController;

public class BlackjackView {

    private static ObservableList<Image> cards = null;

    private static int stakes = CasinoController.DEFAULT_STAKES;

    public static Node getPane() {
        Blackjack game = new Blackjack();

        if (cards == null) {
            Image[] im = new Image[53];
            im[0] = null;
            for (int i = 1; i < 53; i++) {
                im[i] = new Image("src/assets/" + i + ".png");
            }
            cards = FXCollections.observableArrayList(im);
        }

        BorderPane root = new BorderPane();

        // Obere Labels: Dealer & Spieler
        HBox topLabels = new HBox(10);
        topLabels.setPadding(new Insets(20, 100, 0, 100));

        Label dealerLabel = new Label("Dealer");
        dealerLabel.setMaxWidth(200);
        dealerLabel.setMinWidth(200);
        dealerLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        // Unsichtbare Labels
        Label dealerHandLabel = new Label();
        dealerHandLabel.setVisible(false);
        Label playerHandLabel = new Label();
        playerHandLabel.setVisible(false);

        // Punkte
        Label dealerValueLabel = new Label();
        dealerValueLabel.setStyle("-fx-font-size: 20px;");
        HBox.setMargin(dealerLabel, new Insets(5, 20, 5, 20));
        dealerLabel.setAlignment(Pos.CENTER_RIGHT);
        dealerValueLabel.setMaxWidth(200);
        dealerValueLabel.setMinWidth(200);
        HBox.setMargin(dealerValueLabel, new Insets(5, 20, 5, 20));
        dealerValueLabel.setAlignment(Pos.CENTER_LEFT);

        // Karten anzeigen Player
        HBox playerHand = new HBox();
        playerHand.setMaxHeight(140);
        playerHand.setMinHeight(140);
        Label playerLabel = new Label("Spieler");
        playerLabel.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(playerLabel, new Insets(5, 20, 5, 20));
        playerLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        playerLabel.setMaxWidth(200);
        playerLabel.setMinWidth(200);

        playerHand.getChildren().add(playerLabel);
        playerHand.setTranslateY(87);
        playerHand.setAlignment(Pos.TOP_CENTER);

        // Player Bild erstellen
        IntegerProperty[] playerIndices = game.getPlayerProperty();

        for (int i = 0; i < 4; i++) {
            ImageView cardView = new ImageView();
            cardView.setPreserveRatio(true);
            cardView.setFitWidth(80);
            cardView.imageProperty().bind(Bindings.valueAt(cards, playerIndices[i]));
            HBox.setMargin(cardView, new Insets(5, 45, 5, 45));
            playerHand.getChildren().add(cardView);
        }
        Label playerValueLabel = new Label();
        playerValueLabel.setStyle("-fx-font-size: 20px;");
        playerValueLabel.setMaxWidth(200);
        playerValueLabel.setMinWidth(200);
        HBox.setMargin(playerValueLabel, new Insets(5, 20, 5, 20));
        playerValueLabel.setAlignment(Pos.CENTER_LEFT);

        playerHand.getChildren().add(playerValueLabel);

        // Karten anzeigen Dealer
        HBox DealerHand = new HBox();
        DealerHand.setMinHeight(120);
        DealerHand.setMaxHeight(120);
        DealerHand.setTranslateY(20);
        DealerHand.setAlignment(Pos.TOP_CENTER);
        IntegerProperty[] DealerIndices = game.getDealerProperty();
        DealerHand.getChildren().add(dealerLabel);

        for (int i = 0; i < 4; i++) {
            ImageView cardView2 = new ImageView();
            cardView2.setPreserveRatio(true);
            cardView2.setFitWidth(80);
            cardView2.imageProperty().bind(Bindings.valueAt(cards, DealerIndices[i]));
            HBox.setMargin(cardView2, new Insets(5, 45, 5, 45));
            DealerHand.getChildren().add(cardView2);
        }
        DealerHand.getChildren().add(dealerValueLabel);

        // Buttons: Karte nehmen / Bleiben
        Button hitButton = new Button("Karte nehmen");
        Button standButton = new Button("Bleiben");
        hitButton.setPrefSize(200, 50);
        standButton.setPrefSize(200, 50);
        hitButton.disableProperty().bind(game.isGameOver());
        standButton.disableProperty().bind(game.isGameOver());


        // Einsätze erhöhen/senken
//        IntegerProperty stakes = new SimpleIntegerProperty(CasinoController.DEFAULT_STAKES);
        ImageView arrowUpView = createStakeButton("ButtonUpv2.png", "ButtonUpPressedv2.png");
        arrowUpView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(1, stakes));
        arrowUpView.disableProperty().bind(game.isGameOver().not());

        ImageView arrowDownView = createStakeButton("ButtonDownv2.png", "ButtonDownPressedv2.png");
        arrowDownView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(-1, stakes));
        arrowDownView.disableProperty().bind(game.isGameOver().not());

        // Start-Button: Cards Stack + Label
        ImageView startGameView = new ImageView(new Image("src/assets/Cards Stack.png"));
        startGameView.setFitWidth(80);
        startGameView.setPreserveRatio(true);
        Label startLabel = new Label("Spiel starten");
        startLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        VBox startGameBox = new VBox(startGameView, startLabel);
        startGameBox.setAlignment(Pos.CENTER);
        startGameBox.setSpacing(5);
        startGameBox.setOnMouseClicked(e -> {
            ViewManager.getInstance().setStake(0, stakes);
            if (ViewManager.getInstance().getController().getMoney().get() < stakes) {
                ViewManager.getInstance().displayInfoMessage("Nicht genügend Geld für diesen Einsatz.");
                return;
            }
            game.newGame(stakes);
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });
        startGameBox.disableProperty().bind(game.isGameOver().not());

        VBox raiseAndLowerStakes = new VBox(arrowUpView, arrowDownView);
        raiseAndLowerStakes.setAlignment(Pos.CENTER);
        raiseAndLowerStakes.setSpacing(10);
        raiseAndLowerStakes.disableProperty().bind(game.isGameOver().not());

        // stake: deine originale Struktur, nur kein translateX/Y
        HBox stake = new HBox(raiseAndLowerStakes, startGameBox);
        stake.setAlignment(Pos.CENTER);
        stake.setTranslateY(50);
        stake.setSpacing(30);

        stake.getChildren().addAll(hitButton, standButton);
        stake.setMinHeight(140);
        stake.setMaxHeight(140);
        stake.setPadding(new Insets(0, 0, 0, 30));  // Padding links, damit nicht am Rand klebt

        // Center zusammensetzen.
        VBox centerBox = new VBox(15,
                DealerHand,
                stake,
                playerHand
        );
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(10));
        root.setCenter(centerBox);

        // Button-Aktionen
        hitButton.setOnAction(e -> {
            game.playerHit();
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        standButton.setOnAction(e -> {
            game.playerStand();
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        ImageView desk = new ImageView(new Image("src/assets/Deskcardsv5.png"));
        desk.setPreserveRatio(true);
        desk.fitHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().subtract(ViewManager.getInstance().getTopBar().heightProperty()));
        ViewManager.getInstance().getDecoLayer().setBottom(new BorderPane(desk));

        ViewManager.getInstance().getFXLayer().setCenter(MoneyEffect.getRoot());

        return root;
    }

    private static void updateCardLabels(Blackjack game, Label playerHandLabel, Label dealerHandLabel,
                                         Label playerValueLabel, Label dealerValueLabel) {
        playerHandLabel.setText(game.getPlayerHand().toString());
        dealerHandLabel.setText(game.getDealerHand().toString());
        playerValueLabel.setText("Punkte: " + game.getHandValue(game.getPlayerHand()));
        dealerValueLabel.setText("Punkte: " + game.getHandValue(game.getDealerHand()));
    }

    private static ImageView createStakeButton(String normalImg, String pressedImg) {
        Image normal = new Image("src/assets/" + normalImg);
        Image pressed = new Image("src/assets/" + pressedImg);
        ImageView view = ViewManager.defaultView(normal, 35);
        view.imageProperty().bind(Bindings.when(view.pressedProperty()).then(pressed).otherwise(normal));
        return view;
    }
}
