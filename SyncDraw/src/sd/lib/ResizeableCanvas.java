package sd.lib;

import javafx.scene.canvas.Canvas;

public abstract class ResizeableCanvas extends Canvas
{
	public ResizeableCanvas()
	{
		widthProperty().addListener(e -> redraw());
		heightProperty().addListener(e -> redraw());
	}
	
	@Override
	public boolean isResizable()
	{
		return true;
	}
	
	@Override
	public double prefWidth(double h)
	{
		return getWidth();
	}
	
	@Override
	public double prefHeight(double w)
	{
		return getHeight();
	}
	
	protected abstract void redraw();
}
