package tirolilo.param;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MokkiOlio implements Serializable {

    private int MökkiID;
    private int NukkumaPaikat;
    private int Huoneet;
    private String Osoite;
    private double PäiväHinta;
    private String mokkiNimi;

    //Oletusalustaja, joka luo tyhjän mökkiolion.
    public MokkiOlio() {
    }

    //Alustaja, joka luo mökin tietojen perusteella.
    public MokkiOlio(int MökkiID, int NukkumaPaikat, int Huoneet, String Osoite, double Päivähinta, String mokkiNimi ) {
        this.MökkiID = MökkiID;
        this.NukkumaPaikat = NukkumaPaikat;
        this.Huoneet = Huoneet;
        this.Osoite = Osoite;
        this.PäiväHinta = Päivähinta;
        this.mokkiNimi = mokkiNimi;
    }

    public MokkiOlio(int i, int i1, int i2, String s, double v) {
    }

    public static MokkiOlio haeTiedot(Connection conn, int mokkiID)throws SQLException {
        String sql = "Select * FROM mokki WHERE mokkiID = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,mokkiID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("mokkiID");
                    int huoneet = rs.getInt("huoneet");
                    int nukkumaPaikat = rs.getInt("nukkumaPaikat");
                    String osoite = rs.getString("osoite");
                    double paivahinta = rs.getDouble("paivahinta");
                    String mokkiNimi = rs.getString("mokkiNimi");

                    return new MokkiOlio(id, nukkumaPaikat, huoneet, osoite, paivahinta, mokkiNimi);
                }else {
                    return null;
                }
            }
        }
    }

    public static List<MokkiOlio> haeKaikkiMokit(Connection conn) throws SQLException {
        List<MokkiOlio> mokit = new ArrayList<>();

        String sql = "SELECT * FROM mokki";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("mokkiID");
                int huoneet = rs.getInt("huoneet");
                int nukkumaPaikat = rs.getInt("nukkumaPaikat");
                String osoite = rs.getString("osoite");
                double paivahinta = rs.getDouble("hintaPerPaiva");
                String mokkiNimi = rs.getString("mokkiNimi");

                MokkiOlio mokki = new MokkiOlio(id, huoneet, nukkumaPaikat, osoite, paivahinta,mokkiNimi);
                mokit.add(mokki);

            }
        }

        return mokit;
    }

    public String getMokkiNimi() {
        return mokkiNimi;
    }
}
