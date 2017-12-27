package sd.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import sd.lib.Client;
import sd.lib.Event;

public class Connection implements Client
{
	private final Socket mSocket;
	private final Set<Consumer<Event>> mAcceptors;
	private final Thread mThread;
	private final ObjectOutputStream mOut;
	private final ObjectInputStream mIn;
	private Event mReboundLock;
	private State mOpen;
	
	public Connection(Socket s) throws IOException
	{
		mSocket = s;
		mAcceptors = Collections.synchronizedSet(new HashSet<>());
		mThread = new Thread(() -> run());
		mOpen = State.OPEN;
		
		mOut = new ObjectOutputStream(new DataOutputStream(mSocket.getOutputStream()));
		mIn = new ObjectInputStream(new DataInputStream(mSocket.getInputStream()));
		
		mThread.start();
	}

	@Override
	public void accept(Event e)
	{
		if(mOpen == State.OPEN && e != mReboundLock)
		{
			try
			{
				mOut.writeObject(e);
			}
			catch (IOException ex)
			{
				close();
			}
		}
	}
	
	public void accept(Consumer<Event> c)
	{
		if(mOpen == State.OPEN)
		{
			mAcceptors.add(c);
		}
	}
	
	public void close()
	{
		if(mOpen != State.CLOSED)
		{
			try
			{
				mSocket.close();
				mThread.join();
			}
			catch (IOException | InterruptedException e)
			{
				e.printStackTrace();
			}
			
			mOpen = State.CLOSED;
		}
	}
	
	private void run()
	{
		try
		{
			while(true)
			{
				Object o = mIn.readObject();
				
				if(o instanceof Event)
				{
					mReboundLock = (Event) o;
					mAcceptors.forEach(c -> c.accept(mReboundLock));
					mReboundLock = null;
				}
				else
				{
					throw new RuntimeException(o.toString());
				}
			}
		}
		catch (ClassNotFoundException | IOException e)
		{
			mOpen = State.PENDING;
		}
	}
	
	private static enum State
	{
		OPEN,
		CLOSED,
		PENDING;
	}
}
