package src.View_GUI;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class CasinoView {

    public CasinoView() {

    }

    static public TilePane getPane() {
        TilePane root = new TilePane();

        Image i = new Image("src/assets/Slotmachine.png");
        ImageView img = new ImageView(i);
        img.setPreserveRatio(true);
        img.setFitHeight(500);

        Label lbl = new Label("", img);
        root.getChildren().add(lbl);
        return root;
    }
}

