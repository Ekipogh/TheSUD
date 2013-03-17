package items;

import gameworld.Room;

public class Item {
	private String decription;
	private String name;
	private Room r;
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
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
