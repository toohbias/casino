package src.View_GUI;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class CasinoView {

    // helper for animations
    private static final DoubleProperty moneyFrameText = new SimpleDoubleProperty();

    /**
     * Der Inhalt des Start-Menus, wo man das Spiel auswählen kann
     * @return Inhalt des Menus
     */
    public static Node getPane() {
        BorderPane root = new BorderPane();

        Label lblHeading = new Label("Spielauswahl");
        lblHeading.setPadding(new Insets(50, 0, 0, 0));

        // dumme animation, vielleicht integrieren mit neuem gif
        ImageView casinoLogo = new ImageView(new Image("src/assets/animation.gif"));

        root.setTop(new BorderPane(lblHeading));

        //show shop view sleselection button
        ImageView shopImage = ViewManager.defaultView(new Image("src/assets/Shop.jpg"), 4);
        Button shopButton = new Button("SHOP", shopImage);
        shopButton.setContentDisplay(ContentDisplay.TOP);
        shopButton.setOnAction(e -> ViewManager.getInstance().setView(4));
        root.setRight(shopButton);

        HBox selection = new HBox();

        // show slot machine selection button
        ImageView img = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 3);
        Button slotImage = new Button("Slots", img);
        slotImage.setContentDisplay(ContentDisplay.TOP);
        slotImage.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.SLOT_VIEW));
        selection.getChildren().add(slotImage);

        // show roulette selection button TODO: change Image
        ImageView img2 = ViewManager.defaultView(new Image("src/assets/Slotmachinev2.png"), 3);
        Button rouletteImage = new Button("Roulette", img2);
        rouletteImage.setContentDisplay(ContentDisplay.TOP);
        rouletteImage.setOnAction(e -> ViewManager.getInstance().setView(ViewManager.ROULETTE_VIEW));
        selection.getChildren().add(rouletteImage);

        selection.spacingProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(10));
        selection.setAlignment(Pos.CENTER);

        root.setCenter(selection);

        return root;
    }

    /**
     * Zeigt das Money Frame an
     * @param money DoubleProperty vom angezeigten Geld aus dem {@code CasinoController}
     * @return MoneyFrame-Node
     */
    public static Node getMoneyFrame(DoubleProperty money) {
        BorderPane root = new BorderPane();
        Label moneyLbl = new Label();
        // geld geht langsam hoch
        moneyFrameText.set(money.get());
        money.addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() > oldValue.doubleValue()) {
                new Thread(() -> {
                    // timing adjusted to MoneyEffect
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException ignored) {}
                    for(int i = 0; i < 100; i++) {
                        Platform.runLater(() -> moneyFrameText.set(moneyFrameText.get() + (newValue.doubleValue() - oldValue.doubleValue()) / 100));
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException ignored) {}
                    }
                    // animation for how much money you won
                    try {
                        Thread.sleep(100);
                        Platform.runLater(() -> {
                            moneyLbl.textProperty().unbind();
                            moneyLbl.setTextFill(Paint.valueOf("#940303"));
                            moneyLbl.setText("+" + (newValue.doubleValue() - oldValue.doubleValue()));
                        });
                        for(int i = 0; i < 3; i++) {
                            Platform.runLater(() -> moneyLbl.setText("+" + (newValue.doubleValue() - oldValue.doubleValue())));
                            Thread.sleep(500);
                            Platform.runLater(() -> moneyLbl.setText(""));
                            Thread.sleep(300);
                        }
                        Platform.runLater(() -> {
                            moneyLbl.setTextFill(Paint.valueOf("black"));
                            moneyLbl.textProperty().bind(moneyFrameText.asString());
                        });
                    } catch (InterruptedException ignored) {}
                }).start();
            } else {
                moneyFrameText.set(money.get());
            }
        });
        moneyLbl.textProperty().bind(moneyFrameText.asString());
        moneyLbl.setAlignment(Pos.CENTER);
        moneyLbl.getStyleClass().clear();
        moneyLbl.setGraphic(ViewManager.defaultView(new Image("src/assets/Money Framev2.png"), 5));
        moneyLbl.setContentDisplay(ContentDisplay.CENTER);
        // has to divide by zero if text gets empty in winning animation but who cares
        moneyLbl.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt;", ViewManager.getInstance().windowHeightProperty().divide(5).divide(moneyLbl.textProperty().length())));
        root.setCenter(new StackPane(moneyLbl));
        return root;
    }
}

