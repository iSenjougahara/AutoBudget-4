package model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.*;
import java.util.ArrayList;
import web.AppListener;

/**
 *
 * @author Gabriel
 */
public class Pecas {

  @Id
private long rowId;

private String nomePeca;
private double preco;
@Column(name = "modeloCarro_rowId")
private long modeloCarroRowId;

// Getters and setters


    public static String getCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS Pecas("
                + "nomePeca VARCHAR(255),"
                + "preco DOUBLE,"
                + "modeloCarro_rowId BIGINT,"
                + "FOREIGN KEY (modeloCarro_rowId) REFERENCES modeloCarro(rowId)"
                + ")";
    }

    //um é com parametro, outro sem
    public static ArrayList<Pecas> getPecas() throws Exception {
        ArrayList<Pecas> list = new ArrayList<>();
        Connection con = AppListener.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from Pecas");
        while (rs.next()) {
            long rowId = rs.getLong("rowid");
            String nomePeca = rs.getString("nomePeca");
            double preco = rs.getDouble("preco");
            long modeloCarro_rowId = rs.getLong("modeloCarro_rowId");
            list.add(new Pecas(rowId, nomePeca, preco, modeloCarro_rowId));
        }
        rs.close();
        stmt.close();
        con.close();
        return list;
    }

    public static Pecas getPecas(int id) throws Exception {
        Pecas pecas = null;
        Connection con = AppListener.getConnection();
        String sql = "SELECT rowid, * FROM pecas WHERE rowid=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            long rowId = rs.getLong("rowid");
            String nomePeca = rs.getString("nomePeca");
            double preco = rs.getDouble("preco");
            long modeloCarroRowId = rs.getLong("modeloCarro_rowId");
         
            pecas = new Pecas(rowId, nomePeca, preco, modeloCarroRowId);
        }
        rs.close();
        stmt.close();
        con.close();
        return pecas;
    }

    private static ModeloCarro getModeloCarro(long modeloCarroRowId, Connection con) throws SQLException {
        String sql = "SELECT rowid, * FROM modeloCarro WHERE rowid=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, modeloCarroRowId);

        ResultSet rs = stmt.executeQuery();
        ModeloCarro modeloCarro = null;
        if (rs.next()) {
            long rowId = rs.getLong("rowid");
            String nomeModelo = rs.getString("nomeModelo");
            Date ano = rs.getDate("ano");
            String marca = rs.getString("marca");
            modeloCarro = new ModeloCarro(rowId, nomeModelo, ano, marca);
        }
        rs.close();
        stmt.close();
        return modeloCarro;
    }

    public static void insertPecas(String nomePeca, double preco, long modeloCarroRowId) throws Exception {
        Connection con = AppListener.getConnection();
        String sql = "INSERT INTO Pecas(nomePeca, preco, modeloCarro_rowId) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, nomePeca);
        stmt.setDouble(2, preco);
        stmt.setLong(3, modeloCarroRowId);
        stmt.execute();
        stmt.close();
        con.close();
    }

    public static void updatePecas(long rowId, String nomePeca, double preco, long modeloCarroRowId) throws Exception {
    Connection con = AppListener.getConnection();
    String sql = "UPDATE Pecas SET nomePeca=?, preco=?, modeloCarro_rowId=? WHERE rowid=?";
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setString(1, nomePeca);
    stmt.setDouble(2, preco);
    stmt.setLong(3, modeloCarroRowId);
    stmt.setLong(4, rowId);
    stmt.executeUpdate();
    stmt.close();
    con.close();
}

    public static void deletePecas(long rowId) throws Exception {
    Connection con = AppListener.getConnection();
    String sql = "DELETE FROM Pecas WHERE rowid=?";
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setLong(1, rowId);
    stmt.executeUpdate();
    stmt.close();
    con.close();
}

    public Pecas(long rowId, String nomePeca, double preco, long modeloCarro) {
        this.rowId = rowId;
        this.nomePeca = nomePeca;
        this.preco = preco;
        this.modeloCarroRowId = modeloCarro;

    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getNomePeca() {
        return nomePeca;
    }

    public void setNomePeca(String nomePeca) {
        this.nomePeca = nomePeca;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    public long getModeloCarroRowId() {
        return modeloCarroRowId;
    }

    public void setModeloCarroRowId(long modeloCarroRowId) {
        this.modeloCarroRowId = modeloCarroRowId;
    }

}