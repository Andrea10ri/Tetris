package it.polimi.tetris.VIEW;

import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class VirtualServer {

    //Attributes
    private String serverName;  // Server name
    private int serverPort; // Server port
    private BufferedReader in;  // Input buffer
    private PrintWriter out;    // Output buffer
    private Socket socket;  // Socket for communication
    private ClientThread clientThread;  // Thread of client
    private Stage stage;    // Stage

    //Constructor
    public VirtualServer() {
        this.serverName = "";
        this.serverPort = 0;
        this.in=null;
        this.out=null;
        this.socket=null;
        this.clientThread=null;
        this.stage=null;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ClientThread getClientThread() {
        return clientThread;
    }

    public void setClientThread(ClientThread clientThread) {
        this.clientThread = clientThread;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
