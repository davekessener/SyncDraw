package sd;

import javafx.beans.binding.Bindings;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sd.lib.Event;
import sd.lib.RelayManager;
import sd.lib.ResizeableCanvas;
import sd.lib.Utils;
import sd.model.CanvasRenderer;
import sd.model.PathBuilder;
import sd.model.PathStorage;
import sd.model.Vec2;
import sd.net.ConnectionManager;
import sd.ui.AppUI;
import sd.ui.ConnectDialog;

public class SyncDrawApp
{
	private final AppUI mUI;
	private final Canvas mCanvas;
	private final PathBuilder mBuilder;
	private final PathStorage mPaths;
	private final RelayManager mRelay;
	private final CanvasRenderer mRenderer;
	private final ConnectionManager mNetwork;
	private Color mColor;
	private double mStroke;
	private boolean mErasing;
	
	public SyncDrawApp(Stage primary)
	{
		mErasing = false;
		mColor = Color.BLACK;
		mStroke = 5D;
		
		mCanvas = new ResizeableCanvas() {
			@Override
			protected void redraw()
			{
				mPaths.redraw();
			}
		};
		mRelay = new RelayManager();
		mNetwork = new ConnectionManager();
		mBuilder = new PathBuilder(mColor, mStroke);
		mRenderer = new CanvasRenderer(mCanvas);
		mPaths = new PathStorage(mRenderer, mBuilder);
		mUI = new AppUI(primary, mCanvas);
		
		mRelay.register(mPaths);

		mCanvas.setOnMousePressed(e -> mRelay.relay(new Event.Pressed(getColor(), getStroke(), mRenderer.toRelative(new Vec2(e.getX(), e.getY())))));
		mCanvas.setOnMouseDragged(e -> mRelay.relay(new Event.Dragged(mRenderer.toRelative(new Vec2(e.getX(), e.getY())))));
		mCanvas.setOnMouseReleased(e -> mRelay.relay(new Event.Released()));
		
		registerControls();

		mNetwork.handle(c -> {
			c.accept(e -> mRelay.relay(e));
			mRelay.register(c);
		});
		
		mUI.statusProperty().bind(Bindings.concat(
				Bindings.createStringBinding(() -> Utils.Stringify(mBuilder.colorProperty().getValue()) + ",  ", mBuilder.colorProperty()),
				Bindings.createStringBinding(() -> String.format("%.2f,  ", (float) mBuilder.strokeProperty().getValue().doubleValue()), mBuilder.strokeProperty()),
				Bindings.createStringBinding(() -> mNetwork.getAddress())));
		
		mUI.show();
	}
	
	private Color getColor() { return mBuilder.colorProperty().getValue(); }
	private double getStroke() { return mBuilder.strokeProperty().getValue().doubleValue(); }
	
	public void stop()
	{
		mNetwork.interrupt();
	}
	
	private void registerControls()
	{
		mUI.registerControl("erase", () -> {
			if(!mErasing)
			{
				mBuilder.colorProperty().setValue(Utils.Invert(mColor));
				mBuilder.strokeProperty().setValue(3 * mStroke);
			}
			else
			{
				mBuilder.colorProperty().setValue(mColor);
				mBuilder.strokeProperty().setValue(mStroke);
			}
			
			mErasing = !mErasing;
		});
		
		mUI.registerControl("undo", () -> {
			mRelay.relay(new Event.Undo());
		});
		
		mUI.registerControl("connect", () -> {
			new ConnectDialog(url -> mNetwork.connect(url));
		});
		
		mUI.registerControl("clear", () -> {
			mRelay.relay(new Event.Clear());
		});
	}
}
