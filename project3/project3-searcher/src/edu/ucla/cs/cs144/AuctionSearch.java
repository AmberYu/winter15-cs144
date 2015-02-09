package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) 
    {
        try{
            System.out.println("basicSearch begin----");
            // instantiate the search engine
            SearchEngine se = new SearchEngine();
            
            // retrieve top 2000 matching document list for the query "Notre Dame museum"
            TopDocs topDocs = se.performSearch(query, 2000);
            
            // obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
            ScoreDoc[] hits = topDocs.scoreDocs;
            
            System.out.println("the number of search result:"+hits.length);
            
            
            //if the number to skip is larger than the number of search results, then we return an empty set of result
            int len = 0;
            if(numResultsToSkip >= hits.length)
                return new SearchResult[0];
            //otherwise, we get the number of results required to be returned
            else if(numResultsToReturn == 0)
                len = hits.length - numResultsToSkip;
            else 
                len = Math.min(hits.length,numResultsToSkip + numResultsToReturn) - numResultsToSkip;
            SearchResult[] result = new SearchResult[len];
            
            for(int i=0;i<result.length;i++){
                Document doc = se.getDocument(hits[i+numResultsToSkip].doc);
                //Document doc = hits.doc(i+numResultsToSkip);
                String itemID = doc.get("ItemID");
                String name = doc.get("Name");
                SearchResult s = new SearchResult();
                s.setItemId(itemID);
                s.setName(name);
                result[i] = s;
            }
            return result;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return new SearchResult[0];
        }

	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) 
    {
        SearchResult[] basicSearchResult = basicSearch(query, 0, Integer.MAX_VALUE);
        SearchResult[] spatialSearchResult = null;

        try {
            Connection connection = DbManager.getConnection(true);

            String pointLL = region.getLx() + " " + region.getLy();
            String pointRL = region.getRx() + " " + region.getLy();
            String pointRU = region.getRx() + " " + region.getRy();
            String pointLU = region.getLx() + " " + region.getRy();

            // get the itemId of items in the given region, and store them into hashset to make search more quick.
            String queryLocationString = "SELECT ItemID FROM Location WHERE MBRContains(GeomFromText('Polygon((" + pointLL + "," + pointRL + "," + pointRU + "," + pointLU + "," + pointLL + "))'), Coordinates)";
            PreparedStatement queryLocation = connection.prepareStatement(queryLocationString);
            ResultSet locationResult = queryLocation.executeQuery();
            HashSet<String> locationResultHash = new HashSet<String>();
            while (locationResult.next())
                locationResultHash.add(locationResult.getString("ItemID"));

            // compare to see what itemId of basic search result are in given region
            ArrayList<SearchResult> allResults = new ArrayList<SearchResult>();
            for (int i = 0; i < basicSearchResult.length; i++)
            {
                String itemId = basicSearchResult[i].getItemId();
                if (locationResultHash.contains(itemId))
                {
                    allResults.add(basicSearchResult[i]);
                }
            }

            // retrieve required number of results
            int length;
            if (allResults.size() < numResultsToReturn + numResultsToSkip)
                length = allResults.size() - numResultsToSkip;
            else
                length = numResultsToReturn;
            spatialSearchResult = new SearchResult[length];

            for (int i = 0; i < length; i++)
            {
                spatialSearchResult[i] = allResults.get(numResultsToSkip + i);
            }
            connection.close();

        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }

		return spatialSearchResult;
	}


    //TODO: escaping characters issue?

	public String getXMLDataForItemId(String itemId) 
    {
        String resultXML = "";

        try{
            Connection connection = DbManager.getConnection(true);

            // retrieve data of the current itemID
            PreparedStatement queryItemData = connection.prepareStatement("SELECT * FROM Item WHERE ItemID = ?");
            queryItemData.setString(1, itemId);
            ResultSet itemData = queryItemData.executeQuery();

            if (itemData.getRow() == 0)
                return "";

            // start concatenating result xml string
            resultXML = "<Item ItemID = \"" + itemId + "\">\n";
            resultXML += "<Name>" + itemData.getString("Name") + "</Name>\n";
            resultXML += getCategoryString(connection, itemId);
            resultXML += "<Currently>" + getCurrencyString(itemData.getFloat("Currently")) + "</Currently>\n";

            String buyPrice = "";
            try {
                buyPrice = getCurrencyString(itemData.getFloat("Buy_Price"));
            } catch (SQLException exception) { }
            if (buyPrice != "") resultXML += "<Buy_Price>" + buyPrice + "</Buy_Price>\n";

            resultXML += "<First_Bid>" + getCurrencyString(itemData.getFloat("First_Bid")) + "</First_Bid>\n";
            resultXML += "<Number_of_Bids>" + itemData.getString("Bid_Num") + "</Number_of_Bids>\n";
            resultXML += getBidString(connection, itemId);
            resultXML += getLocationString(itemData);
            resultXML += "<Country>" + itemData.getString("Country") + "</Country>\n";
            resultXML += "<Started>" + getTimeString(itemData.getTimestamp("Started").toString()) + "</Started>\n";
            resultXML += "<Ends>" + getTimeString(itemData.getTimestamp("Ends").toString()) + "</Ends>\n";
            resultXML += getSellerString(connection, itemData.getString("Seller"));
            resultXML += "<Description>" + itemData.getString("Description") + "</Description>\n";
            resultXML += "</Item>";

            connection.close();
        } catch (Exception exception){
            System.out.println(exception);
        }
        
        return resultXML;
	}
	
	public String echo(String message) {
		return message;
	}

    // help function: return the string for item categories
    private String getCategoryString(Connection connection, String itemId) throws SQLException
    {
        String categoryString = "";
        PreparedStatement queryCategoryData = connection.prepareStatement("SELECT * FROM ItemCategory WHERE ItemId = ?");
        queryCategoryData.setString(1, itemId);
        ResultSet categoryData = queryCategoryData.executeQuery();

        while (categoryData.getRow() != 0)
        {
            categoryString += "<Category>" + categoryData.getString("Category") + "</Category>\n";
        }
        return categoryString;
    }


    // help function: return the string for currency with $ sign and 2-digit decimal part
    private String getCurrencyString(float amount)
    {
        return String.format("$%.2f", amount);
    }

    // help function: return the complete string for bid information
    private String getBidString(Connection connection, String itemId) throws SQLException
    {        
        PreparedStatement queryBid = connection.prepareStatement("SELECT * FROM Bids WHERE ItemID = ?");
        queryBid.setString(1, itemId);
        ResultSet bidData = queryBid.executeQuery();

        if (bidData.getRow() == 0)
            return "<Bids />\n";

        String bidString = "<Bids>\n";
        while (bidData.next())
        {
            bidString += "<Bid>\n";

            // retrieve bidder information
            String bidderId = bidData.getString("Bidder");
            PreparedStatement queryBidder = connection.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
            queryBidder.setString(1, bidderId);
            ResultSet bidderData = queryBidder.executeQuery();
            if (bidderData.getRow() != 0)
            {
                bidString += "<Bidder Rating=\"" + bidderData.getString("BidRating") + "\" UserID=\"" + bidderId + "\">\n";

                // see if location and country info exist
                boolean hasLocation, hasCountry;
                String location = "", country = "";
                try{
                    location = bidderData.getString("Location");
                    hasLocation = true;
                } catch (SQLException e) {
                    hasLocation = false;
                }
                try {
                    country = bidderData.getString("Country");
                    hasCountry = true;
                } catch (SQLException e) {
                    hasCountry = false;
                }
                if (hasLocation) bidString += "<Location>" + location + "/<Location>\n";
                if (hasCountry) bidString += "<Country>" + country + "/<Country>\n";

                bidString += "</Bidder>\n";
            }

            bidString += "<Time>" + getTimeString(bidData.getTimestamp("Time").toString()) + "</Time>\n";
            bidString += "<Amount>" + getCurrencyString(bidData.getFloat("Amount")) + "</Amount>\n";
            bidString += "</Bid>\n";
        }

        bidString += "</Bids>\n";
        return bidString;
    }

    // help function: return the string for location, country, latitude and longitude (if exists)
    private String getLocationString(ResultSet itemData) throws SQLException
    {
        String locationString = "", latitude = "", longitude = "";
        boolean hasLatLon;
        try{
            latitude = itemData.getString("Latitude");
            longitude = itemData.getString("Longitude");
            hasLatLon = true;
        } catch (SQLException sqlException) {
            hasLatLon = false;
        }

        if (hasLatLon)
            locationString = "<Location Latitude=\"" + latitude + "\" Longitude=\"" + longitude + "\">";
        else
            locationString = "<Location>";

        return locationString + itemData.getString("Location") + "</Location>\n";
    }

    // help function: return the string with specific timestamp format
    private String getTimeString(String timeStamp)
    {
        SimpleDateFormat inputTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputTimeStamp = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        StringBuffer stringBuffer = new StringBuffer();

        try{
            Date parsedDate = inputTimeStamp.parse(timeStamp);
            return "" + outputTimeStamp.format(parsedDate);
        }
        catch (Exception exception){
            System.err.println("Timestamp parse error!");
            return "";
        }
    }

    // help function: return the string for seller information
    private String getSellerString(Connection connection, String sellerId) throws SQLException
    {
        PreparedStatement querySeller = connection.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
        querySeller.setString(1, sellerId);
        ResultSet sellerData = querySeller.executeQuery();

        if (sellerData.getRow() != 0)
            return "<Seller Rating=\"" + sellerData.getString("SellerRating") + "\" UserId=\"" + sellerId + "\" />\n";
        else
            return "";
    }

}
