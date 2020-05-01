package com.batchprocessing.resource;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;

@CsvRecord( separator = "," )
public class Record {
	public String transID;
	public String transTms;
	public String clientID;
	public String rcNum;
	
	
	public Record(String transID, String transTms, String clientID, String rcNum)
	{
		this.transID = transID;
		this.transTms = transTms;
		this.clientID = clientID;
		this.rcNum = rcNum;
	}
	
	public String GetTransID()
	{
		return transID;
	}
	
	public String GetTransTms()
	{
		return transTms;
	}
	
	public String GetClientID()
	{
		return clientID;
	}
	
	public String GetRcNum()
	{
		return rcNum;
	}
	
	public void SetTransID(String transID)
	{
		this.transID = transID;
	}
	
	public void SetTransTms(String transTms)
	{
		this.transTms = transTms;
	}
	
	public void SetClientID(String clientID)
	{
		this.clientID = clientID;
	}
	
	public void SetRcNum(String rcNum)
	{
		this.rcNum = rcNum;
	}
	
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(transID);
        builder.append(",");
        builder.append(transTms);
        builder.append(",");
        builder.append(rcNum);
        builder.append(",");
        builder.append(clientID);
        return builder.toString();
    }
}
