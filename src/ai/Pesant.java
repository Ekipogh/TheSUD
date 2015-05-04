package ai;

import sud.Time;
import entities.NPC;

public class Pesant extends Behavior {
	public void tick(NPC npc) {
		if (Time.getHours() >= 8 && Time.getHours() < 17)
			npc.setRoom(npc.getWorkplace());
		else if (Time.getHours() < 8 || Time.getHours()>=17) { // Свободное время
			npc.setRoom(npc.getHome());
		}

	}
}
