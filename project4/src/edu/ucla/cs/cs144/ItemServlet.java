package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import java.io.StringReader;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}
    private final String columnSeparator = "|*|";
    
    //private String ItemID;
    private String Name;
    private String[] Categories;
    private String Currently;
    private String Buy_Price;//?
    private String First_Bid;
    private String Number_of_Bids;
    //private Bid[] bids;
    private String Location;
    private String Country;
    private String Started;
    private String Ends;
    private String Seller_UID;
    private String Seller_Rating;
    private String Description;
    //private String BidUID;
    //private String BidRating;
    //private String BidLocation;
    //private String BidCountry;
    
    //private String BidTime;
    //private String BidAmount;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        //get the itemID contained in the request
        String itemID = request.getParameter("itemID");
        //get the itemXML by the itemID
        String itemXMLResult = AuctionSearchClient.getXMLDataForItemId(itemID);
        
        Document doc=null;
        
        try {
            doc = loadXMLFromString(itemXMLResult);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
        }
        
        System.out.println("Successfully parsed!");
        
        Element item = doc.getDocumentElement();
        
        if(item!=null && item.getNodeName().equals("Item")){
            Element seller = getElementByTagNameNR(item,"Seller");
            Seller_UID = seller.getAttribute("UserID");
            Seller_Rating = seller.getAttribute("Rating");
            Name = getElementTextByTagNameNR(item, "Name");
            Currently = strip(getElementTextByTagNameNR(item,"Currently"));
            First_Bid = strip(getElementTextByTagNameNR(item,"First_Bid"));
            Number_of_Bids = getElementTextByTagNameNR(item,"Number_of_Bids");
            Buy_Price =strip(getElementTextByTagNameNR(item, "Buy_Price"));
            Location = getElementText(getElementByTagNameNR(item,"Location"));
            Country = getElementText(getElementByTagNameNR(item,"Country"));
            Element location_ele = getElementByTagNameNR(item, "Location");
            String latitude = location_ele.getAttribute("Latitude");
            String longitude = location_ele.getAttribute("Longitude");

            Started = getElementTextByTagNameNR(item,"Started");
            Ends = getElementTextByTagNameNR(item,"Ends");
            String Description = getElementTextByTagNameNR(item, "Description");
            if(Description.length() > 4000)
                Description = Description.substring(0, 4000);
            /*
            Element Bids = getElementByTagNameNR(item,"Bids");
            Element bid[] = getElementsByTagNameNR(Bids,"Bid");
            for(int i =0;i<bid.length;i++)
            {
                Element bidder = getElementByTagNameNR(bid[i],"Bidder");
                BidUID = bidder.getAttribute("UserID");
                BidRating = bidder.getAttribute("Rating");
                BidLocation = getElementTextByTagNameNR(bidder,"Location");
                BidCountry = getElementTextByTagNameNR(bidder,"Country");

                BidTime = getElementTextByTagNameNR(bid[i],"Time");
                BidAmount = strip(getElementTextByTagNameNR(bid[i],"Amount"));
            }*/
            //sortBids();
                
            
        }
        /*else{
            ItemID=null;
        }*/
        
        request.setAttribute("Name", Name);
        request.setAttribute("Currently", Currently);
        request.setAttribute("Buy_Price", Buy_Price);
        request.setAttribute("First_Bid", First_Bid);
        request.setAttribute("Number_of_Bids", Number_of_Bids);
        request.setAttribute("Started", Started);
        request.setAttribute("Ends", Ends);
        request.setAttribute("Seller_UID", Seller_UID);
        request.setAttribute("Seller_Rating", Seller_Rating);
        request.setAttribute("Description", Description);
        request.setAttribute("Categories", Categories);
        //request.setAttribute("BidUID", BidUID);
        //request.setAttribute("BidRating", BidRating);
        //request.setAttribute("BidLocation", BidLocation);
        //request.setAttribute("BidCountry", BidCountry);
        //request.setAttribute("BidTime", BidTime);
        //request.setAttribute("BidAmount", BidAmount);
        //ItemsBean ib = new ItemsBean(itemXMLResult);
        request.setAttribute("title", "Item ID Search Result");
        //request.setAttribute("itemBean", ib);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/item.jsp");
        dispatcher.forward(request,response);
        
    }
    //Initialize the parser
    public Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
    
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    private String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
}
