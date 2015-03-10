package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        response.setContentType("text/xml");
        //get query
        String q = request.getParameter("q");
        //URL encoding of query string parameters
        URL url = new URL("http://google.com/complete/search?output=toolbar&q="+URLEncoder.encode(q, "UTF-8"));
        
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        String suggest = "";
        while((inputLine = in.readLine())!=null){
            suggest += inputLine;
        }
        
        in.close();
        connection.disconnect();
        
        response.getWriter().println(suggest);
    }
}
