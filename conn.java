package com.TMACS.conn;

import java.sql.*;
public class conn {
	
public static Connection getconn() throws SQLException, ClassNotFoundException{
		
		Class.forName("com.mysql.jdbc.Driver");
		  Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/tmacs","root","root");
		 return con; 		
	}
	
}