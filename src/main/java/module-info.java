module org.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires com.google.gson;

    requires javafx.media;

    opens it.polimi.tetris to javafx.fxml;
    opens it.polimi.tetris.CONTROLLER.CommandsAndResponses to com.google.gson;
    exports it.polimi.tetris;
    exports it.polimi.tetris.CONTROLLER;
    opens it.polimi.tetris.CONTROLLER to javafx.fxml;


    opens it.polimi.tetris.MODEL.ENUMS to com.google.gson;


    opens it.polimi.tetris.MODEL to com.google.gson;



}