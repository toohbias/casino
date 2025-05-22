package src.View_GUI;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.Logic.CasinoController;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CasionView extends Scene {
    public CasionView() {
        super(getPane(), 500, 500);
    }

    static private BorderPane getPane() {
        BorderPane root = null;
        try {
            FileInputStream fis = new FileInputStream("Las_Vegas_slot_machines.jpg");
            Image image = new Image(fis);
            ImageView imageView = new ImageView(image);
            root = new BorderPane(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return root;
    }
}

