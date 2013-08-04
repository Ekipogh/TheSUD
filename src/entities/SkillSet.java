package entities;

import java.util.HashMap;
import java.util.Map;

public class SkillSet {
	private Map<String, Float> skillMap = new HashMap<String, Float>();

	public SkillSet(EntityLiving o) {
		skillMap.put("fighting", .0f);
		skillMap.put("short_blades", o.agility - 4.f);
		skillMap.put("karate", .0f);
		skillMap.put("shields", o.agility - 4.f);
	}

	public int getSkill(String ss) {
		return skillMap.get(ss).intValue();
	}

	public void setSkill(String string, int i) {
		skillMap.put(string, new Float(i));
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

	public Map<String, Float> getAll() {
		return skillMap;
	}

	public boolean increase(String skill) {
		int sc = getSkill(skill);
		skillMap.put(
				skill,
				skillMap.get(skill)
						+ 1
						/ (50 * (skillMap.get(skill) + 1) - skillMap.get(skill)));
		return getSkill(skill) - sc == 1;
	}

	public Float getSkillf(String skill) {
		return skillMap.get(skill);
	}

	public void setSkillf(String skill, float val) {
		skillMap.put(skill, val);
	}
}
