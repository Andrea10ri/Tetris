package it.polimi.tetris.VIEW;

import it.polimi.tetris.CONTROLLER.ClientThread;
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

   // Methods
    /**
     * Connects to the server
     */
    private void Connect(){
        try {
            this.socket = new Socket(serverName, serverPort);
        } catch (IOException e) {
            System.err.println("Impossible to connect to: " + serverName);
            System.exit(0);
        }
    }

    /**
     * Initializes the communication buffers
     */
    private void SetBuffers(){
        try {
            out=new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        } catch (IOException e) {
            System.err.println( "Error during buffers settings ");
            System.exit(0);
        }
    }

    /**
     * Start virtual server and client thread
     */
    public void Start() {
        Connect();
        SetBuffers();

        // Start client thread
        this.clientThread = new ClientThread(this.in,this.stage, this);
        this.clientThread.start();
    }
}
