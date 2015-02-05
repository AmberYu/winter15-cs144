package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;

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
			int numResultsToReturn) {
        try{
            System.out.println("basicSearch begin----");
            IndexSearcher searcher = new IndexSearcher(System.getenv("LUCENE_INDEX") + "/directory");
            //DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/directory")))
            //create a QueryParser
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            //get a query
            Query que = parser.parse(query);
            //get all search result
            Hits hits = searcher.search(que);
            
            //System.out.println("the number of search result:"+hits.length());
            
            
            //if the number to skip is larger than the number of search results, then we return an empty set of result
            int len=0;
            if(numResultsToSkip>=hits.length())
                return new SearchResult[0];
            //otherwise, we get the number of results required to be returned
            else if(numResultsToReturn==0)
                len = hits.length()-numResultsToSkip;
            else len = Math.min(hits.length(),numResultsToSkip+numResultsToReturn)-numResultsToSkip;
            SearchResult[] result = new SearchResult[len];
            
            for(int i=0;i<result.length;i++){
                Document doc = hits.doc(i+numResultsToSkip);
                String itemID = doc.get("itemID");
                String name = doc.get("name");
                SearchResult s = new SearchResult();
                s.setItemId(itemID);
                s.setName(name);
                result[i] = s;
            }
            return result;
        }
        
		// TODO: Your code here!
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return new SearchResult[0];
        }

	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
