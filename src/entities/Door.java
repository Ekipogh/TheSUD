package entities;

import gameworld.Room;

public class Door extends Entity{
	private Room r;
	private Room room2;
	private int direction;
	private boolean opened = false;

	public Door(int direction, Room r1, Room r2) {
		super(r1);
		this.Name = "врата";
		this.room2 = r2;
		this.room2 = r2;
		this.direction = direction;
		commands = new String[1];
		commands[0] = "открыть"; //TODO redo!!!!!!!!!
		triggers = new int[1];
		triggers[0] = 7;
	}

	public void open() {
		if (!opened) {
			r.setExit(direction, room2);
			room2.setExit(direction == 2 || direction == 4 ? direction - 1
					: direction + 1, r);
		}
	}
	public boolean isOpened()
	{
		return opened;
	}
}
