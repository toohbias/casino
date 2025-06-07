package src.View_GUI;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.Logic.SlotMachine;

import java.util.Objects;

public class ViewManager {

    private final ObjectProperty<Node> currentNode;

    //Größe des Fensters, die dynamisch angepasst wird, wichtig für die Skalierung
    private final DoubleProperty windowWidth, windowHeight;

    private final Scene defaultScene;

    private static ViewManager instance;

    public static final int MAIN_MENU = 0;
    public static final int SLOT_VIEW = 1;
    public static final int ROULETTE_VIEW = 2;

    /**
     * mit dem Singleton ist z.B. so etwas möglich:
     * button.setOnAction(e -> ViewManager.getInstance().setCurrentNode(CasinoView.getPane()));
     * @return ViewManager-Instanz
     */
    public static ViewManager getInstance() {
        if(instance == null) {
            instance = new ViewManager();
        }
        return instance;
    }

    private ViewManager() {
        windowWidth = new SimpleDoubleProperty();
        windowHeight = new SimpleDoubleProperty();

        currentNode = new SimpleObjectProperty<>();

        BorderPane defaultPane = new BorderPane();
        defaultPane.setBackground(new Background(new BackgroundImage(new Image("src/assets/Background.png"), null, null, null, null)));
        defaultPane.centerProperty().bind(currentNodeProperty());

        defaultScene = new Scene(defaultPane);
        defaultScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("View.css")).toExternalForm());
    }

    /**
     * Setzt den Inhalt des Fensters
     * @param view Inhalt
     */
    public void setView(int view) {
        switch (view) {
            case MAIN_MENU -> setCurrentNode(CasinoView.getPane());
            case SLOT_VIEW -> setCurrentNode(SlotView.getPane());
            case ROULETTE_VIEW -> setCurrentNode(RouletteView.getPane());
        }
    }

    /**
     * initialisiert die Stage (Fenster)
     * @param stage aus Main.start(Stage)
     */
    public void setStage(Stage stage) {
        stage = new Stage();
        stage.setTitle("Casino");
        stage.getIcons().add(new Image("src/assets/Sevensign.png"));
        stage.setScene(getDefaultScene());
        stage.setMaximized(true);
        stage.setMinHeight(500);
        stage.setMinWidth(700);
        stage.show();
        windowWidthProperty().bind(stage.widthProperty());
        windowHeightProperty().bind(stage.heightProperty());
        setCurrentNode(CasinoView.getPane());
    }

    // wird von SlotView aufgerufen, wenn der spieler den Hebel zieht
    public void leverPulled(int einsatz) {
        try {
            new SlotMachine().spin(einsatz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Scene getDefaultScene() { return defaultScene; }

    public final DoubleProperty windowWidthProperty() { return windowWidth; }
    public final DoubleProperty windowHeightProperty() { return windowHeight; }

    public final Node getCurrentNode() { return currentNode.get(); }
    public void setCurrentNode(Node node) { currentNode.set(node); }
    public ObjectProperty<Node> currentNodeProperty() { return currentNode; }
}