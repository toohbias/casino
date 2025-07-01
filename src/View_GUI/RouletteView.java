package src.View_GUI;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * wrappt die {@code getPane()}-Methode für Roulette
 */
public class RouletteView {


    private static final HashSet<Integer> BLACK = new HashSet<>(Arrays.asList(6, 15, 24, 33, 2, 8, 11, 17, 20, 26, 29, 35, 4, 10, 13, 22, 28, 31));

    /**
     * Roulette-Szene
     * hier drin kann der Spieler Roulette spielen
     * @return Szene
     */
    public static Node getPane() {
        BorderPane root = new BorderPane();
        root.setBottom(createBettingTable());
        return root;
    }

    private static GridPane createBettingTable() {
        GridPane bettingTable = new GridPane();

        bettingTable.add(new Label("0"), 0, 0, 3, 1);
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 12; j++) {
                int num = i + (j + 1) * 3;
                Label field = new Label(String.valueOf(num));
                field.setBackground(new Background(new BackgroundFill(Paint.valueOf(BLACK.contains(num) ? "#000000" : "#FF0000"), null, null)));
                field.setId("betting-table");
                bettingTable.add(field, j, i);
            }
        }

        return  bettingTable;
    }

}
