package edu.sjsu.cmpe.bigdata.api.resources;

import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import twitter4j.Status;

import com.yammer.metrics.annotation.Timed;

import edu.sjsu.cmpe.bigdata.dto.RNTN;
import edu.sjsu.cmpe.bigdata.dto.SentimentAnalysisDto;
import edu.sjsu.cmpe.bigdata.dto.Tweets;

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
		SentimentAnalysisDto sentimentAnalysisDto = new SentimentAnalysisDto();
		return sentimentAnalysisDto.getSentiment();
    }
	
	/**
	 * API to get result of sentiment analysis
	 */
	@POST
    @Path("/sentiment")
    @Timed(name = "get-sentiment")
    public String getSentiment(@QueryParam("keyword") String  keyword) throws UnknownHostException {
		RNTN sentiment = new RNTN();
		Tweets twitterSearch = new Tweets();
		List<Status> statuses = twitterSearch.search(keyword);
		return null;
    }
}