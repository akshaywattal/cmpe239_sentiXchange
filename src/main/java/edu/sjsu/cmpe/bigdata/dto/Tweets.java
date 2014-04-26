package edu.sjsu.cmpe.bigdata.dto;


import twitter4j.*;
/**
 * Created by shankey on 4/20/14.
 */
public class Tweets {

    public void search(String keyword) {
        TwitterFactory tf = new TwitterFactory();
        Twitter twitter = tf.getInstance();
        TwitterStreamFactory ts = new TwitterStreamFactory();
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
        /*Query query = new Query(keyword + " -filter:links -filter:replies -filter:images");
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
        return Collections.emptyList();  */



    public static void main(String[] args) {
        
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



