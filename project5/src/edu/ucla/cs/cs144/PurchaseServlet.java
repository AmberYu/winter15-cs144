package edu.ucla.cs.cs144;

import java.util.*;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PurchaseServlet extends HttpServlet implements Servlet {
	public PurchaseServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(true);
		String itemID = (String)session.getAttribute("itemID");
        String name = (String)session.getAttribute("Name");
        String buyPrice = (String)session.getAttribute("Buy_Price");		        

		request.setAttribute("itemID", itemID);
		request.setAttribute("Name", name);
		request.setAttribute("Buy_Price", buyPrice);		        
                
        request.getRequestDispatcher("/WEB-INF/pay.jsp").forward(request, response);
	}
}