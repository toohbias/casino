package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane root;
        //CasinoController casinoController = new CasinoController();
        try {
            FileInputStream is = new FileInputStream("Las_Vegas_slot_machines.jpg");
            Image image = new Image(is);
            ImageView imageView = new ImageView(image);
            root = new BorderPane(imageView);

            //Label balance = new Label();
            //balance.textProperty().bind(casinoController.getMoney().asString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root, 500, 500);
        stage.setScene(scene);
        stage.setTitle("Test");
        stage.setMaximized(true);
        stage.show();
    }
}
