package sd.lib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.scene.paint.Color;

public class Utils
{
	public static Color Invert(Color c)
	{
		return Color.color(1.0 - c.getRed(), 1.0 - c.getGreen(), 1.0 - c.getBlue());
	}
	
	public static String Stringify(Color c)
	{
		return String.format("[%02x, %02x, %02x]", (int) (255 * c.getRed()), (int) (255 * c.getGreen()), (int) (255 * c.getBlue()));
	}
	
	public static int Color2Int(Color c)
	{
		return ((int)(c.getRed() * 255) << 16) | ((int)(c.getGreen() * 255) << 8) | ((int)(c.getBlue() * 255));
	}
	
	public static Color Int2Color(int c)
	{
		return Color.rgb(((c >> 16) & 0xFF), ((c >> 8) & 0xFF), (c & 0xFF));
	}
	
	public static void Sleep(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
		}
	}
	
	public static byte[] Read(InputStream in, int l) throws IOException
	{
		byte[] r = new byte[l];
		int t = 0;
		
		while(t < l)
		{
			t += in.read(r, t, l - t);
		}
		
		return r;
	}
	
	public static <T> Stream<T> AsStream(Enumeration<T> e)
	{
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
			@Override
			public T next()
			{
				return e.nextElement();
			}
			
			@Override
			public boolean hasNext()
			{
				return e.hasMoreElements();
			}
		}, Spliterator.ORDERED), false);
	}
	
	public static <T> Iterable<T> Iterate(Enumeration<T> e)
	{
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator()
			{
				return new Iterator<T>() {
					@Override
					public boolean hasNext()
					{
						return e.hasMoreElements();
					}

					@Override
					public T next()
					{
						return e.nextElement();
					}
				};
			}
		};
	}
	
	private Utils() { }
}
