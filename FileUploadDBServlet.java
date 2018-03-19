package com.TMACS.conn;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)	// upload file's size up to 16MB
public class FileUploadDBServlet extends HttpServlet {
	
	
protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// gets values of text fields
		
	InputStream inputStream = null;	// input stream of the upload file
		
		// obtains the upload file part in this multipart request
		Part filePart = request.getPart("photo");
		inputStream = filePart.getInputStream();
		
		
	try {  
			Connection con=conn.getconn();
			Statement st=con.createStatement();
			ResultSet rs=st.executeQuery("select * from register where Email_id='"+dataconncationlayer.email+"'");
			rs.next();
			String key=rs.getString(7);
			String sql = "INSERT INTO File(Email_id, File_name, File_Key,File) values (?, ?, ?,AES_ENCRYPT(?, 'Tpfsdfdsfds4545'))";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setString(1, dataconncationlayer.email);
			statement.setString(2, request.getParameter("firstName"));
			statement.setString(3, key);
			statement.setBlob(4, inputStream);
			int row = statement.executeUpdate();
			Statement st2=con.createStatement();
			ResultSet rs1=st2.executeQuery("select max(File_id) from file where Email_id='"+dataconncationlayer.email+"'");
			rs1.next();
			String fid=rs1.getString(1);
			System.out.println(fid);
			String ssuuid = UUID.randomUUID().toString();
		    String suuid=ssuuid.substring(0,4);
		    String ssuuid1 = UUID.randomUUID().toString();
		    String suuid1=ssuuid1.substring(0,4);
		    String ssuuid2 = UUID.randomUUID().toString();
		    String suuid2=ssuuid2.substring(0,4);
		    String ssuuid3 = UUID.randomUUID().toString();
		    String suuid3=ssuuid3.substring(0,4);
			PreparedStatement st1 = con.prepareStatement("update file set key1=?,key2=?,key3=?,key4=? where File_id=?");
			st1.setString(1, suuid);
			st1.setString(2, suuid1);
			st1.setString(3, suuid2);
			st1.setString(4, suuid3);
			st1.setString(5, fid);
			st1.executeUpdate();
			if (row > 0) {
				response.sendRedirect("Ownerhome.jsp?mes=completed");
			}else{
				response.sendRedirect("Ownerhome.jsp?mes=plz Try again");
			}
			con.close();
			
		} catch (SQLException ex) {
			
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}