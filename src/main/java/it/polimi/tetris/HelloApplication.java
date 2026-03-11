package it.polimi.tetris;

import it.polimi.tetris.VIEW.VirtualServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    private static String serverNameArg;
    private static int serverPortArg;
    private VirtualServer virtualServer;


    public HelloApplication() {

        // Start View
        this.virtualServer = new VirtualServer();
        this.virtualServer.setServerName(serverNameArg);
        this.virtualServer.setServerPort(serverPortArg);

    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
