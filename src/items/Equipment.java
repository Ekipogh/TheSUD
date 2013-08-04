package items;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import entities.EntityLiving;

public class Equipment {
	private Map<String, Item> equip = new HashMap<String, Item>();

	public Equipment() {
		equip.put("right_hand", null);
		equip.put("left_hand", null);
		equip.put("head", null);
		equip.put("body", null);
		equip.put("legs", null);
		equip.put("foots", null);
	}

	public void equipWEeapon(Weapon weapon, EntityLiving ent) {
		if (ent.getStrength() >= weapon.getMinimalstr()) {
			if (weapon.isOnehand())
				equip.put("right_hand", weapon);
		}
	}

	public void setRighthand(Weapon righthand) {
		equip.put("right_hand", righthand);
	}

	public Weapon getRighthand() {
		return (Weapon) equip.get("right_hand");
	}

	public String toString() {
		String eqs = "<font color=white>В данный момент вы используете:<br>";
		for (String k : equip.keySet()) {
			if (equip.get(k) != null)
				eqs += k + "--" + equip.get(k).getName() + "<br>";
		}
		return eqs + "</font>";
	}

	public int itemtoinventory(String slot, Inventory inventory) {
		if (equip.get(slot) != null) {
			if (inventory.addItem(equip.get(slot))) {
				if (slot.equals("right_hand"))
					equip.put(slot, new Hand());
				else
					equip.put(slot, null);
				return 0;
			} else {
				return 1;
			}
		}
		return 2;
	}

	public Collection<? extends Item> getAll() {
		Collection<Item> all = new HashSet<Item>();
		for (Item i : equip.values())
			if (i != null)
				all.add(i);
		return all;
	}

	public Shield getLeftHand() {
		return (Shield) equip.get("left_hand");
	}

	public void setLefthand(Shield lefthand) {
		equip.put("left_hand", lefthand);
	}
}
