package items;

public class Sword extends Weapon {

	public Sword() {
		setDescription("������� ���");
		setName("���");
		setOnehand(true);
		setMinimalstr(8);
		setModamp(2);
		setModstr(1);
		skills = new String[1];
		skills[0] = "short_blades";
		setDefaultskill("agilityplus-4");
	}
}
