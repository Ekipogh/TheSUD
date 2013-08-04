package entities;

import utils.TextCollector;
import utils.Trigger;
import gameworld.Room;

public abstract class Entity implements SUDObject{
	private Room r;
	protected String Name;
	protected int ticks;
	protected String[] commands;
	protected int[] triggers;
	private String description;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Entity() {
	}
	public Entity(Room r) {
		super();
		this.ticks = 0;
		this.setRoom(r);
	}
	public Entity(Room r, String name) {
		super();
		this.Name = name;
		this.ticks = 0;
		this.setRoom(r);
	}

	public Room getRoom() {
		return r;
	}

	public void setRoom(Room r) {
		this.r = r;
	}

	public void command(String c) {
		boolean found = false;
		if (commands != null) {
			for (int i = 0; i < commands.length; i++) {
				if (commands[i].startsWith(c.toLowerCase())) {
					found = true;
					Trigger.trig(triggers[i],this);
					break;
				}
			}
		}
		if (!found)
			TextCollector.Add("<font color = white>Что?<br>\n");
	}

	public void tick() {
	}

	public String getDescription() {
		return description;
	}
}
