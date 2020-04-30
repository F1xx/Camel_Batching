package com.batchprocessing.resource;

import java.util.List;

public class CSVGenerator {
	
	private static String CSV;
	
	public static String GenerateCSV(List<Record> recs)
	{
		CSV = "";
		//loop through what we are passed and add them to a CSV type string
		recs.forEach(rec -> AddtoCSV(rec));
		
		//return the formatted string for file output
		return CSV;
	}
	
	private static void AddtoCSV(Record rec)
	{
		//CSV += rec.toString();
		CSV += String.format("%s, %s, %s, %s\n", rec.transID, rec.transTms, rec.rcNum, rec.clientID);
	}
}
