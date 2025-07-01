package src.View_GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.Logic.BlackJack;

public class BlackJackView  {

    private BlackJack game;
    private TextArea playerArea, dealerArea;
    private Label resultLabel;
    private Button hitButton, standButton, newGameButton;


    public void start(Stage stage) {
        game = new BlackJack();

        playerArea = new TextArea();
        dealerArea = new TextArea();
        resultLabel = new Label();
        hitButton = new Button("Hit");
        standButton = new Button("Stand");
        newGameButton = new Button("Neues Spiel");

        playerArea.setEditable(false);
        dealerArea.setEditable(false);

        hitButton.setOnAction(e -> {
            game.playerHit();
            updateUI();
        });

        standButton.setOnAction(e -> {
            game.playerStand();
            updateUI();
        });

        newGameButton.setOnAction(e -> {
            game.startNewGame();
            updateUI();
        });

        HBox buttons = new HBox(10, hitButton, standButton, newGameButton);
        VBox layout = new VBox(10,
                new Label("Spieler Hand:"), playerArea,
                new Label("Dealer Hand:"), dealerArea,
                buttons,
                resultLabel
        );
        layout.setPadding(new Insets(15));

        updateUI();

        Scene scene = new Scene(layout, 400, 500);
        stage.setTitle("BlackJack");
        stage.setScene(scene);
        stage.show();
    }

    private void updateUI() {
        playerArea.setText(game.getPlayerHand().toString() + " = " + game.getPlayerTotal());
        dealerArea.setText(game.getDealerHand().toString() +
                (game.isGameOver() ? " = " + game.getDealerTotal() : " (verdeckt)"));
        if (game.isGameOver()) {
            resultLabel.setText(game.getResultText());
            hitButton.setDisable(true);
            standButton.setDisable(true);
        } else {
            resultLabel.setText("");
            hitButton.setDisable(false);
            standButton.setDisable(false);
        }
    }

}