package entities;

//import items.*;
import utils.*;
import gameworld.Room;

public class Player extends EntityLiving {

	private int charpoints = 0;
	private boolean resting = false;
	private int Sex;

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
			setHpcur(getHpcur() + (resting ? 3 : 1));
	}

	public int getCharpoints() {
		return charpoints;
	}

	public void setCharpoints(int charpoints) {
		this.charpoints = charpoints;
	}

	public void setResting(boolean r) {
		resting = true;
	}

	public void setSex(int s) {
		if (s >= 0 && s <= 2)
			this.Sex = s;
	}

	public int getSex() {
		return Sex;
	}

	public void calculateSkills() {
		this.setSkills(null);
		this.setSkills(new SkillSet(this));
	}
}
