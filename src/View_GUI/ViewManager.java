package src.View_GUI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;

public class ViewManager {

    private final ObjectProperty<Node> currentNode;

    private final Scene defaultScene;

    private static ViewManager instance;

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
        currentNode = new SimpleObjectProperty<>();
        setCurrentNode(CasinoView.getPane());

        BorderPane defaultPane = new BorderPane();
        defaultPane.setBackground(new Background(new BackgroundImage(new Image("src/assets/Background.png"), null, null, null, null)));
        defaultPane.centerProperty().bind(currentNodeProperty());

        defaultScene = new Scene(defaultPane);
    }

    public Scene getDefaultScene() { return defaultScene; }

    public final Node getCurrentNode() { return currentNode.get(); }
    public void setCurrentNode(Node node) { currentNode.set(node); }
    public ObjectProperty<Node> currentNodeProperty() { return currentNode; }
}
/*
 * Wechselt zwischen den Szenen
 * erstellt die Szenen
 * Kontroller
 */
//C:\Users\thoma\.m2\repository\org\openjfx