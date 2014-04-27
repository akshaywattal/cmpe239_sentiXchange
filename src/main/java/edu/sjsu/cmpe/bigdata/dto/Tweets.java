package edu.sjsu.cmpe.bigdata.dto;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Created by shankey on 4/20/14.
 */
public class Tweets {
	
	

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
     	            if (sent == 2);
     	            else if (sent < 2) {score--; negative++;}
     	            else if (sent > 2) {score++; positive ++;}
     	            	            
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
     		 
     		 
     		 
     		 Thread.sleep(5000);
             
             
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



