package gameworld;

import utils.TextCollector;
import utils.Trigger;
public class Room {
	int Id;
	String initText;
	String[] commands;
	int[] triggers;
	Room[] exits = new Room[4];
	private String name;;
	public Room(int id,String name, String initText, String[] commands, int[] triggers) {
		Id = id;
		this.setName(name);
		this.initText = initText;
		this.commands = commands;
		this.triggers = triggers;
	}
	public void command(String c){
		boolean found = false;
		for(int i = 0; i < commands.length; i++)
		{
			if(commands[i].startsWith(c.toLowerCase()))
			{
				found = true;
				Trigger.trig(triggers[i]);
			}
		}
		if(!found) TextCollector.Add("<font color = white>Что?<br>\n");
	}
	public String InitText()
	{
		return initText;
	}
	public String exitstext()
	{
		String ex = "";
		if(exits[0]!=null) ex+="с ";
		if(exits[1]!=null) ex+="ю ";
		if(exits[2]!=null) ex+="в ";
		if(exits[3]!=null) ex+="з";
		return ex;
	}
	public void setExits(Room[] exits)
	{
		this.exits = exits;
	}
	public Room getExit(int d)
	{
		return exits[d];
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
