package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import src.Logic.Blackjack;
import src.Logic.CasinoController;

public class BlackjackView {

    private static ObservableList<Image> cards = null;

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

        Label dealerLabel = new Label("Dealer Karten");
        dealerLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        HBox dealerBox = new HBox(dealerLabel);
        dealerBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(dealerBox, Priority.ALWAYS);

        Label playerLabel = new Label("Spieler Karten");
        playerLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        HBox playerBox = new HBox(playerLabel);
        playerBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(playerBox, Priority.ALWAYS);

        topLabels.getChildren().addAll(dealerBox, playerBox);
        root.setTop(topLabels);

        // Unsichtbare Labels
        Label dealerHandLabel = new Label();
        dealerHandLabel.setVisible(false);
        Label playerHandLabel = new Label();
        playerHandLabel.setVisible(false);

        // Punkte
        Label dealerValueLabel = new Label();
        dealerValueLabel.setStyle("-fx-font-size: 36px;");
        Label playerValueLabel = new Label();
        playerValueLabel.setStyle("-fx-font-size: 36px;");

        HBox punktestandBox = new HBox(10,
                new HBox(dealerValueLabel), new Region(), new HBox(playerValueLabel)
        );
        punktestandBox.setPadding(new Insets(0, 100, 10, 100));
        HBox.setHgrow(punktestandBox.getChildren().get(1), Priority.ALWAYS);

        // Karten anzeigen
        HBox playerHand = new HBox(5);
        playerHand.setAlignment(Pos.CENTER);
        IntegerProperty[] playerIndices = game.getPlayerProperty();
        for (int i = 0; i < 5; i++) {
            ImageView cardView = new ImageView();
            cardView.setFitWidth(50);
            cardView.setFitHeight(50);
            cardView.imageProperty().bind(Bindings.valueAt(cards, playerIndices[i]));
            playerHand.getChildren().add(cardView);
        }

        // Buttons: Karte nehmen / Bleiben
        Button hitButton = new Button("Karte nehmen");
        Button standButton = new Button("Bleiben");
        hitButton.setPrefSize(200, 50);
        standButton.setPrefSize(200, 50);
        hitButton.disableProperty().bind(game.isGameOver());
        standButton.disableProperty().bind(game.isGameOver());
        hitButton.visibleProperty().bind(hitButton.disabledProperty().not());
        standButton.visibleProperty().bind(hitButton.disabledProperty().not());
        VBox buttonBox = new VBox(15, hitButton, standButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Gewinn-/Verloren-Labels
        Label gewonnenLabel = new Label();
        gewonnenLabel.textProperty().bind(Blackjack.GewonnenText);
        Label verlorenLabel = new Label();
        verlorenLabel.textProperty().bind(Blackjack.VerlorenText);

        // Einsätze erhöhen/senken
        IntegerProperty stakes = new SimpleIntegerProperty(CasinoController.DEFAULT_STAKES);
        ImageView arrowUpView = createStakeButton("ButtonUpv2.png", "ButtonUpPressedv2.png");
        arrowUpView.setOnMouseClicked(e -> stakes.set(CasinoController.getNextStakes(1, stakes.get())));
        arrowUpView.disableProperty().bind(game.isGameOver().not());

        ImageView arrowDownView = createStakeButton("ButtonDownv2.png", "ButtonDownPressedv2.png");
        arrowDownView.setOnMouseClicked(e -> stakes.set(CasinoController.getNextStakes(-1, stakes.get())));
        arrowDownView.disableProperty().bind(game.isGameOver().not());

        ImageView confirmView = createStakeButton("RoundButtonv2.png", "RoundButtonPressedv2.png");
        confirmView.setOnMouseClicked(e -> {
            if (ViewManager.getInstance().getController().getMoney().get() < stakes.get()) {
                ViewManager.getInstance().displayInfoMessage("Nicht genügend Geld für diesen Einsatz.");
                return;
            }
            game.newGame(stakes.get());
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });
        confirmView.disableProperty().bind(game.isGameOver().not());

        VBox raiseAndLowerStakes = new VBox(arrowUpView, arrowDownView);
        raiseAndLowerStakes.setAlignment(Pos.CENTER);
        raiseAndLowerStakes.setSpacing(10);
        HBox stake = new HBox(raiseAndLowerStakes, confirmView);
        stake.setAlignment(Pos.CENTER);
        stake.setSpacing(30);

        // Center zusammensetzen.
        VBox centerBox = new VBox(15,
                buttonBox,
                playerHand,
                stake,
                gewonnenLabel,
                verlorenLabel,
                punktestandBox
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

        ImageView desk = new ImageView(new Image("src/assets/Blackjack desk .png"));
        desk.setPreserveRatio(true);
        desk.fitHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().subtract(ViewManager.getInstance().getTopBar().heightProperty()));
        ViewManager.getInstance().getDecoLayer().setBottom(new BorderPane(desk));

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
