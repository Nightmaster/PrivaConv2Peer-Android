package esgi.priva2peer.p2pc.window;

import java.util.HashMap;
import java.util.Map;
import esgi.priva2peer.communication.client.Client;
import esgi.priva2peer.communication.client.ClientInfo;
import esgi.priva2peer.communication.server.Server;

public class ChatWindow
{

	protected static final int MAJ_KEY = 16;

	boolean clearArea = false;
	String currentInterlocuteur = "";
	ClientInfo ci = new ClientInfo("stephen");
	Map<String, String> discution = new HashMap<String, String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{

		try
		{
			new Thread(new Server()).start();
			new Thread(new Client()).start();
			ChatWindow window = new ChatWindow();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Create the application.
	 */

	/**
	 * Initialize the contents of the frame.
	 */

}
