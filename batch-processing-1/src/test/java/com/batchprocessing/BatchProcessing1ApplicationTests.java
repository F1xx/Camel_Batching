package com.batchprocessing;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.batchprocessing.resource.ApplicationResource;

@SpringBootTest
class BatchProcessing1ApplicationTests extends CamelTestSupport 
{
	 @Override
	    protected RoutesBuilder createRouteBuilder() throws Exception {
	        return new ApplicationResource();
	    }

	@Test
	void contextLoads() throws Exception
	{
		Logger logger = LoggerFactory.getLogger(BatchProcessing1Application.class);
		logger.info("Post Request Test Starting");

		//place the sample path here. I'm sure you could also set this up to read any and every file there and pass if if desired.
		Path filepath = Paths.get("target\\\\In\\\\sample-payload.json");
		assert(filepath != null);
		
		logger.info("Post Test using file: {}", filepath.toString());
		
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/payload"))
                .POST(HttpRequest.BodyPublishers.ofFile(filepath))
                .build();
        
        try
        {
			HttpResponse<String> response = client.send(request,
		    HttpResponse.BodyHandlers.ofString());
			
			//200 is the status code for everything went well
			logger.info("Response Code: {}", response.statusCode());
			assert(response.statusCode() == 200);
			logger.info(response.body());
        }
        catch (Exception e)
		{
			e.printStackTrace();
		}
    }
}
