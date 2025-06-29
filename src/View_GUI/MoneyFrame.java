package src.View_GUI;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

/**
 * Wrapper für das Money Frame
 */
public class MoneyFrame {

    private static final DoubleProperty money = new SimpleDoubleProperty(0);

    // helper for animations
    private static final DoubleProperty moneyFrameText = new SimpleDoubleProperty();

    // actual frame
    private static final Label moneyLbl = new Label();

    private static boolean isStakesAnimationRunning = false;


    /**
     * Zeigt das Money Frame an
     *
     * @param moneyIn DoubleProperty vom angezeigten Geld aus dem {@code CasinoController}
     * @return MoneyFrame-Node
     */
    public static Node init(DoubleProperty moneyIn) {
        money.bind(moneyIn);
        money.addListener((observable, oldValue, newValue) -> moneyFrameText.set(money.get()));
        //Money Frame
        moneyLbl.textProperty().bind(moneyFrameText.asString());
        moneyLbl.setAlignment(Pos.CENTER);
        moneyLbl.getStyleClass().clear();
        moneyLbl.setGraphic(ViewManager.defaultView(new Image("src/assets/Money Framev2.png"), 5));
        moneyLbl.setContentDisplay(ContentDisplay.CENTER);
        // has to divide by zero if text gets empty in winning animation but who cares
        moneyLbl.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", ViewManager.getInstance().windowHeightProperty().divide(5).divide(moneyLbl.textProperty().length())));
        return moneyLbl;
    }

    /**
     * zeigt die money animation an, wenn man gewinnt.
     * diese Methode wird aufgerufen von {@code CasinoController.win(double money)}
     * an die man durch {@code ViewManager.getInstance().getController()} kommt
     * @param oldValue alter Geldwert (aus {@code money.get()}
     * @param newValue neuer Geldwert
     */
    public static void animateMoneyFrame(double oldValue, double newValue) {
        // geld geht langsam hoch
        if (newValue > oldValue) {
            new Thread(() -> {
                // timing adjusted to MoneyEffect
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ignored) {
                }

                // money increase animation
                double iterator = oldValue;
                double step = (newValue - oldValue) / 100;
                while (iterator < newValue) {
                    final double it = iterator;
                    Platform.runLater(() -> moneyFrameText.set(it));
                    iterator += step;
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException ignored) {
                    }
                }
                // set to right number (https://github.com/toohbias/casino/issues/10)
                Platform.runLater(() -> moneyFrameText.set(newValue));

                // animation for how much money you won
                try {
                    Thread.sleep(100);
                    Platform.runLater(() -> {
                        moneyLbl.textProperty().unbind();
                        moneyLbl.setTextFill(Paint.valueOf("#940303"));
                        moneyLbl.setText("+" + (newValue - oldValue));
                    });
                    for (int i = 0; i < 3; i++) {
                        Platform.runLater(() -> moneyLbl.setText("+" + (newValue - oldValue)));
                        Thread.sleep(500);
                        Platform.runLater(() -> moneyLbl.setText(""));
                        Thread.sleep(300);
                    }
                    Platform.runLater(() -> {
                        moneyLbl.setTextFill(Paint.valueOf("black"));
                        moneyLbl.textProperty().bind(moneyFrameText.asString());
                    });
                } catch (InterruptedException ignored) {
                }
            }).start();
        } else {
            moneyFrameText.set(newValue);
        }
    }

    /**
     * zeigt eine animation, die den Einsatz anzeigt.
     * Diese Methode wird aufgerufen von {@code CasinoController.setStakes(int raiseOrReduce, double stakes)}
     * die wiederum von {@code ViewManager.getInstance().setStakes(int raiseOrReduce, double stakes} aufgerufen wird
     * die wiederum von den Einsatz-Knöpfen der Spiele aufgerufen wird.
     * @param stakes Einsatz
     */
    public static void runStakesAnimation(double stakes) {
        if(!isStakesAnimationRunning) {
            isStakesAnimationRunning = true;
            new AnimationThread().start();
        }
        moneyFrameText.set(stakes);
    }

    /**
     * stoppt die animation, die den Einsatz anzeigt.
     * Diese Methode kann direkt von den {@code ViewManager.getInstance().setStakes(0)} aufgerufen werden
     * oder von den Action-Wrappern der Spiele in {@code ViewManager}, z.B. {@code ViewManager.getInstance().leverPulled(int einsatz, ToggleButton slotArm)}
     */
    public static void stopStakesAnimation() {
        isStakesAnimationRunning = false;
        moneyFrameText.set(money.get());
    }

    /**
     * Dieser Thread animiert den Einsatz.
     * Er wird gestartet in {@code MoneyFrame.runStakesAnimation(double stakes)}
     * und wird gestoppt, wenn in {@code MoneyFrame.stopStakesAnimation} isStakesAnimationRunning auf false gesetzt wird.
     */
    private static class AnimationThread extends Thread {
        @Override
        @SuppressWarnings("BusyWait")
        public void run() {
            int opacity = 0;
            while(isStakesAnimationRunning) {
                try {
                    moneyLbl.setTextFill(Paint.valueOf("rgba(0, 0, 0, " + ((float) Math.abs(opacity % 20 - 10) / 15 + 0.2) + ")"));
                    opacity++;
                    Thread.sleep(40);
                } catch(InterruptedException ignored) {}
            }
            moneyLbl.setTextFill(Paint.valueOf("#000000"));
            interrupt();
        }
    }
}
