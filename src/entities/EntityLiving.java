package entities;

import items.Equipment;
import items.Hand;
import items.Inventory;
import items.Shield;
import items.Weapon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import sud.SudGame;
import utils.MobType;
import utils.TextCollector;
import gameworld.Room;

public abstract class EntityLiving extends Entity {

	private int hpcur;
	protected int hpmax;
	protected boolean isDead;
	protected boolean attacked;
	protected boolean imortal = false;
	protected boolean cantdie = false;
	private EntityLiving attacker;
	Set<EntityLiving> allies = new HashSet<EntityLiving>();
	private List<EntityLiving> enemies = new ArrayList<EntityLiving>();
	private boolean attacking;
	private boolean unconscious;
	private int unconscioustick;
	private Room spawn;
	protected MobType type;
	protected int strength;
	protected int agility;
	private SkillSet skills;
	protected int actiontick;
	private boolean sleeping;
	protected int Sex;

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getAgility() {
		return agility;
	}

	public void setAgility(int agility) {
		this.agility = agility;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
		hpmax = health;
		setHpcur(hpmax);
	}

	protected int intelligence;
	protected int health;
	private Random random = new Random();
	private Inventory inventory;
	private Equipment equipment;
	private float basespeed;

	public EntityLiving(Room r) {
		super(r);
		isDead = false;
		inventory = new Inventory();
		setEquipment(new Equipment());
		setSkills(new SkillSet(this));
		equipment.setRighthand(new Hand());
		setSpawn(r);
	}

	public EntityLiving() {
		super();
		isDead = false;
		inventory = new Inventory();
		setEquipment(new Equipment());
		setSkills(new SkillSet(this));
		equipment.setRighthand(new Hand());
	}

	public void setDead(boolean b) {
		this.isDead = b;

	}

	public void tick() {
		// TODO: заплатки
		if (getTicks() >= Integer.MAX_VALUE) // TODO: Нужно ли... Пока нужно(
			setTicks(0);
		setTicks(getTicks() + 1);
		if (sleeping) {
			actiontick--;
			if (actiontick == 0) {
				sleeping = false;
			}
		}
		if (!isDead && !unconscious) {
			if (type == MobType.Neutral) { // TODO: allies
				if (attacked) {
					attack(attacker);
				}
				if (enemies.size() > 0) {
					strike(enemies.get(0));
				} else
					attacking = false;
			}
		} else {
			attacking = false;
			enemies.clear();
		}
		if (isUnconscious() && getTicks() - getUnconscioustick() == 10) {
			setUnconscious(false);
			if (getClass() == Player.class)
				SudGame.updateOut();
		}
	}

	public void setAttacked(boolean b) {
		attacked = b;
	}

	private int getDamage(boolean type) {
		int dam = 0;
		if (type) {
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
				dam = random.nextInt(6) + 4;
				break;
			case 19:
				dam = random.nextInt(6) + random.nextInt(6) + 1;
				break;
			case 20:
				dam = random.nextInt(6) + random.nextInt(6) + 1;
				break;
			}
			dam += getEquipment().getRighthand().getModstr();
		} else {
			switch (strength) {
			case 1:
				dam = random.nextInt(6) - 4;
				break;
			case 2:
				dam = random.nextInt(6) - 4;
				break;
			case 3:
				dam = random.nextInt(6) - 3;
				break;
			case 4:
				dam = random.nextInt(6) - 3;
				break;
			case 5:
				dam = random.nextInt(6) - 2;
				break;
			case 6:
				dam = random.nextInt(6) - 2;
				break;
			case 7:
				dam = random.nextInt(6) - 1;
				break;
			case 8:
				dam = random.nextInt(6) - 1;
				break;
			case 9:
				dam = random.nextInt(6);
				break;
			case 10:
				dam = random.nextInt(6) + 1;
				break;
			case 11:
				dam = random.nextInt(6) + 2;
				break;
			case 12:
				dam = random.nextInt(6) + 3;
				break;
			case 13:
				dam = random.nextInt(6) + random.nextInt(6) + 1;
				break;
			case 14:
				dam = random.nextInt(6) + random.nextInt(6) + 2;
				break;
			case 15:
				dam = random.nextInt(6) + random.nextInt(6) + 3;
				break;
			case 16:
				dam = random.nextInt(6) + random.nextInt(6) + 3;
				break;
			case 17:
				dam = random.nextInt(6) + random.nextInt(6) + random.nextInt(6)
						+ 2;
				break;
			case 18:
				dam = random.nextInt(6) + random.nextInt(6) + random.nextInt(6)
						+ 3;
				break;
			case 19:
				dam = random.nextInt(6) + random.nextInt(6) + random.nextInt(6)
						+ 4;
				break;
			case 20:
				dam = random.nextInt(6) + random.nextInt(6) + random.nextInt(6)
						+ 5;
				break;
			}
			dam += getEquipment().getRighthand().getModamp();
		}
		System.out.println(dam);
		return dam >= 0 ? dam : 0;
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
		TextCollector text = TextCollector.getInstance();
		if (!e.isDead() && !e.isUnconscious()) {
			int hit = striked(this.getEquipment().getRighthand());
			if (hit == 1) {
				if (!e.defended(2)) {
					text.Add("<font color=white>" + Name + " ударил по "
							+ e.getName() + "<br>\n");
					e.damage(getDamage(random.nextBoolean()));
					if (this.getSkills().increase(
							getEquipment().getRighthand().getSkills()[0]))
						text.Add("<font color = white>" + this.Name
								+ " улучшил текущий оружейный навык<br></font>");
				} else
					text.Add("<font color=white>" + e.getName()
							+ " отразил атаку " + getName() + "<br>\n");
			} else if (hit == 2) {
				e.damage(getDamage(random.nextBoolean()));
				text.Add("<font color=white>" + Name
						+ " <font color = red>критически</font> ударил по "
						+ e.getName() + "<br>\n");
				if (this.getSkills().increase(
						getEquipment().getRighthand().getSkills()[0]))
					text.Add("<font color = white>" + this.Name
							+ " улучшил текущий оружейный навык<br></font>");
			} else if (hit == 3) {
				e.damage(getCritical(random.nextBoolean()));
				text.Add("<font color=white>" + Name + " критически ударил по "
						+ e.getName() + "<br>\n");
				if (this.getSkills().increase(
						getEquipment().getRighthand().getSkills()[0]))
					text.Add("<font color = white>" + this.Name
							+ " улучшил текущий оружейный навык<br></font>");
			} else
				text.Add("<font color=white>" + this.Name + " промахнулся по "
						+ e.Name + "</font><br>");
			if (e.isDead || e.unconscious)
				enemies.remove(e);
		} else {
			enemies.remove(e);
		}
	}

	private boolean defended(int type) {
		int diceroll = random.nextInt(6) + random.nextInt(6)
				+ random.nextInt(6) + 3;
		int defendval = 0;
		switch (type) {
		case 2:
			Shield sh = this.getEquipment().getLeftHand();
			if (sh != null) {
				defendval = 3 + (int) (getSkills().getSkill("shields") / 2)
						+ sh.getBlockingVal();
				break;
			} else
				type = random.nextInt(2);

		case 0:
			defendval = (int) getBasespeed() + 3;
			break;
		case 1:
			defendval = (int) getHighestSkill(equipment.getRighthand()) + 3;
			break;
		}
		if (diceroll == 17 || diceroll == 18)
			return false;
		else if (diceroll == 3 || diceroll == 5 || diceroll <= defendval) {
			if (type == 2)
				this.getSkills().increase("shields");
			return true;
		} else
			return false;
	}

	private int getCritical(boolean type) {
		int dam = 0;
		if (type) {
			switch (strength) {
			case 1:
				dam = 0;
				break;
			case 2:
				dam = 0;
				break;
			case 3:
				dam = 1;
				break;
			case 4:
				dam = 1;
				break;
			case 5:
				dam = 2;
				break;
			case 6:
				dam = 2;
				break;
			case 7:
				dam = 3;
				break;
			case 8:
				dam = 3;
				break;
			case 9:
				dam = 4;
				break;
			case 10:
				dam = 4;
				break;
			case 11:
				dam = 5;
				break;
			case 12:
				dam = 5;
				break;
			case 13:
				dam = 6;
				break;
			case 14:
				dam = 6;
				break;
			case 15:
				dam = 7;
				break;
			case 16:
				dam = 7;
				break;
			case 17:
				dam = 8;
				break;
			case 18:
				dam = 8;
				break;
			case 19:
				dam = 11;
				break;
			case 20:
				dam = 11;
				break;
			}
			dam += getEquipment().getRighthand().getModstr();
		} else {
			switch (strength) {
			case 1:
				dam = 1;
				break;
			case 2:
				dam = 1;
				break;
			case 3:
				dam = 2;
				break;
			case 4:
				dam = 2;
				break;
			case 5:
				dam = 3;
				break;
			case 6:
				dam = 3;
				break;
			case 7:
				dam = 4;
				break;
			case 8:
				dam = 4;
				break;
			case 9:
				dam = 5;
				break;
			case 10:
				dam = 6;
				break;
			case 11:
				dam = 7;
				break;
			case 12:
				dam = 8;
				break;
			case 13:
				dam = 11;
				break;
			case 14:
				dam = 12;
				break;
			case 15:
				dam = 13;
				break;
			case 16:
				dam = 14;
				break;
			case 17:
				dam = 17;
				break;
			case 18:
				dam = 18;
				break;
			case 19:
				dam = 19;
				break;
			case 20:
				dam = 20;
				break;
			}
			dam += getEquipment().getRighthand().getModamp();
		}
		System.out.println(dam);
		return dam >= 0 ? dam : 0;
	}

	private int striked(Weapon righthand) {
		int diceroll = random.nextInt(6) + random.nextInt(6)
				+ random.nextInt(6) + 3;
		int effectiveskill = getHighestSkill(righthand);
		if (diceroll == 3)
			return 3;
		else if (diceroll == 4 || (effectiveskill >= 15 && diceroll == 5)
				|| (effectiveskill >= 16 && diceroll == 6))
			return 2;
		else if (diceroll <= effectiveskill)
			return 1;
		else
			return 0;
	}

	private int getHighestSkill(Weapon righthand) {
		int defaultskill = 0;
		String atribute = righthand.getDefaultskill().split("plus")[0];
		int modifier = Integer.parseInt(righthand.getDefaultskill().split(
				"plus")[1]);
		switch (atribute) {
		case "agility":
			defaultskill = agility;
			break;
		case "strength":
			defaultskill = strength;
			break;
		case "intelligence":
			defaultskill = intelligence;
			break;
		}
		defaultskill += modifier;
		int max = 0;
		for (String s : righthand.getSkills())
			if (getSkills().getSkill(s) > max)
				max = getSkills().getSkill(s);
		return max > defaultskill ? max : defaultskill;
	}

	public void damage(int dam) {
		TextCollector text = TextCollector.getInstance();
		if (!imortal) {
			if (getHpcur() > 0) {
				setHpcur(getHpcur() - dam);
				if (isUnconscious() || isDead())
					System.err.println("shmip");
				text.Add("<font color=white>Здоровье: " + Name + " "
						+ getHealthHTML() + "<br>\n");
				if (getHpcur() <= 0) {
					if (!cantdie) {
						isDead = true;
						text.Add("<font color=white>" + Name
								+ " испустил дух..<br>");
					} else {
						if (this.getClass() == Player.class) {
							text.Add("<font color=yellow>Вы бессознания. Вы придете в себя через 10 сек<br>\n");
							setRoom(spawn);
						}

						setUnconscious(true);
						setUnconscioustick(getTicks());
					}
					if (attacking) {// После драки кулаками не машут
						attacking = false;
					}
				}
			}
		} else
			text.Add("<font color=yellow>" + Name
					+ "совершенно не пострадал.<br>\n");
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
		if (getHpcur() >= hpmax * 0.80)
			return "<font color=#006400>Отличное";
		else if (getHpcur() >= hpmax * 0.60 && getHpcur() < hpmax * 0.80)
			return "<font color=green>Хорошее";
		else if (getHpcur() >= hpmax * 0.40 && getHpcur() < hpmax * 0.60)
			return "<font color=yellow>Хорошее";
		else if (getHpcur() >= hpmax * 0.20 && getHpcur() < hpmax * 0.40)
			return "<font color=red>Плохое";
		else
			return "<font color=#8b0000>Плохое";
	}

	public Room getSpawn() {
		return spawn;
	}

	public void attack(EntityLiving e) {
		if (!enemies.contains(e)) {
			attacking = true;
			attacked = false;
			if (!e.enemies.contains(this)) {
				e.setAttacked(true);
				e.setAttacker(this);
			}
			enemies.add(e);
		}
		attacker = null;
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

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public void setHpcur(int hpcur) {
		this.hpcur = hpcur;
	}

	public float getBasespeed() {
		return basespeed;
	}

	public void setBasespeed() {
		this.basespeed = (agility + health) / 4;
	}

	public SkillSet getSkills() {
		return skills;
	}

	public void setSkills(SkillSet skills) {
		this.skills = skills;
	}

	public String toString() {
		return Name + " " + hpcur + " / " + hpmax;
	}

	public boolean isSleeping() {
		return sleeping;
	}

	public void setSleeping(boolean sleeping) {
		this.sleeping = sleeping;
	}
}
