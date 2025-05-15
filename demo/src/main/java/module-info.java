module MokinVarausJarjestelma {
    requires javafx.controls;
    requires javafx.fxml;

    exports projektityo;
    opens projektityo to javafx.fxml;
}
