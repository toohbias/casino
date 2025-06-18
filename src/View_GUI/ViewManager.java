package src.View_GUI;

import javafx.beans.property.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import src.Logic.CasinoController;
import src.Logic.Login;
import src.Logic.SlotMachinev2;

import java.util.Objects;

public class ViewManager {

    private final ObjectProperty<Node> currentNode;

    //Größe des Fensters, die dynamisch angepasst wird, wichtig für die Skalierung
    private final DoubleProperty windowWidth, windowHeight;

    private final Scene defaultScene;

    private final BorderPane fxlayer;

    private static ViewManager instance;

    // View: Szene
    public static final int LOGIN_MENU = 0;
    public static final int MAIN_MENU = 1;
    public static final int SLOT_VIEW = 2;
    public static final int ROULETTE_VIEW = 3;

    // Logic: Szene
    private final SlotMachinev2 slotMachine;

    private CasinoController controller;

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

        fxlayer = new BorderPane();
        fxlayer.setPickOnBounds(false);

        BorderPane defaultPane = new BorderPane();
        defaultPane.setBackground(new Background(new BackgroundImage(new Image("src/assets/Background.png"), null, null, null, null)));
        defaultPane.centerProperty().bind(currentNodeProperty());

        StackPane root = new StackPane(defaultPane, fxlayer);

        defaultScene = new Scene(root);
        defaultScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("View.css")).toExternalForm());

        slotMachine = new SlotMachinev2();
    }

    public BorderPane getFXLayer() {
        return fxlayer;
    }

    /**
     * Setzt den Inhalt des Fensters
     * @param view Inhalt
     */
    public void setView(int view) {
        switch (view) {
            case LOGIN_MENU -> {
                Login.anmeldung.bind(LoginView.anmeldung);
                Login.canSign.bind(LoginView.canSign);
                Login.is18.bind(LoginView.is18);
                Login.username.bind(LoginView.username);
                Login.password.bind(LoginView.password);
                setShowMoney(false);
                setCurrentNode(LoginView.getPane());
            }
            case MAIN_MENU -> setCurrentNode(CasinoView.getPane());
            case SLOT_VIEW -> {
                setCurrentNode(SlotView.getPane());
                // binds the symbols of the view to the rng in SlotMachine
                SlotView.spin1.bind(slotMachine.symbol1);
                SlotView.spin2.bind(slotMachine.symbol2);
                SlotView.spin3.bind(slotMachine.symbol3);
            }
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
        stage.setMaxHeight(1080);
        stage.setMaxWidth(1920);
        stage.show();
        windowWidthProperty().bind(stage.widthProperty());
        windowHeightProperty().bind(stage.heightProperty());
//        setView(LOGIN_MENU); // nur zum testen TODO
        setView(MAIN_MENU);
    }

    // wird von SlotView aufgerufen, wenn der spieler den Hebel zieht
    public void leverPulled(int einsatz, ToggleButton slotArm) {
        try {
            slotMachine.spin(einsatz, slotArm);
        } catch (IllegalAccessException e) {
            e.printStackTrace(); // TODO: das ist der Fehler mit den liquiden Mitteln
        }
    }

    public void sign(String username, String password) {
        Login.login(username, password);
    }

    // Vereinfachung der Erstellung von Bildern
    public static ImageView defaultView(Image img, double scale) {
        ImageView view = new ImageView();
        if (img != null) {
            view.setImage(img);
        }
        view.setPreserveRatio(true);
        view.fitHeightProperty().bind(instance.windowHeightProperty().divide(scale));
        return view;
    }

    public CasinoController getController() {
        return controller;
    }

    public void setController(CasinoController controller) {
        this.controller = controller;
    }

    public void setShowMoney(boolean show) {
        if(show) {
            ((BorderPane) ((StackPane) defaultScene.getRoot()).getChildren().getFirst()).setTop(CasinoView.getMoneyFrame(controller.getMoney()));
        } else {
            ((BorderPane) ((StackPane) defaultScene.getRoot()).getChildren().getFirst()).setTop(null);
        }
    }

    public Scene getDefaultScene() { return defaultScene; }

    public final DoubleProperty windowWidthProperty() { return windowWidth; }
    public final DoubleProperty windowHeightProperty() { return windowHeight; }

    public final Node getCurrentNode() { return currentNode.get(); }
    public void setCurrentNode(Node node) { currentNode.set(node); }
    public ObjectProperty<Node> currentNodeProperty() { return currentNode; }
}