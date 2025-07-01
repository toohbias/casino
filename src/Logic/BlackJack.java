package src.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlackJack {

    private List<Integer> deck;
    private List<Integer> playerHand;
    private List<Integer> dealerHand;
    private boolean gameOver;

    public BlackJack() {
        startNewGame();
    }

    public void startNewGame() {
        deck = createDeck();
        Collections.shuffle(deck);
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();
        gameOver = false;

        playerHand.add(drawCard());
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        dealerHand.add(drawCard());
    }

    private List<Integer> createDeck() {
        List<Integer> newDeck = new ArrayList<>();
        for (int i = 2; i <= 10; i++) {
            for (int j = 0; j < 4; j++) newDeck.add(i);
        }
        for (int j = 0; j < 12; j++) newDeck.add(10); // Bildkarten
        for (int j = 0; j < 4; j++) newDeck.add(11);  // Asse
        return newDeck;
    }

    private int drawCard() {
        if (deck.isEmpty()) {
            deck = createDeck();
            Collections.shuffle(deck);
        }
        return deck.remove(deck.size() - 1);
    }

    private int calculateHand(List<Integer> hand) {
        int sum = 0;
        int aceCount = 0;
        for (int card : hand) {
            sum += card;
            if (card == 11) aceCount++;
        }
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }
        return sum;
    }

    public void playerHit() {
        if (!gameOver) {
            playerHand.add(drawCard());
            if (getPlayerTotal() > 21) {
                gameOver = true;
            }
        }
    }

    public void playerStand() {
        if (!gameOver) {
            while (getDealerTotal() < 17) {
                dealerHand.add(drawCard());
            }
            gameOver = true;
        }
    }

    public String getResultText() {
        int player = getPlayerTotal();
        int dealer = getDealerTotal();

        if (player > 21) return "Du hast verloren (über 21)";
        if (dealer > 21) return "Dealer hat verloren (über 21)";
        if (player > dealer) return "Du hast gewonnen!";
        if (player < dealer) return "Dealer gewinnt.";
        return "Unentschieden.";
    }

    public int getPlayerTotal() {
        return calculateHand(playerHand);
    }

    public int getDealerTotal() {
        return calculateHand(dealerHand);
    }

    public List<Integer> getPlayerHand() {
        return playerHand;
    }

    public List<Integer> getDealerHand() {
        return dealerHand;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}