package items;

import entities.SUDObject;
import gameworld.Room;

public class Item implements SUDObject {
	private String decription;
	private String name;
	private Room r;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return decription;
	}

	public void setDescription(String decription) {
		this.decription = decription;
	}

	public Room getRoom() {
		return r;
	}

	public Item setRoom(Room r) {
		this.r = r;
		return this;
	}

	public String Èìÿ() {
		return name;
	}
}
