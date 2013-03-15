package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.MobType;
import utils.TextCollector;
import gameworld.Room;

public abstract class EntityLiving extends Entity {

	protected int hpcur;
	protected int hpmax;
	protected boolean isDead;
	protected boolean attacked;
	protected boolean imortal = false;
	protected boolean cantdie = false;
	private EntityLiving attacker;
	List<EntityLiving> allies = new ArrayList<EntityLiving>();
	private List<EntityLiving> enemies = new ArrayList<EntityLiving>();
	private boolean attacking;
	private boolean unconscious;
	private int unconscioustick;
	private Room spawn;
	protected MobType type;
	protected int strength;
	protected int agility;
	protected int intelligence;
	protected int health;
	private Random random = new Random();

	public EntityLiving(Room r) {
		super(r);
		setSpawn(r);
	}

	public void setDead(boolean b) {
		this.isDead = b;

	}

	public void tick() {
		if (ticks >= Integer.MAX_VALUE)
			ticks = 0;
		ticks++;

		if (isAttacking() && !isDead && getEnemies().size() != 0) {
			strike(getEnemies().get(0));

		}
		if (isAttacked() && !isAttacking() && type == MobType.Agressive) {
			attack(getAttacker());
			attacked = false;
		}
	}

	public void setAttacked(boolean b) {
		attacked = b;
	}

	public int getDamage() {
		int dam = 0;
		switch (strength) {
		case 1:
			dam = random.nextInt(6) - 5;
			break;
		case 2:
			dam = random.nextInt(6) - 5;
			break;
		case 3:
			dam = random.nextInt(6) - 4;
			break;
		case 4:
			dam = random.nextInt(6) - 4;
			break;
		case 5:
			dam = random.nextInt(6) - 3;
			break;
		case 6:
			dam = random.nextInt(6) - 3;
			break;
		case 7:
			dam = random.nextInt(6) - 2;
			break;
		case 8:
			dam = random.nextInt(6) - 2;
			break;
		case 9:
			dam = random.nextInt(6) - 1;
			break;
		case 10:
			dam = random.nextInt(6) - 1;
			break;
		case 11:
			dam = random.nextInt(6);
			break;
		case 12:
			dam = random.nextInt(6);
			break;
		case 13:
			dam = random.nextInt(6) + 1;
			break;
		case 14:
			dam = random.nextInt(6) + 1;
			break;
		case 15:
			dam = random.nextInt(6) + 2;
			break;
		case 16:
			dam = random.nextInt(6) + 2;
			break;
		case 17:
			dam = random.nextInt(6) + 3;
			break;
		case 18:
			dam = random.nextInt(6) + 3;
			break;
		case 19:
			dam = (random.nextInt(6) + 1) * (random.nextInt(6) + 1) - 1;
			break;
		case 20:
			dam = (random.nextInt(6) + 1) * (random.nextInt(6) + 1) - 1;
			break;
		}
		System.out.println(dam);
		return dam>=0?dam:0;
	}

	public int getHpcur() {
		return hpcur;
	}

	public int getHpmax() {
		return hpmax;
	}

	public boolean isAttacked() {
		return attacked;
	}

	public boolean isImortal() {
		return imortal;
	}

	public boolean isCantdie() {
		return cantdie;
	}

	public boolean isDead() {
		return isDead;
	}

	public void strike(EntityLiving e) {
		if (type == MobType.Agressive) {
			if (!e.isDead() && !e.isUnconscious()) {
				TextCollector.Add("<font color=white>" + Name + " ударил по "
						+ e.getName() + "<br>\n");
				e.damage(getDamage());
			} else
				setAttacking(false);
		}

	}
	


	public void damage(int dam) {
		if (!imortal) {
			if (hpcur > 0) {
				hpcur -= dam;
				if (isUnconscious() || isDead())
					System.err.println("shmip");
				TextCollector.Add("<font color=white>Здоровье: " + Name + " "
						+ getHealthHTML() + "<br>\n");
				if (hpcur <= 0 && this.getClass() != Player.class) {
					if (!cantdie) {
						isDead = true;
						if (attacked)
							attacked = false;
						if (attacking) {
							attacking = false;
						}
						attacker.getEnemies().remove(this);
						TextCollector.Add("<font color=white>" + Name
								+ ": Помераю..<br>");
					} else {
						if (attacked)
							attacked = false;
						setUnconscious(true);
						setUnconscioustick(ticks);
					}
				} else if (hpcur <= 0 && this.getClass() == Player.class) {
					TextCollector
							.Add("<font color=yellow>Вы бессознания. Вы придете в себя через 10 сек<br>\n");
					setRoom(spawn); // TODO change to respawn
					if (attacked)
						attacked = false; // room
					if (attacking) {
						attacking = false;
					}
					setUnconscious(true);
					setUnconscioustick(ticks);
				}
			}
		} else
			TextCollector.Add("<font color=yellow>" + Name
					+ " не пострадал. Он бесмертнный же..<br>\n");
	}

	public EntityLiving getAttacker() {
		return attacker;
	}

	public void setAttacker(EntityLiving attacker) {
		this.attacker = attacker;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isUnconscious() {
		return unconscious;
	}

	public void setUnconscious(boolean unconscious) {
		this.unconscious = unconscious;
	}

	public int getUnconscioustick() {
		return unconscioustick;
	}

	public void setUnconscioustick(int unconscioustick) {
		this.unconscioustick = unconscioustick;
	}

	public String getHealthHTML() {
		if (hpcur >= hpmax * 0.80)
			return "<font color=#006400>Отличное";
		else if (hpcur >= hpmax * 0.60 && hpcur < hpmax * 0.80)
			return "<font color=green>Хорошее";
		else if (hpcur >= hpmax * 0.40 && hpcur < hpmax * 0.60)
			return "<font color=yellow>Хорошее";
		else if (hpcur >= hpmax * 0.20 && hpcur < hpmax * 0.40)
			return "<font color=red>Плохое";
		else
			return "<font color=#8b0000>Плохое";
	}

	public Room getSpawn() {
		return spawn;
	}

	public void attack(EntityLiving e) {
		setAttacking(true);
		e.setAttacked(true);
		e.setAttacker(this);
		getEnemies().add(e);
	}

	public void setSpawn(Room spawn) {
		this.spawn = spawn;
	}

	public List<EntityLiving> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<EntityLiving> enemies) {
		this.enemies = enemies;
	}

}
