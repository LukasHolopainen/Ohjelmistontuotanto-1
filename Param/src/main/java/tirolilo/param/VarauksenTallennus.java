package tirolilo.param;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class VarauksenTallennus {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/tietokanta_mokki";
    private static final String käyttäjä = "root";
    private static final String salasana = "1234";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, käyttäjä, salasana);
    }

    public static int insertVaraus(Connection conn, Varaus varaus) throws SQLException {
        String sql = "INSERT INTO varaus (PaivamaaraStart, PaivamaaraEnd, mokkiID, asiakasID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, varaus.getPaivamaaraStart());
            statement.setString(2, varaus.getPaivamaaraEnd());
            statement.setInt(3, varaus.getMokkiID());
            statement.setInt(4, varaus.getAsiakas().getAsiakasID());

            statement.executeUpdate();

            try (ResultSet generoituavain = statement.getGeneratedKeys()){
                if (generoituavain.next()){
                    return generoituavain.getInt(1);
                } else {
                    throw new SQLException("Virhe varausID:n tekemisessä");
                }
            }
        }
    }

    public static int insertAsiakas(Connection conn, AsiakasOlio asiakas) throws SQLException {
        String sql = "INSERT INTO asiakas (nimi, puhelinNumero, sahkoPostiOsoite, osoite) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, asiakas.getNimi());
            stmt.setString(2, asiakas.getPuhelin());
            stmt.setString(3, asiakas.getSpostiOsoite());
            stmt.setString(4, asiakas.getOsoite());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int newID = rs.getInt(1);
                    asiakas.setAsiakasID(newID);
                    return newID;
                } else {
                    throw new SQLException("Failed to retrieve generated asiakasID.");
                }
            }
        }
    }
    public static List<Varaus> haeVaraukset(Connection conn) throws SQLException {
        List<Varaus> varaukset = new ArrayList<>();
        String sql = "SELECT v.varausID, v.PaivamaaraStart, v.PaivamaaraEnd, " +
                "m.mokkiID, m.mokkiNimi, " +
                "a.asiakasID, a.nimi AS asiakasNimi, a.puhelinNumero, a.sahkoPostiOsoite " +
                "FROM varaus v " +
                "JOIN mokki m ON v.mokkiID = m.mokkiID " +
                "JOIN asiakas a ON v.asiakasID = a.asiakasID";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int varausID = rs.getInt("varausID");
                int mokkiID = rs.getInt("mokkiID");
                String mokkiNimi = rs.getString("mokkiNimi");
                String paivaStart = rs.getString("PaivamaaraStart");
                String paivaEnd = rs.getString("PaivamaaraEnd");

                AsiakasOlio asiakas = new AsiakasOlio();
                asiakas.setAsiakasID(rs.getInt("asiakasID"));
                asiakas.setNimi(rs.getString("asiakasNimi"));
                String puhelin = rs.getString("puhelinNumero");
                String email = rs.getString("sahkoPostiOsoite");

                Varaus varaus = new Varaus(mokkiID, varausID, mokkiNimi, paivaStart, paivaEnd, asiakas, puhelin, email);
                varaukset.add(varaus);
            }
        }
        return varaukset;
    }
    public static List<AsiakasOlio> haeAsiakkaat(Connection conn) throws SQLException {
        List<AsiakasOlio> asiakkaat = new ArrayList<>();
        String sql = "SELECT asiakasID, nimi, puhelinNumero, sahkoPostiOsoite, osoite FROM asiakas";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AsiakasOlio asiakas = new AsiakasOlio();
                asiakas.setAsiakasID(rs.getInt("asiakasID"));
                asiakas.setNimi(rs.getString("nimi"));
                asiakas.setPuhelin(rs.getString("puhelinNumero"));
                asiakas.setSpostiOsoite(rs.getString("sahkoPostiOsoite"));
                asiakas.setOsoite(rs.getString("osoite"));

                asiakkaat.add(asiakas);
            }
        }

        return asiakkaat;
    }
    public static boolean poistaVaraus(Connection conn, int varausID) throws SQLException {
        String sql = "DELETE FROM varaus WHERE varausID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, varausID);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
