package entities;

import utils.MobType;
import gameworld.Room;
import items.*;

public class Ogre extends EntityLiving {

	public Ogre(Room r) {
		super(r);
		Name = "огр";
		getEquipment().setRighthand(new Club());
		type = MobType.Agressive;
		strength = 8;
		setHealth(20);
		getSkills().setSkill("fighting", 13);
	}

}
