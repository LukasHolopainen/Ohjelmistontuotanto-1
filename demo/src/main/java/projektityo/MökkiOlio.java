package projektityo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MökkiOlio implements Serializable {

    private int MökkiID;
    private int NukkumaPaikat;
    private int Huoneet;
    private String Osoite;
    private double PäiväHinta;

    /**Oletusalustaja, joka luo tyhjän mökkiolion.*/
    public MökkiOlio() {
    }

    /**Alustaja, joka luo mökin tietojen perusteella.*/
    public MökkiOlio(int MökkiID, int NukkumaPaikat, int Huoneet, String Osoite, double Päivähinta ) {
        this.MökkiID = MökkiID;
        this.NukkumaPaikat = NukkumaPaikat;
        this.Huoneet = Huoneet;
        this.Osoite = Osoite;
        this.PäiväHinta = Päivähinta;
    }

    /**Asettaa mökin ID:n.*/
    public void setMökkiID(int MökkiID) {
        this.MökkiID = MökkiID;
    }

    /**Palauttaa mökin ID:n.*/
    public int getMökkiID() {
        return MökkiID;
    }

    /**Asettaa nukumapaikkojen määrän*/
    public void setNukkumaPaikat(int NukkumaPaikat) {
        this.NukkumaPaikat = NukkumaPaikat;
    }

    /**Palauttaa nukumapaikkojen määrän.*/
    public int getNukkumaPaikat() {
        return NukkumaPaikat;
    }

    /**Asettaa huoneitten määrän.*/
    public void setHuoneet(int Huoneet) {
        this.Huoneet = Huoneet;
    }

    /**Palauttaa huoneitten määrän.*/
    public int getHuoneet() {
        return Huoneet;
    }

    /**Asettaa mökin Osoitteen.*/
    public void setOsoite(String Osoite) {
        this.Osoite = Osoite;
    }

    /**Palauttaa mökin Osoitteen.*/
    public String getOsoite() {
        return Osoite;
    }

    /**Asettaa mökin hinnan päivää kohti.*/
    public void setPäiväHinta(double PäiväHinta) {
        this.PäiväHinta = PäiväHinta;
    }

    /**palauttaa mökin hinnan päivää kohti.*/
    public double getPäiväHinta() {
        return PäiväHinta;
    }

    /**Palauttaa mökin tiedot merkkijonona.*/
    @Override
    public String toString() {
        return MökkiID + ", " + Osoite + ", " + Huoneet + ", " + NukkumaPaikat + ", " + PäiväHinta;
    }

    /**Tallentaa mökin tiedot tiedostoon.*/
    public void Tallenna(MökkiOlio mökki) {
        List<MökkiOlio> MökkiList = Lue();
        MökkiList.remove(mökki);
        MökkiList.add(mökki);

        try (ObjectOutputStream Tallennus = new ObjectOutputStream(new FileOutputStream("Asiakkaat.dat"))) {
            Tallennus.writeObject(MökkiList);
        } catch (Exception e) {
            System.out.println("Virhe tallennuksessa");
        }
    }

    /**Lukee mökkien tiedot tiedostosta. Palauttaa Listan mökeistä.*/
    public List<MökkiOlio> Lue() {
        List<MökkiOlio> Mökit = new ArrayList<>();
        File file = new File("Mökit.dat");

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream Lukeminen = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = Lukeminen.readObject();
                if (obj instanceof List<?>) {
                    Mökit = (List<MökkiOlio>) obj;
                }
            } catch (Exception e) {
                System.out.println("Virhe lukemisessa");
            }
        }

        return Mökit;
    }

    /**Vertaa kahta mökkioliota MökkiID:n perusteella.return true, jos MökkiID on sama, muuten false.*/
    @Override
    public boolean equals(Object asiakasOlio) {
        if (this == asiakasOlio) return true;
        if (asiakasOlio == null || getClass() != asiakasOlio.getClass()) return false;
        AsiakasOlio asiakas = (AsiakasOlio) asiakasOlio;
        return MökkiID == asiakas.MökkiID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(MökkiID);
    }
}