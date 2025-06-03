package src.View_GUI;

import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import src.Logic.SlotMachine;
import src.Logic.SlotMaschineObserver;

public class SlotView implements SlotMaschineObserver {

    private Button spinButton;
    private SlotMachine logic;
    private IntegerProperty spin1, spin2, spin3;
    private static Label[] signs;
    private static final double SIGN_SIZE = 12.5;

    public SlotView(SlotMachine logic) {
        this.logic = logic;
        logic.addObserver(this);

        spinButton = new Button("Spin");
        spinButton.setOnAction((event -> {
            try {
                logic.spin(1);
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    private static void initSigns() {
        final String[] signs = {"Bananas.png", "Bell.png", "Cherries.png", "Grapes.png", "Sevensign.png"};
        SlotView.signs = new Label[signs.length];
        ImageView img;
        for(int i = 0; i < signs.length; i++) {
            img = new ImageView(new Image("src/assets/" + signs[i]));
            img.setPreserveRatio(true);
            img.fitHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(SIGN_SIZE));
            SlotView.signs[i] = new Label("", img);
        }
    }

    public static Node getPane() {
        BorderPane root = new BorderPane();
        if(signs == null) {
            initSigns();
        }

        ImageView img = new ImageView(new Image("src/assets/Slotmachine.png"));
        img.setPreserveRatio(true);
        img.fitHeightProperty().bind(ViewManager.getInstance().windowHeightProperty().divide(1.25));
        Label slotMachine = new Label("", img);

        HBox signBox = new HBox();
        signBox.getChildren().addAll(signs[0], signs[1], signs[2]);
        signBox.setAlignment(Pos.CENTER);
        signBox.spacingProperty().bind(ViewManager.getInstance().windowWidthProperty().divide(30));
        root.setBottom(new BorderPane(new StackPane(slotMachine, signBox)));

        return root;
    }

    //TODO
    @Override
    public void updateSpielErgebnis(int symbol1, int symbol2, int symbol3, double gewinn) {

    }

    //TODO
    @Override
    public void fehler(String fehlermeldung) {

    }
}
