package items;

public abstract class Weapon extends Item{ //TODO redo
	private int[] modifiers = new int[2];
	private int minimalstr;
	private boolean onehand;
	protected String[] skills;
	private String defaultskill;
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
	public boolean isOnehand() {
		return onehand ;
	}
	public void setOnehand(boolean isonehand)
	{
		onehand=isonehand;
	}
	public String getDefaultskill() {
		return defaultskill;
	}
	public void setDefaultskill(String defaultskill) {
		this.defaultskill = defaultskill;
	}
	public String[] getSkills() {
		return skills;
	}
	public void setSkills(String[] skills) {
		this.skills = skills;
	}
}
