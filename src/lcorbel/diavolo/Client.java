package lcorbel.diavolo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
	private int size;
	private int port;
	private InetAddress host;
	
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	private Diavolo game;
	
	public Client(InetAddress host, int port, int size)
	{
		this.size = size;
		this.host = host;
		this.port = port;
	}
	
	public void connect()
	{
		try
		{
			socket = new Socket(host, port);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			
			System.out.println("-- DIAVOLO --");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		try
		{
			is.close();
			os.close();
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		game = new Diavolo(size);
		
		Action ac = null;
		
		while(game.isRunning())
		{			
			ac = receiveAction();
			System.out.println(ac);
			ac = game.play(ac);
					
			if(ac != null)
			{
				sendAction(ac);
				System.out.println(ac);
			}
		}
	}
	
	private Action receiveAction()
	{
		int buffer = -1;
		int data[] = new int[5];
		int i = 0;
		
		try
		{
			buffer = is.read();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		data[i] = buffer;
		
		while(buffer < 64 && i < 4)
		{
			i++;
			
			try
			{
				buffer = is.read();
				data[i] = buffer;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		if(i == 0)
		{
			return new Action(data[0]);
		}
		else
		{
			int x1 = Character.getNumericValue(data[0]);
			int y1 = Character.getNumericValue(data[1]);
			int x2 = Character.getNumericValue(data[3]);
			int y2 = Character.getNumericValue(data[4]);
			
			return new Action(data[2], x1+1, y1+1, x2+1, y2+1);
		}
	}
	
	private void sendAction(Action ac)
	{
		String stream = ac.stream();
		
		for(int i=0; i < stream.length(); i++)
		{
			try
			{
				os.write((int) stream.charAt(i));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
