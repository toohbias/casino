package src.Logic;

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
        CasinoController controller = ViewManager.getInstance().getController();
        //Objekt von ViewMager der Zugriff auf CasionController hat wird erzeugt

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
            ViewManager.getInstance().getController().win(AktulerGestzterWert * 2); // Auszahlung über Controller
            return;
        }

        if (playerValue > dealerValue) {
            GewonnenText.set("Herzlichen Glückwunsch! Du hast gewonnen.");
            VerlorenText.set("");
            ViewManager.getInstance().getController().win(AktulerGestzterWert * 2);
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

    /**
     * Berechnet den Gesamtwert einer Hand im Blackjack.
     * Kartenwerte basieren auf Standard-Blackjack-Regeln:
     * - 2 bis 10: Zählen als ihr Wert
     * - Bube, Dame, König: Jeweils 10 Punkte
     * - Ass: Zählt als 11 Punkte, wird aber zu 1 reduziert, wenn nötig
     *
     * @param hand Liste von Karten (Zahlen von 1–52)
     * @return Gesamtwert der Hand unter Berücksichtigung von Ass-Anpassung
     */
    public int getHandValue(List<Integer> hand) {
        int value = 0;   // Gesamtpunktzahl der Hand
        int aces = 0;    // Anzahl der Asse (für spätere Korrektur bei >21)

        for (int card : hand) {
            // Berechne den "Rang" der Karte: 1 = Ass, 2–10 = Zahlenkarten, 11–13 = Bube/Dame/König
            int rank = ((card - 1) % 13) + 1;

            if (rank >= 2 && rank <= 10) {
                // Karten von 2 bis 10 zählen entsprechend ihrem Wert
                value += rank;
            } else if (rank >= 11 && rank <= 13) {
                // Bube, Dame, König zählen jeweils 10 Punkte
                value += 10;
            } else if (rank == 1) {
                // Ass zählt standardmäßig 11 Punkte
                value += 11;
                aces++; // Zähle Ass separat für mögliche Anpassung
            }
        }

        // Wenn der Gesamtwert über 21 ist, reduziere Ass(e) von 11 auf 1 (so lange nötig)
        while (value > 21 && aces > 0) {
            value -= 10; // Ziehe 10 ab (d.h. 11 wird zu 1)
            aces--;
        }

        return value;
    }

    public boolean isGameOver() {
        return gameOver;
    }

}
