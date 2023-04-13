module org.example.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires controller;

    opens org.example.gui to javafx.fxml;
    exports org.example.gui;
}