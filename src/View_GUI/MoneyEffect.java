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

public class MoneyEffect {

    private static final Pane root = new Pane();

    private static final double COIN_SIZE = 15;
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

    public static Pane getRoot() {
        root.setPickOnBounds(false); // damit die Mouseevents beim darunterliegenden Layer ankommen
        return root;
    }

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

    public static void stop() {
        root.getChildren().clear();
    }

    public static class AnimationThread extends Thread {
        @SuppressWarnings("BusyWait")
        @Override
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
