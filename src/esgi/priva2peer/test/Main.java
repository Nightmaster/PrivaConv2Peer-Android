package esgi.priva2peer.test;
import java.net.InetAddress;
import java.util.Map;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;

public class Main
{

	public static final String ADRESS_TO_CONNECT = "92.132.173.71";
	public static int SAMPLE_PORT = 6991;

	private static short WAIT_TIME = 120;

	public static void main(String[] args) throws Exception
	{

		// décrouvrir la box
		GatewayDiscover gatewayDiscover = new GatewayDiscover();

		Map<InetAddress, GatewayDevice> gateways = gatewayDiscover.discover();
		// si pas de box
		if (gateways.isEmpty())
		{
			return;
		}

		// choose the first active gateway for the tests
		// prend la box
		GatewayDevice activeGW = gatewayDiscover.getValidGateway();

		if (null == activeGW)
		{
			return;
		}

		// testing getGenericPortMappingEntry
		PortMappingEntry portMapping = new PortMappingEntry();
		// local address = adresse ip machine
		InetAddress localAddress = activeGW.getLocalAddress();

		// verifie que le port est pas déjà utilisé sur la box ou PC
		if (activeGW.getSpecificPortMappingEntry(SAMPLE_PORT, "TCP", portMapping))
		{
			System.out.println("Port " + SAMPLE_PORT + " is already mapped.");
			// delete s'il est utilisé
			activeGW.deletePortMapping(SAMPLE_PORT, "TCP");
			return;
		}
		else
		{
			// test static lease duration mapping
			// vers ou on map
			// SAMPLE_PORT box, SAMPLE_PORT machine, adresse machine, Protocole,
			// description
			if (activeGW.addPortMapping(SAMPLE_PORT, SAMPLE_PORT, localAddress.getHostAddress(), "TCP", "test"))
			{
				Thread t = new Thread(new Server());
				t.start();
				Thread.sleep(1000 * WAIT_TIME);
				t.interrupt();
				activeGW.deletePortMapping(SAMPLE_PORT, "TCP");
			}
		}
	}

}
