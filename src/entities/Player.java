package entities;

//import items.*;
import utils.*;
import gameworld.Room;

public class Player extends EntityLiving {

	public Player(Room r) {
		super(r);
		commands = new String[2];
		commands[0] = "пнуть";
		triggers = new int[1];
		triggers[0] = 5;
		type = MobType.Agressive;
		hpmax = health;
		setHpcur(hpmax);
	}

	public void tick() {
		super.tick();
		if (isUnconscious() && ticks - getUnconscioustick() == 10) {
			setUnconscious(false);
			Trigger.trig(0, null);
		}
		// TODO healing
		if (getHpcur() < hpmax && !isAttacking() && !isUnconscious())
			setHpcur(getHpcur() + 1);
	}
}
