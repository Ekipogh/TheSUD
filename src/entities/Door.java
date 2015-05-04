package entities;

import sud.SudGame;
import gameworld.Room;

public class Door extends Entity {
	// private Room r;
	private Room room2;
	private int direction;
	private boolean opened = false;

	public Door() {
		super();
		this.Name = "�����";
	}

	public Door(int direction, Room r1, Room r2) {
		super(r1);
		this.Name = "�����";
		this.room2 = r2;
		this.room2 = r2;
		this.direction = direction;
	}

	public void open() {
		if (!opened) {
			getRoom().setExit(direction, room2);
			room2.setExit(direction == 2 || direction == 4 ? direction - 1
					: direction + 1, getRoom());
		}
	}

	public void command(String c) {
		if (c.equals("�������"))
			open();
		SudGame.text.Add(this.Name+" �������");
	}

	public boolean isOpened() {
		return opened;
	}

	public void setRoom2(Room room) {
		this.room2 = room;
	}

	public void setDir(int dir) {
		this.direction = dir;
	}

	public Room getRoom2() {
		return room2;
	}

	public int getDir() {
		return direction;
	}
}
