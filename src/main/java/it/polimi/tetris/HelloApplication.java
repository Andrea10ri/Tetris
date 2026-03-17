package it.polimi.tetris;

import it.polimi.tetris.CONTROLLER.ClientHandler;
import it.polimi.tetris.VIEW.VirtualServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloApplication extends Application {


    private static String serverNameArg;
    private static int serverPortArg;
    private VirtualServer virtualServer;


    public HelloApplication() {

        //Start View
        this.virtualServer = new VirtualServer();
        this.virtualServer.setServerName(serverNameArg);
        this.virtualServer.setServerPort(serverPortArg);

    }


    @Override
    public void start(Stage stage) throws IOException {


        if (this.virtualServer == null) {
            this.virtualServer = new VirtualServer();
            this.virtualServer.setServerName(serverNameArg);
            this.virtualServer.setServerPort(serverPortArg);
        }
        this.virtualServer.setStage(stage);

        stage.setTitle("Tetris");



        // Connette al server
        this.virtualServer.Start();


        // Set view
       /* FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/galaxytrucker/FXML/StartView.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);*/

        // Get controller
       /* StartController controller = fxmlLoader.getController();   // Get the controller
        controller.initialize(stage, this.virtualServer);*/

        // Stage settings
       /* stage.setWidth(MAXWIDTH);
        stage.setHeight(MAXHEIGHT);*/

      //  stage.setResizable(false);
        //  stage.setFullScreen(true);
        //Image icon = new Image(getClass().getResourceAsStream("/it/polimi/galaxytrucker/resource/Icon.jpg"));
       // stage.getIcons().add(icon);



        // Set and show scene
       //stage.setScene(scene);
        stage.show();

    }

    private static void startServerLogic(String hostname, int port) {
        System.out.println("Starting server on " + (hostname == null ? "all interfaces" : hostname) + " port " + port + "...");
        Server server = new Server(); // Create a new Server instance here

        try {
            ServerSocket serverSocket;
            if (hostname != null && !hostname.equalsIgnoreCase("localhost") && !hostname.equals("0.0.0.0")) {
                // Bind to the specific hostname/IP provided
                serverSocket = new ServerSocket(port, 50, InetAddress.getByName(hostname));
            } else {
                serverSocket = new ServerSocket(port); // Binds to 0.0.0.0 (all available interfaces)
            }
            System.out.println("Server Started!");
            System.out.println("Listening on port " + port);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, server);
                Thread t = new Thread(clientHandler);
                t.start();
                server.AddClient(clientHandler);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "--server":
                    String hostname = "localhost"; // Default hostname for server
                    int serverPort = 12345; // Default server port
                    if (args.length > 1) {
                        // Try to parse as hostname or port
                        try {
                            serverPort = Integer.parseInt(args[1]);
                            // If args[1] was a port, hostname remains default "localhost"
                        } catch (NumberFormatException e) {
                            // If args[1] is not a number, assume it's a hostname
                            hostname = args[1];
                            if (args.length > 2) {
                                try {
                                    serverPort = Integer.parseInt(args[2]);
                                } catch (NumberFormatException ex) {
                                    System.err.println("Invalid port number: " + args[2]);
                                    printUsage();
                                    return;
                                }
                            }
                        }
                    }
                    startServerLogic(hostname, serverPort);
                    break;
                case "--gui":
                    if (args.length > 1) {
                        serverNameArg = args[1]; // ServerIPAddress
                        serverPortArg = 12345; // Default client connection port
                        if (args.length > 2) {
                            try {
                                serverPortArg = Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid port number for GUI client: " + args[2]);
                                printUsage();
                                return;
                            }
                        }
                        launch(args); // Launch JavaFX application
                    } else {
                        System.err.println("Missing ServerIPAddress for --gui mode.");
                        printUsage();
                    }
                    break;
                default:
                    System.err.println("Unknown option: " + args[0]);
                    printUsage();
            }
        } else {
            // Default behavior: launch GUI, try to connect to localhost:12345
            // This is if you just run `java -jar programName.jar` with no arguments
            System.out.println("No arguments provided. Defaulting to GUI client mode, attempting to connect to localhost:12345");
            serverNameArg = "localhost";
            serverPortArg = 12345;
            launch(args);
        }
    }
    // Show command line instructions
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java -jar your-app.jar --server [hostname/ip] [port]");
        System.out.println("    (hostname/ip defaults to localhost, port defaults to 12345)");
        System.out.println("  java -jar your-app.jar --gui <ServerIPAddress> [port]");
        System.out.println("    (port defaults to 12345)");
        System.out.println("  java -jar your-app.jar (defaults to GUI client connecting to localhost:12345)");
    }


}
