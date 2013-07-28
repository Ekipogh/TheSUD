package entities;

import java.util.HashMap;
import java.util.Map;

public class SkillSet {
	private Map<String, Integer> skillMap = new HashMap<String, Integer>();

	public SkillSet(EntityLiving o) {
		skillMap.put("fighting", 0);
		skillMap.put("short_blades", o.agility - 4);
		skillMap.put("karate", 0);
	}

	public int getSkill(String ss) {
		return skillMap.get(ss);
	}

	public void setSkill(String string, int i) {
		skillMap.put(string, i);
	}

	public String toString() {
		String ret = "<font color=white>";
		for (String s : skillMap.keySet())
			ret += s + " -- " + skillMap.get(s) + "<br>";
		return ret + "</font><br>";
	}

	public String getAttribute(String skill) {
		switch (skill) {
		case "short_blades":
			return "agility";
		case "fighting":
			return "agility";
		case "karate":
			return "agility";
		}
		return "donotknow";
	}

	public int getDifficulty(String skill) {
		switch (skill) {
		case "short_blades":
			return 1;
		case "fighting":
			return 0;
		case "karate":
			return 2;
		}
		return -1;
	}

	public  Map<String, Integer> getAll() {
		return skillMap;
	}
}
