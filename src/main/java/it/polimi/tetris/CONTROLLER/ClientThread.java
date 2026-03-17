package it.polimi.tetris.CONTROLLER;

import com.google.gson.Gson;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.BufferedReader;

public class ClientThread extends Thread {

    private final BufferedReader in;    // Input buffer
    private Gson gson;  // Gson for json communication
    private FXMLLoader fxmlLoader;  // Fxml loader to load the correct page
    private Parent root;    // Root
    private Scene scene;    // Scene
    private final Stage stage;  // Stage
    private final VirtualServer virtualServer;  // Virtual server to communicate with server
   // private Response response;  // Response received from server
   // private Command command;    // Command to send to server
    private String nickname;    // Nickname of the player


    public ClientThread(BufferedReader in, Stage stage, VirtualServer virtualServer) {
        this.in = in;
        this.stage = stage;
        this.virtualServer = virtualServer;
    }
}
