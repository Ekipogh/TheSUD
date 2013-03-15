package entities;

import utils.MobType;
import gameworld.Room;

public class Ogre extends EntityLiving {

	public Ogre(Room r) {
		super(r);
		Name = "огр";
		
		isDead = false;
		type = MobType.Agressive;
		strength = 8;
		health = 13;
		hpmax = health;
		hpcur = hpmax;
	}

}
