module tirolilo.param {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens tirolilo.param to javafx.fxml;
    exports tirolilo.param;
}