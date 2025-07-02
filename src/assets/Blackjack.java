package src.assets;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import src.View_GUI.ViewManager;

import java.util.*;

import java.util.Random;

public class Blackjack {

    private final Random random = new Random();
    private final List<Integer> playerHand = new ArrayList<>();
    private final List<Integer> dealerHand = new ArrayList<>();
    private final Stack<Integer> deck = new Stack<>();
    private boolean gameOver = false;
    public static final StringProperty VerlorenText = new SimpleStringProperty("");
    public static final StringProperty GewonnenText = new SimpleStringProperty("");
    private int AktulerGestzterWert ;

    public Blackjack() {
    }

    public void newGame(int betrag) {
        deck.clear();
        playerHand.clear();
        dealerHand.clear();
        gameOver = false;
        AktulerGestzterWert = betrag;


        // Deck füllen
        for (int i = 1; i <= 52; i++) {
            deck.push(i);
        }

        // Karten austeilen
        playerHand.add(drawCard());
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        dealerHand.add(drawCard());

    }

    public void playerHit() {
        if (isGameOver()) {
            auswertung();
        }
        else playerHand.add(drawCard());
        if (getHandValue(playerHand) > 21) {
            gameOver = true;
            auswertung();
        }
    }

    public void playerStand() {
        gameOver = true;
        auswertung();
    }

    private int drawCard() {
        while (true) {
            int card = random.nextInt(52) + 1; // Zufallszahl zwischen 1 und 52

            if (deck.contains(card)) {
                deck.remove((Integer) card);
                return card;
            }

            // Wenn die Karte NICHT im Deck ist → einfach nochmal versuchen
        }
    }
    public void auswertung() {
        int playerValue = getHandValue(playerHand);
        int dealerValue = getHandValue(dealerHand);

        if (playerValue > 21) {
            VerlorenText.set("Du hast überkauft! Du hast verloren.");
            GewonnenText.set("");
            return;
        }

        while (dealerValue < 17) {
            dealerHand.add(drawCard());
            dealerValue = getHandValue(dealerHand);
        }

        if (dealerValue > 21) {
            GewonnenText.set("Dealer hat überkauft! Du hast gewonnen.");
            VerlorenText.set("");
            ViewManager.getInstance().getController().addMoney(AktulerGestzterWert * 2); // Auszahlung über Controller
            return;
        }

        if (playerValue > dealerValue) {
            GewonnenText.set("Herzlichen Glückwunsch! Du hast gewonnen.");
            VerlorenText.set("");
            ViewManager.getInstance().getController().addMoney(AktulerGestzterWert * 2);
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
            if (rank >= 10) value += 10;
            else if (rank == 1) {
                value += 11;
                aces++;
            } else {
                value += rank;
            }
        }
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
        return value;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
