package sd.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class PathBuilder
{
	private Path mCurrent;
	private Pair<Vec2, Double> mLast;
	private Vec2 mTmp = null;
	private final Property<Color> mColor;
	private final Property<Number> mStroke;
	
	public PathBuilder(Color p, double s)
	{
		mColor = new SimpleObjectProperty<>();
		mStroke = new SimpleDoubleProperty();
		
		mColor.setValue(p);
		mStroke.setValue(s);
	}
	
	public Property<Color> colorProperty() { return mColor; }
	public Property<Number> strokeProperty() { return mStroke; }
	
	public void start(Vec2 p)
	{
		mCurrent = new Path(p, mColor.getValue(), mStroke.getValue().doubleValue());
		mLast = new Pair<>(p, mStroke.getValue().doubleValue() * 0.75);
		mTmp = null;
	}
	
	public void moveTo(Vec2 p)
	{
		if(p.distanceTo(mLast.getKey()) >= mLast.getValue())
		{
			mCurrent.add(p);
			mLast = new Pair<>(p, mLast.getValue());
		}
		
		mTmp = p;
	}
	
	public Path stop()
	{
		if(mTmp != null && mTmp != mLast.getKey())
		{
			mCurrent.add(mTmp);
		}
		
		Path p = mCurrent;
		
		mCurrent = null;
		
		return p;
	}
}
