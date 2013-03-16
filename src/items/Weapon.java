package items;

public abstract class Weapon extends Item{ //TODO redo
	private int[] modifiers = new int[2];
	private int minimalstr;
	public int getModamp() {
		return modifiers[0];
	}
	public void setModamp(int modapm) {
		modifiers[0] = modapm;
	}
	public int getModstr() {
		return modifiers[1];
	}
	public void setModstr(int modstr) {
		modifiers[1] = modstr;
	}
	public int getMinimalstr() {
		return minimalstr;
	}
	public void setMinimalstr(int minimalstr) {
		this.minimalstr = minimalstr;
	}
}
