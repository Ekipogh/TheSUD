package entities;

import sud.SudGame;
import ai.Pesant;

public class TestDumy extends NPC {
	public TestDumy() {
		commands = new String[2];
		commands[0] = "enter";
		commands[1] = "leave";
		scripts = new String[2];
		scripts[0] = "slavegreet";
		scripts[1] = "slavebye";
		Name = "Сенья";
		Sex = 1;
		setHome(SudGame.rooms.get(0));
		setWorkplace(SudGame.rooms.get(4));
		setBehavior(new Pesant());
	}
}
