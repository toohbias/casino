package src.Logic;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import src.View_GUI.BlackjackView;
import src.View_GUI.MusicManager;
import src.View_GUI.ViewManager;

import java.util.*;

import java.util.Random;

public class Blackjack {

    private final Random random = new Random();
    private final List<Integer> playerHand = new ArrayList<>();
    private final List<Integer> dealerHand = new ArrayList<>();
    private final Stack<Integer> deck = new Stack<>();
    private final BooleanProperty gameOver = new SimpleBooleanProperty(true);
    public static final StringProperty VerlorenText = new SimpleStringProperty("");
    public static final StringProperty GewonnenText = new SimpleStringProperty("");
    private int AktulerGestzterWert;
    private IntegerProperty[] playerPropertys;

    public Blackjack() {
        VerlorenText.set("");
        GewonnenText.set("");
        playerPropertys = new IntegerProperty[5];
        for (int i = 0; i < 5; i++) {
            playerPropertys[i] = new SimpleIntegerProperty(0);
        }
    }

    public void newGame(int betrag) {

        MusicManager.playSoundEffect("src/assets/soundEffects/deckShuffle.wav", +3f);

        deck.clear();
        playerHand.clear();
        dealerHand.clear();
        gameOver.set(false);
        AktulerGestzterWert = betrag;

        for (int i = 1; i <= 52; i++) {
            deck.push(i);
        }
        for (int i = 0; i < 5; i++) {
            playerPropertys[i].set(0);
        }

        playerHand.add(drawCardPlayer());
        playerHand.add(drawCardPlayer());
        dealerHand.add(drawCardDealer());
    }

    public void playerHit() {
        if (isGameOver().get()) {
            auswertung();
        } else {
            playerHand.add(drawCardPlayer());
        }
        if (getHandValue(playerHand) > 21) {
            gameOver.set(true);
            auswertung();
        }
    }

    public void playerStand() {
        gameOver.set(true);
        auswertung();
    }

    private int drawCardDealer() {
        MusicManager.playSoundEffect("src/assets/soundEffects/drawingCard.wav", +3f);

        while (true) {
            int card = random.nextInt(52) + 1;

            if (deck.contains(card)) {
                deck.remove((Integer) card);
                dealerHand.add(card);

                ImageView cardImage = new ImageView(new Image("src/assets/" + card + ".png"));

                if (dealerHand.size() == 1) {
                    cardImage.setTranslateX(-300);
                    cardImage.setTranslateY(-200);
                } else if (dealerHand.size() == 2) {
                    cardImage.setTranslateX(-240);
                    cardImage.setTranslateY(-200);
                } else if (dealerHand.size() == 3) {
                    cardImage.setTranslateX(-180);
                    cardImage.setTranslateY(-200);
                } else if (dealerHand.size() == 4) {
                    cardImage.setTranslateX(-120);
                    cardImage.setTranslateY(-200);
                } else if (dealerHand.size() == 5) {
                    cardImage.setTranslateX(-60);
                    cardImage.setTranslateY(-200);
                    gameOver.set(true);
                    auswertung();
                }

                return card;
            }
        }
    }
    private int drawCardPlayer() {
        MusicManager.playSoundEffect("src/assets/soundEffects/drawingCard.wav", +3f);

        while (true) {
            int card = random.nextInt(52) + 1;

            if (deck.contains(card)) {
                deck.remove((Integer) card);
                playerHand.add(card);

                ImageView cardImage = new ImageView(new Image("src/assets/" + card + ".png"));
                playerPropertys[playerHand.size()-1].set(card);

                if (playerHand.size() == 5) {
                    gameOver.set(true);
                    auswertung();
                }
                return card;
            }
        }
    }



    public void auswertung() {
        int playerValue = getHandValue(playerHand);
        int dealerValue = getHandValue(dealerHand);
        CasinoController controller = ViewManager.getInstance().getController();

        if (playerValue > 21) {
            VerlorenText.set("Du hast überkauft! Du hast verloren.");
            GewonnenText.set("");
            return;
        }

        while (dealerValue < 17) {
            dealerHand.add(drawCardDealer());
            dealerValue = getHandValue(dealerHand);
        }

        if (dealerValue > 21) {
            MusicManager.playSoundEffect("src/assets/soundEffects/bing.wav", +4f);
            GewonnenText.set("Dealer hat überkauft! Du hast gewonnen.");
            VerlorenText.set("");
            controller.win(AktulerGestzterWert * 2);
            return;
        }

        if (playerValue > dealerValue) {
            MusicManager.playSoundEffect("src/assets/soundEffects/bing.wav", +4f);
            GewonnenText.set("Herzlichen Glückwunsch! Du hast gewonnen.");
            VerlorenText.set("");
            controller.win(AktulerGestzterWert * 2);
        } else if (playerValue < dealerValue) {
            VerlorenText.set("Leider verloren. Dealer hat gewonnen.");
            GewonnenText.set("");
        } else {
            VerlorenText.set("Du hast leider verloren, probiere es noch mal");
            GewonnenText.set("");
        }
    }

    public List<Integer> getPlayerHand() {
        return new ArrayList<>(playerHand);
    }

    public List<Integer> getDealerHand() {
        return new ArrayList<>(dealerHand);
    }

    public int getHandValue(List<Integer> hand) {
        int value = 0;
        int aces = 0;

        for (int card : hand) {
            int rank = ((card - 1) % 13) + 1;

            if (rank >= 2 && rank <= 10) {
                value += rank;
            } else if (rank >= 11 && rank <= 13) {
                value += 10;
            } else if (rank == 1) {
                value += 11;
                aces++;
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        return value;
    }

    public BooleanProperty isGameOver() {
        return gameOver;
    }

    public IntegerProperty[] getPlayerProperty() {
        return playerPropertys;
    }
}
