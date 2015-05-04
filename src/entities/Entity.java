package entities;

import sud.SudGame;
import utils.TextCollector;
import gameworld.Room;

public abstract class Entity implements SUDObject{
	private Room r;
	protected String Name;
	private int ticks;
	protected String[] commands;
	protected String[] scripts;
	private String description;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Entity() {
		commands = new String[2];
		scripts = new String[2];
		commands[0] = "enter";
		commands[1] = "leave";
		scripts[0] = "empty";
		scripts[1] = "empty";
	}
	public Entity(Room r) {
		super();
		this.setTicks(0);
		this.setRoom(r);
	}
	public Entity(Room r, String name) {
		super();
		this.Name = name;
		this.setTicks(0);
		this.setRoom(r);
	}

	public Room getRoom() {
		return r;
	}

	public void setRoom(Room r) {
		this.r = r;
	}

	public void command(String c) {
		TextCollector text = TextCollector.getInstance();
		boolean found = false;
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].startsWith(c.toLowerCase())) {
				found = true;
				SudGame.execute(scripts[i], this);
			}
		}
		if (!found)
			text.Add("<font color = white>Что?<br>\n");
	}

	public void tick() {
	}

	public String getDescription() {
		return description;
	}

	public int getTicks() {
		return ticks;
	}

	public void setTicks(int ticks) {
		this.ticks = ticks;
	}
}
