package edu.ucla.cs.cs144;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException{
    	if(indexWriter == null){
    		String directory = System.getenv("LUCENE_INDEX")+"/directory";
    		indexWriter = new IndexWriter(directory,new StandardAnalyzer(),create);
    	}
    	return indexWriter;
    }
   
    public void closeIndexWriter() throws IOException{
    	if(indexWriter != null){
    		indexWriter.close();
    	}
    }
    

    public void rebuildIndexes(){

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	    
	  //Read data from database
		Statement stmt1 = conn.createStatement();
		Statement stmt2 = conn.createStatement();
        //Execute SELECT Statement
		String query1="SELECT ItemID, Name, Description FROM Item";
		ResultSet ItemRS=stmt1.executeQuery(query1);
        
		String query2="SELECT * FROM ItemCategory";
		ResultSet CateRS=stmt2.executeQuery(query2);
	    //Create Lucene Index
		IndexWriter writer=getIndexWriter(true);
        //Get objects from ResultSet and add them to Index
        while(ItemRS.next()){
            
            String itemID1=ItemRS.getString("ItemID");
            String name=ItemRS.getString("Name");
            String description=ItemRS.getString("Description");
            
            String category="";
            while(CateRS.next()){
                String itemID2=CateRS.getString("ItemID");
                if(itemID1.compareTo(itemID2)==0){
                    category += (CateRS.getString("Category")+" ");
                }
                else{
                    CateRS.previous();
                    break;
                }
            }
            
            Document doc = new Document();
            doc.add(new Field("itemID",itemID1,Field.Store.YES,Field.Index.NO));
            doc.add(new Field("name",name,Field.Store.YES,Field.Index.TOKENIZED));
            doc.add(new Field("description",description,Field.Store.YES,Field.Index.TOKENIZED));
            doc.add(new Field("category",category,Field.Store.YES,Field.Index.TOKENIZED));
            String content=name+" "+category+" "+description;
            doc.add(new Field("content",content,Field.Store.NO,Field.Index.TOKENIZED));
            writer.addDocument(doc);
        }
		
		closeIndexWriter();
	
	}
	catch(IOException e){
	    System.out.println("IOException : " + e.getMessage());
	}
    catch (SQLException ex) {
        System.err.println(ex.getMessage());
        System.out.println("SQLException: " + ex);
            
    }

	
	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
	
	
        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) throws SQLException {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
