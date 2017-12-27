package sd.net;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import sd.lib.Utils;

public class ConnectionManager
{
	private Impl mImpl;
	
	public void connect(String url)
	{
		String[] tmp = url.split(":");
		String ip = tmp[0];
		int port = Integer.parseInt(tmp[1]);
		
		try
		{
			mImpl.connect(ip, port);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void handle(Consumer<Connection> handler)
	{
		try
		{
			mImpl = new Impl(handler);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public void interrupt()
	{
		try
		{
			mImpl.stop();
		}
		catch (IOException | InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		
		mImpl = null;
	}
	
	public String getAddress()
	{
		return mImpl == null ? "" : (mImpl.getAddress() + ":" + mImpl.getPort());
	}
	
	private static class Impl implements Runnable
	{
		private final ServerSocket socket;
		private final Thread thread;
		private final Consumer<Connection> handler;
		private final Set<Connection> connections;
		
		public Impl(Consumer<Connection> cc) throws IOException
		{
			connections = new HashSet<>();
			thread = new Thread(this);
			handler = cc;
			socket = new ServerSocket(0);
			
			thread.start();
		}
		
		public void connect(String ip, int port) throws UnknownHostException, IOException
		{
			create(new Socket(ip, port));
		}
		
		public void stop() throws IOException, InterruptedException
		{
			socket.close();
			thread.join();
			connections.forEach(c -> c.close());
		}
		
		public int getPort()
		{
			return socket.getLocalPort();
		}
		
		public String getAddress()
		{
			Set<String> addr = new HashSet<>();
			
			try
			{
				for(NetworkInterface eif : Utils.Iterate(NetworkInterface.getNetworkInterfaces()))
				{
					if(!eif.isLoopback() && eif.isUp() && !eif.isVirtual())
					{
						for(InetAddress a : Utils.Iterate(eif.getInetAddresses()))
						{
							if(a instanceof Inet4Address)
							{
								addr.add(a.getHostAddress());
							}
						}
					}
				}
			}
			catch (SocketException e)
			{
				throw new RuntimeException(e);
			}
			
			return "[" + addr.stream().collect(Collectors.joining(", ")) + "]";
		}
		
		@Override
		public void run()
		{
			try
			{
				while(true)
				{
					create(socket.accept());
				}
			}
			catch(SocketException e) { }
			catch(IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		private void create(Socket s) throws IOException
		{
			Connection c = new Connection(s);
			
			connections.add(c);
			handler.accept(c);
		}
	}
}
