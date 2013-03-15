package utils;

public enum MobType {
	Passive(0), Neutral(1), Agressive(2), NPC(3);
	public int n;
	
	MobType(int n)
	{
		this.n = n;
	}

}
