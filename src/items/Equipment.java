package items;

public class Equipment {
	private Weapon righthand;
	public Equipment() {
	}
	public void setRighthand(Item righthand) {
		this.righthand = (Weapon) righthand;
	}
	public Weapon getRighthand()
	{
		return righthand;
	}
}
