package be.marain.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EcoleSkiConnection {
	private static Connection instance = null;
	
	private EcoleSkiConnection() {
		 try {
	           // Charger le driver Oracle JDBC
	           Class.forName("oracle.jdbc.driver.OracleDriver");
	           
	           // Connexion à la base de données
	           String url = "jdbc:oracle:thin:@//193.190.64.10:1522/xepdb1"; // Ajuste les paramètres à ta configuration
	           String user = "STUDENT03_07";
	           String password = "KiloWattHeure23";
	           System.setProperty("oracle.jdbc.Trace", "true"); //Activation des traces JDBC pour debug
	           instance = DriverManager.getConnection(url, user, password);
	           System.out.println("Connexion réussie !");
	       }catch (ClassNotFoundException ex) {
	    	   ex.printStackTrace();
	       }catch (SQLException e) {
			e.printStackTrace();
	       }catch (Exception e) {
	           e.printStackTrace();
	       }
		 
		 if(instance == null) {
			 System.out.println("Instance nulle");
			 System.exit(0);
		 }
	}
   
   public static Connection getInstance() {
       if(instance == null)
    	   new EcoleSkiConnection();
       
       return instance;
   }

}