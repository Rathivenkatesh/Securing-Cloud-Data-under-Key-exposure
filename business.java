package com.TMACS.conn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import sun.misc.Perf.GetPerfAction;

/**
 * Servlet implementation class business
 */
@WebServlet("/business")
@MultipartConfig(maxFileSize = 16177215)
public class business extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public business() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("table").equalsIgnoreCase("request")){
			try {
				dataconncationlayer.requestkey(request.getParameter("id"));
				response.sendRedirect("userhome.jsp");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(request.getParameter("table").equalsIgnoreCase("generate key")){
		try {
			dataconncationlayer.updatekey(request.getParameter("id"), request.getParameter("email"));
			response.sendRedirect("userhome.jsp");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		}
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("table").equalsIgnoreCase("Register")){
			try {
		boolean	b=dataconncationlayer.Register(request.getParameter("fn"),request.getParameter("ln"),request.getParameter("email"),request.getParameter("ps"),request.getParameter("type"),request.getParameter("ph"));
			if(b){
				response.sendRedirect("Register.jsp?mes=successfully registered");
			}else{
				response.sendRedirect("Register.jsp?mes=try some other time");
			}
			
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(request.getParameter("table").equalsIgnoreCase("certificate authority login") || request.getParameter("table").equalsIgnoreCase("Owner Login") || request.getParameter("table").equalsIgnoreCase("cloud user Login") || request.getParameter("table").equalsIgnoreCase("Cloude Login")  || request.getParameter("table").equalsIgnoreCase("Attribute authority login") ){
			
			if(request.getParameter("type").equalsIgnoreCase("ca") || request.getParameter("type").equalsIgnoreCase("cloude")){
				
				if(request.getParameter("email").equalsIgnoreCase("cloude@gmail.com") && request.getParameter("ps").equalsIgnoreCase("cloude")){
					response.sendRedirect("cloudehome.jsp");
				}else{
					if(request.getParameter("type").equalsIgnoreCase("cloude")){
						response.sendRedirect("cloude.jsp?mes=Invalied email id and password");
					}
				}
			}else{
				try {
				if(request.getParameter("type").equalsIgnoreCase("user")){
					if(dataconncationlayer.login(request.getParameter("email"),request.getParameter("ps"),request.getParameter("type"))){
						response.sendRedirect("userhome.jsp");
						}else{
							response.sendRedirect("user.jsp?mes=Invalied user name and password");
						}
				}else if(request.getParameter("type").equalsIgnoreCase("owner")){
					if(dataconncationlayer.login(request.getParameter("email"),request.getParameter("ps"),request.getParameter("type"))){
						response.sendRedirect("Ownerhome.jsp");	
					}else{
						response.sendRedirect("owner.jsp?mes=Invalied user name and password");
					}
				}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		if(request.getParameter("table").equalsIgnoreCase("downloading")){
			Connection con = null;
			try {
				con = conn.getconn();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Statement st = null;
			try {
				st = con.createStatement();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				ResultSet rs=st.executeQuery("select AES_DECRYPT(file,'Tpfsdfdsfds4545') as file from file,filerequst where file.File_id=filerequst.file_id and filerequst.valueofkey='"+request.getParameter("ps")+"' and filerequst.Email_id='"+dataconncationlayer.email+"';");
				if(rs.next()){
				response.setContentType("application/octet-stream");
		        response.setHeader("Content-Disposition","attachment;filename=test.txt");
		        ServletOutputStream out = response.getOutputStream();
				String sb = rs.getString(1);
				System.out.println(rs.getString(1));
				InputStream in =new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));

				byte[] outputByte = new byte[4096];
				//copy binary contect to output stream
				while(in.read(outputByte, 0, 4096) != -1)
				{
					out.write(outputByte, 0, 4096);
				}
				in.close();
				out.flush();
				out.close();
				response.sendRedirect("download.jsp");
				}else{
					response.sendRedirect("download.jsp?mes=Invalied key");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
