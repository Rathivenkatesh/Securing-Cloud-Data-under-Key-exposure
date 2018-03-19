package com.TMACS.conn;

import java.io.InputStream;
import java.util.*;  

import javax.mail.*;  
import javax.mail.internet.*;  
import javax.activation.*; 

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class dataconncationlayer {
	public static String  email;
	
	public static boolean Register(String name,String lname,String email1,String password,String role,String ph) throws ClassNotFoundException, SQLException{
		Boolean a;
	    String ssuuid = UUID.randomUUID().toString();
	    String suuid=ssuuid.substring(0,4);
	    System.out.println(suuid);
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		int i=st.executeUpdate("Insert into register values('"+name+"','"+lname+"','"+email1+"','"+password+"','"+role+"','"+ph+"','"+suuid+"')");
		if(i>0){
			mail(email1, "your public key is"+suuid);
			a=true;
			
		}else{
			a=false;
		}
		con.close();
		return a;
	}
	static int  rad(){
		Random a=new Random();
		return a.nextInt(10000000);
	}
	public static boolean login(String email1,String password,String type) throws ClassNotFoundException, SQLException{
		Boolean a;
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		String att="Email_id";
		
		ResultSet rs=st.executeQuery("select Email_id from register where  "+att+"='"+email1+"' and Password='"+password+"' and Role='"+type+"'");
		if(rs.next()){
			email=rs.getString(1);
			a=true;
		}else{
			a=false;
		}
		con.close();
		return a;
	
	}
	public static boolean  uploding(InputStream file,String name) throws ClassNotFoundException, SQLException{
		boolean b;
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		String sql = "INSERT INTO contacts (Email_id, File_name, File_Key,File) values (?, ?, ?,?)";
		PreparedStatement statement = con.prepareStatement(sql);
		statement.setString(1, email);
		statement.setString(2, name);
		statement.setString(3, "Tpfsdfdsfds4545");
		statement.setBlob(4, file);
		int row = statement.executeUpdate();
		if (row > 0) {
			b=true;
		}else{
			b=false;
		}
		con.close();
		return b;
	}
	public static ArrayList getlistoffiles() throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		String sql="select * from File";
		ResultSet s=st.executeQuery(sql);
		ResultSetMetaData rsmd=s.getMetaData();
		ArrayList<listoffiles> list=new ArrayList<listoffiles>(); 
		 listoffiles li=new listoffiles();
		 li.setId(rsmd.getColumnName(1).replaceAll("_"," "));
		 li.setEmail(rsmd.getColumnName(2).replace("_"," "));
		 li.setFileName(rsmd.getColumnName(3).replace("_"," "));
		 li.setFile_key(rsmd.getColumnName(4).replace("_"," "));
		
		 list.add(li);
		 if(s.next()){
			 do{
				 listoffiles li1=new listoffiles();
				 li1.setId(s.getString(1));
				 li1.setEmail(s.getString(2));
				 li1.setFileName(s.getString(3));
				 li1.setFile_key(s.getString(4));
				 list.add(li1); 
			 }while(s.next());
		 }
		 con.close();
		return list;
		
	}
	public static ArrayList<listofuser> getusers() throws ClassNotFoundException, SQLException{
		
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		String sql="select * from register";
		ResultSet s=st.executeQuery(sql);
		ResultSetMetaData rsmd=s.getMetaData();
		ArrayList<listofuser> list=new ArrayList<listofuser>(); 
		listofuser li=new listofuser();
		li.setName(rsmd.getColumnName(1).replaceAll("_"," "));
		 li.setLast(rsmd.getColumnName(2).replace("_"," "));
		 li.setEmail(rsmd.getColumnName(3).replace("_"," "));
		 li.setRole(rsmd.getColumnName(4).replace("_"," "));
		 li.setPh(rsmd.getColumnName(5).replace("_"," "));
		list.add(li);
		 if(s.next()){
			 do{
				 listofuser li1=new listofuser();
				 li1.setName(s.getString(1));
				 li1.setLast(s.getString(2));
				 li1.setEmail(s.getString(3));
				 li1.setRole(s.getString(4));
				 li1.setPh(s.getString(5));
				list.add(li1); 
			 }while(s.next());
		 }
		 con.close();
		return list;
		
	}
	public static ArrayList requst() throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		Statement st1=con.createStatement();
		System.out.println(email);
		ResultSet rs=st.executeQuery("select * from file");
		ResultSetMetaData rsmd=rs.getMetaData();
		ArrayList<listoffiles> list=new ArrayList<listoffiles>(); 
		 listoffiles li=new listoffiles();
		 li.setId(rsmd.getColumnName(1).replaceAll("_"," "));
		 li.setEmail(rsmd.getColumnName(2).replace("_"," "));
		 li.setFileName(rsmd.getColumnName(3).replace("_"," "));
		 li.setFile_key(rsmd.getColumnName(4).replace("_"," "));
		 list.add(li);
		 if(rs.next()){
			 do{
				 ResultSet rs1=st1.executeQuery("select * from filerequst where file_id='"+rs.getString(1)+"' and Email_id='"+email+"'");
				 if(!rs1.next()){
				 listoffiles li1=new listoffiles();
				 li1.setId(rs.getString(1));
				 li1.setEmail(rs.getString(2));
				 li1.setFileName(rs.getString(3));
				 li1.setFile_key(rs.getString(4));
				 list.add(li1); 
				 }
			 }while(rs.next());
		 }
		 con.close();
		return list;
	}
	public static void mail(String email,String value){
		String text ="you id is "+value+" ";
    	String to=email;//change accordingly

     	Properties props = new Properties();
     	  props.put("mail.smtp.host", "smtp.gmail.com");
     	  props.put("mail.smtp.socketFactory.port", "465");
     	  props.put("mail.smtp.socketFactory.class",
     	        	"javax.net.ssl.SSLSocketFactory");
     	  props.put("mail.smtp.auth", "true");
     	  props.put("mail.smtp.port", "465");
     	 
     	  Session ses = Session.getInstance(props,new javax.mail.Authenticator() {
     	   protected PasswordAuthentication getPasswordAuthentication() {
     	   return new PasswordAuthentication("cvsr.1993@gmail.com","Venkat@89");//change accordingly
     	   }
     	  });
     	 
     	//compose message
     	  try {
     	   MimeMessage message = new MimeMessage(ses);
     	   message.setFrom(new InternetAddress("cvsr.1993@gmail.com"));//change accordingly
     	   message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
     	   message.setSubject("Hello");
     	   message.setText(text);
     	   
     	   //send message
     	   Transport.send(message);

     	   System.out.println("message sent successfully");
     	 
     	  } catch (MessagingException e) {
     		  
     	  }
		
	}
	public static void requestkey(String id) throws ClassNotFoundException, SQLException{
	
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		int a1=st.executeUpdate("insert into filerequst values ('"+id+"','"+email+"','request')");
		con.close();
	}
	public static ArrayList getrequst() throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		ResultSet s=st.executeQuery("SELECT file.File_id,file.File_name,file.File_Key,filerequst.Email_id from file,filerequst where file.File_id=filerequst.file_id and filerequst.valueofkey='request';");
		ArrayList<listoffiles> list=new ArrayList<listoffiles>(); 
		ResultSetMetaData rsmd=s.getMetaData();
		 listoffiles li=new listoffiles();
		 li.setId(rsmd.getColumnName(1).replaceAll("_"," "));
		 li.setEmail(rsmd.getColumnName(4).replace("_"," "));
		 li.setFileName(rsmd.getColumnName(2).replace("_"," "));
		 li.setFile_key(rsmd.getColumnName(3).replace("_"," "));
		
		 list.add(li);
		 if(s.next()){
			 do{
				 listoffiles li1=new listoffiles();
				 li1.setId(s.getString(1));
				 li1.setEmail(s.getString(4));
				 li1.setFileName(s.getString(2));
				 li1.setFile_key(s.getString(3));
				 list.add(li1); 
			 }while(s.next());
		 }
		 con.close();
		return list;
		
	}
	public static void updatekey(String id,String email) throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		ResultSet rs=st.executeQuery("select key1,key2,key3,key4 from file where File_id='"+id+"'");
		rs.next();
		Random r5=new Random();
		String s=rs.getString(1+r5.nextInt(4));
		int a=st.executeUpdate("update filerequst set valueofkey='"+s+"' where Email_id='"+email+"' and file_id='"+id+"'");
		con.close();
		mail(email, s+"");
	}
	
	public static Map getusergraph() throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		TreeMap<String,Integer> user=new TreeMap<String,Integer>();
		ResultSet rs=st.executeQuery("select Role,count(*) from register GROUP by Role");
		while(rs.next()){
			user.put(rs.getString(1),rs.getInt(2));
		}
		con.close();
		return user;
	}
	public static Map getfilegraph() throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		TreeMap<String,Integer> file=new TreeMap<String,Integer>();
		ResultSet rs=st.executeQuery("select Email_id,count(*) from file GROUP by Email_id");
		while(rs.next()){
			file.put(rs.getString(1),rs.getInt(2));
		}
		con.close();
		return file;                                                                                                              
	}
	public static void createkeys() throws ClassNotFoundException, SQLException{
		Connection con=conn.getconn();
		Statement st=con.createStatement();
		
	}

}
