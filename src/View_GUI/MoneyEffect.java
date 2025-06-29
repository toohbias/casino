package src.View_GUI;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.Random;

/**
 * Effekt, der beim Gewinnen von einem Spiel angezeigt wird.
 * die Details stehen bei {@code animate(int moneyCount)}
 */
public class MoneyEffect {

    private static final Pane root = new Pane();

    private static final int COIN_SIZE = 15;
    private static final IntegerProperty coinState = new SimpleIntegerProperty(0);

    // init List of coin states
    private static final ObservableList<Image> states = FXCollections.observableArrayList(
            Arrays.asList(
//                    new Image("src/assets/Standard coin.png"),
                    new Image("src/assets/coin 3 4 positive.png"),
                    new Image("src/assets/coin side positivev2.png"),
                    new Image("src/assets/coin 3 4 negative.png"),
                    new Image("src/assets/coin side negativev2.png")
//                    new Image("src/assets/coin 3 4 positive.png")
            )
    );

    /**
     * Der MoneyEffect liegt im ViewManager als fxlayer über dem eigentlichen Spiel.
     * Obwohl er normalerweise durchsichtig ist, absorbiert er die MouseEvents,
     * wodurch der Spieler die Slot Machine nicht mehr betätigen kann.
     * das wird durch {@code Pane.setPickOnBounds(false)} vorgebäugt.
     * @return Szene
     */
    public static Pane getRoot() {
        root.setPickOnBounds(false); // damit die Mouseevents beim darunterliegenden Layer ankommen
        return root;
    }

    /**
     * hiermit wird die Coin Animation gestartet.
     * Dabei wird eine bestimmte Menge Coins zufällig in einem Bereich des Fensters erstellt,
     * die zum Money Frame fliegen und schließlich verblassen während dieser eine eigene Animation abspielt.
     * diese Methode wird in {@code SlotMachinev2.berechnen(int einsatz, int multiplikator)} eingesetzt, wenn der Spieler eine Slot-Runde gewinnt.
     * In Zukunft kann diese Methode auch für andere Spiele wie Roulette verwendet werden.
     * @param moneyCount Anzahl der Münzen
     */
    public static void animate(int moneyCount) {
        // fill List of coins
        for(int i = 0; i < moneyCount; i++) {
            ImageView img = ViewManager.defaultView(null, COIN_SIZE);
            img.imageProperty().bind(Bindings.valueAt(states, coinState));
            Label lbl = new Label("", img);
            double randX = (new Random().nextDouble() - 0.5) / 2.5;
            double randY = (new Random().nextDouble() - 0.5) / 2.5;
            lbl.layoutXProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(2).add(ViewManager.getInstance().windowHeightProperty().multiply(randX)).subtract(lbl.widthProperty().divide(2)));
            lbl.layoutYProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(2).subtract(ViewManager.getInstance().windowHeightProperty().multiply(randY)).subtract(lbl.heightProperty().divide(2)));

            // wildes animations Zeug
            MoveTo moveTo = new MoveTo();
            moveTo.xProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(2).add(ViewManager.getInstance().windowHeightProperty().multiply(randX)));
            moveTo.yProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(2).subtract(ViewManager.getInstance().windowHeightProperty().multiply(randY)));

            QuadCurveTo quadCurveTo = new QuadCurveTo();
            quadCurveTo.xProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(2));
            quadCurveTo.yProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(8));
            quadCurveTo.controlXProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(1.6));
            quadCurveTo.controlYProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(6));

            Path path = new Path();
            path.getElements().add(moveTo);
            path.getElements().add(quadCurveTo);

            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.seconds(1));
            pathTransition.setPath(path);

            FadeTransition fadeTransition = new FadeTransition();
            fadeTransition.setDuration(Duration.millis(250));
            fadeTransition.setDelay(Duration.millis(500));
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);

            ParallelTransition parallelTransition = new ParallelTransition(lbl, pathTransition, fadeTransition);

            Platform.runLater(() -> root.getChildren().add(lbl));
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {}
                Platform.runLater(() -> {
                    lbl.layoutXProperty().unbind();
                    lbl.setLayoutX(0.0);
                    lbl.layoutYProperty().unbind();
                    lbl.setLayoutY(0.0);
                    parallelTransition.play();
                });
            }).start();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * hiermit wird die Coin Animation gestoppt.
     * Alle Coins werden aus der Pane gelöscht.
     * diese Methode wird in {@code SlotMachinev2.berechnen(int einsatz, int multiplikator)} eingesetzt, wenn der Spieler eine Slot-Runde gewinnt.
     * In Zukunft kann diese Methode auch für andere Spiele wie Roulette verwendet werden.
     */
    public static void stop() {
        root.getChildren().clear();
    }

    /**
     * Dieser Thread ändert die Property für den aktuellen Coin-Frame, bis er interrupted wird
     * er wird in {@code SlotMachinev2.berechnen(int einsatz, int multiplikator)} eingesetzt, wenn der Spieler eine Slot-Runde gewinnt.
     * In Zukunft kann diese Methode auch für andere Spiele wie Roulette verwendet werden.
     * Momentan wird der Coin-Frame nicht geändert
     */
    public static class AnimationThread extends Thread {
        @Override
        @SuppressWarnings("BusyWait")
        public void run() {
            while(!isInterrupted()) {
                try {
                    Platform.runLater(() -> coinState.set((coinState.get() + 1) % 4));
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
