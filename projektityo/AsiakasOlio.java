package projekti.projektityo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsiakasOlio implements Serializable {
    public int MökkiID;
    private int AsiakasID;
    private String Nimi;
    private String Pnumero;
    private String Osoite;
    private String SpostiOsoite;

    /**Oletusalustaja, joka luo tyhjän asiakkaan.*/
    public AsiakasOlio() {
    }

    /**Alustaja, joka luo asiakkaan asiakasnumeron, nimen ja puhelinnumeron perusteella.*/
    public AsiakasOlio(int Anumero, String Nimi, String Pnumero) {
        this.AsiakasID = Anumero;
        this.Nimi = Nimi;
        this.Pnumero = Pnumero;
    }

    /**Asettaa asiakkaan asiakasnumeron.*/
    public void setAsiakasID(int Anumero) {
        this.AsiakasID = AsiakasID;
    }

    /**Palauttaa asiakkaan asiakasnumeron.*/
    public int getAnumero() {
        return AsiakasID;
    }

    /**Asettaa asiakkaan nimen.*/
    public void setNimi(String Nimi) {
        this.Nimi = Nimi;
    }

    /**Palauttaa asiakkaan nimen.*/
    public String getNimi() {
        return Nimi;
    }

    /**Asettaa asiakkaan puhelinnumeron.*/
    public void setPnumero(String Pnumero) {
        this.Pnumero = Pnumero;
    }

    /**Palauttaa asiakkaan puhelinnumeron.*/
    public String getPnumero() {
        return Pnumero;
    }

    /**Asettaa asiakkaan Osoitteen.*/
    public void setOsoite(String Osoite) {
        this.Osoite = Osoite;
    }

    /**Palauttaa asiakkaan Osoitteen.*/
    public String getOsoite() {
        return Osoite;
    }

    /**Asettaa asiakkaan Sähköpostiosoitteen.*/
    public void setSpostiOsoite(String SpostiOsoite) {
        this.SpostiOsoite = SpostiOsoite;
    }

    /**palauttaa asiakkaan Sähköpostiosoitteen.*/
    public String getSpostiOsoite() {
        return SpostiOsoite;
    }

    /**Palauttaa asiakkaan tiedot merkkijonona.*/
    @Override
    public String toString() {
        return AsiakasID + ", " + Nimi + ", " + Pnumero;
    }

    /**Tallentaa asiakkaan tiedot tiedostoon.*/
    public void Tallenna(AsiakasOlio asiakas) {
        List<AsiakasOlio> asiakasList = Lue();
        asiakasList.remove(asiakas);
        asiakasList.add(asiakas);

        try (ObjectOutputStream Tallennus = new ObjectOutputStream(new FileOutputStream("Asiakkaat.dat"))) {
            Tallennus.writeObject(asiakasList);
        } catch (Exception e) {
            System.out.println("Virhe tallennuksessa");
        }
    }

    /**Lukee asiakkaiden tiedot tiedostosta. Palauttaa Listan asiakkaista.*/
    public List<AsiakasOlio> Lue() {
        List<AsiakasOlio> asiakkaat = new ArrayList<>();
        File file = new File("Asiakkaat.dat");

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream Lukeminen = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = Lukeminen.readObject();
                if (obj instanceof List<?>) {
                    asiakkaat = (List<AsiakasOlio>) obj;
                }
            } catch (Exception e) {
                System.out.println("Virhe lukemisessa");
            }
        }

        return asiakkaat;
    }

    /**Vertaa kahta asiakasoliota asiakasnumeron perusteella.return true, jos asiakasnumero on sama, muuten false.*/
    @Override
    public boolean equals(Object asiakasOlio) {
        if (this == asiakasOlio) return true;
        if (asiakasOlio == null || getClass() != asiakasOlio.getClass()) return false;
        AsiakasOlio asiakas = (AsiakasOlio) asiakasOlio;
        return AsiakasID == asiakas.AsiakasID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(AsiakasID);
    }
}
