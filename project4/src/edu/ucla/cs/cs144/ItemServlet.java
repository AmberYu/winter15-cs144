package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        //get the itemID contained in the request
        String itemID = request.getParameter("itemID");
        //get the itemXML by the itemID
        String itemXMLResult = AuctionSearchClient.getXMLDataForItemId(itemID);
        
        request.setAttribute("itemXMLResult",itemXMLResult);

        request.setAttribute("title", "Item ID Search Result");


        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/item.jsp");
        dispatcher.forward(request,response);
        
    }
}
