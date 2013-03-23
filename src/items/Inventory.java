package items;

public class Inventory {
	private Item[] slots;

	public Inventory() {
		slots = new Item[16];
	}

	public void setItem(Item item, int slot) {
		if (slot >= 0 && slot < slots.length)
			slots[slot] = item;
	}

	public Item getItem(int slot) {
		if (slot >= 0 && slot < slots.length)
			return slots[slot];
		return null;
	}

	public boolean addItem(Item item) {
		for (int i = 0; i < slots.length; i++)
			if (slots[i] == null) {
				setItem(item, i);
				return true;
			}
		return false;
	}

	public String toString() {
		String inv = "<font color=white>В вашем рюкзаке лежит:<br>";
		for (Item i : slots) {
			if (i != null)
				inv += i.getName() + "<br>";
		}
		return inv + "</font>";
	}

	public Item getItem(String name) {
		for (Item i : slots)
			if (i != null)
				if (i.Имя().equals(name))
					return i;
		return null;
	}
}
