package com.batchprocessing.resource;

public class Record {
	public String transID;
	public String transTms;
	public String clientID;
	public String rcNum;
	
	
	public Record(String tID, String tTms, String cID, String rcnum)
	{
		transID = tID;
		transTms = tTms;
		clientID = cID;
		rcNum = rcnum;
	}
	
	public void PrintRecord()
	{
		System.out.println("transID: " + transID);
		System.out.println("transTms: " + transTms);
		System.out.println("clientID: " + clientID);
		System.out.println("rcNum: " + rcNum);
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
