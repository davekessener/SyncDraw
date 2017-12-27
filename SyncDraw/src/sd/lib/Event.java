package sd.lib;

import java.io.Serializable;

import javafx.scene.paint.Color;
import sd.model.Vec2;

public abstract class Event implements Serializable
{
	private static final long serialVersionUID = -924714124074378894L;

	public static class Pressed extends Event
	{
		private static final long serialVersionUID = -6194176085452995403L;
		
		public final Vec2 v;
		public final int color;
		public final double stroke;
		
		public Pressed(Color color, double stroke, Vec2 v)
		{
			this.v = v;
			this.color = Utils.Color2Int(color);
			this.stroke = stroke;
		}

		public int red() { return (color >> 16) & 0xFF; }
		public int green() { return (color >> 8) & 0xFF; }
		public int blue() { return color & 0xFF; }
		
		@Override
		public String toString()
		{
			return String.format("[PRESSED @(%.2f, %.2f) /w (%02X%02X%02X @%.2f)]", v.X, v.Y, red(), green(), blue(), stroke);
		}
	}

	public static class Dragged extends Event
	{
		private static final long serialVersionUID = 3316514862999948995L;
		
		public final Vec2 v;
		
		public Dragged(Vec2 v) { this.v = v; }
		
		@Override
		public String toString()
		{
			return String.format("[DRAGGED @(%.2f, %.2f)]", v.X, v.Y);
		}
	}
	
	public static class Released extends Event
	{
		private static final long serialVersionUID = -5289635325808666646L;
		
		@Override
		public String toString()
		{
			return "[RELEASED]";
		}
	}
	
	public static class Undo extends Event
	{
		private static final long serialVersionUID = -1326871122495011684L;

		@Override
		public String toString()
		{
			return "[UNDO]";
		}
	}
	
	public static class Clear extends Event
	{
		private static final long serialVersionUID = -5346407384503713066L;

		@Override
		public String toString()
		{
			return "[CLEAR]";
		}
	}
	
	private Event() { }
}
