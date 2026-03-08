module org.example.tetris {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.example.tetris to javafx.fxml;
    exports org.example.tetris;
}