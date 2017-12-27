package sd.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CanvasRenderer
{
	private final GraphicsContext mGC;
	private final Property<Number> mWidth;
	private final Property<Number> mHeight;
	
	public CanvasRenderer(Canvas c)
	{
		mGC = c.getGraphicsContext2D();
		mWidth = new SimpleDoubleProperty();
		mHeight = new SimpleDoubleProperty();
		
		mWidth.bind(c.widthProperty());
		mHeight.bind(c.heightProperty());
		mGC.setFill(Color.WHITE);
	}
	
	public void clear()
	{
		mGC.fillRect(0, 0, mWidth.getValue().doubleValue(), mHeight.getValue().doubleValue());
	}
	
	public void redraw(Path path)
	{
		boolean f = true;
		
		setStroke(path.getColor(), path.getStroke());
		
		for(Vec2 p : path)
		{
			if(f)
			{
				startPath(p);
			}
			else
			{
				moveTo(p);
			}
			
			f = false;
		}
	}
	
	public void setStroke(Paint c, double w)
	{
		mGC.setStroke(c);
		mGC.setLineWidth(w);
	}
	
	public void startPath(Vec2 p)
	{
		mGC.beginPath();
		mGC.moveTo(p.X, p.Y);
		mGC.stroke();
	}
	
	public void moveTo(Vec2 p)
	{
		mGC.lineTo(p.X, p.Y);
		mGC.stroke();
	}
}
