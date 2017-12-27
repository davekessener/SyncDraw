import javafx.application.Application;
import javafx.stage.Stage;
import sd.SyncDrawApp;

public class Start extends Application
{
	private SyncDrawApp mApp;
	
	public static void main(String[] args)
	{
		launch(args);
	}

	@Override
	public void start(Stage primary) throws Exception
	{
		mApp = new SyncDrawApp(primary);
	}
	
	@Override
	public void stop()
	{
		mApp.stop();
	}
}
