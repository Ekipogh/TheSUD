package items;

public class Hand extends Weapon {

	public Hand() {
		setModamp(0);
		setModstr(0);
		setMinimalstr(0);
		skills = new String[2];
		skills[0] = "fighting";
		skills[1] = "karate";
		setName("кулак");
		setDefaultskill("agilityplus0");
	}
}
