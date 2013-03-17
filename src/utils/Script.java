package utils;

import sud.SudGame;
import entities.*;
import gameworld.*;

public class Script {

	public static void script(int n, Object... objects)
	{
		switch(n)
		{
		case 0:
			((Player)objects[0]).setHpcur(0);
			break;
		case 1://TODO redo!!!!!!!!!
			TextCollector.Add("<font color=white>Вы открыль ворота в подземелье<br></font>");
			((Entity)objects[0]).setName("Открытые ворота");
			((Room) objects[1]).setExit(0,SudGame.rooms.get(3));
			Trigger.trig(0, null);
			break;
		}
	}
}
