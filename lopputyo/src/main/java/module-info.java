module com.button.lopputyo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.button.lopputyo to javafx.fxml;
    exports com.button.lopputyo;
}