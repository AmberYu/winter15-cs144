/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

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


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static HashSet<String> userIDs = new HashSet<String>();
    static PrintWriter itemFile;
    static PrintWriter itemcategoryFile;
    static PrintWriter userFile;
    static PrintWriter bidFile;
    static int bidID = 0;
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     * @param e[IN] element node
     * @param tagName[IN] the name of a tag
     * @return Element[]. an array of element nodes
     */
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
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
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
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        //get root element, in this case, root is items
        Element root = doc.getDocumentElement();
        //get multiple item element nodes
        Element item[] = getElementsByTagNameNR(root,"item");
        //for each item, retrieve the data stored in XML DOM and store it into corresponding table
        try{
            for(int i=0;i<item.length;i++)
            {
                parseItem(item[i]);
                parseUser(item[i]);
                parseItemCategory(item[i]);
                parseBid(item[i]);
            }
        }
        catch(ParseException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        
        /**************************************************************/
        
    }
    /* Parse from the XML DOM tree, and get nodes information about itemID(key), userID, Name, Currently, First_Bid, Number_of_Bids, Buy_Price, Started, Ends, and Description.
     
     * @param input the root element
     */
    public static void parseItem(Element item) throws ParseException{
        String itemID = item.getAttribute("ItemID");
        itemFile.append(itemID + columnSeparator);
        
        Element seller = getElementByTagNameNR(item,"Seller");
        String userID = seller.getAttribute("UserID");
        itemFile.append(userID + columnSeparator);
        
        String name = getElementTextByTagNameNR(item, "Name");
        itemFile.append(name + columnSeparator);
        
        String currently = strip(getElementTextByTagNameNR(item,"Currently"));
        itemFile.append(currently + columnSeparator);
        
        String first_Bid = strip(getElementTextByTagNameNR(item,"First_Bid"));
        itemFile.append(first_Bid + columnSeparator);
        
        String bid_Num = strip(getElementTextByTagNameNR(item,"Number_of_Bids"));
        itemFile.append(bid_Num + columnSeparator);
        
        String buy_Price = strip(getElementTextByTagNameNR(item,"Buy_Price"));
        itemFile.append(buy_Price + columnSeparator);
        
        SimpleDateFormat input = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat output =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String started = getElementTextByTagNameNR(item,"Started");
        Date startTime = input.parse(started);
        itemFile.append(output.format(startTime) + columnSeparator);
        
        String ends = getElementTextByTagNameNR(item,"Ends");
        Date endTime = input.parse(ends);
        itemFile.append(output.format(endTime) + columnSeparator);
        
        String description = getElementTextByTagNameNR(item, "Description");
        if(description.length() > 4000)
            description = description.substring(0, 4000);
        itemFile.append(description);
        
            itemFile.append("\n");
    }
    
    /* Parse from the XML DOM tree, and get nodes information about userID(key), rating, location, and country.
     *
     * @param input the root element
     */
    public static void parseUser(Element item) throws IOException{
        Element seller = getElementByTagNameNR(item,"Seller");
        String userID = seller.getAttribute("UserID");
        boolean isDuplicate = userIDs.add(userID);
        String rating = seller.getAttribute("Rating");
        String location = getElementText(getElementByTagNameNR(item,"Location"));
        String country = getElementText(getElementByTagNameNR(item,"Country"));
        if(location==null)
            location = "";
        if(country==null)
            country = "";
        //Check whether the user has been stored in the table or not
        if(isDuplicate)
        {
            userFile.append(userID + columnSeparator + rating + columnSeparator + location + columnSeparator + country);
            userFile.append("\n");
        }
        
        Element Bids = getElementByTagNameNR(item,"Bids");
        Element bid[] = getElementsByTagNameNR(item,"Bid");
        for(int i =0;i<bid.length;i++)
        {
            Element bidder = getElementByTagNameNR(bid[i],"Bidder");
            String biduserID = bidder.getAttribute("UserID");
            boolean bidisDuplicate = userIDs.add(biduserID);
            String bidrating = bidder.getAttribute("Rating");
            String bidlocation = getElementTextByTagNameNR(bidder,"Location");
            String bidcountry = getElementTextByTagNameNR(bidder,"Country");
            if(bidlocation == null)
                bidlocation = "";
            if(bidcountry == null)
                bidcountry = "";
            if(bidisDuplicate)
            {
                userFile.append(biduserID + columnSeparator + bidrating + columnSeparator + bidlocation + columnSeparator + bidcountry);
                userFile.append("\n");
            }
        }
        
    }
    /* Parse from the XML DOM tree, and get nodes information about itemID(key), and category.
     *
     * @param input the root element
     */
    public static void parseItemCategory(Element item) throws IOException{
        String itemID = item.getAttribute("ItemID");
        Element Category[] = getElementsByTagNameNR(item,"Category");
        for(int i=0;i<Category.length;i++)
        {
            String cate = getElementText(Category[i]);
            itemcategoryFile.append(itemID + columnSeparator + cate);
            itemcategoryFile.append("\n");
        }
    }
    /* Parse from the XML DOM tree, and get nodes information about bidID(key), userID, itemID, time, and amount.
     *
     * @param input the root element
     */
    public static void parseBid(Element item) throws ParseException{
        Element Bids = getElementByTagNameNR(item,"Bids");
        Element bid[] = getElementsByTagNameNR(Bids,"Bid");
        String itemID = item.getAttribute("ItemID");
        for(int i=0;i<bid.length;i++)
        {
            Element bidder = getElementByTagNameNR(bid[i],"Bidder");
            String UserID = bidder.getAttribute("UserID");
            SimpleDateFormat input = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
            SimpleDateFormat output =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tempTime = getElementTextByTagNameNR(bid[i],"Time");
            Date Time = input.parse(tempTime);
            String amount = strip(getElementTextByTagNameNR(bid[i],"Amount"));
            
            bidFile.append(bidID++ + columnSeparator + UserID + columnSeparator + itemID +columnSeparator + output.format(Time) + columnSeparator + amount);
            bidFile.append("\n");
            
        }
    }

    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        try{
            //create four files corresponding to four tables
            itemFile = new PrintWriter(new FileWriter("item.csv",true));
            itemcategoryFile = new PrintWriter(new FileWriter("ItemCategory.csv",true));
            userFile = new PrintWriter(new FileWriter("Users.csv",true));
            bidFile = new PrintWriter(new FileWriter("Bids.csv",true));
            
            
            /* Process all files listed on command line. */
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }
            //close files
            itemFile.close();
            itemcategoryFile.close();
            userFile.close();
            bidFile.close();
            
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(5);
        }

        
    }
}
