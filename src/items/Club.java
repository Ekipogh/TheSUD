package items;

public class Club extends Weapon {

	public Club() {
		setDescription("������� ������");
		setName("������");
		setOnehand(true);
		setMinimalstr(7);
		setModamp(0);
		setModstr(0);
		skills = new String[1];
		skills[0] = "fighting";
		setDefaultskill("agilityplus0");
	}

}
