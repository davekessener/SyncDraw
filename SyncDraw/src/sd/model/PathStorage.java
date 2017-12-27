package sd.model;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.scene.paint.Color;
import sd.lib.Client;
import sd.lib.Event;
import sd.lib.Utils;

public class PathStorage implements Client
{
	private final CanvasRenderer mRender;
	private final Deque<Path> mPaths;
	private final PathBuilder mBuilder;
	
	public PathStorage(CanvasRenderer render, PathBuilder b)
	{
		mRender = render;
		mPaths = new ArrayDeque<>();
		mBuilder = b;
	}
	
	public void accept(Event e)
	{
		if(e instanceof Event.Pressed)
		{
			Event.Pressed p = (Event.Pressed) e;
			
			onPressed(Utils.Int2Color(p.color), p.stroke, mRender.toAbsolute(p.v));
		}
		else if(e instanceof Event.Dragged)
		{
			onDragged(mRender.toAbsolute(((Event.Dragged) e).v));
		}
		else if(e instanceof Event.Released)
		{
			onReleased();
		}
		else if(e instanceof Event.Undo)
		{
			undo();
		}
		else if(e instanceof Event.Clear)
		{
			clear();
		}
	}
	
	public void onPressed(Color c, double s, Vec2 p)
	{
		mRender.setStroke(c, s);
		mRender.startPath(p);
		mBuilder.start(p);
	}
	
	public void onDragged(Vec2 p)
	{
		mRender.moveTo(p);
		mBuilder.moveTo(p);
	}
	
	public void onReleased()
	{
		mPaths.add(mBuilder.stop());
	}
	
	public void redraw()
	{
		mRender.clear();
		
		for(Path p : mPaths)
		{
			mRender.redraw(p);
		}
	}
	
	public void undo()
	{
		if(!mPaths.isEmpty())
		{
			mPaths.removeLast();
			
			redraw();
		}
	}
	
	public void clear()
	{
		mPaths.clear();
		redraw();
	}
}
