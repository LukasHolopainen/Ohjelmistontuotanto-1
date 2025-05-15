package projektityo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VarausOlio implements Serializable {

    private int VarausID;
    private int MökkiID;
    private int AsiakasID;
    private int Päivät;

    /**Oletusalustaja, joka luo tyhjän varausolion.*/
    public VarausOlio() {
    }

    /**Alustaja, joka luo varauksen tietojen perusteella.*/
    public VarausOlio(int VarausID, int MökkiID, int AsiakasID, int Päivät) {
        this.VarausID = VarausID;
        this.MökkiID = MökkiID;
        this.AsiakasID = AsiakasID;
        this.Päivät = Päivät;
    }

    /**Asettaa varauksen ID:n.*/
    public void setVarausID(int VarausID) {
        this.VarausID = VarausID;
    }

    /**Palauttaa varauksen ID:n.*/
    public int getVarausID() {
        return VarausID;
    }

    /**Asettaa mökin id:n*/
    public void setMökkiID(int MökkiID) {
        this.MökkiID = MökkiID;
    }

    /**Palauttaa mökin id:n.*/
    public int getNukkumaPaikat() {
        return MökkiID;
    }

    /**Asettaa Asiakas ID:n.*/
    public void setAsiakasID(int AsiakasID) {
        this.AsiakasID = AsiakasID;
    }

    /**Palauttaa AsiakasId:n.*/
    public int getAsiakasID() {
        return AsiakasID;
    }

    /**Asettaa keston päivinä.*/
    public void setPäivät(int Päivät) {
        this.Päivät = Päivät;
    }

    /**Palauttaa varauksen keston päivinä-.*/
    public int getPäivät() {
        return Päivät;
    }


    /**Palauttaa varauksen tiedot merkkijonona.*/
    @Override
    public String toString() {
        return VarausID + ", " + Päivät + ", " + AsiakasID + ", " + MökkiID;
    }

    /**Tallentaa varauksen tiedot tiedostoon.*/
    public void Tallenna(VarausOlio mökki) {
        List<VarausOlio> VarausList = Lue();
        VarausList.remove(mökki);
        VarausList.add(mökki);

        try (ObjectOutputStream Tallennus = new ObjectOutputStream(new FileOutputStream("Varaukset.dat"))) {
            Tallennus.writeObject(VarausList);
        } catch (Exception e) {
            System.out.println("Virhe tallennuksessa");
        }
    }

    /**Lukee varauksen tiedot tiedostosta. Palauttaa Listan varauksista.*/
    public List<VarausOlio> Lue() {
        List<VarausOlio> Varaukset = new ArrayList<>();
        File file = new File("Varaukset.dat");

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream Lukeminen = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = Lukeminen.readObject();
                if (obj instanceof List<?>) {
                    Varaukset = (List<VarausOlio>) obj;
                }
            } catch (Exception e) {
                System.out.println("Virhe lukemisessa");
            }
        }

        return Varaukset;
    }

    /**Vertaa kahta varausoliota varausID:n perusteella.return true, jos varausID on sama, muuten false.*/
    @Override
    public boolean equals(Object VarausOlio) {
        if (this == VarausOlio) return true;
        if (VarausOlio == null || getClass() != VarausOlio.getClass()) return false;
        VarausOlio asiakas = (VarausOlio) VarausOlio;
        return VarausID == asiakas.VarausID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(VarausID);
    }
}