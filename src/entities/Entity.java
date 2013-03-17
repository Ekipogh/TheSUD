package entities;

import sud.MyTextPane;
import sud.SudGame;
import utils.TextCollector;
import utils.Trigger;
import gameworld.Room;

public abstract class Entity {
	private Room r;
	protected String Name;
	protected int ticks;
	protected MyTextPane out;
	protected String[] commands;
	protected int[] triggers;

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Entity(Room r) {
		super();
		this.ticks = 0;
		out = SudGame.w.out;
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

	

}
