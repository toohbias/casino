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

/**
 * MVC -> Controller
 * Hier findet die Kommunikation zwischen UI und Logik statt
 */
public class ViewManager {

    private final ObjectProperty<Node> currentNode;

    //Größe des Fensters, die dynamisch angepasst wird, wichtig für die Skalierung
    private final DoubleProperty windowWidth, windowHeight;

    private final Scene defaultScene;

    private final BorderPane fxlayer;

    private final BorderPane msglayer;

    private final BorderPane decolayer;

    private final StackPane topBar;

    private static ViewManager instance;

    // View: Szene
    public static final int LOGIN_MENU = 0;
    public static final int MAIN_MENU = 1;
    public static final int SLOT_VIEW = 2;
    public static final int ROULETTE_VIEW = 3;
    public static final int SHOP_VIEW = 4;
    public static final int BLACKJACK_VIEW = 5;
    // Logic: Szene
    private final SlotMachinev2 slotMachine;

    private final CasinoController controller;

    /**
     * mit dem Singleton ist z.B. so etwas möglich:
     * button.setOnAction(e → ViewManager.getInstance().setCurrentNode(CasinoView.getPane()));
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

        MusicManager.playBackgroundMusic("src/assets/music/backgroundfinal.wav", +6f);

        currentNode = new SimpleObjectProperty<>();

        fxlayer = new BorderPane();
        fxlayer.setPickOnBounds(false);

        msglayer = new BorderPane();
        msglayer.setId("text-pane");
        msglayer.setVisible(false);

        decolayer = new BorderPane();
        decolayer.setBackground(new Background(new BackgroundImage(new Image("src/assets/Background.png"), null, null, null, null)));

        topBar = new StackPane();

        BorderPane defaultPane = new BorderPane();
        defaultPane.centerProperty().bind(currentNodeProperty());
        defaultPane.setTop(topBar);

        StackPane root = new StackPane(decolayer, defaultPane, msglayer, fxlayer);

        defaultScene = new Scene(root);
        defaultScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("View.css")).toExternalForm());

        slotMachine = new SlotMachinev2();

        controller = new CasinoController();
    }

    /**
     * gibt die Ebene, auf dem die Effekte angezeigt werden, z.B. der {@code MoneyEffect}
     * @return FX Layer
     */
    public BorderPane getFXLayer() { return fxlayer; }

    /**
     * gibt die Ebene, auf der Nachrichten und Fehlermeldungen angezeigt werden
     * @return Message Layer
     */
    public BorderPane getMsgLayer() { return msglayer; }

    /**
     * gibt die Ebene, auf der ggf. Dekorationen angezeigt werden
     * @return Deko Layer
     */
    public BorderPane getDecoLayer() { return decolayer; }

    /**
     * zeigt eine Information an
     * @param text info
     */
    public void displayInfoMessage(String text) { TextLayer.displayMessage(text, "info-pane"); }

    /**
     * zeigt einen Fehler an
     * @param text error
     */
    public void displayErrorMessage(String text) { TextLayer.displayMessage(text, "error-pane"); }

    /**
     * Setzt den Inhalt des Fensters
     * @param view Inhalt
     */
    public void setView(int view) {
        MoneyFrame.stopStakesAnimation(); // no stakes selection cross scenes plz
        decolayer.setCenter(null);

        switch (view) {
            case LOGIN_MENU -> {
                setCurrentNode(LoginView.getPane());
                // binds the properties to check to the backend
                Login.anmeldung.bind(LoginView.anmeldung);
                Login.canSign.bind(LoginView.canSign);
                Login.is18.bind(LoginView.is18);
                Login.username.bind(LoginView.username);
                Login.password.bind(LoginView.password);

                setShowBack(false);
                setShowMoney(false);
                setShowShop(false);
            }
            case MAIN_MENU -> {
                setCurrentNode(CasinoView.getPane());

                setShowBack(false);
                setShowMoney(true);
                setShowShop(true);
            }
            case SLOT_VIEW -> {
                setCurrentNode(SlotView.getPane());
                // binds the symbols of the view to the rng in SlotMachine
                SlotView.spin1.bind(slotMachine.symbol1);
                SlotView.spin2.bind(slotMachine.symbol2);
                SlotView.spin3.bind(slotMachine.symbol3);

                setShowBack(true);
                setShowMoney(true);
                setShowShop(true);
            }
            case ROULETTE_VIEW ->{
                setCurrentNode(RouletteView.getPane());

                setShowBack(true);
                setShowMoney(true);
                setShowShop(true);
            }
            case SHOP_VIEW -> {
                setCurrentNode(ShopView.getPane());

                setShowBack(true);
                setShowMoney(true);
                setShowShop(false);
            }
            case BLACKJACK_VIEW -> {
            setCurrentNode(BlackjackView.getPane());

                setShowBack(true);
                setShowMoney(true);
                setShowShop(true);
            }
        }
    }

    /**
     * initialisiert die Stage (Fenster)
     * wird in Main aufgerufen
     * @param stage aus Main.start(Stage)
     */
    @SuppressWarnings("ParameterCanBeLocal")
    public void setStage(Stage stage) {
        // init window
        stage = new Stage();
        stage.setTitle("Casino");
        stage.getIcons().add(new Image("src/assets/Sevensign.png"));
        stage.setScene(getDefaultScene());
        stage.setMaximized(true);
        stage.setMinHeight(500);
        stage.setMinWidth(750);
        stage.setMaxHeight(1080);
        stage.setMaxWidth(1920);
        stage.show();

        // init size properties
        windowWidthProperty().bind(stage.widthProperty());
        windowHeightProperty().bind(stage.heightProperty());

        // init top bar with its components
        BorderPane top = new BorderPane();
        top.setLeft(CasinoView.getBackButton());
        top.setRight(CasinoView.getShopButton());
        topBar.getChildren().addAll(top, MoneyFrame.init(controller.getMoney()));

        setView(LOGIN_MENU);
    }

    /**
     * wird von SlotView aufgerufen, wenn der spieler den Hebel zieht
     * und reicht einfach die Methode aus SlotMachine {@code slotMachine.spin(int einsatz, ToggleButton slotArm)} durch
     * die nötigen Bindings wurden schon in {@code setView(int view)} gesetzt
     * <p>
     * der {@code ToggleButton} ist notwendig, damit der Knopf am Anfang der animation gesperrt
     * und am Ende wieder entsperrt werden kann, da dies vom Timing der Threads abhängt
     */
    public void leverPulled(int einsatz, ToggleButton slotArm) {
            MoneyFrame.stopStakesAnimation();
            slotMachine.spin(einsatz, slotArm);
    }

    /**
     * wird von allen Spielen (z.B. SlotView) aufgerufen, um den Einsatz anzupassen.
     * @param raiseOrReduce 1 oder -1, um Einsatz zu erhöhen bzw. erniedrigen, 0 um zu bestätigen
     * @param current jetziger Einsatz
     * @return neuer Einsatz
     */
    public int setStake(int raiseOrReduce, int current) {
        if(raiseOrReduce == 0) {
            MoneyFrame.stopStakesAnimation();
            return current;
        }
        return CasinoController.setStakes(raiseOrReduce, current);
    }

    /**
     * wird von LoginView aufgerufen, wenn der spieler entweder den anmelden oder registrieren button klickt
     * und reicht einfach die Methode aus Login {@code Login.login(String username, String password)} durch
     * die nötigen Bindings wurden schon in {@code setView(int view)} gesetzt
     * @param username username
     * @param password passwort
     */
    public void sign(String username, String password) {
        Login.login(username, password);
    }

    /**
     * Vereinfachte Erstellung von skalierbaren ImageViews
     * @param img Image
     * @param scale Skalierfaktor in Abhängigkeit von der Höhe des Fensters
     * @return ImageView
     */
    public static ImageView defaultView(Image img, double scale) {
        ImageView view = new ImageView();
        if (img != null) {
            view.setImage(img);
        }
        view.setPreserveRatio(true);
        view.fitHeightProperty().bind(instance.windowHeightProperty().divide(scale));
        return view;
    }

    /**
     * gibt die CasinoController-Instanz zurück
     * @return CasinoController
     */
    public CasinoController getController() {
        return controller;
    }

    /**
     * soll das Money Frame sichtbar oder unsichtbar sein?
     * @param show Sichtbarkeit
     */
    public void setShowMoney(boolean show) {
        topBar.getChildren().get(1).setVisible(show);
    }

    /**
     * soll der Shop-Button sichtbar oder unsichtbar sein?
     * @param show Sichtbarkeit
     */
    public void setShowShop(boolean show) {
        ((BorderPane) (topBar.getChildren().getFirst())).getRight().setVisible(show);
    }

    /**
     * soll der Zurück-Button sichtbar oder unsichtbar sein?
     * @param show Sichtbarkeit
     */
    public void setShowBack(boolean show) {
        ((BorderPane) (topBar.getChildren().getFirst())).getLeft().setVisible(show);
    }

    public Scene getDefaultScene() { return defaultScene; }

    /**
     * gibt die aktuelle Breite des Fensters zurück
     * @return DoubleProperty für Fensterbreite
     */
    public final DoubleProperty windowWidthProperty() { return windowWidth; }

    /**
     * gibt die aktuelle Höhe des Fensters zurück
     * @return DoubleProperty für Fensterhöhe
     */
    public final DoubleProperty windowHeightProperty() { return windowHeight; }

    public final Node getCurrentNode() { return currentNode.get(); }

    /**
     * ist zum Main-Fenster der Stage gebindet
     * und soll nur von {@code setView(int view)} genutzt werden
     * @param node aktuelle Szene
     */
    public void setCurrentNode(Node node) { currentNode.set(node); }

    public ObjectProperty<Node> currentNodeProperty() { return currentNode; }
}