package edu.sjsu.cmpe.bigdata;

import java.io.IOException;
import java.util.List;

import de.spinscale.dropwizard.jobs.Job;
import de.spinscale.dropwizard.jobs.annotations.OnApplicationStart;
import edu.sjsu.cmpe.bigdata.config.BigDataServiceConfiguration;
import edu.sjsu.cmpe.bigdata.dto.Tweets;

@OnApplicationStart
public class StreamingAnalyticsJob extends Job{

	        @Override
	        public void doJob() {
	        	BigDataServiceConfiguration configuration = new BigDataServiceConfiguration();
	        	
	        	//Piggybacking property for Stream Keywords
	        	List<String> twitterStreamingKewordsList = configuration.getStompQueueName();
	        	String twitterStreamingKewords = twitterStreamingKewordsList.toString().replace("[", "");
	        	twitterStreamingKewords = twitterStreamingKewords.replace("]", "");
	        	System.out.println("Streaming-BEGINS");
	        	Tweets twitterSearch = new Tweets();
	            try {
					twitterSearch.searchStream(twitterStreamingKewords);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	}
	        }

