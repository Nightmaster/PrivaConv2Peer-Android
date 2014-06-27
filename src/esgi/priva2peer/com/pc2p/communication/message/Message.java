package esgi.priva2peer.com.pc2p.communication.message;

import java.util.Date;
import android.text.Editable;

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

	public void setMessage(Editable text)
	{
		// TODO Auto-generated method stub

	}

}
