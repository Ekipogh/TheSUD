package entities;

import utils.MobType;
import utils.TextCollector;
import utils.Trigger;
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
	
	public void command(String c)
	{
		boolean found = false;
		if (commands != null) {
			for (int i = 0; i < commands.length; i++) {
				if (commands[i].startsWith(c.toLowerCase())) {
					found = true;
					Trigger.trig(triggers[i]);
					break;
				}
			}
		}
		if (!found)
			TextCollector.Add("<font color = white>Что?<br>\n");
	}

}
