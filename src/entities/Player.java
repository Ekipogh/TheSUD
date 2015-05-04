package entities;

import utils.*;
import gameworld.Room;

public class Player extends EntityLiving {

	private int charpoints = 0;
	private boolean resting = false;

	public Player(Room r) {
		super(r);
		cantdie = true;
		type = MobType.Neutral;
		hpmax = health;
		setHpcur(hpmax);
	}

	public void tick() {
		super.tick();

		// TODO healing
		if (getHpcur() < hpmax && !isAttacking() && !isUnconscious()) {
			setHpcur(getHpcur() + (resting ? 3 : 1));
			if (getHpcur() > getHpmax())
				setHpcur(getHpmax());
		}
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

	public void Sleep() {
		setSleeping(true);
		actiontick = 30;
		TextCollector.getInstance().Add("<font color = white>Вы разделись и легли спать</font><br>");
	}
}
