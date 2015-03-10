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
    private Bid[] Bids;
    private String Location;
    private String Country;
    private String Started;
    private String Ends;
    private String Seller_ID;
    private String Seller_Rating;
    private String Description;
    private String Latitude;
    private String Longitude;

    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        //get the itemID contained in the request
        String Error = "", itemID = "";
        if (request.getParameter("itemID") != null)
            itemID = request.getParameter("itemID");

        itemID = itemID.replaceAll("\\s+","");
        if(itemID.length()>=1){
            //get the itemXML by the itemID
            String itemXMLResult = AuctionSearchClient.getXMLDataForItemId(itemID);
            if(itemXMLResult!=null && itemXMLResult.length()>0){
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
                
                Element item = doc.getDocumentElement();
                
                if(item!=null && item.getNodeName().equals("Item")){
                    Element seller = getElementByTagNameNR(item,"Seller");
                    Seller_ID = seller.getAttribute("UserID");
                    Seller_Rating = seller.getAttribute("Rating");
                    Name = getElementTextByTagNameNR(item, "Name");
                    Currently = strip(getElementTextByTagNameNR(item,"Currently"));
                    First_Bid = strip(getElementTextByTagNameNR(item,"First_Bid"));
                    Number_of_Bids = getElementTextByTagNameNR(item,"Number_of_Bids");
                    Buy_Price =strip(getElementTextByTagNameNR(item, "Buy_Price"));
                    Location = getElementText(getElementByTagNameNR(item,"Location"));
                    Country = getElementText(getElementByTagNameNR(item,"Country"));
                    Element location_ele = getElementByTagNameNR(item, "Location");
                    Latitude = location_ele.getAttribute("Latitude");
                    Longitude = location_ele.getAttribute("Longitude");
                    
                    Started = getElementTextByTagNameNR(item,"Started");
                    Ends = getElementTextByTagNameNR(item,"Ends");
                    
                    Description = getElementTextByTagNameNR(item, "Description");
                    if(Description.length() > 4000)
                        Description = Description.substring(0, 4000);
                    
                    Element Category[] = getElementsByTagNameNR(item,"Category");
                    String cate = "";
                    for(int i=0;i<Category.length;i++)
                    {
                        cate += getElementText(Category[i])+columnSeparator;;
                    }
                    Categories = cate.split("\\|\\*\\|");
                    //parse Bids
                    Element bid[] = getElementsByTagNameNR(getElementByTagNameNR(item,"Bids"),"Bid");
                    Bids = new Bid[bid.length];
                    for(int i =0;i<bid.length;i++)
                    {
                        Bids[i] = new Bid();
                        Element bidder = getElementByTagNameNR(bid[i],"Bidder");
                        Bids[i].setBidder(bidder.getAttribute("UserID"));
                        Bids[i].setBidder_rating(bidder.getAttribute("Rating"));
                        Bids[i].setLocation(getElementTextByTagNameNR(bidder,"Location"));
                        Bids[i].setCountry(getElementTextByTagNameNR(bidder,"Country"));
                        Bids[i].setTime(getElementTextByTagNameNR(bid[i],"Time"));
                        Bids[i].setAmount(strip(getElementTextByTagNameNR(bid[i],"Amount")));
                    }
                    //sortBids();
                }
            }
            else{
                Error="The item whose ID is " + itemID + " does not exist, please try again.";
                itemID = "";
                request.setAttribute("Error", Error);
            }
        }
        else{
            Error="Search query can't be empty.";
            request.setAttribute("Error", Error);
        }
        
        /*else{
            ItemID=null;
        }*/
        request.setAttribute("itemID", itemID);
        request.setAttribute("Name", Name);
        request.setAttribute("Currently", Currently);
        request.setAttribute("Buy_Price", Buy_Price);
        request.setAttribute("First_Bid", First_Bid);
        request.setAttribute("Number_of_Bids", Number_of_Bids);
        request.setAttribute("Started", Started);
        request.setAttribute("Ends", Ends);
        request.setAttribute("Seller_ID", Seller_ID);
        request.setAttribute("Seller_Rating", Seller_Rating);
        request.setAttribute("Description", Description);
        request.setAttribute("Categories", Categories);
        request.setAttribute("Bids", Bids);
        request.setAttribute("Country", Country);
        request.setAttribute("Location", Location);
        request.setAttribute("Longitude", Longitude);
        request.setAttribute("Latitude", Latitude);
        request.setAttribute("title", "Item ID Search Result");

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
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                //if the node is an element node, transfer its type to element and store in the vector
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        //since we don't know the number of element nodes at first, we have to create a vecotr buffer to store result. After we store all elements into buffer, we can get the size of it so that we can establish an array
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
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
