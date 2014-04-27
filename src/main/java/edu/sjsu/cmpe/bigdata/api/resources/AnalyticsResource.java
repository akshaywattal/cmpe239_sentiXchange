package edu.sjsu.cmpe.bigdata.api.resources;

import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.bigdata.dao.MongoDBDAO;
import edu.sjsu.cmpe.bigdata.dto.RNTN;
import edu.sjsu.cmpe.bigdata.dto.SentimentAnalysisDto;
import edu.sjsu.cmpe.bigdata.dto.Tweets;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import twitter4j.Status;
import twitter4j.User;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Main Analytics resource, contains API for all Big Data Analysis 
 */

@Path("/v1/analytics")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnalyticsResource {
	
	/**
	 * API to get result of sentiment analysis
	 */
	@GET
    @Path("/sentiment")
    @Timed(name = "view-sentiment")
    public String viewSentiment() throws UnknownHostException {

        MongoDBDAO mongoClient = new MongoDBDAO();
        mongoClient.getDBConnection(mongoClient.getDbHostName(), mongoClient.getDbPortNumber());
        mongoClient.getDB(mongoClient.getDbName());

        /**
         * Creating a new Collection: bigdataUserCollection
         */
        mongoClient.getCollection(mongoClient.getBigdataUserCollection());

        //Cookie cookie =  request.getCookies().get("senti");
        //if(mongoClient.findData(new BasicDBObject("password",cookie)).count()>0){

		SentimentAnalysisDto sentimentAnalysisDto = new SentimentAnalysisDto();
		return sentimentAnalysisDto.getSentiment();
        //}
        //else return "Not Authorized";

    }
	
	/**
	 * API to get result of sentiment analysis
	 */
	@POST
    @Path("/sentiment")
    @Timed(name = "get-sentiment")
    public String getSentiment(@QueryParam("keyword") String  keyword) throws UnknownHostException {
		//RNTN sentiment = new RNTN();
		Tweets twitterSearch = new Tweets();
		twitterSearch.searchStream(keyword);
		
		return null;
    }
}