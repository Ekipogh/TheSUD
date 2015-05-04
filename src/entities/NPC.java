package entities;

import ai.Behavior;
import gameworld.Room;

public class NPC extends EntityLiving {
	private Room home;
	private Room workplace;
	private Behavior behavior;
	public NPC(Room r) {
		super(r);
	}

	public NPC() {
	}

	public Room getHome() {
		return home;
	}

	public void setHome(Room home) {
		this.home = home;
	}

	public Room getWorkplace() {
		return workplace;
	}

	public void setWorkplace(Room workplace) {
		this.workplace = workplace;
	}
	@Override
	public void tick() {
		getBehavior().tick(this);
		super.tick();
	}

	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}
}
