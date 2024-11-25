package be.marain.dao;

import java.sql.*;

public class EcoleSkiConnection {
	protected static Connection instance = null;

	private EcoleSkiConnection() {
		try {
			// Charger le driver Oracle JDBC
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// Connexion à la base de données
			String url = "jdbc:oracle:thin:@//193.190.64.10:1522/xepdb1"; // Ajuste les paramètres à ta configuration
			String user = "STUDENT03_07";
			String password = "KiloWattHeure23";
			System.setProperty("oracle.jdbc.Trace", "true"); // Activation des traces JDBC pour debug
			instance = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (instance == null) {
			System.exit(0);
		}
	}

	public static Connection getInstance() {
		if (instance == null)
			new EcoleSkiConnection();

		return instance;
	}

}