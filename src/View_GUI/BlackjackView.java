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
import javafx.beans.value.ChangeListener;

import java.util.Arrays;


public class BlackjackView {

    private static ObservableList<Image> cards =null;
    public static Node getPane() {
        Blackjack game = new Blackjack();
        if (cards == null) {
            Image im []= new Image[53];
            im[0]=null;
            for (int i=1;i<53;i++){
                im[i]=new Image("src/assets/" +i + ".png");
            }
            cards = FXCollections.observableArrayList(im);
        }

        BorderPane root = new BorderPane();

        HBox topLabels = new HBox();
        topLabels.setPadding(new Insets(20, 100, 0, 100));
        topLabels.setSpacing(10);

        Label dealerLabel = new Label("Dealer Karten");
        dealerLabel.setStyle(dealerLabel.getStyle().concat("-fx-font-size: 30px; -fx-text-fill: white;"));
        HBox dealerBox = new HBox(dealerLabel);
        dealerBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(dealerBox, Priority.ALWAYS);

        Label playerLabel = new Label("Spieler Karten");
        playerLabel.setStyle(playerLabel.getStyle().concat("-fx-font-size: 30px; -fx-text-fill: white;"));
        HBox playerBox = new HBox(playerLabel);
        playerBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setHgrow(playerBox, Priority.ALWAYS);

        topLabels.getChildren().addAll(dealerBox, playerBox);
        root.setTop(topLabels);

        // Kartenlabels (unsichtbar, da nicht verwendet)
        Label dealerHandLabel = new Label();
        dealerHandLabel.setVisible(false);
        Label playerHandLabel = new Label();
        playerHandLabel.setVisible(false);

        // Buttons zum spielen
        Button hitButton = new Button("Karte nehmen");
        Button standButton = new Button("Bleiben");
        hitButton.disableProperty().bind(game.isGameOver());
        standButton.disableProperty().bind(game.isGameOver());
        hitButton.visibleProperty().bind(hitButton.disabledProperty().not());
        standButton.visibleProperty().bind(hitButton.disabledProperty().not());
        hitButton.setPrefSize(200, 50);
        standButton.setPrefSize(200, 50);

        VBox buttonBox = new VBox(15, hitButton, standButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 0, 0)); // mittig über Einsatzfeld

        HBox einsatzBox = new HBox(10);
        einsatzBox.setAlignment(Pos.CENTER);
        einsatzBox.setPadding(new Insets(50, 0, 0, 0));
        Label einsatzFrage = new Label("Einsatz:");
        TextField einsatzFeld = new TextField();
        Button newGameButton = new Button("Neues Spiel");
        einsatzBox.getChildren().addAll(einsatzFrage, einsatzFeld, newGameButton);

        // Gewinn-/Verloren-Labels
        Label gewonnenLabel = new Label();
        gewonnenLabel.textProperty().bind(Blackjack.GewonnenText);
        Label verlorenLabel = new Label();
        verlorenLabel.textProperty().bind(Blackjack.VerlorenText);

        // Punkteanzeige
        HBox punktestandBox = new HBox();
        punktestandBox.setPadding(new Insets(30, 100, 10, 100));
        punktestandBox.setSpacing(10);

        Label dealerValueLabel = new Label();
        dealerValueLabel.setStyle("-fx-font-size: 22px;");
        HBox dealerPoints = new HBox(dealerValueLabel);
        dealerPoints.setAlignment(Pos.BOTTOM_LEFT);
        dealerPoints.setPrefWidth(200);

        Label playerValueLabel = new Label();
        playerValueLabel.setStyle("-fx-font-size: 22px;");
        HBox playerPoints = new HBox(playerValueLabel);
        playerPoints.setAlignment(Pos.BOTTOM_RIGHT);
        playerPoints.setPrefWidth(200);

        Region spacerPoints = new Region();
        HBox.setHgrow(spacerPoints, Priority.ALWAYS);
        punktestandBox.getChildren().addAll(dealerPoints, spacerPoints, playerPoints);



        // Spiel starten
        newGameButton.setOnAction(e -> {
            String eingabe = einsatzFeld.getText();
            if (!eingabe.matches("\\d+")) {
                ViewManager.getInstance().displayInfoMessage("Bitte gib eine gültige Zahl ein.");
                return;
            }

            int betrag = Integer.parseInt(eingabe);
            if (betrag <= 0) {
                ViewManager.getInstance().displayInfoMessage("Der Einsatz muss größer als 0 sein.");
                return;
            }

            if (ViewManager.getInstance().getController().getMoney().get() < betrag) {
                ViewManager.getInstance().displayInfoMessage("Nicht genügend Geld für diesen Einsatz.");
                return;
            }
            einsatzFeld.clear();
            game.newGame(betrag);

            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        hitButton.setOnAction(e -> {
            game.playerHit();
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        standButton.setOnAction(e -> {
            game.playerStand();
            updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        IntegerProperty stakes = new SimpleIntegerProperty(CasinoController.DEFAULT_STAKES);
        // buttons for setting the stake and to confirm
        Image arrowUp = new Image("src/assets/ButtonUpv2.png");
        Image arrowUpPressed = new Image("src/assets/ButtonUpPressedv2.png");
        ImageView arrowUpView = ViewManager.defaultView(arrowUp, 35);
        // when pressed: raise stakes
        arrowUpView.imageProperty().bind(Bindings.when(arrowUpView.pressedProperty()).then(arrowUpPressed).otherwise(arrowUp));
        arrowUpView.setOnMouseClicked(e -> stakes.set(CasinoController.getNextStakes(1,stakes.get())));
        arrowUpView.disableProperty().bind(game.isGameOver().not());

        Image arrowDown = new Image("src/assets/ButtonDownv2.png");
        Image arrowDownPressed = new Image("src/assets/ButtonDownPressedv2.png");
        ImageView arrowDownView = ViewManager.defaultView(arrowDown, 35);
        // when pressed: reduce stakes
        arrowDownView.imageProperty().bind(Bindings.when(arrowDownView.pressedProperty()).then(arrowDownPressed).otherwise(arrowDown));
        arrowDownView.setOnMouseClicked(e -> stakes.set(CasinoController.getNextStakes(-1,stakes.get())));
        arrowDownView.disableProperty().bind(game.isGameOver().not());

        Image confirm = new Image("src/assets/RoundButtonv2.png");
        Image confirmPressed = new Image("src/assets/RoundButtonPressedv2.png");
        ImageView confirmView = ViewManager.defaultView(confirm, 25);
        // when pressed: confirm stakes
        confirmView.disableProperty().bind(game.isGameOver().not());
        confirmView.imageProperty().bind(Bindings.when(confirmView.pressedProperty()).then(confirmPressed).otherwise(confirm));
        confirmView.setOnMouseClicked(e ->{
        if (ViewManager.getInstance().getController().getMoney().get() < stakes.get() ) {
            ViewManager.getInstance().displayInfoMessage("Nicht genügend Geld für diesen Einsatz.");
            return;
        }

        einsatzFeld.clear();
        game.newGame(stakes.get());

        updateCardLabels(game, playerHandLabel, dealerHandLabel, playerValueLabel, dealerValueLabel);
        });

        // arranges the raise/reduce steaks buttons horizontally
        VBox raiseAndLowerStakes = new VBox(arrowUpView, arrowDownView);
        raiseAndLowerStakes.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(50));

        //
        Label counter = new Label();
        counter.setAlignment(Pos.CENTER);
        counter.textProperty().bind(stakes.asString());
        counter.textProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                einsatzFeld.setText(counter.getText());
            }
        }));

        // arranges the steaks buttons and confirm button horizontally
        HBox stake = new HBox( counter,raiseAndLowerStakes,confirmView);
        stake.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(12.5));
        stake.setAlignment(Pos.CENTER);

        //Karten
        HBox playerHand = new HBox(5);
        playerHand.setAlignment(Pos.CENTER);
        IntegerProperty index []= game.getPlayerProperty();
        for (int i=0;i<5;i++){
            ImageView im = new ImageView();
            im.setFitWidth(50);
            im.setFitHeight(50);
            im.imageProperty().bind(Bindings.valueAt(cards,index[i] ));
            playerHand.getChildren().add(im);
        }



        // Zentrum zusammenbauen
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(10));
        centerBox.getChildren().addAll(
                buttonBox,
                einsatzBox,
                playerHand,
                stake,
                gewonnenLabel,
                verlorenLabel,
                punktestandBox
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
