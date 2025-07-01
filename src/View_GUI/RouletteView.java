package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import src.Logic.CasinoController;

import java.util.Arrays;
import java.util.HashSet;

/**
 * wrappt die {@code getPane()}-Methode für Roulette
 */
public class RouletteView {

    /**
     * alle Felder, die schwarz sind
     */
    private static final HashSet<Integer> BLACK = new HashSet<>(Arrays.asList(6, 15, 24, 33, 2, 8, 11, 17, 20, 26, 29, 35, 4, 10, 13, 22, 28, 31));

    /**
     * wie groß die Zellen proportional zum Fenster sind
     */
    private static final double CELL_SIZE = 20;

    /**
     * wie hoch der Einsatz ist
     */
    private static int stakes = CasinoController.DEFAULT_STAKES;

    /**
     * Roulette-Szene
     * hier drin kann der Spieler Roulette spielen
     * @return Szene
     */
    public static Node getPane() {
        BorderPane root = new BorderPane();
        root.setBottom(createBettingTable());
        Image roulette = new Image("src/assets/Roulette Desk.png");
        ImageView rouletteView = ViewManager.defaultView(roulette, 1.8);
        root.setCenter(rouletteView);
        return root;
    }

    /**
     * Erstellt die Tabelle, auf der man den Einsatz bestimmen kann
     * @return Tabelle
     */
    private static GridPane createBettingTable() {
        GridPane bettingTable = new GridPane();

        // 0-Feld ganz links
        Label zero = createBettingCell("0", 3, 0, 0, 1, 3, bettingTable);
        zero.setBackground(new Background(new BackgroundFill(Paint.valueOf("#15b500"), null, null)));
        // rote/schwarze Felder
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 12; j++) {
                int num = (j + 1) * 3 - i;
                Label field = createBettingCell(String.valueOf(num), 3, j + 1, i, 1, 1, bettingTable);
                field.setBackground(new Background(new BackgroundFill(Paint.valueOf(BLACK.contains(num) ? "#000000" : "#bc0101"), null, null)));
            }
            // 2to1-Feld ganz rechts
            createBettingCell("2to1", 5, 13, i, 1, 1, bettingTable);
        }
        // 12er
        createBettingCell("1st 12", 3, 1, 3, 4, 1, bettingTable);
        createBettingCell("2nd 12", 3, 5, 3, 4, 1, bettingTable);
        createBettingCell("3rd 12", 3, 9, 3, 4, 1, bettingTable);
        // unterste Reihe
        createBettingCell("1-18", 3, 1, 4, 2, 1, bettingTable);
        createBettingCell("EVEN", 3, 3, 4, 2, 1, bettingTable);
        Label red = createBettingCell("", 3, 5, 4, 2, 1, bettingTable);
        red.setBackground(new Background(new BackgroundFill(Paint.valueOf("#bc0101"), null, null)));
        Label black = createBettingCell("", 3, 7, 4, 2, 1, bettingTable);
        black.setBackground(new Background(new BackgroundFill(Paint.valueOf("#000000"), null, null)));
        createBettingCell("ODD", 3, 9, 4, 2, 1, bettingTable);
        createBettingCell("19-36", 3, 11, 4, 2, 1, bettingTable);

        // buttons for setting the stake and to confirm
        Image arrowUp = new Image("src/assets/ButtonUpv2.png");
        Image arrowUpPressed = new Image("src/assets/ButtonUpPressedv2.png");
        ImageView arrowUpView = ViewManager.defaultView(arrowUp, 75);
        // when pressed: raise stakes
        arrowUpView.imageProperty().bind(Bindings.when(arrowUpView.pressedProperty()).then(arrowUpPressed).otherwise(arrowUp));
        arrowUpView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(1, stakes));

        Image arrowDown = new Image("src/assets/ButtonDownv2.png");
        Image arrowDownPressed = new Image("src/assets/ButtonDownPressedv2.png");
        ImageView arrowDownView = ViewManager.defaultView(arrowDown, 75);
        // when pressed: reduce stakes
        arrowDownView.imageProperty().bind(Bindings.when(arrowDownView.pressedProperty()).then(arrowDownPressed).otherwise(arrowDown));
        arrowDownView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(-1, stakes));

        Image confirm = new Image("src/assets/RoundButtonv2.png");
        Image confirmPressed = new Image("src/assets/RoundButtonPressedv2.png");
        ImageView confirmView = ViewManager.defaultView(confirm, 60);
        // when pressed: confirm stakes
        confirmView.imageProperty().bind(Bindings.when(confirmView.pressedProperty()).then(confirmPressed).otherwise(confirm));
        confirmView.setOnMouseClicked(e -> ViewManager.getInstance().setStake(0, stakes));

        VBox stakeButtons = new VBox(arrowUpView, confirmView, arrowDownView);
        stakeButtons.prefHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL_SIZE).multiply(2));
        stakeButtons.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL_SIZE * 5));
        stakeButtons.setAlignment(Pos.CENTER);
        bettingTable.add(stakeButtons, 0, 3, 1, 2);

        bettingTable.setAlignment(Pos.CENTER);
        return  bettingTable;
    }

    /**
     * Helper-Klasse, um die Zellengenerierung zu vereinfachen
     * @param text Text in der Zelle
     * @param textSizeMult wie groß der Text sein soll
     * @param colIndex in welcher Spalete die Zelle sein soll
     * @param rowIndex in welcher Reihe die Zelle sein soll
     * @param colSpan über wie viele Spalten sich die Zelle zieht
     * @param rowSpan über wie viele Reihen sich die Zelle zieht
     * @param parent in welche Tabelle die Zelle soll
     * @return Label (Zelle)
     */
    private static Label createBettingCell(String text, double textSizeMult, int colIndex, int rowIndex, int colSpan, int rowSpan, GridPane parent) {
        // erstellt Zelle
        Label lbl = new Label(text);
        lbl.setId("betting-table");
        lbl.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt", ViewManager.getInstance().windowHeightProperty().divide(CELL_SIZE * textSizeMult)));
        parent.add(lbl, colIndex, rowIndex, colSpan, rowSpan);
        lbl.prefHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL_SIZE).multiply(rowSpan));
        lbl.prefWidthProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL_SIZE).multiply(colSpan));
        // Einsatz pro Feld bestimmen
        lbl.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY) {
                Image chip = new Image(getStakesChip());
                ImageView chipView = ViewManager.defaultView(chip, CELL_SIZE + 2.5);
                lbl.setGraphic(chipView);
                lbl.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else if(e.getButton() == MouseButton.SECONDARY) {
                lbl.setContentDisplay(ContentDisplay.TEXT_ONLY);
                lbl.setGraphic(null);
            }
        });
        return lbl;
    }

    private static String getStakesChip() {
        String result = "src/assets/Standard coin";
        switch (stakes) {
            case 1: result += " white 1"; break;
            case 2: result += " pink 2,5"; break;
            case 5: result += " red 5"; break;
            case 10: result += " blue 10"; break;
            case 25: result += " green 25"; break;
            case 100: result += " black 100"; break;
            case 500: result += " purple 500"; break;
            case 1000: result += " orange 1000"; break;
            case 5000: result += " grey 5000"; break;
        }
        return result + ".png";
    }

}
