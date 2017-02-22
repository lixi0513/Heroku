package com.cisco.mediasense.serlvet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.cisco.mediasense_jsc.MediasenseConnector;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8889277478723266260L;
	/**
	 * Constructor of the object.
	 */
	private static Logger logger = Logger.getLogger(LoginServlet.class.getName());
	public LoginServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String username=request.getParameter("username");
		logger.info("capturing username");
		String password=request.getParameter("password");
		logger.info("capturing password");

		
		System.out.println("TRUSTSTORE LOCATION"  + System.getProperty("javax.net.ssl.trustStore"));
		
		if(doLogin(username, password)) {
			HttpSession session = request.getSession(true);
			session.setAttribute("LOGIN_RESULT", "PASS");
			
			session.setAttribute("USERNAME", username);
			session.setAttribute("PASSWORD", password);
			
			
			response.sendRedirect("../SearchPlay.html");	
			logger.info("login sucessful");
		} else {
			HttpSession session = request.getSession(true);
			session.setAttribute("LOGIN_RESULT", "FAIL");
			
			response.sendRedirect("../index.jsp");
			logger.info("login failed");
		}

	}

	
	public boolean doLogin(String username, String password) {
		MediasenseConnector mc=new MediasenseConnector();
	
		return mc.login(username, password);
	}
	
	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		
	}

}
