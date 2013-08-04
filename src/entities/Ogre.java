package entities;

import utils.MobType;
import gameworld.Room;
import items.*;

public class Ogre extends EntityLiving {

	public Ogre() {
		super();
		Name = "огр";
		getEquipment().setRighthand(new Club());
		type = MobType.Neutral;
		strength = 8;
		setHealth(13);
		getSkills().setSkill("fighting", 13);
		setBasespeed();
	}

	public Ogre(Room r) {
		super(r);
		Name = "огр";
		getEquipment().setRighthand(new Club());
		type = MobType.Neutral;
		strength = 8;
		setHealth(13);
		getSkills().setSkill("fighting", 13);
		setBasespeed();
	}

}
