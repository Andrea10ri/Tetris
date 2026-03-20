package it.polimi.tetris.CONTROLLER;

import it.polimi.tetris.VIEW.VirtualServer;
import javafx.stage.Stage;

public class Controller {
    // Fields
    protected VirtualServer virtualServer;  // Virtual server to communicate with server
    protected Stage stage;  // Stage of the application
    protected String nickname;  // Nickname of the client

    // Methods
    /**
     * Method that initialize the data of the controller
     * @param stage Stage to display the pages
     * @param virtualServer Virtual server to send command
     * @param nickname Nickname of the player
     */
    public void initialize(Stage stage, VirtualServer virtualServer, String nickname) {
        this.virtualServer = virtualServer;
        this.stage = stage;
        this.nickname = nickname;
    }

    /**
     * Method that initialize the data of the controller
     * @param stage Stage to display the pages
     * @param virtualServer Virtual server to send command
     */
    public void initialize(Stage stage, VirtualServer virtualServer) {
        this.virtualServer = virtualServer;
        this.stage = stage;
        this.nickname = "";
    }
}
