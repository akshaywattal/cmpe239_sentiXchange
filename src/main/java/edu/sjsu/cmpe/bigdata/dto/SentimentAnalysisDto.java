// This program is for fetching most recent analysis details

package edu.sjsu.cmpe.bigdata.dto;

import com.mongodb.*;
import edu.sjsu.cmpe.bigdata.domain.Sentiment;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class SentimentAnalysisDto {
	private Sentiment response;
	
	public Sentiment getSentiment() throws UnknownHostException {


		// Creating connection with MongoDB
		MongoClient client = new MongoClient();
		DB courseDB = client.getDB("bigdata");
		DBCollection collection = courseDB.getCollection("data");
		
		// Creating query for fetching details of latest analysis
		DBObject query1 = new BasicDBObject()
						.append("time", "2222");

		//DBObject query2 = new BasicDBObject()
			//			.append("date", -1);
		
		// Executing query
		//DBCursor cursor = collection.find(query1).sort(query2).limit(1);
		DBCursor cursor = collection.find(query1).limit(10);
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int[] tempArray = new int[cursor.size()];
        int i=0;
        for(DBObject dp : cursor){

            //temp.add(Integer.parseInt(dp.get("values").toString()));
            tempArray[i++] = Integer.parseInt(dp.get("values").toString());

        }


        response.setValues(tempArray);

        /*response = "{\"values\":[";
		for(DBObject dp : cursor){
                int sentiment = Integer.parseInt(dp.get("pos").toString()) - Integer.parseInt(dp.get("neg").toString());
               response =  response + sentiment + ;
              */


		// Fetching "keys" from cursor	
		/*DBObject cur = cursor.next();
		String business_id = (String) cur.get("business_id");
		int positive = ((Number) cur.get("positive")).intValue();
		int negative = ((Number) cur.get("negative")).intValue();
		int neutral  = ((Number) cur.get("neutral")).intValue();
		int notEval  = ((Number) cur.get("notEval")).intValue();
			
		// Printing analysis details
		System.out.println("Number of neutral reviews       : "+ neutral);
		System.out.println("Number of positive reviews      : "+ positive);
		System.out.println("Number of negative reviews      : "+ negative);
		System.out.println("Number of reviews not evaluated : "+ notEval);
		
		// Creating String in json format
		 response = "[{" + "\"sentiment\":" + "\"neutral\"," + "\"value\":"  + "\"" + neutral + "\"" + "},{"
		              + "\"sentiment\":" + "\"positive\"," + "\"value\":" + "\"" + positive + "\"" + "},{"
		              + "\"sentiment\":" + "\"negative\"," + "\"value\":"  + "\"" + negative + "\"" + "},{"
		              + "\"sentiment\":" + "\"notevaluated\"," + "\"value\":"  + "\"" + notEval + "\"" + "}]";
		*/
		 return response;

		}
	}
