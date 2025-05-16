package tirolilo.param;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Arrays;
import java.util.Optional;

public class MokkiGUI extends Application {


    private TableView<AsiakasOlio> asiakasTableView;
    private ObservableList<AsiakasOlio> asiakkaat;
    private TableView<Varaus> varausTable;
    private ComboBox<String> mokkiComboBox;
    private DatePicker varausPaivamaaraStart, varausPaivamaaraEnd;
    private TextField etunimiField, sukunimiField, puhelinField, emailField;
    private final ObservableList<tirolilo.param.Varaus> varausLista = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        lataaVarauksetTietokannasta();
        try {
            Connection conn = VarauksenTallennus.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Mökkivarausjärjestelmä");

        TabPane tabPane = new TabPane();

        Tab varausTab = new Tab("Varaus", createVarausLayout());
        varausTab.setClosable(false);

        Tab mokkienTiedotTab = new Tab("Mökkien tiedot", createMokkienTiedotLayout());
        mokkienTiedotTab.setClosable(false);

        Tab varauksenHallintaTab = new Tab("Varaukset", createVarauksenHallintaLayout());
        varauksenHallintaTab.setClosable(false);

        Tab asiakasTab = new Tab("Asiakas lista");
        asiakasTab.setClosable(false);
        asiakasTab.setContent(createAsiakasLayout());

        tabPane.getTabs().addAll(varausTab, mokkienTiedotTab, varauksenHallintaTab, asiakasTab);

        Scene scene = new Scene(tabPane, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        lataaMokitTietokannasta();
    }


    private VBox createVarausLayout() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        varausTable = new TableView<>();
        varausTable.setItems(varausLista);
        varausTable.getColumns().addAll(
                createColumn("Mökki", v -> v.mokkiProperty()),
                createColumn("Päivämäärä", v -> new SimpleStringProperty(v.getPaivamaaraStart() + " - " + v.getPaivamaaraEnd())),
                createColumn("Asiakas", v -> new SimpleStringProperty(v.getAsiakas().getNimi())),
                createColumn("Puhelinnumero", v -> v.puhelinProperty()),
                createColumn("Sähköposti", v -> v.emailProperty())
        );

        int columnCount = varausTable.getColumns().size();
        for (TableColumn<?, ?> col : varausTable.getColumns()) {
            col.prefWidthProperty().bind(varausTable.widthProperty().divide(columnCount));
        }

        mokkiComboBox = new ComboBox<>();
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

    List<MokkiOlio> mokit = Arrays.asList(
            new MokkiOlio(1, 4, 2, "Järvitie 1", 150.0),
            new MokkiOlio(2, 6, 3, "Metsäpolku 2", 200.0),
            new MokkiOlio(3, 8, 4, "Rantatie 3", 250.0),
            new MokkiOlio(4, 10, 5, "Kukkatie 4", 300.0)
    );

    private VBox createMokkienTiedotLayout() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        layout.getChildren().addAll(
                createMokkiInfo("Revontulen ratsataja", 6, 3, "Katukujantie 99", "98,75€/yö"),
                createMokkiInfo("Villa Crocodilo", 8,5, "Skibidinkatu 420", "199,9€/yö"),
                createMokkiInfo("Tralaleron Tila", 10,8, "Metsämökintie 33", "299,95€/yö"),
                createMokkiInfo("Cocofanton Cartano", 4, 4, "Kappucinonkuja 1",  "79,99€/yö"),
                createMokkiInfo("Meemimökki", 2, 1, "Kauppiaankatu 22",  "14,99€/yö"),
                new Text("Valitse sopiva mökki ja nauti lomasta luonnon rauhassa!")
        );

        return layout;
    }
    private VBox createAsiakasLayout() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #f5f5f5;");

        asiakasTableView = new TableView<>();

        TableColumn<AsiakasOlio, String> nimiCol = new TableColumn<>("Nimi");
        nimiCol.setCellValueFactory(cellData -> cellData.getValue().nimiProperty());

        TableColumn<AsiakasOlio, String> puhelinCol = new TableColumn<>("Puhelin");
        puhelinCol.setCellValueFactory(cellData -> cellData.getValue().puhelinProperty());

        TableColumn<AsiakasOlio, String> emailCol = new TableColumn<>("Sähköposti");
        emailCol.setCellValueFactory(cellData -> cellData.getValue().spostiOsoiteProperty());

        asiakasTableView.getColumns().addAll(nimiCol, puhelinCol, emailCol);

        asiakkaat = FXCollections.observableArrayList();
        asiakasTableView.setItems(asiakkaat);

        reloadAsiakkaat();

        layout.getChildren().add(asiakasTableView);
        return layout;
    }
    private void reloadAsiakkaat() {
        try (Connection conn = VarauksenTallennus.getConnection()) {
            List<AsiakasOlio> haetutAsiakkaat = VarauksenTallennus.haeAsiakkaat(conn);
            asiakkaat.setAll(haetutAsiakkaat);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Virhe", "Asiakkaiden lataaminen epäonnistui.");
        }
    }

    private VBox createVarauksenHallintaLayout() {
        VBox layout = new VBox(20);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 20px;");

        // Hakukenttä
        TextField hakukentta = new TextField();
        hakukentta.setPromptText("Hae varauksia (mökin nimi, asiakas...)");

        // Suodatettu lista
        FilteredList<Varaus> suodatettuLista = new FilteredList<>(varausLista, v -> true);

        // Tekstin Listener
        hakukentta.textProperty().addListener((obs, vanhaTeksti, uusiTeksti) -> {
            String hakusana = uusiTeksti.toLowerCase().trim();
            suodatettuLista.setPredicate(varaus -> {
                if (hakusana.isEmpty()) {
                    return true; // näytä kaikki jos hakusana tyhjä
                }
                if (varaus.getMokki().toLowerCase().contains(hakusana)) {
                    return true;
                }
                if (varaus.getAsiakas().getNimi().toLowerCase().contains(hakusana)) {
                    return true;
                }
                if (varaus.getPuhelin().toLowerCase().contains(hakusana)) {
                    return true;
                }
                if (varaus.getEmail().toLowerCase().contains(hakusana)) {
                    return true;
                }
                if (varaus.getPaivamaaraStart().toLowerCase().contains(hakusana)) {
                    return true;
                }
                if (varaus.getPaivamaaraEnd().toLowerCase().contains(hakusana)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Varaus> jarjestettyLista = new SortedList<>(suodatettuLista);

        TableView<Varaus> hallintaTable = new TableView<>();
        hallintaTable.setEditable(true);

        jarjestettyLista.comparatorProperty().bind(hallintaTable.comparatorProperty());

        hallintaTable.setItems(jarjestettyLista);

        TableColumn<Varaus, String> mokkiCol = new TableColumn<>("Mökki");
        mokkiCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMokki()));
        mokkiCol.setCellFactory(TextFieldTableCell.forTableColumn());
        mokkiCol.setOnEditCommit(event -> {
            Varaus varaus = event.getRowValue();
            varaus.setMokki(event.getNewValue());
            varausTable.refresh(); // lisää tämä
            hallintaTable.refresh();
        });


        TableColumn<Varaus, String> alkuPvmCol = new TableColumn<>("Alkupäivämäärä");
        alkuPvmCol.setPrefWidth(120);
        alkuPvmCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaivamaaraStart()));
        alkuPvmCol.setCellFactory(TextFieldTableCell.forTableColumn());
        alkuPvmCol.setOnEditCommit(event -> {
            Varaus varaus = event.getRowValue();
            varaus.setPaivamaaraStart(event.getNewValue());
            hallintaTable.refresh();
        });


        TableColumn<Varaus, String> loppuPvmCol = new TableColumn<>("Loppupäivämäärä");
        loppuPvmCol.setPrefWidth(120);
        loppuPvmCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaivamaaraEnd()));
        loppuPvmCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loppuPvmCol.setOnEditCommit(event -> {
            Varaus varaus = event.getRowValue();
            varaus.setPaivamaaraEnd(event.getNewValue());
            hallintaTable.refresh();
        });


        TableColumn<Varaus, String> asiakasCol = new TableColumn<>("Asiakas");
        asiakasCol.setPrefWidth(100);
        asiakasCol.setCellValueFactory(cellData -> cellData.getValue().getAsiakas().nimiProperty());
        asiakasCol.setCellFactory(TextFieldTableCell.forTableColumn());
        asiakasCol.setOnEditCommit(event -> {
            Varaus varaus = event.getRowValue();
            varaus.getAsiakas().setNimi(event.getNewValue());
            hallintaTable.refresh();
        });

        TableColumn<Varaus, String> puhelinCol = new TableColumn<>("Puhelin");
        puhelinCol.setPrefWidth(100);
        puhelinCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPuhelin()));
        puhelinCol.setCellFactory(TextFieldTableCell.forTableColumn());
        puhelinCol.setOnEditCommit(event -> {
            Varaus varaus = event.getRowValue();
            varaus.setPuhelin(event.getNewValue());
            hallintaTable.refresh();
        });

        TableColumn<Varaus, String> emailCol = new TableColumn<>("Sähköposti");
        emailCol.setPrefWidth(180);
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setOnEditCommit(event -> {
            Varaus varaus = event.getRowValue();
            varaus.setEmail(event.getNewValue());
            hallintaTable.refresh();
        });

        hallintaTable.getColumns().addAll(mokkiCol, alkuPvmCol, loppuPvmCol, asiakasCol, puhelinCol, emailCol);

        Button poistaButton = new Button("Poista varaus");
        poistaButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        poistaButton.setOnAction(e -> {
            Varaus valittu = hallintaTable.getSelectionModel().getSelectedItem();
            if (valittu != null) {
                Alert vahvistus = new Alert(Alert.AlertType.CONFIRMATION);
                vahvistus.setTitle("Varauksen poisto");
                vahvistus.setHeaderText(null);
                vahvistus.setContentText("Haluatko varmasti poistaa varauksen?");

                Optional<ButtonType> vastaus = vahvistus.showAndWait();
                if (vastaus.isPresent() && vastaus.get() == ButtonType.OK) {
                    try (Connection conn = VarauksenTallennus.getConnection()) {
                        boolean poistettu = VarauksenTallennus.poistaVaraus(conn, valittu.getVarausID());
                        if (poistettu) {
                            varausLista.remove(valittu);
                            hallintaTable.refresh();
                            varausTable.refresh();
                            Alert info = new Alert(Alert.AlertType.INFORMATION);
                            info.setTitle("Poisto onnistui");
                            info.setHeaderText(null);
                            info.setContentText("Varaus poistettiin tietokannasta.");
                            info.showAndWait();
                        } else {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            error.setTitle("Virhe");
                            error.setHeaderText(null);
                            error.setContentText("Varauksen poisto tietokannasta epäonnistui.");
                            error.showAndWait();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Virhe");
                        error.setHeaderText(null);
                        error.setContentText("Tietokantavirhe: " + ex.getMessage());
                        error.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Varauksen poisto");
                alert.setHeaderText(null);
                alert.setContentText("Valitse ensin poistettava varaus.");
                alert.showAndWait();
            }
        });


        layout.getChildren().addAll(new Label("Muokkaa varauksia:"), hakukentta, hallintaTable, poistaButton);
        return layout;
    }

    private TableColumn<Varaus, String> createColumn(String title, javafx.util.Callback<Varaus, ObservableValue<String>> mapper) {
        TableColumn<Varaus, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> mapper.call(cellData.getValue()));
        return column;
    }

    private Text createMokkiInfo(String nimi, int nukkumaPaikat, int huoneet, String osoite, String hinta) {
        Text text = new Text(nimi + "\nNukkumapaikat: " + nukkumaPaikat + "\nHuoneet: " + huoneet + "\nOsoite: " + osoite + "\nHinta: " + hinta);
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

        if (mokki == null || mokki.isEmpty() || paivaStart.isEmpty() || paivaEnd.isEmpty() ||
                etunimi.isEmpty() || sukunimi.isEmpty() || puhelin.isEmpty() || email.isEmpty()) {
            showAlert("Virhe", "Täytä kaikki kentät.");
            return;
        }

        AsiakasOlio asiakas = new AsiakasOlio();
        asiakas.setNimi(etunimi + " " + sukunimi);
        asiakas.setPuhelin(puhelin);
        asiakas.setSpostiOsoite(email);

        try (Connection conn = VarauksenTallennus.getConnection()) {
            // Tallenna asiakas tietokantaan
            int asiakasId = VarauksenTallennus.insertAsiakas(conn, asiakas);
            asiakas.setAsiakasID(asiakasId);

            // Luo uusi varaus
            tirolilo.param.Varaus varaus = new tirolilo.param.Varaus(0, 0, mokki, paivaStart, paivaEnd, asiakas, puhelin, email);

            // Tallenna varaus tietokantaan ja hae kannassa generoitu id
            int varausId = VarauksenTallennus.insertVaraus(conn, varaus);
            varaus.setVarausID(varausId);

            // Lisää varaus listaan
            varausLista.add(varaus);

            showAlert("Varaus onnistui", "Mökki varattu ja asiakas tallennettu onnistuneesti.");
            tyhjennaKentat();

        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Virhe", "Tallennus epäonnistui: " + ex.getMessage());
        }
        reloadAsiakkaat();
    }

    private void tyhjennaKentat() {
        varausPaivamaaraStart.setValue(null);
        varausPaivamaaraEnd.setValue(null);
        etunimiField.clear();
        sukunimiField.clear();
        puhelinField.clear();
        emailField.clear();
    }

    private void showAlert(String otsikko, String viesti) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(otsikko);
        alert.setHeaderText(null);
        alert.setContentText(viesti);
        alert.showAndWait();
    }


    private void lataaMokitTietokannasta() {
        try (Connection conn = VarauksenTallennus.getConnection()) {
            List<MokkiOlio> mokitTietokannasta = MokkiOlio.haeKaikkiMokit(conn);
            mokkiComboBox.getItems().clear();
            for (MokkiOlio mokki : mokitTietokannasta) {
                mokkiComboBox.getItems().add(mokki.getMokkiNimi());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Virhe", "Mökkien lataaminen epäonnistui.");
        }
    }

    public static class Asiakas {
        private SimpleStringProperty nimi;

        public Asiakas(String nimi) {
            this.nimi = new SimpleStringProperty(nimi);
        }

        public String getNimi() {
            return nimi.get();
        }

        public void setNimi(String nimi) {
            this.nimi.set(nimi);
        }

        public SimpleStringProperty nimiProperty() {
            return nimi;
        }
    }
    private void lataaVarauksetTietokannasta() {
        try (Connection conn = VarauksenTallennus.getConnection()) {
            List<Varaus> varaukset = VarauksenTallennus.haeVaraukset(conn);
            varausLista.clear();
            varausLista.addAll(varaukset);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Virhe", "Varauksien lataaminen epäonnistui.");
        }
    }

}
