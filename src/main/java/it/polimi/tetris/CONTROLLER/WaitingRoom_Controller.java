package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import it.polimi.tetris.MODEL.ENUMS.PlayerColor;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.MODEL.Player;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class WaitingRoom_Controller extends Controller {

    @FXML private HBox playersContainer;
    @FXML private ComboBox<PlayerColor> cmbColor;
    @FXML private Button btnReady;
    @FXML private Label lblTitle;
    @FXML private Label lblStatus;

    private int maxPlayers;

    @Override
    public void initialize(Stage stage, VirtualServer virtualServer, String nickname) {
        super.initialize(stage, virtualServer, nickname);
        cmbColor.getItems().addAll(PlayerColor.values());
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

        // popola subito i pannelli dei giocatori già presenti
        ArrayList<Player> players = lobby.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            UpdatePlayerPanel(i + 1, players.get(i).getNickname(), null);
        }
    }

    /**
     * Crea un pannello per uno slot giocatore
     */
    private VBox CreatePlayerPanel(int slotNumber) {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10; -fx-alignment: CENTER;");
        panel.setMaxWidth(Double.MAX_VALUE);

        Label lblSlot = new Label("Player " + slotNumber);
        lblSlot.setId("lblSlot" + slotNumber);

        Label lblNick = new Label("Waiting...");
        lblNick.setId("lblNick" + slotNumber);

        Label lblColor = new Label("");
        lblColor.setId("lblColor" + slotNumber);

        Label lblReady = new Label("");
        lblReady.setId("lblReady" + slotNumber);

        panel.getChildren().addAll(lblSlot, lblNick, lblColor, lblReady);
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
        Label lblColor = (Label) panel.lookup("#lblColor" + slot);
        Label lblReady = (Label) panel.lookup("#lblReady" + slot);

        lblNick.setText(nickname);
        if(color != null) {
        lblColor.setText("Colore: " + color.toString());
        lblColor.setStyle("-fx-text-fill: " + GetColorHex(color));
        lblReady.setText("✓ READY");
        lblReady.setStyle("-fx-text-fill: green;");}
    }

    /**
     * Quando il giocatore clicca Ready
     */
    @FXML
    public void onReadyClick() {
        PlayerColor selectedColor = cmbColor.getValue();
        if (selectedColor == null) {
            lblStatus.setText("Seleziona un colore!");
            return;
        }
        // Disabilita bottone e combobox
        btnReady.setDisable(true);
        cmbColor.setDisable(true);

        // Manda al server
       // LoginCommand cmd = new LoginCommand("PlayerReady", this.nickname, selectedColor);
        //Gson gson = new Gson();
        //this.virtualServer.Send(gson.toJson(cmd));
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
}
