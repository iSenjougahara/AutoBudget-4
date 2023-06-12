package web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.Date;
import java.sql.*;
import java.security.*;
import java.math.*;
import model.User;
import model.Estoque;
import model.ModeloCarro;
import model.Pecas;

@WebListener
public class AppListener implements ServletContextListener {
    public static final String CLASS_NAME = "org.sqlite.JDBC";
    public static final String URL = "jdbc:sqlite:parkapp.db";
    public static String initializeLog = "";
    public static Exception exception = null;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
             
             Connection c = AppListener.getConnection();
            Statement s = c.createStatement();
            initializeLog += new Date() + ": Initializing database creation; ";
        
            //s.execute("DROP TABLE Pecas");
             //s.execute("DROP TABLE modeloCarro");
            initializeLog += new Date() + ": Initializing database creation; ";
            
            //USERS
            initializeLog += "Creating Users table if not exists...";
            s.execute(User.getCreateStatement());
            initializeLog += "done; ";
            
            if(User.getUsers().isEmpty())
            {
                initializeLog += "Adding default users...";
                User.insertUser("admin", "Administrador", "ADMIN", "1234");
                initializeLog += "Admin added; ";
                User.insertUser("Vendedor", "Vendedor1", "Vendedor", "1234");
                initializeLog += "Vendedor added; ";
            }
            
            // Estoque
            initializeLog += "Creating Estoque table...";
            s.execute(Estoque.getCreateStatement());
        
            // Pecas
            initializeLog += "Creating Pecas table...";
            s.execute(Pecas.getCreateStatement());
            initializeLog += "done; ";
        
            // ModeloCarro
            initializeLog += "Creating ModeloCarro table...";
            s.execute(ModeloCarro.getCreateStatement());
            initializeLog += "done; ";
            if (Pecas.getPecas().isEmpty()) {
    initializeLog += "Adding default Pecas...";
    Pecas.insertPecas("Spark Plug", 10.0, 1);
    Pecas.insertPecas("Brake Pad", 20.0, 1);
    Pecas.insertPecas("Oil Filter", 30.0, 2);
    Pecas.insertPecas("Battery", 50.0, 2);
    Pecas.insertPecas("Tire", 100.0, 3);
    Pecas.insertPecas("Air Filter", 15.0, 3);
    Pecas.insertPecas("Radiator", 80.0, 3);
    Pecas.insertPecas("Alternator", 120.0, 4);
    Pecas.insertPecas("Starter Motor", 90.0, 4);
    Pecas.insertPecas("Fuel Pump", 70.0, 4);
    initializeLog += "Pecas added; ";
}

if (ModeloCarro.getModeloCarros().isEmpty()) {
    initializeLog += "Adding default ModeloCarros...";
    ModeloCarro.insertModeloCarro("Sedan", new Date(), "Toyota");
    ModeloCarro.insertModeloCarro("SUV", new Date(), "Ford");
    ModeloCarro.insertModeloCarro("Hatchback", new Date(), "Volkswagen");
    ModeloCarro.insertModeloCarro("Pickup Truck", new Date(), "Chevrolet");
    initializeLog += "ModeloCarros added; ";
}
            initializeLog += "Fetching recently inserted data from Pecas table...\n";

                try 
                {
                    Connection con = AppListener.getConnection();
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM Pecas");

                    initializeLog += "Recently inserted data:\n";

                    while (rs.next()) {
                    String nomePeca = rs.getString("nomePeca");
                    double preco = rs.getDouble("preco");
                    long modeloCarroRowId = rs.getLong("modeloCarro_rowId");

                    initializeLog += "Nome da Peça: " + nomePeca + ", Preço: " + preco + ", Modelo Carro RowId: " + modeloCarroRowId + "\n";
                }

                rs.close();
                stmt.close();
                con.close();
        
                } catch (Exception ex) {
                    initializeLog += "Error fetching data: " + ex.getMessage();
                }

            initializeLog += "Data fetch completed.\n";
        
            s.close();
            c.close();
        } 
        catch (Exception ex) 
        {
            initializeLog += "Error: " + ex.getMessage();
        }
         
    }

    public static String getMd5Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(text.getBytes(), 0, text.length());
        return new BigInteger(1, m.digest()).toString();
    }

    public static Connection getConnection() throws Exception {
        Class.forName(CLASS_NAME);
        return DriverManager.getConnection(URL);
    }
    
}
     
    
    

