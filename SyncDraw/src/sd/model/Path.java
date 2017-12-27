package sd.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.paint.Color;

public class Path implements Iterable<Vec2>
{
	private final List<Vec2> mPath;
	private final Color mColor;
	private final double mStroke;
	
	public Path(Vec2 start, Color color, double stroke)
	{
		mPath = new ArrayList<>();
		mColor = color;
		mStroke = stroke;
		
		mPath.add(start);
	}
	
	public Color getColor() { return mColor; }
	public double getStroke() { return mStroke; }
	
	public void add(Vec2 p)
	{
		mPath.add(p);
	}

	@Override
	public Iterator<Vec2> iterator()
	{
		return mPath.iterator();
	}
}
