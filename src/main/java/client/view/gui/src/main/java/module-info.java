module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires TP2_IFT1025;

    opens gui to javafx.fxml;
    exports org.example.gui;
}