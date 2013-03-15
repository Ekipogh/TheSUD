package entities;

import utils.MobType;
import utils.Trigger;
import gameworld.Room;

public class Player extends EntityLiving {

	public Player(Room r) {
		super(r);
		Name = "�����";
		commands = new String[1];
		commands[0] = "�����";
		triggers = new int[1];
		triggers[0] = 5;
		type = MobType.Agressive;
		strength = 11;
		health = 15;
		hpmax = health;
		hpcur = hpmax;
	}

	
	public void tick() {
		super.tick();
		if (isUnconscious() && ticks - getUnconscioustick() == 10) {
			setUnconscious(false);
			Trigger.trig(0);
		}
		// TODO healing
		if (hpcur < hpmax && ticks % (int) (60 / hpmax) == 0 && !isAttacking()
				&& !isUnconscious())
			hpcur++;
	}
}
