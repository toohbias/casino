package src.View_GUI;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    private static final double CELL = 30;
    private static final int CELLS_X = 27;
    private static final int CELLS_Y = 11;
    private static final DoubleProperty CELL_ABS = new SimpleDoubleProperty();

    /**
     * wie hoch der Einsatz ist
     */
    private static int stakes = CasinoController.DEFAULT_STAKES;

    /**
     * DoubleProperties, an die der Rotationswinkel der Scheibe und des Balls gebunden sind.
     * Diese werden mit {@code Roulettev2.spin(int einsatz)} gesteuert.
     */
    public static DoubleProperty DESK_ROTATION = new SimpleDoubleProperty(0);
    public static DoubleProperty BALL_ROTATION = new SimpleDoubleProperty(0);

    private static final BorderPane root = new BorderPane();

    /**
     * Roulette-Szene
     * hier drin kann der Spieler Roulette spielen
     * @return Szene
     */
    public static Node getPane() {
        // buttons for setting the stake and to confirm
        Image arrowUp = new Image("src/assets/ButtonUpv2.png");
        Image arrowUpPressed = new Image("src/assets/ButtonUpPressedv2.png");
        ImageView arrowUpView = ViewManager.defaultView(arrowUp, CELL);
        // when pressed: raise stakes
        arrowUpView.imageProperty().bind(Bindings.when(arrowUpView.pressedProperty()).then(arrowUpPressed).otherwise(arrowUp));
        arrowUpView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(1, stakes));

        Image arrowDown = new Image("src/assets/ButtonDownv2.png");
        Image arrowDownPressed = new Image("src/assets/ButtonDownPressedv2.png");
        ImageView arrowDownView = ViewManager.defaultView(arrowUp, CELL);
        // when pressed: reduce stakes
        arrowDownView.imageProperty().bind(Bindings.when(arrowDownView.pressedProperty()).then(arrowDownPressed).otherwise(arrowDown));
        arrowDownView.setOnMouseClicked(e -> stakes = ViewManager.getInstance().setStake(-1, stakes));

        Image confirm = new Image("src/assets/RoundButtonv2.png");
        Image confirmPressed = new Image("src/assets/RoundButtonPressedv2.png");
        ImageView confirmView = ViewManager.defaultView(confirm, CELL * 0.8);
        // when pressed: confirm stakes
        confirmView.imageProperty().bind(Bindings.when(confirmView.pressedProperty()).then(confirmPressed).otherwise(confirm));
        confirmView.setOnMouseClicked(e -> ViewManager.getInstance().setStake(0, stakes));

        VBox stakeButtons = new VBox(arrowUpView, confirmView, arrowDownView);
        stakeButtons.prefHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL * 2).multiply(CELLS_Y));
        stakeButtons.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL * 2));
        stakeButtons.setAlignment(Pos.CENTER);

        // start button
        ImageView start = new ImageView(new Image("src/assets/Roullete Wheel.png"));
        start.setPreserveRatio(true);
        start.fitWidthProperty().bind(stakeButtons.widthProperty());
        Label startBtn = new Label("start", start);
        startBtn.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt", ViewManager.getInstance().windowHeightProperty().divide(CELL / 2 * 3)));
        startBtn.setContentDisplay(ContentDisplay.TOP);
        // when pressed: start game
        startBtn.setOnMouseClicked(e -> ViewManager.getInstance().rouletteSpinned(stakes));

        HBox bets = new HBox();
        bets.getChildren().addAll(stakeButtons, new StackPane(getBettingTable()), startBtn);
        bets.spacingProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(25));
        bets.setAlignment(Pos.CENTER);

        root.setCenter(bets);
        return root;
    }

    /**
     * hier wird die Szene von der Wette zum Spiel gesetzt,
     * und zwar in {@code Roulettev2.spin(int einsatz)}, damit eine Pleite abgefangen werden kann,
     * bevor der Szenenwechsel erfolgt
     */
    public static void setGameView() {
        ImageView desk = ViewManager.defaultView(new Image("src/assets/Roulette Desk.png"), 1.8);
        desk.rotateProperty().bind(DESK_ROTATION);

        ImageView ball = ViewManager.defaultView(new Image("src/assets/Roulette Ball.png"), 50);
        ball.rotateProperty().bind(BALL_ROTATION);

        StackPane game = new StackPane(desk, ball);
        root.setCenter(game);
    }

    /**
     * Helper, um die Farbe des Chips mit dem zurzeitigen Einsatz zu bestimmen
     * @return Name der Datei mit dem richtigen Chip
     */
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
        return result + "v2.png";
    }

    /**
     * Erstellt die Tabelle, auf der man seine Wetten abschließen kann
     * @return Tabelle
     */
    private static GridPane getBettingTable() {
        GridPane bettingTable = new GridPane();
        bettingTable.maxWidthProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL).multiply(CELLS_X));
        bettingTable.maxHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(CELL).multiply(CELLS_Y));

        CELL_ABS.bind(ViewManager.getInstance().windowHeightProperty().divide(CELL));

        // row 1 - 5
        Label zero = (Label) createVisibleCell("0", 3, 0, 1, 1, 5, bettingTable).getChildren().getFirst();
        zero.setBackground(new Background(new BackgroundFill(Paint.valueOf("#15b500"), null, null)));
        for(int y = 1; y <= 5; y++) {
            for(int x = 1; x < CELLS_X - 2; x++) {
                if(x % 2 == 0 && y % 2 == 1) {
                    int num = (x / 2 - 1) * 3 + (3 - (y / 2));
                    Label pnl = (Label) createVisibleCell(String.valueOf(num), 3, x, y, 1, 1, bettingTable).getChildren().getFirst();
                    pnl.setBackground(new Background(new BackgroundFill(Paint.valueOf(BLACK.contains(num) ? "#000000" : "#bc0101"), null, null)));
                }
            }
            createUselessCell(CELLS_X - 2, 0, 1, 1, bettingTable);
            if(y % 2 == 1) {
                createVisibleCell("2to1", 5, CELLS_X - 1, y, 1, 1, bettingTable);
            } else {
                createUselessCell(CELLS_X - 1, y, 1, 1, bettingTable);
            }
        }

        // row 0 (now because otherwise the big labels cover the invisible labels)
        createUselessCell(0, 0, 1, 1, bettingTable);
        for(int x = 1; x < CELLS_X - 2; x++) {
            createInvisibleCell(x, 0, 1, 1, bettingTable);
        }
        createUselessCell(CELLS_X - 2, 0, 2, 1, bettingTable);

        // back at row 1 - 5
        for(int y = 1; y <= 5; y++) {
            for(int x = 1; x < CELLS_X - 2; x++) {
                if(!(x % 2 == 0 && y % 2 == 1)) {
                    createInvisibleCell(x, y, 1, 1, bettingTable);
                }
            }
        }

        // row 6
        createUselessCell(0, 6, CELLS_X, 1, bettingTable);

        // row 7
        createUselessCell(0, 7, 2, 1, bettingTable);
        createVisibleCell("1st 12", 3, 2, 7, 7, 1, bettingTable);
        createUselessCell(9, 7, 1, 1, bettingTable);
        createVisibleCell("2nd 12", 3, 10, 7, 7, 1, bettingTable);
        createUselessCell(17, 7, 1, 1, bettingTable);
        createVisibleCell("3rd 12", 3, 18, 7, 7, 1, bettingTable);
        createUselessCell(25, 7, 2, 1, bettingTable);

        // row 8
        createUselessCell(0, 8, CELLS_X, 1, bettingTable);

        // row 9
        createUselessCell(0, 9, 2, 1, bettingTable);
        createVisibleCell("1-18", 3, 2, 9, 3, 1, bettingTable);
        createUselessCell(5, 9, 1, 1, bettingTable);
        createVisibleCell("EVEN", 3, 6, 9, 3, 1, bettingTable);
        createUselessCell(9, 9, 1, 1, bettingTable);
        Label red = (Label) createVisibleCell("", 100, 10, 9, 3, 1, bettingTable).getChildren().getFirst();
        red.setBackground(new Background(new BackgroundFill(Paint.valueOf("#bc0101"), null, null)));
        createUselessCell(13, 9, 1, 1, bettingTable);
        Label black = (Label) createVisibleCell("", 100, 14, 9, 3, 1, bettingTable).getChildren().getFirst();
        black.setBackground(new Background(new BackgroundFill(Paint.valueOf("#000000"), null, null)));
        createUselessCell(17, 9, 1, 1, bettingTable);
        createVisibleCell("ODD", 3, 18, 9, 3, 1, bettingTable);
        createUselessCell(21, 9, 1, 1, bettingTable);
        createVisibleCell("19-36", 3, 22, 9, 3, 1, bettingTable);
        createUselessCell(25, 9, 2, 1, bettingTable);

        // row 10
        createUselessCell(0, 10, CELLS_X, 1, bettingTable);

        bettingTable.setAlignment(Pos.CENTER);
        return bettingTable;
    }

    /**
     * Helper-Methode für eine Zelle, mit der sich nicht interagieren lässt
     * @param x Spalte in Tabelle
     * @param y Zeile in Tabelle
     * @param span_x Spannweite in x-Richtung
     * @param span_y Spannweite in y-Richtung
     * @param parent Tabelle
     * @return Zelle
     */
    private static Pane createUselessCell(int x, int y, int span_x, int span_y, GridPane parent) {
        Pane r = new Pane();
        parent.add(r, x, y, span_x, span_y);
        r.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        return r;
    }

    /**
     * Helper-Methode für eine unsichtbare Zelle, mit der sich interagieren lässt
     * @param x Spalte der Tabelle
     * @param y Zeile der Tabelle
     * @param span_x Spannweite in x-Richtung
     * @param span_y Spannweiter in y-Richtung
     * @param parent Tabelle
     * @return Zelle
     */
    private static Pane createInvisibleCell(int x, int y, int span_x, int span_y, GridPane parent) {
        Pane r = createUselessCell(x, y, span_x, span_y, parent);
        Label lbl = new Label("");
        lbl.setId("invisible-cell");
        lbl.prefWidthProperty().bind(r.widthProperty());
        lbl.prefHeightProperty().bind(r.heightProperty());
        lbl.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY) {
                Image chip = new Image(getStakesChip());
                ImageView chipView = ViewManager.defaultView(chip, CELL);
                lbl.setGraphic(chipView);
                lbl.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else if(e.getButton() == MouseButton.SECONDARY) {
                lbl.setContentDisplay(ContentDisplay.TEXT_ONLY);
                lbl.setGraphic(null);
            }
        });
        r.getChildren().add(lbl);
        return r;
    }

    /**
     * Helper-Methode für eine sichtbare Zelle, mit der sich interagieren lässt
     * @param text Text in der Zelle
     * @param textScale Größe des Textes
     * @param x Spalte der Tabelle
     * @param y Zeile der Tabelle
     * @param span_x Spannweite in x-Richtung
     * @param span_y Spannweite in y-Richtung
     * @param parent Tabelle
     * @return Zelle
     */
    private static Pane createVisibleCell(String text, double textScale, int x, int y, int span_x, int span_y, GridPane parent) {
        Pane r = createInvisibleCell(x, y, span_x, span_y, parent);
        Label lbl = (Label) r.getChildren().getFirst();
        lbl.prefWidthProperty().unbind();
        lbl.prefWidthProperty().bind(r.widthProperty().add(CELL_ABS));
        lbl.prefHeightProperty().unbind();
        lbl.prefHeightProperty().bind(r.heightProperty().add(CELL_ABS));
        lbl.translateXProperty().bind(CELL_ABS.divide(-2));
        lbl.translateYProperty().bind(CELL_ABS.divide(-2));
        lbl.setText(text);
        lbl.setId("betting-table");
        lbl.styleProperty().bind(Bindings.format("-fx-font-size: %.2fpt", ViewManager.getInstance().windowHeightProperty().divide(CELL / 2 * textScale)));
        return r;
    }
}