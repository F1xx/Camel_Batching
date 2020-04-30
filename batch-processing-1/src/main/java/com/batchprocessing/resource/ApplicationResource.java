package com.batchprocessing.resource;
import java.util.List;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregateController;
import org.apache.camel.processor.aggregate.DefaultAggregateController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.batchprocessing.BatchProcessing1Application;

@RestController
public class ApplicationResource extends RouteBuilder{

	@GetMapping("/")
	public String landing() 
	{
		return "This is just a landing. This page does nothing.\nGo to /payload to deliver the json data";
	}
	
	@GetMapping("/payload")
	public String payloadlanding() 
	{
		return "This is the landing of payload. This isn't an actual website. it should be being interacted with by an application. Are you here by accident?";
	}
	
	@Autowired
	ProducerTemplate producerTemplate;
	
	//commented out as you don't use get for this type of thing. 
	//This was a little test and I want it here to look at if needed
	//get version. Works if you can manage to pass the whole json file's data through the URL (I couldn't)
//	@GetMapping("/payload/{message}")
//	public String startCamelGet(@PathVariable String message) 
//	{
//        System.out.println("Received GET call!");
//        
//		producerTemplate.sendBody("direct:payload", message);
//		
//		return "Payload Taken possibly but GET is a really weird way to send it so it may have error-ed out";
//	}
	
	//post version of REST. Requires the payload data to be sent via post
	@PostMapping(path = "/payload")
	public String startCamelPost(@RequestBody String  message) 
	{
		Logger logger = LoggerFactory.getLogger(BatchProcessing1Application.class);
		logger.info("Received POST call!");
        
		producerTemplate.sendBody("direct:payload", message);
		
		return "Payload Taken";
	}
	
	@Override
	public void configure() throws Exception 
	{
		//restConfiguration().component("servlet").port(8080).host("localhost");//.bindingMode(RestBindingMode.json);

		Splitting splitter = new Splitting();
		Logger logger = LoggerFactory.getLogger(BatchProcessing1Application.class);
		AggregateController controller = new DefaultAggregateController();
		
		//REST DSL is not remotely working and I've spent far too many hours on it.
//		rest("/say")
//	    .get("/hello/{name}").to("direct:hello")
//
//	from("direct:hello")
//		.log(LoggingLevel.INFO, logger, "Hit Hello")
//	    .transform().simple("Hello ${header.name}");
		
		//simple safety that isn't actually safety but I don't have time to make this perfectly data-safe
		try
		{
			//from("file:target/In?delete=true") //this is the from one folder into another method which does work
			//start from whatever the rest service tosses us
			from("direct:payload")
			//.to("json-validator") //I'd use this if I had a JSON Schema as it would be perfect
			.process(p -> //get the message from the exchange
			{
				//get the content
				String body = p.getIn().getBody(String.class);
	
				//split the content into what we want
				List<Record> records = splitter.splitBody(body, p);
				
				//format as a CSV
				logger.info("Formatting into CSV for batch: {}", p.getIn().getHeader("BATCHID"));
				String CSV = CSVGenerator.GenerateCSV(records);
				
				//change the contents we're passing through to be the CSV format we made
				p.getIn().setBody(CSV);
			})
			.aggregate(header("BATCHID"), new Aggregation()) //make an aggregate on this batch
			.completionSize(10).id("myAggregator") //it will complete after 10 items
			.completionInterval(60000) //it will force complete after x milliseconds (60000 for 1 minute, lower for testing)
			.aggregateController(controller) //set the controller if needed
			.to("file:target/Out?filename=${date:now:yyyyMMdd}-${id}.csv");//output to file
		}
		catch(Exception e)
		{
			//most likely reason to hit this is passing in anything that isn't JSON and possibly 
			//if it just doesn't have the format we need
			e.printStackTrace();
		}
	}
}
