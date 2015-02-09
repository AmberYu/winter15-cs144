package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException{
    	if(indexWriter == null){
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/directory"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            //config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(indexDir, config);
    	}
    	return indexWriter;
    }
   
    public void closeIndexWriter() throws IOException{
    	if(indexWriter != null){
    		indexWriter.close();
    	}
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
        getIndexWriter(true);
	    //Create Lucene Index
        /*Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/directory"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        indexWriter = new IndexWriter(indexDir, config);*/
        IndexWriter writer=getIndexWriter(false);
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
            doc.add(new StringField("ItemID",itemID1,Field.Store.YES));
            doc.add(new StringField("Name",name,Field.Store.YES));
            doc.add(new TextField("Description",description,Field.Store.YES));
            doc.add(new StringField("Category",category,Field.Store.YES));
            String content=name+" "+category+" "+description;
            //doc.add(new TextField("content",content,Field.Store.NO));
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
