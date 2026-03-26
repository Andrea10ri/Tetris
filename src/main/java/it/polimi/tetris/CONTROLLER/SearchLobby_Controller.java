package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import it.polimi.tetris.MODEL.ENUMS.DurationTime;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SearchLobby_Controller extends Controller{

    @FXML
    Button btnJoinLobby;
    @FXML
    Button btnCreateLobby;
    @FXML
    ComboBox<Integer> cmbDuration;
    @FXML
    ComboBox<Integer> cmbNumOfPlayers;
    @FXML
    Label lblWelcome;
    @FXML
    ListView<Lobby> lstLobbies;
    @FXML
    Button btnRefresh;

    public Button getBtnJoinLobby() {
        return btnJoinLobby;
    }

    public void setBtnJoinLobby(Button btnJoinLobby) {
        this.btnJoinLobby = btnJoinLobby;
    }

    public Button getBtnCreateLobby() {
        return btnCreateLobby;
    }

    public void setBtnCreateLobby(Button btnCreateLobby) {
        this.btnCreateLobby = btnCreateLobby;
    }

    public ComboBox<Integer> getCmbNumOfPlayers() {
        return cmbNumOfPlayers;
    }

    public void setCmbNumOfPlayers(ComboBox<Integer> cmbNumOfPlayers) {
        this.cmbNumOfPlayers = cmbNumOfPlayers;
    }

    public Label getLblWelcome() {
        return lblWelcome;
    }

    public void setLblWelcome(Label lblWelcome) {
        this.lblWelcome = lblWelcome;
    }

    public ListView<Lobby> getLstLobbies() {
        return lstLobbies;
    }

    public void setLstLobbies(ListView<Lobby> lstLobbies) {
        this.lstLobbies = lstLobbies;
    }

    @Override
    public void initialize(Stage stage, VirtualServer virtualServer, String nickname) {
        super.initialize(stage, virtualServer, nickname);

        lblWelcome.setText("Welcome " + nickname + "!");
        cmbNumOfPlayers.getItems().addAll(2, 3, 4);
        cmbDuration.getItems().addAll(5, 10, 30);

        //sending the request for updated lobbie list
        LoginCommand cmd = new LoginCommand("GetLobbies", nickname);
        String json = new Gson().toJson(cmd);
        virtualServer.Send(json);

        //populating the listview
        lstLobbies.setCellFactory(list -> new ListCell<>() {

            private final VBox box = new VBox();
            private final Label host = new Label();
            private final Label duration = new Label();
            private final Label players = new Label();

            {
                // Stile uniforme
                host.setStyle("-fx-font-size: 13px; -fx-text-fill: #222;");
                duration.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
                players.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

                // Spaziatura e padding
                box.setSpacing(3);
                box.setPadding(new Insets(6));

                // Sfondo leggero e bordino
                box.setStyle(
                        "-fx-background-color: #f4f4f4;" +
                                "-fx-background-radius: 6;" +
                                "-fx-border-radius: 6;" +
                                "-fx-border-color: #ddd;"
                );

                box.getChildren().addAll(host, duration, players);
            }

            @Override
            protected void updateItem(Lobby lobby, boolean empty) {
                super.updateItem(lobby, empty);

                if (empty || lobby == null) {
                    setGraphic(null);
                } else {

                    // SWITCH sulla durata
                    String durationText;
                    switch (lobby.getDurationTime()) {
                        case FIVE_MINUTES -> durationText = "5 mins";
                        case TEN_MINUTES -> durationText = "10 mins";
                        case THIRTY_MINUTES -> durationText = "30 mins";
                        default -> durationText = lobby.getDurationTime().toString();
                    }

                    host.setText(lobby.getHost() + "'s lobby");
                    duration.setText("Duration: " + durationText);
                    players.setText("Players: " + lobby.getNumOfPlayers());

                    setGraphic(box);
                }
            }
        });




    }

    @FXML
    public void BtnOnRefreshClick()
    {
        // Send command to server
        LoginCommand cmd=new LoginCommand("GetLobbies", this.nickname);
        Gson gson=new Gson();
        String json=gson.toJson(cmd);
        this.virtualServer.Send(json);

    }

    @FXML
    public void OnBtnCreateLobbyClick() {

        //le fasi che devo fare sono
        //1 prendo i dati dalle combobox, invio i dati al server
        //2 il server ci crea una lobby e la aggiunge alla lista
        //3 metto che all'inizio del caricamento di questa view la lista viene popolata delle lobbies

        DurationTime duration;
        int numOfPlayers=cmbNumOfPlayers.getValue();

        switch( cmbDuration.getValue() ) {
            case 5:
                duration = DurationTime.FIVE_MINUTES;
                break;

            case 10:
                duration = DurationTime.TEN_MINUTES;
                break;

            case 30:
                duration = DurationTime.THIRTY_MINUTES;
                break;

            default:
                duration = null;
                break;

        }


        // Send command to server
        LoginCommand cmd=new LoginCommand("Create_Lobby", this.nickname, numOfPlayers, duration);
        Gson gson=new Gson();
        String json=gson.toJson(cmd);
        this.virtualServer.Send(json); 



    }


    @FXML
    public void OnBtnJoinClick()
    {
        Lobby selectedLobby = lstLobbies.getSelectionModel().getSelectedItem();


        // Send command to server
        LoginCommand cmd=new LoginCommand("Join_Lobby", this.nickname, selectedLobby);
        Gson gson=new Gson();
        String json=gson.toJson(cmd);
        this.virtualServer.Send(json);


    }

    }
