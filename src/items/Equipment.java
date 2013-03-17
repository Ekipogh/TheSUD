package items;

import java.util.HashMap;
import java.util.Map;

public class Equipment {
	private Map<String, Item> equip = new HashMap<String, Item>();
	public Equipment() {
	}
	public void setRighthand(Weapon righthand) {
		equip.put("right_hand", righthand);
	}
	public Weapon getRighthand()
	{
		return (Weapon) equip.get("right_hand");
	}
	public String toString()
	{
		String eqs = "<font color=white>В данный момент вы используете:<br>";
		for(String k : equip.keySet())
		{
			eqs+=k+"--"+equip.get(k).getName()+"<br>";
		}
		return eqs+"</font>";
	}
}
