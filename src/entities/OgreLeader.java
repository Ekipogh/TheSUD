package entities;

import items.Club;
import sud.SudGame;
import utils.MobType;
import gameworld.Room;

public class OgreLeader extends EntityLiving {

	public OgreLeader(Room r) {
		super();
		Name = "Вожак огров";
		getEquipment().setRighthand(new Club());
		type = MobType.Neutral;
		strength = 11;
		setHealth(15);
		getSkills().setSkill("fighting", 16);
		setBasespeed();
	}

	public OgreLeader() {
		super();
		Name = "вожак огров";
		getEquipment().setRighthand(new Club());
		type = MobType.Neutral;
		strength = 11;
		setHealth(15);
		getSkills().setSkill("fighting", 16);
		setBasespeed();
	}

	public void tick() {
		for (Entity e : SudGame.entities) {
			if (e.getClass() == Ogre.class
					&& e.getRoom().equals(this.getRoom())
					&& (((EntityLiving) e).getEnemies().size() > 0)) {
				if (!getEnemies().contains(e))
					attack(((EntityLiving) e).getEnemies().get(0));
			}
		}
		super.tick();
	}
}
