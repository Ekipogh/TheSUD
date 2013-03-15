package utils;

public class TextCollector {
	private static String Text = "";
	public TextCollector() {
		
	}
	
	public static void Clear()
	{
		Text = "";
	}
	
	public static void Add(String add)
	{
		Text+=add;
	}
	
	public static String Get()
	{
		return Text;
	}

	public static boolean isEmpty() {
		return Text.isEmpty();
	}

}
