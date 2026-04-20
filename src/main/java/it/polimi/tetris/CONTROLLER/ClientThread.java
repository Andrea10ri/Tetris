package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.*;
import it.polimi.tetris.MODEL.ENUMS.LobbyStatus;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ClientThread extends Thread {

    private final BufferedReader in;    // Input buffer
    private Gson gson;  // Gson for json communication
    private FXMLLoader fxmlLoader;  //Fxml loader to load the correct view page
    private Parent root;    //Root
    private Scene scene;    //Scene
    private final Stage stage;  //Stage
    private final VirtualServer virtualServer;  // Virtual server to communicate with server
    private Response response;  //Response received from server
    private Command command;    //Command to send to server
    private String nickname;    //Nickname of the player



    public ClientThread(BufferedReader in, Stage stage, VirtualServer virtualServer) {
        this.in = in;
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
        this.fxmlLoader = new FXMLLoader();
        this.stage = stage;
        this.virtualServer = virtualServer;
        this.nickname = "";
    }



    // Methods
    /**
     * Client job
     */
    @Override
    public void run() {
        // Start thread
        System.out.println("Client thread started");
        // Login phase
        LoginLoop();
        //Game phase
        GameLoop();



    }

    /**
     * Manage the login phase
     */
    public void LoginLoop() {

        response = new LoginResponse();

        //string from server
        String answer = "";

        command = new LoginCommand();

        while (!this.virtualServer.getSocket().isClosed() && !response.getMessage().equals("FINISH LOGIN")) {
            try {
                answer = in.readLine();

            }
            catch (IOException e) {

                try {
                    this.virtualServer.getSocket().close();
                    System.out.println("Server closed");
                    System.exit(0);
                }
                catch (IOException e1) {

                    System.out.println("Try to close socket");

                }
            }

            response = gson.fromJson(answer, LoginResponse.class);
            LoginResponseProcess((LoginResponse) response);
        }

    }

    /**
     * Process the response from server
     * @param loginResponse Response to process
     */
    public void LoginResponseProcess(LoginResponse loginResponse) {

        //print the message
        System.out.println(loginResponse.getMessage());

        switch (loginResponse.getMessage()) {

            case "LOGIN OK":
                this.nickname = loginResponse.getNickname();
                ChangeScene("SearchLobby_View", loginResponse, this.nickname);
                break;

            case "LOBBY LIST":
                //aggiorno la listview mettenod solo le lobby non piene
                Platform.runLater(() -> {
                    SearchLobby_Controller controller = fxmlLoader.getController();

                    List<Lobby> waitingLobbies = loginResponse.getLobbies().stream()
                            .filter(l -> l.getStatus() == LobbyStatus.WAITING)
                            .toList();


                    controller.getLstLobbies().getItems().setAll(waitingLobbies);
                });
                break;

            case "FULL LOBBY":
                Platform.runLater(() -> {
                    SearchLobby_Controller controller = (SearchLobby_Controller) fxmlLoader.getController();
                    controller.getLblError().setVisible(true);
                });
                break;


            case "LOBBY CREATED":
                ChangeScene("WaitingRoom_View", loginResponse, this.nickname);
                break;

            case "LOBBY JOINED":
                //devo però ricaricare la scena anche a tutti gli altri giocatori della lobby
                ChangeScene("WaitingRoom_View", loginResponse, this.nickname);
                break;


            case "LOBBY UPDATED":
                Platform.runLater(() -> {
                    WaitingRoom_Controller controller = (WaitingRoom_Controller) fxmlLoader.getController();
                    controller.SetupRoom(loginResponse.getSelectedLobby());
                });
                break;

            case "COLOR OK":
                Platform.runLater(() -> {
                    WaitingRoom_Controller controller = (WaitingRoom_Controller) fxmlLoader.getController();
                    controller.SetupRoom(loginResponse.getSelectedLobby());
                    controller.updateAvailableColors(loginResponse.getSelectedLobby());

                });
                break;

            case "COLOR NOT OK":
                Platform.runLater(() -> {
                    WaitingRoom_Controller controller = (WaitingRoom_Controller) fxmlLoader.getController();
                    controller.getBtnReady().setDisable(false);
                    controller.getCmbColor().setDisable(false);
                    controller.SetupRoom(loginResponse.getSelectedLobby());
                    controller.updateAvailableColors(loginResponse.getSelectedLobby());

                });
                break;

            case "LAST PLAYER ENTERED":
                Platform.runLater(() -> {
                    WaitingRoom_Controller controller = fxmlLoader.getController();
                    controller.startCountdownAndSwitchScene();

                });

                LoginCommand cmd=new LoginCommand("FinishLogin", this.nickname);
                Gson gson=new Gson();
                String json=gson.toJson(cmd);
                this.virtualServer.Send(json);

                break;

            case "FINISH LOGIN":
                ChangeScene("Game_View", loginResponse, this.nickname);
                break;

        }
    }

    public void GameLoop() {

        response = new GameResponse();

        //string from server
        String answer = "";

        command = new GameCommand();

        while (!this.virtualServer.getSocket().isClosed() && !response.getMessage().equals("FINISH GAME")) {
            try {
                answer = in.readLine();

            }
            catch (IOException e) {

                try {
                    this.virtualServer.getSocket().close();
                    System.out.println("Server closed");
                    System.exit(0);
                }
                catch (IOException e1) {

                    System.out.println("Try to close socket");

                }
            }

            response = gson.fromJson(answer, GameResponse.class);
            GameResponseProcess((GameResponse) response);
        }


    }

    public void GameResponseProcess(GameResponse gameResponse) {
        System.out.println(gameResponse.getMessage());

        switch (gameResponse.getMessage()) {

            case "SET GAME":
                Platform.runLater(() -> {
                    Game_Controller gameController = (Game_Controller) fxmlLoader.getController();
                    gameController.SetGame(
                            gameResponse.getRemainingTime(),
                            gameResponse.getPhase(),
                            gameResponse.getEnemiesNicknames(),
                            gameResponse.getPlayerColor()
                    );
                    gameController.SetupKeyboardInput();
                });
                break;

            case "TICK_UPDATE":
                Platform.runLater(() -> {
                    Game_Controller gameController = (Game_Controller) fxmlLoader.getController();
                    gameController.RenderGameState(gameResponse);
                });
                break;

            case "FINISH GAME":

                GameCommand cmd=new GameCommand("FinishGame", this.nickname);
                Gson gson=new Gson();
                String json=gson.toJson(cmd);
                this.virtualServer.Send(json);

                break;

        }
    }
    /**
     * Method that change the scene
     *
     * @param view Page to show
     */
    public void ChangeScene(String view, Response response, String nickname) {

        Platform.runLater(() -> {
            //new page
            this.fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/tetris/FXML/" + view + ".fxml"));
            // Load the page
            try {
                this.root = this.fxmlLoader.load();
            } catch (IOException e) {
                System.out.println("Impossible to load ");
            }
            //set scene
            this.scene = new Scene(this.root);
            this.stage.setScene(this.scene);
            //Initialize the controller
            Initialize(view, response, nickname);
            this.stage.show();
        });
    }

    /**
     * Set the correct settings for the correct view of the page
     *
     * @param view     Page to view
     * @param response Response to received from server
     */
    public void Initialize(String view, Response response, String nickname) {

        switch (view) {

            case "SearchLobby_View":
                SearchLobby_Controller searchLobby_Controller = fxmlLoader.getController();
                searchLobby_Controller.initialize(stage, virtualServer, nickname);
                break;

            case "WaitingRoom_View":
                WaitingRoom_Controller waitingRoomController = fxmlLoader.getController();
                waitingRoomController.initialize(stage, virtualServer, nickname);

                //passo la lobby selezionata per caricare
                Lobby lobby = ((LoginResponse) response).getSelectedLobby();
                waitingRoomController.SetupRoom(lobby);
                waitingRoomController.updateAvailableColors(lobby);

                break;

            case "Game_View":
               Game_Controller gameController = fxmlLoader.getController();
                gameController.initialize(stage, virtualServer, nickname);

                break;
        }
    }
}
