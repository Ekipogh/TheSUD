package gameworld;

import sud.SudGame;
import utils.TextCollector;

public class Room {
	private int Id;
	String initText;
	String[] commands;
	String[] scripts;
	Room[] exits = new Room[4];
	private String name;;

	public String[] getCommands() {
		return commands;
	}

	public String[] getScripts() {
		return scripts;
	}

	public Room(int id, String name, String inittext, String[] comms,
			String[] scripts) {
		this.Id = id;
		this.setName(name);
		this.initText = inittext;
		this.commands = comms;
		this.scripts = scripts;
	}

	public void setInitText(String initText) {
		this.initText = initText;
	}

	public Room(int id) {
		this.Id = id;
		this.name = "Unnamed";
		initText = "Enter description";
		commands = new String[2];
		scripts = new String[2];
		scripts[0] = "empty";
		scripts[1] = "empty";
		commands[0] = "enter";
		commands[1] = "leave";
	}

	public Room[] getExits() {
		return exits;
	}

	public void command(String c) {
		TextCollector text = TextCollector.getInstance();
		boolean found = false;
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].startsWith(c.toLowerCase())) {
				found = true;
				SudGame.execute(scripts[i], this);
				// Trigger.trig(triggers[i], this);
			}
		}
		if (!found)
			text.Add("<font color = white>Что?<br>\n");
	}

	public String InitText() {
		return initText;
	}

	public String exitstext() {
		String ex = "";
		if (exits[0] != null)
			ex += "с ";
		if (exits[1] != null)
			ex += "ю ";
		if (exits[2] != null)
			ex += "в ";
		if (exits[3] != null)
			ex += "з";
		return ex;
	}

	public void setExits(Room[] exits) {
		this.exits = exits;
	}

	public Room getExit(int d) {
		return exits[d];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public void setExit(int i, Room ex) {
		exits[i] = ex;
	}

	public int getId() {
		return Id;
	}
	
	public boolean equals(Room r)
	{
		return this.Id==r.Id;
	}

	public void setCommands(String[] commands) {
		this.commands = commands;
	}

	public void setScripts(String[] scripts) {
		this.scripts = scripts;
	}
}
