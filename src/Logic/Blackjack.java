package src.Logic;

import java.util.*;

public class Blackjack {

    private final List<Integer> playerHand = new ArrayList<>();
    private final List<Integer> dealerHand = new ArrayList<>();
    private final Stack<Integer> deck = new Stack<>();
    private boolean gameOver = false;
    private String status = "";

    public Blackjack() {
        newGame();
    }

    public void newGame() {
        deck.clear();
        playerHand.clear();
        dealerHand.clear();
        gameOver = false;
        status = "";

        // Deck füllen
        for (int i = 1; i <= 52; i++) {
            deck.push(i);
        }
        Collections.shuffle(deck);

        // Karten austeilen
        playerHand.add(drawCard());
        playerHand.add(drawCard());
        dealerHand.add(drawCard());
        dealerHand.add(drawCard());

        status = "Karte nehmen oder passen?";
    }

    public void playerHit() {
        if (gameOver) return;
        playerHand.add(drawCard());
        if (getHandValue(playerHand) > 21) {
            gameOver = true;
            status = "Überkauft! Du verlierst.";
        }
    }

    public void playerStand() {
        if (gameOver) return;
        while (getHandValue(dealerHand) < 17) {
            dealerHand.add(drawCard());
        }

        int playerScore = getHandValue(playerHand);
        int dealerScore = getHandValue(dealerHand);

        if (dealerScore > 21 || playerScore > dealerScore) {
            status = "Du hast gewonnen!";
            ViewManager.getInstance().getController().win(ViewManager.getInstance().getController().getMoney().get() + 100); // 100 als Gewinn
        } else if (playerScore == dealerScore) {
            status = "Unentschieden!";
        } else {
            status = "Dealer gewinnt!";
        }

        gameOver = true;
    }

    private int drawCard() {
        return deck.pop();
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

    public String getStatus() {
        return status;
    }
}
