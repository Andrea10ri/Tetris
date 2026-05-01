package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import it.polimi.tetris.MODEL.ENUMS.PlayerColor;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.MODEL.Player;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaitingRoom_Controller extends Controller {

    @FXML private HBox playersContainer;
    @FXML private ComboBox<PlayerColor> cmbColor;
    @FXML private Button btnReady;
    @FXML private Label lblTitle;
    @FXML private Label lblStatus;
    @FXML private Label lblTetromino;
    @FXML private StackPane loadingIcon;
    @FXML private ImageView imgTetronimo;

    private Timeline imageSwitchTimeline;
    private int imageIndex = 0;
    private int maxPlayers;

    //click audio
    private static final AudioClip clickSound = new AudioClip(Game_Controller.class.getResource("/it/polimi/tetris/Sounds/Click.wav").toExternalForm());



    @Override
    public void initialize(Stage stage, VirtualServer virtualServer, String nickname) {
        super.initialize(stage, virtualServer, nickname);

        startTetronimoAnimation();


    }

    public Button getBtnReady() {
        return btnReady;
    }

    public ComboBox<PlayerColor> getCmbColor() {
        return cmbColor;
    }

    public Label getLblStatus() {
        return lblStatus;
    }

    public void updateAvailableColors(Lobby lobby) {

        //tutti i colori possibili
        List<PlayerColor> allColors = List.of(
                PlayerColor.LIGHTBLUE,
                PlayerColor.RED,
                PlayerColor.PURPLE,
                PlayerColor.LIGHTGREEN
        );

        //colori non disponibili
        List<PlayerColor> usedColors = lobby.getPlayers().stream()
                .map(Player::getPlayerColor)
                .filter(Objects::nonNull)
                .toList();

        //colori ancora disponibili
        List<PlayerColor> freeColors = allColors.stream()
                .filter(c -> !usedColors.contains(c))
                .toList();

        // Aggiorna la ComboBox
        cmbColor.getItems().setAll(freeColors);


        if (!freeColors.contains(cmbColor.getValue())) {
            cmbColor.setValue(null);
        }
    }

    /**
     * Inizializza la sala d'attesa con il numero di giocatori della lobby
     * Crea i pannelli per ogni slot giocatore
     */
    public void SetupRoom(Lobby lobby) {
        this.maxPlayers = lobby.getNumOfPlayers();
        playersContainer.getChildren().clear();


        // crea tutti gli slot
        for (int i = 0; i < maxPlayers; i++) {
            VBox playerPanel = CreatePlayerPanel(i + 1);
            HBox.setHgrow(playerPanel, Priority.ALWAYS);
            playersContainer.getChildren().add(playerPanel);
        }

        //popola i pannelli dei giocatori già presenti
        ArrayList<Player> players = lobby.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            UpdatePlayerPanel(i + 1, players.get(i).getNickname(), players.get(i).getPlayerColor());
        }
    }

    /**
     * Crea un pannello per uno slot giocatore
     */
    private VBox CreatePlayerPanel(int slotNumber) {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10; -fx-alignment: CENTER;");
        panel.setMaxWidth(Double.MAX_VALUE);

        Label lblNick = new Label("Waiting...");
        lblNick.setId("lblNick" + slotNumber);
        lblNick.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Ready
        Label lblReady = new Label("");
        lblReady.setId("lblReady" + slotNumber);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Label lblSlot = new Label("Player " + slotNumber);
        lblSlot.setId("lblSlot" + slotNumber);
        lblSlot.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #333;");


        panel.getChildren().addAll(lblNick,  lblReady, spacer, lblSlot);



        panel.setId("panel" + slotNumber);
        return panel;

    }

    /**
     * Aggiorna il pannello di un giocatore quando clicca Ready
     * Chiamato dal ClientThread quando arriva la risposta dal server
     */
    public void UpdatePlayerPanel(int slot, String nickname, PlayerColor color) {
        VBox panel = (VBox) playersContainer.getChildren().get(slot - 1);

        Label lblNick = (Label) panel.lookup("#lblNick" + slot);
        Label lblReady = (Label) panel.lookup("#lblReady" + slot);

        lblNick.setText(nickname);
        lblNick.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #333;");

        if (color != null) {

            // Converti enum → colore HEX
            String bgColor = switch (color) {
                case LIGHTBLUE -> "#52BFF2";
                case RED -> "#FF4136";
                case PURPLE -> "#C64FD6";
                case LIGHTGREEN -> "#43D154";
            };

            //colore del panel
            panel.setStyle(
                    "-fx-background-color: " + bgColor + ";" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: gray;" +
                            "-fx-border-width: 1;" +
                            "-fx-padding: 10;" +
                            "-fx-alignment: CENTER;"
            );


            lblReady.setText("READY");
            lblReady.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }

    }

    /**
     * Quando il giocatore clicca Ready
     */
    @FXML
    public void onReadyClick() {
        clickSound.play();

        PlayerColor selectedColor = cmbColor.getValue();
        if (selectedColor == null) {
            lblStatus.setText("Select a color!");
            return;
        }
        // Disabilita bottone e combobox
        btnReady.setDisable(true);
        cmbColor.setDisable(true);

        //Notifica il server che il giocatore è pronto e il colore è selezionato
        LoginCommand cmd = new LoginCommand("PlayerReady", this.nickname, selectedColor);
        Gson gson = new Gson();
        this.virtualServer.Send(gson.toJson(cmd));
    }

    /**
     * Converte PlayerColor in hex per il CSS
     */
    private String GetColorHex(PlayerColor color) {
        switch (color) {
            case LIGHTBLUE: return "#00bfff";
            case RED: return "#ff4455";
            case PURPLE: return "#aa44ff";
            case LIGHTGREEN: return "#44ff88";
            default: return "white";
        }
    }


    public void startCountdownAndSwitchScene() {

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> lblStatus.setText("--3--")),
                new KeyFrame(Duration.seconds(1), e -> lblStatus.setText("--2--")),
                new KeyFrame(Duration.seconds(2), e -> lblStatus.setText("--1--"))

        );

        timeline.play();
    }

    private void startTetronimoAnimation() {

        //immagine iniziale
        imgTetronimo.setImage(new Image(
                getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_J.png")
        ));

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

         int[] tick = {0};
         int[] imageIndex = {0};

        KeyFrame frame = new KeyFrame(Duration.millis(30), e -> {

            //rotazione
            imgTetronimo.setRotate(imgTetronimo.getRotate() + 5);

            //ogni tot secondi faccio cambiare l'immagine
            tick[0]++;
            if (tick[0] >= 70) {
                tick[0] = 0;
                imageIndex[0]++;

                if (imageIndex[0] > 5)
                    imageIndex[0] = 0;

                switch (imageIndex[0]) {
                    case 0 -> imgTetronimo.setImage(new Image(
                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_Z.png")
                    ));
                    case 1 -> imgTetronimo.setImage(new Image(
                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_T.png")
                    ));
                    case 2 -> imgTetronimo.setImage(new Image(
                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_L.png")
                    ));
                    case 3 -> imgTetronimo.setImage(new Image(
                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_O.png")
                    ));
                    case 4 -> imgTetronimo.setImage(new Image(
                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_S.png")
                    ));
                    case 5 -> imgTetronimo.setImage(new Image(
                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_J.png")
                    ));
//                    case 6 -> imgTetronimo.setImage(new Image(
//                            getClass().getResourceAsStream("/it/polimi/tetris/Support_images/Animation_I.png")
//                    ));
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }



}
