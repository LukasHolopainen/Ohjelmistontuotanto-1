package projekti.projektityo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LaskuOlio implements Serializable {

    private int AsiakasID;
    private int LaskuID;
    private int VarausID;
    private double Hinta;


    /**Oletusalustaja, joka luo tyhjän laskuolion.*/
    public LaskuOlio() {
    }

    /**Alustaja, joka luo laskun tietojen perusteella.*/
    public LaskuOlio(int VarausID, int LaskuID, int AsiakasID, double Hinta) {
        this.VarausID = VarausID;
        this.LaskuID = LaskuID;
        this.AsiakasID = AsiakasID;
        this.Hinta = Hinta;
    }

    /**Asettaa varauksen ID:n.*/
    public void setVarausID(int VarausID) {
        this.VarausID = VarausID;
    }

    /**Palauttaa varauksen ID:n.*/
    public int getVarausID() {
        return VarausID;
    }

    /**Asettaa laskun id:n*/
    public void setLaskuID(int LaskuID) {
        this.LaskuID = LaskuID;
    }

    /**Palauttaa laskun id:n.*/
    public int getLaskuID() {
        return LaskuID;
    }

    /**Asettaa Asiakas ID:n.*/
    public void setAsiakasID(int AsiakasID) {
        this.AsiakasID = AsiakasID;
    }

    /**Palauttaa AsiakasId:n.*/
    public int getAsiakasID() {
        return AsiakasID;
    }

    /**Asettaa hinnan.*/
    public void setHinta(double Hinta) {
        this.Hinta = Hinta;
    }

    /**Palauttaa hinnan.*/
    public double getHinta() {
        return Hinta;
    }


    /**Palauttaa laskun tiedot merkkijonona.*/
    @Override
    public String toString() {
        return VarausID + ", " + Hinta + ", " + AsiakasID + ", " + LaskuID;
    }

    /**Tallentaa laskun tiedot tiedostoon.*/
    public void Tallenna(LaskuOlio lasku) {
        List<LaskuOlio> LaskuList = Lue();
        LaskuList.remove(lasku);
        LaskuList.add(lasku);

        try (ObjectOutputStream Tallennus = new ObjectOutputStream(new FileOutputStream("Laskut.dat"))) {
            Tallennus.writeObject(LaskuList);
        } catch (Exception e) {
            System.out.println("Virhe tallennuksessa");
        }
    }

    /**Lukee laskun tiedot tiedostosta. Palauttaa Listan laskuista.*/
    public List<LaskuOlio> Lue() {
        List<LaskuOlio> Laskut = new ArrayList<>();
        File file = new File("Laskut.dat");

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream Lukeminen = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = Lukeminen.readObject();
                if (obj instanceof List<?>) {
                    Laskut = (List<LaskuOlio>) obj;
                }
            } catch (Exception e) {
                System.out.println("Virhe lukemisessa");
            }
        }

        return Laskut;
    }

    /**Vertaa kahta laskuoliota LaskuID:n perusteella.return true, jos LaskuID on sama, muuten false.*/
    @Override
    public boolean equals(Object LaskuOlio) {
        if (this == LaskuOlio) return true;
        if (LaskuOlio == null || getClass() != LaskuOlio.getClass()) return false;
        LaskuOlio Lasku = (LaskuOlio) LaskuOlio;
        return LaskuID == Lasku.LaskuID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(LaskuID);
    }
}