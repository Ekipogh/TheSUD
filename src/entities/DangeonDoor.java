package entities;

import gameworld.Room;

public class DangeonDoor extends Entity {

	public DangeonDoor(Room r) {
		super(r);
		Name = "������";
		commands = new String[1];
		commands[0] = "�������"; //TODO redo!!!!!!!!!
		triggers = new int[1];
		triggers[0] = 7;
	}
}
