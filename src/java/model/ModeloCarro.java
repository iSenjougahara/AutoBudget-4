package model;

import java.util.Date;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.sql.*;
import java.util.ArrayList;
import web.AppListener;

public class ModeloCarro {

    private long rowId;
    private String nomeModelo;
    private Date ano;
    private String marca;

    public ModeloCarro(long rowId, String nomeModelo, Date ano, String marca) {
        this.rowId = rowId;
        this.nomeModelo = nomeModelo;
        this.ano = ano;
        this.marca = marca;
    }

    // Getters and setters for the attributes
    public static String getCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS modeloCarro("
                + "nomeModelo VARCHAR(255),"
                + "ano DATE,"
                + "marca VARCHAR(255)"
                + ")";
    }
    
     public static void insertModeloCarro(String nomeModelo, Date ano, String marca) {
        try {
            Connection connection = AppListener.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO modeloCarro (nomeModelo, ano, marca) VALUES (?, ?, ?)"
            );
            statement.setString(1, nomeModelo);
            statement.setDate(2, new java.sql.Date(ano.getTime()));
            statement.setString(3, marca);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Handle any exceptions
        }
    }
     
    

   public static ArrayList<ModeloCarro> getModeloCarros() throws Exception {
    ArrayList<ModeloCarro> list = new ArrayList<>();
    Connection con = AppListener.getConnection();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT rowid, * from modeloCarro");
    while (rs.next()) {
        long rowId = rs.getLong("rowid");
        String nomeModelo = rs.getString("nomeModelo");
        Date ano = rs.getDate("ano");
        String marca = rs.getString("marca");
        ModeloCarro modeloCarro = new ModeloCarro(rowId, nomeModelo, ano, marca);
        list.add(modeloCarro);
    }
    rs.close();
    stmt.close();
    con.close();
    return list;
}


    // ...




    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public Date getAno() {
        return ano;
    }

    public void setAno(Date ano) {
        this.ano = ano;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
    

   
    public static void testGetModeloCarros() {
        try {
            ArrayList<ModeloCarro> modeloCarros = getModeloCarros();
            for (ModeloCarro modeloCarro : modeloCarros) {
                System.out.println("Row ID: " + modeloCarro.getRowId());
                System.out.println("Nome Modelo: " + modeloCarro.getNomeModelo());
                System.out.println("Ano: " + modeloCarro.getAno());
                System.out.println("Marca: " + modeloCarro.getMarca());
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    // Rest of the class code...



