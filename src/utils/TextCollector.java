package utils;

public class TextCollector {
	private static TextCollector instance;
	private String Text;;
	private TextCollector() {
		Text = "";
	}
	public static TextCollector getInstance(){
		if(instance==null)
		{
			instance = new TextCollector();
			return instance;
		}else
			return instance;
	}
	public void Clear()
	{
		instance.Text = "";
	}
	
	public void Add(String add)
	{
		instance.Text+=add;
	}
	
	public String Get()
	{
		return instance.Text;
	}

	public boolean isEmpty() {
		return instance.Text.isEmpty();
	}

}
