package sd.model;

import java.io.Serializable;

public class Vec2 implements Serializable
{
	private static final long serialVersionUID = 5217001545411633608L;
	
	public final double X, Y;
	
	public Vec2(double x, double y)
	{
		X = x;
		Y = y;
	}
	
	public Vec2 add(Vec2 v) { return new Vec2(X + v.X, Y + v.Y); }
	public Vec2 sub(Vec2 v) { return new Vec2(X - v.X, Y - v.Y); }
	
	public double abs() { return Math.sqrt(abs_sqr()); }
	public double abs_sqr() { return X * X + Y * Y; }
	
	public double distanceTo(Vec2 v)
	{
		return v.sub(this).abs();
	}
}
