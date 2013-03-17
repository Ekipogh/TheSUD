package entities;

//import items.*;
import utils.*;
import gameworld.Room;

public class Player extends EntityLiving {

	public Player(Room r) {
		super(r);
		Name = "�����";
		commands = new String[2];
		commands[0] = "�����";
		triggers = new int[1];
		triggers[0] = 5;
		type = MobType.Agressive;
		strength = 11;
		health = 15;
		hpmax = health;
		setHpcur(hpmax);
//		getEquipment().setRighthand(new Sword());
	}

	
	public void tick() {
		super.tick();
		if (isUnconscious() && ticks - getUnconscioustick() == 10) {
			setUnconscious(false);
			Trigger.trig(0,null);
		}
		// TODO healing
		if (getHpcur() < hpmax && ticks % (int) (60 / hpmax) == 0 && !isAttacking()
				&& !isUnconscious())
			setHpcur(getHpcur() + 1);
	}
}
