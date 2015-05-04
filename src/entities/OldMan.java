package entities;

import utils.MobType;
//import utils.TextCollector;
//import utils.Trigger;
import gameworld.Room;

public class OldMan extends EntityLiving {
	public OldMan() {
		super();
		setName("������");
		imortal = true;
		commands = new String[1];
		commands[0] = "���������";
		scripts = new String[1];
		scripts[0] = "empty";
		type = MobType.NPC;
	}

	public OldMan(Room r) {
		super(r);
		setName("������");
		imortal = true;
		commands = new String[1];
		commands[0] = "���������";
		scripts = new String[1];
		scripts[0] = "empty";
		type = MobType.NPC;
	}
}
