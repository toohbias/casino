package src.View_GUI;

import com.sun.javafx.scene.layout.region.Margins;
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
        dealerLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
        /*
        HBox dealerBox = new HBox(dealerLabel);
        dealerBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(dealerBox, Priority.ALWAYS);
         */

        /*
        HBox playerBox = new HBox(playerLabel);
        playerBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(playerBox, Priority.ALWAYS);
         */
        //topLabels.getChildren().addAll(dealerBox, playerBox);
        //root.setTop(topLabels);

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

        /*
        HBox punktestandBox = new HBox(10,
                new HBox(dealerValueLabel), new Region(), new HBox(playerValueLabel)
        );
        punktestandBox.setPadding(new Insets(0, 100, 10, 100));
        HBox.setHgrow(punktestandBox.getChildren().get(1), Priority.ALWAYS);
        */
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
        playerHand.setTranslateY(90);
        //playerHand.setBackground(new Background(new BackgroundImage(new Image("src/assets/Deskcardsv5.png"),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        playerHand.setAlignment(Pos.TOP_CENTER);
        IntegerProperty[] playerIndices = game.getPlayerProperty();

        for (int i = 0; i < 4; i++) {
            ImageView cardView = new ImageView();
            cardView.setPreserveRatio(true);
            cardView.setFitWidth(80);
            cardView.imageProperty().bind(Bindings.valueAt(cards, playerIndices[i]));
            HBox.setMargin(cardView,new Insets(5,45,5,45));
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
        DealerHand.setMinHeight(140);
        DealerHand.setMaxHeight(140);
        DealerHand.setTranslateY(15);
        //playerHand.setBackground(new Background(new BackgroundImage(new Image("src/assets/Deskcardsv5.png"),BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,BackgroundSize.DEFAULT)));
        DealerHand.setAlignment(Pos.BOTTOM_CENTER);
        IntegerProperty[] DealerIndices = game.getDealerProperty();
        DealerHand.getChildren().add(dealerLabel);

        for (int i = 0; i < 4; i++) {
            ImageView cardView2 = new ImageView();
            cardView2.setPreserveRatio(true);
            cardView2.setFitWidth(80);
            cardView2.imageProperty().bind(Bindings.valueAt(cards, DealerIndices[i]));
            HBox.setMargin(cardView2,new Insets(5,45,5,45));
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
        stake.getChildren().add(buttonBox);
        stake.setSpacing(30);

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
            System.out.println(playerHand.getHeight());
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        standButton.setOnAction(e -> {
            game.playerStand();
            System.out.println(playerHand.getHeight());
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        ImageView desk = new ImageView(new Image("src/assets/Deskcardsv5.png"));
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
