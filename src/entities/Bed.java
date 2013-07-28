package entities;

import gameworld.Room;

public class Bed extends Entity {

	public Bed() {
		// TODO Auto-generated constructor stub
	}

	public Bed(Room r) {
		super(r);
		commands = new String[2];
		commands[0] = "лечь";
		commands[1] = "встать";
		Name = "кровать";
		triggers = new int[2];
		triggers[0] = 8;
		triggers[1] = 9;
	}

	public Bed(Room r, String name) {
		super(r, name);
		// TODO Auto-generated constructor stub
	}

}
