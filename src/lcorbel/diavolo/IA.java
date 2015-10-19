package lcorbel.diavolo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IA
{
	public static void main(String[] args)
	{
		InetAddress host = null;
		int port = 0;
		int size = 0;
		
		try
		{
			host = InetAddress.getByName(args[0]);
		}
		catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			port = Integer.parseInt(args[1]);
			size = Integer.parseInt(args[2]);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		Client client = new Client(host, port, size);
		
		client.connect();
		client.run();
		client.disconnect();
	}
}
