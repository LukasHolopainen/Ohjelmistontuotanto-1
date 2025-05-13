module ohjelmistontuotanto {
    requires javafx.controls;
    requires javafx.fxml;

    opens ohjelmistontuotanto to javafx.fxml;
    exports ohjelmistontuotanto;
}