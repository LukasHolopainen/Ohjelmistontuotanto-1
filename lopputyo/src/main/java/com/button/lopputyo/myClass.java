package com.button.lopputyo;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class myClass extends Application {

    private TableView<Varaus> varausTable;
    private ComboBox<String> mokkiComboBox;
    private DatePicker varausPaivamaaraStart, varausPaivamaaraEnd;
    private TextField etunimiField, sukunimiField, puhelinField, emailField;
    private final List<Varaus> varaukset = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mökkivarausjärjestelmä");

        TabPane tabPane = new TabPane();

        Tab varausTab = new Tab("Varaus", createVarausLayout());
        varausTab.setClosable(false);

        Tab mokkienTiedotTab = new Tab("Mökkien tiedot", createMokkienTiedotLayout());
        mokkienTiedotTab.setClosable(false);

        tabPane.getTabs().addAll(varausTab, mokkienTiedotTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        varausPaivamaaraStart.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(java.time.LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && !empty) {
                    boolean paivaVarattu = varaukset.stream().anyMatch(v -> {
                        java.time.LocalDate start = java.time.LocalDate.parse(v.getPaivamaaraStart());
                        java.time.LocalDate end = java.time.LocalDate.parse(v.getPaivamaaraEnd());
                        return !date.isBefore(start) && !date.isAfter(end);
                    });
                    if (paivaVarattu) {
                        setStyle("-fx-background-color: #ff9999;");
                    }
                }
            }
        });

    }

    private VBox createVarausLayout() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        varausTable = new TableView<>();
        varausTable.getColumns().addAll(
                createColumn("Mökki", Varaus::getMokki),
                createColumn("Päivämäärä", v -> v.getPaivamaaraStart() + " - " + v.getPaivamaaraEnd()),
                createColumn("Asiakas", Varaus::getAsiakas),
                createColumn("Puhelinnumero", Varaus::getPuhelin),
                createColumn("Sähköposti", Varaus::getEmail)
        );

        mokkiComboBox = new ComboBox<>();
        mokkiComboBox.getItems().addAll("Mökki 1", "Mökki 2", "Mökki 3", "Mökki 4");
        mokkiComboBox.setPromptText("Valitse mökki");

        varausPaivamaaraStart = new DatePicker();
        varausPaivamaaraEnd = new DatePicker();

        etunimiField = new TextField();
        sukunimiField = new TextField();
        puhelinField = new TextField();
        emailField = new TextField();

        etunimiField.setPromptText("Etunimi");
        sukunimiField.setPromptText("Sukunimi");
        puhelinField.setPromptText("Puhelinnumero");
        emailField.setPromptText("Sähköposti");

        Button varaaButton = new Button("Varaa!");
        varaaButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        varaaButton.setOnAction(e -> varaaMokki());

        Button tyhjennaButton = new Button("Tyhjennä");
        tyhjennaButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        tyhjennaButton.setOnAction(e -> tyhjennaKentat());

        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setPadding(new Insets(20));

        form.add(new Label("Mökki:"), 0, 0);
        form.add(mokkiComboBox, 1, 0);
        form.add(new Label("Alkupäivämäärä:"), 0, 1);
        form.add(varausPaivamaaraStart, 1, 1);
        form.add(new Label("Loppupäivämäärä:"), 0, 2);
        form.add(varausPaivamaaraEnd, 1, 2);
        form.add(new Label("Etunimi:"), 0, 3);
        form.add(etunimiField, 1, 3);
        form.add(new Label("Sukunimi:"), 0, 4);
        form.add(sukunimiField, 1, 4);
        form.add(new Label("Puhelin:"), 0, 5);
        form.add(puhelinField, 1, 5);
        form.add(new Label("Email:"), 0, 6);
        form.add(emailField, 1, 6);
        form.add(varaaButton, 0, 7);
        form.add(tyhjennaButton, 1, 7);

        layout.getChildren().addAll(form, varausTable);
        return layout;
    }

    private VBox createMokkienTiedotLayout() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        layout.getChildren().addAll(
                createMokkiInfo("Mökki 1", "50 m²", "150€/yö"),
                createMokkiInfo("Mökki 2", "70 m²", "200€/yö"),
                createMokkiInfo("Mökki 3", "90 m²", "250€/yö"),
                createMokkiInfo("Mökki 4", "120 m²", "300€/yö"),
                new Text("Valitse sopiva mökki ja nauti lomasta luonnon rauhassa!")
        );

        return layout;
    }

    private TableColumn<Varaus, String> createColumn(String title, javafx.util.Callback<Varaus, String> mapper) {
        TableColumn<Varaus, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> new SimpleStringProperty(mapper.call(cellData.getValue())));
        return column;
    }

    private Text createMokkiInfo(String nimi, String koko, String hinta) {
        Text text = new Text(nimi + "\nKoko: " + koko + "\nHinta: " + hinta);
        text.setFont(Font.font("Arial", 18));
        text.setStyle("-fx-font-weight: bold;");
        return text;
    }

    private void varaaMokki() {
        String mokki = mokkiComboBox.getValue();
        String paivaStart = varausPaivamaaraStart.getValue() != null ? varausPaivamaaraStart.getValue().toString() : "";
        String paivaEnd = varausPaivamaaraEnd.getValue() != null ? varausPaivamaaraEnd.getValue().toString() : "";

        String etunimi = etunimiField.getText();
        String sukunimi = sukunimiField.getText();
        String puhelin = puhelinField.getText();
        String email = emailField.getText();

        if (mokki != null && !paivaStart.isEmpty() && !paivaEnd.isEmpty() && !etunimi.isEmpty() && !sukunimi.isEmpty() && !puhelin.isEmpty() && !email.isEmpty()) {
            String asiakas = etunimi + " " + sukunimi;
            varaukset.add(new Varaus(mokki, paivaStart, paivaEnd, asiakas, puhelin, email));
            varausTable.getItems().setAll(varaukset);
            showAlert("Varaus onnistui!", mokki + " varattu ajalle " + paivaStart + " - " + paivaEnd + ". Asiakas: " + asiakas);
        } else {
            showAlert("Virhe", "Täytä kaikki kentät!");
        }

        varausPaivamaaraStart.setValue(null); // tyhjennä valinta
        varausPaivamaaraEnd.setValue(null);   // tyhjennä valinta
    }

    private void tyhjennaKentat() {
        mokkiComboBox.setValue(null);
        varausPaivamaaraStart.setValue(null);
        varausPaivamaaraEnd.setValue(null);
        etunimiField.clear();
        sukunimiField.clear();
        puhelinField.clear();
        emailField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Varaus {
        private final String mokki, paivamaaraStart, paivamaaraEnd, asiakas, puhelin, email;

        public Varaus(String mokki, String paivamaaraStart, String paivamaaraEnd, String asiakas, String puhelin, String email) {
            this.mokki = mokki;
            this.paivamaaraStart = paivamaaraStart;
            this.paivamaaraEnd = paivamaaraEnd;
            this.asiakas = asiakas;
            this.puhelin = puhelin;
            this.email = email;
        }

        public String getMokki(){
            return mokki;
        }

        public String getPaivamaaraStart() {
            return paivamaaraStart;
        }

        public String getPaivamaaraEnd() {
            return paivamaaraEnd;
        }

        public String getAsiakas() {
            return asiakas;
        }

        public String getPuhelin() {
            return puhelin;
        }

        public String getEmail() {
            return email;
        }
    }
}