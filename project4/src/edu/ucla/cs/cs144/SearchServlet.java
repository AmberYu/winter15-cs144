package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        //get the parameters contained in this request
        String content = "";
        content = request.getParameter("content");
        int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
        
        SearchResult[] results = AuctionSearchClient.basicSearch(content, 0, 0);
        request.setAttribute("lengthOfAllResults",results.length);
        //get itemID and itemName from the searchresult
        String[] itemIDs = new String[0];
        String[] itemNames = new String[0];
        if(content.length()>=1){
            SearchResult[] s = AuctionSearchClient.basicSearch(content, numResultsToSkip, numResultsToReturn);
            int lengthOfResult = s.length;
            if(lengthOfResult>0){
                itemIDs = new String[lengthOfResult];
                itemNames = new String[lengthOfResult];
                for(int i=0;i<lengthOfResult;i++){
                    itemIDs[i] = s[i].getItemId();
                    itemNames[i] = s[i].getName();
                }
            }
            else{
                String error = "No Matching Result!";
                request.setAttribute("Error",error);
            }
        }
        request.setAttribute("title", "Basic Search Result");
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);
        request.setAttribute("content", content);
        request.setAttribute("itemIDs", itemIDs);
        request.setAttribute("itemNames", itemNames);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/search.jsp");
        dispatcher.forward(request,response);
        
    }
}
