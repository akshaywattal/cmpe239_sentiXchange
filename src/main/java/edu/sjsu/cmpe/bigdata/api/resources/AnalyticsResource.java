package edu.sjsu.cmpe.bigdata.api.resources;

import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.bigdata.config.BigDataServiceConfiguration;
import edu.sjsu.cmpe.bigdata.dto.Tweets;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

/**
 * Main Analytics resource, contains API for all Big Data Analysis 
 */

@Path("/v1/analytics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalyticsResource {

	/**
	 * API to get result of sentiment analysis
	 * @throws IOException 
	 */
	@GET
    @Path("/sentiment")
    @Timed(name = "view-sentiment")
    public int viewSentiment(@QueryParam("keyword") String  keyword) throws IOException {
		int flag = 0;
		int finalScore = 0; 
		
		BigDataServiceConfiguration configuration = new BigDataServiceConfiguration(); 
	    List<String> twitterStreamingKewordsList = configuration.getStompQueueName();
	    //Creating HBase configuration
	    Configuration conf = HBaseConfiguration.create();
	    
	    for(String key:twitterStreamingKewordsList) {
             if  (keyword.equals(key)) { 
            	 //Fetch from Streaming API table
            	 HTable table = new HTable(conf, "streamingSentimentAnalysis");
            	 //Get Values
            	 Get get = new Get(Bytes.toBytes(keyword));
            	 try {
            		 Result result = table.get(get);
            		 ListIterator i = result.list().listIterator(result.size());
            		 int countValue = 0;
            		 int score = 0;
                     int positive = 0;
                     int negative = 0;
            		 while(i.hasPrevious() && countValue < 50 ) {
	            			 String tweetSentiment = Bytes.toString(((KeyValue) i.previous()).getValue());
	            			 System.out.println(tweetSentiment);
	            			 //if (Integer.parseInt(tweetSentiment) == 2);
	          	             if (Integer.parseInt(tweetSentiment) < 2) {negative++;}
	          	             else if (Integer.parseInt(tweetSentiment) >= 2) {positive ++;}
	            			 System.out.println("POSITIVE: " + positive + ", " + "NEGATIVE: " + negative);
	                 			 
	            			 finalScore = positive-negative;

	            			 countValue ++;
            			 }           		 
            		 } catch (IOException e) {
            			 // TODO Auto-generated catch block
            			 e.printStackTrace();
            			 return finalScore;
            			 } 
            	 flag = 1;
            	 break;
            	 }	 
             }
	    //Fetch from Search API Database
	    if(flag ==0){
	    	//Fetch from Streaming API table
	    	HTable table = new HTable(conf, "searchAPISentimentAnalysis");
	    	//Get Values
       	 	Get get = new Get(Bytes.toBytes(keyword));
       	 	Result result = table.get(get);
       	 	ListIterator i = result.list().listIterator(result.size());
       	 	int countValue = 0;
       	 	while(i.hasPrevious() && countValue < 1 ) {
       	 		String tweetSentiment = Bytes.toString(((KeyValue) i.previous()).getValue());
       	 		System.out.println(tweetSentiment);
       	 		finalScore = Integer.parseInt(tweetSentiment);
       	 		countValue ++;
       	 		}
       	 	}
	    return finalScore;
	    }

	/**
	 * API to get result of sentiment analysis
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@POST
    @Path("/sentiment")
    @Timed(name = "get-sentiment")
    public String getSentiment(@QueryParam("keyword") String  keyword) throws InterruptedException, IOException {
		int flag = 0;
		BigDataServiceConfiguration configuration = new BigDataServiceConfiguration(); 
	    List<String> twitterStreamingKewordsList = configuration.getStompQueueName();
		
	    for(String key:twitterStreamingKewordsList)
             if  (keyword.equals(key)) {
            	 flag = 1;
            	 break;
            	 }
	    	if (flag == 0) {
	    		Configuration conf = HBaseConfiguration.create();
            	HTable table = new HTable(conf, "searchAPISentimentAnalysis");
            	Get get = new Get(Bytes.toBytes(keyword));
            	Result result = table.get(get);
            	if(result.size() == 0) {
            		Tweets twitterSearch = new Tweets();
            		twitterSearch.search(keyword);
            		}
	    	}
		return "Search Initiated...";
    }

}