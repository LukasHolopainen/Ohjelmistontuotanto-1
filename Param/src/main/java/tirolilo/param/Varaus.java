package tirolilo.param;

import javafx.beans.property.*;

public class Varaus {

    private IntegerProperty mokkiID;
    private StringProperty mokki;
    private StringProperty paivamaaraStart;
    private StringProperty paivamaaraEnd;
    private ObjectProperty<AsiakasOlio> asiakas;
    private StringProperty puhelin;
    private StringProperty email;
    private IntegerProperty varausID;

    public Varaus(int mokkiID, int varausID, String mokki, String paivamaaraStart, String paivamaaraEnd, AsiakasOlio asiakas, String puhelin,String email) {
        this.mokkiID = new SimpleIntegerProperty(mokkiID);
        this.varausID = new SimpleIntegerProperty(varausID);
        this.mokki = new SimpleStringProperty(mokki);
        this.paivamaaraStart = new SimpleStringProperty(paivamaaraStart);
        this.paivamaaraEnd = new SimpleStringProperty(paivamaaraEnd);
        this.asiakas = new SimpleObjectProperty<>(asiakas);
        this.puhelin = new SimpleStringProperty(puhelin);
        this.email = new SimpleStringProperty(email);

    }

    // Getterit property-objekteille (näitä käytetään TableView:ssa)
    public StringProperty mokkiProperty() {
        return mokki;
    }

    public StringProperty puhelinProperty() {
        return puhelin;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public int getMokkiID() {
        return mokkiID.get();
    }

    public int getVarausID() {
        return varausID.get();
    }

    public String getMokki() {
        return mokki.get();
    }

    public String getPaivamaaraStart() {
        return paivamaaraStart.get();
    }

    public String getPaivamaaraEnd() {
        return paivamaaraEnd.get();
    }

    public AsiakasOlio getAsiakas() {
        return asiakas.get();
    }

    public String getPuhelin() {
        return puhelin.get();
    }

    public String getEmail() {
        return email.get();
    }

    public void setVarausID(int varausID) {
        this.varausID.set(varausID);
    }

    public void setMokki(String mokki) {
        this.mokki.set(mokki);
    }

    public void setPaivamaaraStart(String paivamaaraStart) {
        this.paivamaaraStart.set(paivamaaraStart);
    }

    public void setPaivamaaraEnd(String paivamaaraEnd) {
        this.paivamaaraEnd.set(paivamaaraEnd);
    }

    public void setPuhelin(String puhelin) {
        this.puhelin.set(puhelin);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}
