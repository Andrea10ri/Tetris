module org.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens it.polimi.tetris to javafx.fxml;
    exports it.polimi.tetris;
    exports it.polimi.tetris.CONTROLLER;
    opens it.polimi.tetris.CONTROLLER to javafx.fxml;
}