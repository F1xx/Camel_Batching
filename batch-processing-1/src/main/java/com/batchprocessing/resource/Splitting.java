package com.batchprocessing.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.camel.Exchange;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.batchprocessing.BatchProcessing1Application;

public class Splitting {

	List<Record> records = new ArrayList<Record>();
	
	//takes in the file passed in preferably through REST if I can figure that out
	public List<Record> splitBody(String body, Exchange ex) 
	{
		//since I'm using a member variable, ensure it is clear for every try
		records.clear();
		Logger logger = LoggerFactory.getLogger(BatchProcessing1Application.class);

      //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        //make a reader to take the file apart
        try
        {
            //Read JSON file
        	JSONObject obj = (JSONObject)jsonParser.parse(body);
        	
        	ex.getIn().setHeader("BATCHID", (String) obj.get("batchId"));
        	
        	//before anything get and log the batch ID
        	String batchID = (String) obj.get("batchId");
        	logger.info("BatchID: {}", batchID);
        	
        	//create an array from the following records
            JSONArray contents = (JSONArray) obj.get("records");
            //contents.add(obj.get("records"));
            
            //iterate through the array and create records
            Iterator iterator = contents.iterator();
            while(iterator.hasNext()) 
            {
            	parseObject((JSONObject)iterator.next());
            	logger.info("Record added to batch with batchID: {}", batchID);
            }
        } 
        catch (Exception e)
        {
        	e.printStackTrace();
        }
        
        return records;
    }
	
	//takes in a JSON object and, if it is in the right format, gets the data from it and adds it to the records list
    private void parseObject(JSONObject record) 
    {
    	String transID = (String)record.get("transId");
    	String transTms = (String)record.get("transTms");
    	String clientID = (String)record.get("clientId");
    	String rcNum = (String)record.get("rcNum");
    	
    	Record rec = new Record(transID, transTms, clientID, rcNum);
    	//rec.PrintRecord();
    	records.add(rec);
    	
    }
}
