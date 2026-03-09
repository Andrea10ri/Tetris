module org.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens it.polimi.tetris to javafx.fxml;
    exports it.polimi.tetris;
}