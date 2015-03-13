package edu.ucla.cs.cs144;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

public class ConfirmationServlet extends HttpServlet implements Servlet {
	public ConfirmationServlet() {}

	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {        
        HttpSession session = request.getSession(true);
        String itemID = (String) session.getAttribute("itemID");
        String name = (String) session.getAttribute("Name");
        String buyPrice = (String) session.getAttribute("Buy_Price");
        String creditCardNumber = request.getParameter("creditCardNumber");

		request.setAttribute("itemID", itemID);
		request.setAttribute("Name", name);
		request.setAttribute("Buy_Price", buyPrice);		        
        request.setAttribute("creditCardNumber", creditCardNumber);

        // Get the current timestamp
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        String currentTimeStamp = dateFormat.format(date);
        request.setAttribute("currentTimeStamp", currentTimeStamp);
                
        request.getRequestDispatcher("/WEB-INF/confirm.jsp").forward(request, response);
    }    
}