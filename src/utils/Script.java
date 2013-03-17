package utils;

import entities.Player;

public class Script {
	
	public static void script(int n, Object... objects)
	{
		switch(n)
		{
		case 0:
			((Player)objects[0]).setHpcur(0);
		}
	}
}
