package tirolilo.param;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsiakasOlio implements Serializable {
    private transient SimpleIntegerProperty mokkiID = new SimpleIntegerProperty();
    private transient SimpleIntegerProperty asiakasID = new SimpleIntegerProperty();
    private transient StringProperty nimi = new SimpleStringProperty();
    private transient StringProperty puhelin = new SimpleStringProperty();
    private transient StringProperty osoite = new SimpleStringProperty();
    private transient StringProperty spostiOsoite = new SimpleStringProperty();

    // Oletusalustaja
    public AsiakasOlio() {
        this(0, "", "");
    }

    // Alustaja ID:llä, nimellä ja puhelinnumerolla
    public AsiakasOlio(int id, String nimi, String puhelin) {
        this.asiakasID.set(id);
        this.nimi.set(nimi);
        this.puhelin.set(puhelin);
    }

    public int getAsiakasID() {
        return asiakasID.get();
    }

    public void setAsiakasID(int id) {
        this.asiakasID.set(id);
    }

    public String getNimi() {
        return nimi.get();
    }

    public void setNimi(String nimi) {
        this.nimi.set(nimi);
    }

    public StringProperty nimiProperty() {
        return nimi;
    }

    public String getPuhelin() {
        return puhelin.get();
    }

    public void setPuhelin(String puhelin) {
        this.puhelin.set(puhelin);
    }

    public StringProperty puhelinProperty() {
        return puhelin;
    }

    public String getOsoite() {
        return osoite.get();
    }

    public void setOsoite(String osoite) {
        this.osoite.set(osoite);
    }

    public String getSpostiOsoite() {
        return spostiOsoite.get();
    }

    public void setSpostiOsoite(String email) {
        this.spostiOsoite.set(email);
    }

    public StringProperty spostiOsoiteProperty() {
        return spostiOsoite;
    }


    public List<AsiakasOlio> lue() {
        List<AsiakasOlio> asiakkaat = new ArrayList<>();
        File file = new File("Asiakkaat.dat");

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof List<?>) {
                    asiakkaat = (List<AsiakasOlio>) obj;
                }
            } catch (Exception e) {
                System.out.println("Virhe lukemisessa");
            }
        }
        return asiakkaat;
    }
}