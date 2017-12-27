package sd.lib;

import java.util.HashSet;
import java.util.Set;

public class RelayManager
{
	private Set<Client> mClients = new HashSet<>();
	
	public void register(Client c)
	{
		mClients.add(c);
	}
	
	public void relay(Event e)
	{
		mClients.forEach(c -> c.accept(e));
	}
}
