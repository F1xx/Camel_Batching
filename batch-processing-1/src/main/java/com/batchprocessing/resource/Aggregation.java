package com.batchprocessing.resource;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public final class Aggregation implements AggregationStrategy {
	
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) 
            {
                return newExchange;
            }

            //combine the 2 message bodies
            String body1 = oldExchange.getIn().getBody(String.class);
            String body2 = newExchange.getIn().getBody(String.class);

            oldExchange.getIn().setBody(body1 + "/n" + body2);
            
            return oldExchange;
        } 
    }