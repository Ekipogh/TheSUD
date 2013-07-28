package entities;

import utils.MobType;
//import utils.TextCollector;
//import utils.Trigger;
import gameworld.Room;

public class OldMan extends EntityLiving {

	public OldMan(Room r) {
		super(r);
		setName("старик");
		imortal = true;
		commands = new String[1];
		commands[0] = "послушать";
		triggers = new int[1];
		triggers[0] = 6;
		type = MobType.NPC;
	}
}
