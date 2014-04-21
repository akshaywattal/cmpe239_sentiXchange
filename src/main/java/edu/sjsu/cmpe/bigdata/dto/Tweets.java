package edu.sjsu.cmpe.bigdata.dto;

import twitter4j.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by shankey on 4/20/14.
 */
public class Tweets {

    public List<Status> search(String keyword) {
        TwitterFactory tf = new TwitterFactory();
        Twitter twitter = tf.getInstance();
        Query query = new Query(keyword + " -filter:links -filter:replies -filter:images");
        query.setCount(200);
        query.setLocale("en");
        query.setLang("en");
        try {
            QueryResult queryResult = twitter.search(query);
            return queryResult.getTweets();
        } catch (TwitterException e) {
            // ignore
            e.printStackTrace();
        }
        return Collections.emptyList();

    }

    public static void main(String[] args) {
        RNTN sentiment = new RNTN();
        Tweets twitterSearch = new Tweets();
        List<Status> statuses = twitterSearch.search("modi");
        //System.out.print(sentiment.findSentiment("I am extremely best"));
        for (Status status : statuses) {
            System.out.println(status.getCreatedAt()+"||||||||" +
                    sentiment.findSentiment(status.getText())+"|||||||"+
                    status.getText());
        }
    }
}



