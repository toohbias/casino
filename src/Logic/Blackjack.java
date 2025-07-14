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
    private IntegerProperty[] dealerPropertys;

    public Blackjack() {
        VerlorenText.set("");
        GewonnenText.set("");

        dealerPropertys = new IntegerProperty[5];
        for (int i = 0; i < 5; i++) {
            dealerPropertys[i] = new SimpleIntegerProperty(0);
        }

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
            dealerPropertys[i].set(0);
        }

        drawCardPlayer();
        drawCardPlayer();
        drawCardDealer();
    }

    public void playerHit() {
        if (isGameOver().get()) {
            auswertung();
        } else {
            //playerHand.add(drawCardPlayer());
            drawCardPlayer();
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
            int card = random.nextInt(52) + 1; // Zufallszahl z 0<= z < 52

            if (deck.contains(card)) {
                deck.remove((Integer) card);
                dealerHand.add(card);

                int index = dealerHand.size() - 1;
                if (index < 4) {
                    dealerPropertys[index].set(card);
                }
                return card;
            }
        }
    }
    private int drawCardPlayer() {
        MusicManager.playSoundEffect("src/assets/soundEffects/drawingCard.wav", +3f);

        while (true) {
            int card = random.nextInt(52) + 1; // Zufallszahl z 0<= z < 52

            if (deck.contains(card)) {
                deck.remove((Integer) card);
                playerHand.add(card);

                int index = playerHand.size() - 1;
                if (index < 4) {
                    playerPropertys[index].set(card);}
                }
            if (playerHand.size() == 4) {
                   gameOver.set(true);
                   auswertung();
               }
                return card;
            }
        }

    public void auswertung() {
        int playerValue = getHandValue(playerHand);
        int dealerValue = getHandValue(dealerHand);
        CasinoController controller = ViewManager.getInstance().getController();

        if (playerValue > 21) {

            MusicManager.playSoundEffect("src/assets/soundEffects/BlackJackLoss.wav", 0.0f);

            VerlorenText.set("Du hast überkauft! Du hast verloren.");
            GewonnenText.set("");
            return;
        }

        while (dealerValue < 17)
        {
            if (dealerHand.size() < 4) {
                drawCardDealer();
                dealerValue = getHandValue(dealerHand);
            }
            else {
                break;
            }
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

            MusicManager.playSoundEffect("src/assets/soundEffects/BlackJackLoss.wav", 0.0f);

            VerlorenText.set("Leider verloren. Dealer hat gewonnen.");
            GewonnenText.set("");
        } else {

            MusicManager.playSoundEffect("src/assets/soundEffects/BlackJackLoss.wav", 0.0f);

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
        int assCount=0;
        for (int card : hand) {
            int r = (card-1)/4;
            if (r< 9 ){
                value = value + 2 + r;
            }
            else if (r< 12){
                value = value + 10;
            }
            else if (r==12){
                assCount++;
            }
        }
        if (assCount>0){
            value+= (assCount-1);
            if ( value +11 <= 21){
                value += 11;
            }
            else {
                value++;
            }
        }
        return value;
    }

    public BooleanProperty isGameOver() {
        return gameOver;
    }

    public IntegerProperty[] getPlayerProperty() {
        return playerPropertys;
    }
    public IntegerProperty[] getDealerProperty() {
        return dealerPropertys;
    }
}
