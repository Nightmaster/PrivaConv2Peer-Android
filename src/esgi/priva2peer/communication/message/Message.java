package esgi.priva2peer.communication.message;

import java.util.Date;

public class Message
{
	String message;
	Date receiveDate;

	public String getMessage()
	{
		return message;
	}

	public Date getReceiveDate()
	{
		return receiveDate;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setReceiveDate(Date receiveDate)
	{
		this.receiveDate = receiveDate;
	}

}
