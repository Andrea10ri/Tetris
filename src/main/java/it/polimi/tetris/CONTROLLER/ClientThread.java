package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.Command;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginCommand;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.LoginResponse;
import it.polimi.tetris.CONTROLLER.CommandsAndResponses.Response;
import it.polimi.tetris.MODEL.Lobby;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.IOException;

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

        // Print the message
        System.out.println(loginResponse.getMessage());

        switch (loginResponse.getMessage()) {

            case "LOGIN OK":
                this.nickname = loginResponse.getNickname();
                ChangeScene("SearchLobby_View", loginResponse, this.nickname);
                break;

            case "LOBBY LIST":
                Platform.runLater(() -> {
                    SearchLobby_Controller controller = fxmlLoader.getController();
                    controller.getLstLobbies().getItems().setAll(loginResponse.getLobbies());
                });
                break;


            case "LOBBY CREATED":
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
            // New page
            this.fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/tetris/FXML/" + view + ".fxml"));
            // Load the page
            try {
                this.root = this.fxmlLoader.load();
            } catch (IOException e) {
                System.out.println("Impossible to load ");
            }
            // Set scene
            this.scene = new Scene(this.root);
            this.stage.setScene(this.scene);
            // Initialize the controller
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
        }
    }
}
