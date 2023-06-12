/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import web.AppListener;
import java.util.Calendar;
import java.util.Date;
import model.Pecas;

/**
 *
 * @author Gabriel
 */
public class Estoque {
    
    private long rowId;
    private String cdpeca;
    private String nomepeca;
    private String filial;
    private Integer quantidade;
    private Date create_date;

    public static String getCreateStatement() {
        return "CREATE TABLE IF NOT EXISTS estoque("
                + "cdpeca VARCHAR(50)  NOT NULL,"
                + "nomepeca VARCHAR(50)  ,"
                + "filial VARCHAR(50) NOT NULL,"
                + "quantidade INT NOT NULL,"
                + "create_date datetime NOT NULL"
                + ")";
    }

    public static ArrayList<Estoque> getEstoques() throws Exception{
        ArrayList<Estoque> list = new ArrayList<>();
        Connection con = AppListener.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rowid, * from estoque");
        
        while(rs.next()){
            long rowId = rs.getLong("rowid");
            String cdpeca = rs.getString("cdpeca");
            String nomepeca = rs.getString("nomepeca");
            String filial = rs.getString("filial");
            Integer quantidade = rs.getInt("quantidade");
            Date create_date = rs.getTime("create_date");
            list.add(new Estoque(rowId, cdpeca, nomepeca, filial, quantidade, create_date));
        }
        rs.close();
        stmt.close();
        con.close();
        return list;
    }
    
    public static Estoque getPecaEstoque(String cdpeca, String filial) throws Exception{
        Estoque pecaEstoque = null;
        Connection con = AppListener.getConnection();
        String sql = "SELECT * from estoque WHERE cdpeca=? AND filial=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, cdpeca);
        stmt.setString(2, filial);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            long rowId = rs.getLong("rowid");
            cdpeca = rs.getString("cdpeca");
            String nomepeca = rs.getString("nomepeca");
            filial = rs.getString("filial");
            Integer quantidade = rs.getInt("quantidade");
            Date create_date = rs.getTime("create_date");
            pecaEstoque = new Estoque(rowId, cdpeca, nomepeca, filial, quantidade, create_date);
        }
        rs.close();
        stmt.close();
        con.close();
        return pecaEstoque;
    }
    
    public static void insertEstoque(String cdpeca, String filial, Integer quantidade) throws Exception{
        
        String nmpeca = Estoque.findPeca(Long.parseLong(cdpeca));
        
        System.out.println("Valor de nmpeca: " + nmpeca);
        
        Connection con = AppListener.getConnection();
        
        
        String sql = "INSERT INTO estoque(cdpeca,nomepeca,filial,quantidade,create_date) "
                + "VALUES(?,?,?,?,?)";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, cdpeca);
        if (nmpeca == null) {
        nmpeca = "";
        stmt.setString(2, nmpeca);  // Valor padrão caso nmpeca seja nulo
        }
        stmt.setString(2, nmpeca); 
        stmt.setString(3, filial);
        stmt.setInt(4,quantidade);
        stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
        stmt.execute();
        stmt.close();
        con.close();
        
        
    }
    
    public static void updateEstoque(long rowId, String cdpeca, String nomepeca, String filial, Integer quantidade) throws Exception{
        Connection con = AppListener.getConnection();
        String sql = "UPDATE estoque SET cdpeca=?, nomepeca=?, filial=?, quantidade=?, create_date=? WHERE rowid=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, cdpeca);
        stmt.setString(2, nomepeca);
        stmt.setString(3, filial);
        stmt.setInt(4, quantidade);
        stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
        stmt.execute();
        stmt.close();
        con.close();
    }
    
    public static void deleteEstoqueline(long rowId) throws Exception{
        Connection con = AppListener.getConnection();
        String sql = "DELETE FROM estoque WHERE rowid = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, rowId);
        stmt.execute();
        stmt.close();
        con.close();
    }
    
     public static String findPeca(Long cdpeca) throws Exception{
        
     
    String nmPeca = null;
    int intpeca = Math.toIntExact(cdpeca);
 
    Pecas peca = Pecas.getPecas(intpeca);
    
    if (peca != null) {
        nmPeca = peca.getNomePeca();
    }
    
    return nmPeca;
}
    
    
    
    public static void changeQuantidade(long rowid, String cdpeca, Integer quantidade) throws Exception{
        Connection con = AppListener.getConnection();
        String sql = "UPDATE estoque SET quantidade = ? WHERE rowid = ? and cdpeca=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, quantidade);
        stmt.setLong(2, rowid);
        stmt.setString(2, cdpeca);
        stmt.execute();
        stmt.close();
        con.close();
    }

    public Estoque(long rowId, String cdpeca, String nomepeca, String filial, Integer quantidade, Date create_date) {
        this.rowId = rowId;
        this.cdpeca = cdpeca;
        this.nomepeca = nomepeca;
        this.filial = filial;
        this.quantidade = quantidade;
        this.create_date = create_date;
    }

    
    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getPeca() {
        return cdpeca;
    }

    public void setPeca(String cdpeca) {
        this.cdpeca = cdpeca;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    
    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }
    
    
    public String getNomepeca() {
        return nomepeca;
    }

    public void setNomepeca(String nomepeca) {
        this.nomepeca = nomepeca;
    }
    
}
