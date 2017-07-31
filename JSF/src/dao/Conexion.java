package dao;

import java.sql.*;


public class Conexion {
	
	private static Connection conn = null;
	
	//VARIABLES SQL
	private static String className_SQL = "com.microsoft.sqlserver.jdbc.SQLServerDriver";	
	private static String URL_SQL = "jdbc:sqlserver://;DatabaseName=farmaVETBD";
	private static String USER_SQL = "sa";
	private static String PASSWORD_SQL = "sql";
	
	private static Conexion conexion = null;
	
	public static Conexion getInstancia(){
		if(conexion == null){
			conexion = new  Conexion();
		}
		return conexion;
	}
	
	private Conexion() {
		
	}
	
	public Connection getConexion(){
		
		try {
			if(conn == null){
				Class.forName(className_SQL);
				conn = DriverManager.getConnection(URL_SQL, USER_SQL, PASSWORD_SQL);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return conn;
	}
	
}