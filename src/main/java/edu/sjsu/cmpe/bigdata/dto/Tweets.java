package edu.sjsu.cmpe.bigdata.dto;

import edu.sjsu.cmpe.bigdata.config.BigDataServiceConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import twitter4j.*;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
/**
 * Created by shankey on 4/20/14.
 */
public class Tweets {
	Put p;
	public void searchStream(String twitterStreamingKewords) throws IOException {
		 	        TwitterFactory tf = new TwitterFactory();
		 	        Twitter twitter = tf.getInstance();
		 	        TwitterStreamFactory ts = new TwitterStreamFactory();
		 	        TwitterStream tsi = ts.getInstance();
		 	        
		 	        //Creating HBase configuration
		 	        Configuration config = HBaseConfiguration.create();
		 	        //Creating HBase table for Streaming Sentiment Analysis
		 	        final HTable table = new HTable(config, "streamingSentimentAnalysis");
		 	        
		 	       BigDataServiceConfiguration configuration = new BigDataServiceConfiguration(); 
		 	       final List<String> twitterStreamingKewordsList = configuration.getStompQueueName();
		 	       
		 	        StatusListener listener = new StatusListener() {
		 
		 	            @Override
		 	            public void onException(Exception arg0) {
		 	                // TODO Auto-generated method stub
		 
		 	            }
		 
		 	            @Override
		 	            public void onDeletionNotice(StatusDeletionNotice arg0) {
		 	                // TODO Auto-generated method stub
		 
		 	            }
		 
		 	            @Override
		 	            public void onScrubGeo(long arg0, long arg1) {
		 	                // TODO Auto-generated method stub
		 
		 	            }
		 
		 	            @Override
		 	            public void onStallWarning(StallWarning stallWarning) {
		 
		 	            }
		 
		 	            public void onStatus(Status status) {
		 	            	RNTN sentiment = new RNTN();
		 	            	User user = status.getUser();
		 
		 	            	String content = status.getText();
		 	                System.out.println("Tweet:" + content +"\n");
		 	                
		 	                String keyword = "";
		 	                for(String key:twitterStreamingKewordsList)
		 	                if (content.contains(key)) keyword = key;
		 	                
		 	                if(!keyword.equals("")) {
		 	                	
		 	                	// Get tweet Details & Sentiment
			 	                String username = status.getUser().getScreenName();
			 	                System.out.println("Username:" + username);
			 	                String profileLocation = user.getLocation();
			 	                System.out.println("Profile Location:" + profileLocation);
			 	                long tweetId = status.getId();
			 	                System.out.println("Tweet ID:" +tweetId);
			 	                int sentimentOut = sentiment.findSentiment(content);
				 	            System.out.println("Sentiment:" + sentimentOut);
			 	                
		 	                
		 	                p = new Put(Bytes.toBytes(keyword));
		 	               
		 	                // Adding value to HBase family "tweets"
		 	                p.add(Bytes.toBytes("tweets"), Bytes.toBytes(String.valueOf(tweetId)),
				 	            		  Bytes.toBytes(String.valueOf(sentimentOut)));
		 	                
		 	                
		 	                try {
		 	                // Inserting value to HBase family "tweets"
								table.put(p);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		 	               }
		 	            }
		 
		 	            @Override
		 	            public void onTrackLimitationNotice(int arg0) {
		 	                // TODO Auto-generated method stub
		 
		 	            }};
		 	            FilterQuery fq = new FilterQuery();
		 	            
		 	            //String keywords[] = twitterStreamingKewords.toArray();
		 	            String keywords[] = twitterStreamingKewordsList.toArray(new String[twitterStreamingKewordsList.size()]);
		 	            fq.track(keywords);
		 	            tsi.addListener(listener);
		 	            tsi.filter(fq);
		 	            }

    public void search(String keyword) throws InterruptedException, IOException {
    	
		while(true)
		{
    	
    	 TwitterFactory tf = new TwitterFactory();
         Twitter twitter = tf.getInstance();
         List<Status> statuses;
         
         Query query = new Query(keyword + " -filter:links -filter:replies -filter:images");
         query.setCount(50);
         query.setLocale("en");
         query.setLang("en");
         
         String strContent = "This example shows how to write string content to a file";
         File myFile = new File("./"+keyword+".txt");
         // check if file exist, otherwise create the file before writing
         if (!myFile.exists()) {
             myFile.createNewFile();
         }
         Writer writer = new FileWriter(myFile,true);
         BufferedWriter bufferedWriter = new BufferedWriter(writer);
         PrintWriter printWriter = new PrintWriter(bufferedWriter);
         
         try {
             QueryResult queryResult = twitter.search(query);
             //return queryResult.getTweets();
             statuses = queryResult.getTweets();
             
             int score = 0;
             int positive = 0;
             int negative = 0;
     		RNTN sentiment = new RNTN();
     		//Tweets twitterSearch = new Tweets();
     		// List<Status> statuses = twitterSearch.search(keyword);
     		 for (Status status : statuses) {
     			 	int sent = sentiment.findSentiment(status.getText());
     	            System.out.println(status.getCreatedAt()+"||||||||" + sent+"|||||||"+ status.getText());
     	            printWriter.write(status.getCreatedAt()+"|" + sent+"|"+ status.getText()+'\n');
     	            //if (sent == 2);
     	            if (sent < 2) {score--; negative++;}
     	            else if (sent >= 2) {score++; positive ++;}
     	            	            
     	        }
     		 score = score * 2;
     		 if(score == 0)
     		 {
     			 System.out.println("Total Sentiment: " + score + " (NEUTRAL)");
     			 printWriter.write("Total Sentiment: " + score + " (NEUTRAL)"+'\n');
     		 }
     		 else
     		 {
     			 
     			 System.out.println("POSITIVE: " + positive + ", " + "NEGATIVE: " + negative);
     			 printWriter.write("POSITIVE: " + positive + ", " + "NEGATIVE: " + negative+'\n');
     			 System.out.println("Total Sentiment: " + Math.abs(score) + "% " + ((score > 0)? "POSITVE":"NEGATIVE"));
     			 printWriter.write("Total Sentiment: " + Math.abs(score) + "% " + ((score > 0)? "POSITVE":"NEGATIVE")+'\n');
     			 printWriter.write("\n\n----------------------------------------------------------------------------\n\n");
     			 int finalScore = positive-negative;
     			 Timestamp stamp = new Timestamp(System.currentTimeMillis());
     			 Date d = new Date(stamp.getTime());
     			 
     			 
     			//Creating HBase configuration
		 	    Configuration config = HBaseConfiguration.create();
		 	    //Creating HBase table for Streaming Sentiment Analysis
		 	    HTable table = new HTable(config, "searchAPISentimentAnalysis");
		 	    Put p = new Put(Bytes.toBytes(keyword));  
                // Adding value to HBase family "tweets"
                p.add(Bytes.toBytes("tweets"), Bytes.toBytes(String.valueOf(stamp)),
	 	            		  Bytes.toBytes(String.valueOf(finalScore)));
                
                // Inserting value to HBase family "tweets"
				table.put(p);
				}
     		 
            // BufferedWriter bufferedWriter = null;
             try {
                
                 //printdWriter.write(strContent);
             } finally{
                 try{
                     if(printWriter != null) printWriter.close();
                 } catch(Exception ex){
                      
                 }
             }
     		 
     		 
     		 
     		 Thread.sleep(60000);
             
             
         } catch (TwitterException e) {
             // ignore
             e.printStackTrace();
         }
       //  return Collections.emptyList(); 
         
		}
		
    	}
    
		//}
       
       /* TwitterStreamFactory ts = new TwitterStreamFactory();
        TwitterStream tsi = ts.getInstance();
        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStallWarning(StallWarning stallWarning) {

            }

            public void onStatus(Status status) {
            	RNTN sentiment = new RNTN();
            	User user = status.getUser();

                // gets Username
                String username = status.getUser().getScreenName();
                System.out.println(username);
                String profileLocation = user.getLocation();
                System.out.println(profileLocation);
                long tweetId = status.getId();
                System.out.println(tweetId);
                String content = status.getText();
                System.out.println(content +"\n");
                System.out.println(sentiment.findSentiment(content));
            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }};



        FilterQuery fq = new FilterQuery();

        String keywords[] = {keyword};

        fq.track(keywords);

        tsi.addListener(listener);
        tsi.filter(fq);




    }
    */
       
        
    //}



    public static void main(String[] args) throws InterruptedException, IOException {
        
        Tweets twitterSearch = new Tweets();
        //List<Status> statuses = twitterSearch.search("modi");
        twitterSearch.search("modi");
        //System.out.print(sentiment.findSentiment("I am extremely best"));
        /*for (Status status : statuses) {
            System.out.println(status.getCreatedAt()+"||||||||" +
                    sentiment.findSentiment(status.getText())+"|||||||"+
                    status.getText());
        }   */
    }
}



