package utils;

import items.Item;

import java.util.List;

import javax.swing.JTextField;
import sud.MyTextPane;
import sud.SudGame;

import entities.Entity;
import entities.Player;

public class Trigger {
	// private static JTextField input;
	private static List<Entity> entities;
	private static List<Item> items = SudGame.items;
	private static Player p;
	static final int NORTH = 0;
	static final int SOUTH = 1;
	static final int EAST = 2;
	static final int WEST = 3;

	public Trigger(JTextField input, MyTextPane out, Player p) {
		super();
		// Trigger.input = input;
		Trigger.p = p;
	}

	public static void trig(int n, Object caller) {
		switch (n) {
		case 0:
			TextCollector.Add("<font color=white>" + p.getRoom().getName()
					+ "<br>\n" + p.getRoom().InitText() + "<br>\n");
			TextCollector.Add("<font color=white>Выходы: "
					+ "<font color=yellow>" + p.getRoom().exitstext()
					+ "<br>\n");
			TextCollector.Add("<font color = white>Здоровье: "
					+ p.getHealthHTML() + "<br>\n");
			for (Entity e : entities) {
				if (e != p && e.getRoom() == p.getRoom())
					TextCollector.Add("<font color = red>" + e.getName()
							+ " стоит здесь.<br>\n");
			}
			for (Item item : items) {
				if (item.getRoom() == p.getRoom())
					TextCollector.Add("<font color = red>" + item.getName()
							+ " лежит здесь.<br>\n");
			}
			break;
		case 1:
			if (p.getRoom().getExit(NORTH) != null) {
				p.setRoom(p.getRoom().getExit(NORTH));
				Trigger.trig(0, null);
			} else
				TextCollector.Add("<font color=white>Пойти куда?<br>\n");
			break;
		case 2:
			if (p.getRoom().getExit(SOUTH) != null) {
				p.setRoom(p.getRoom().getExit(SOUTH));
				Trigger.trig(0, null);
			} else
				TextCollector.Add("<font color=white>Пойти куда?<br>\n");
			break;
		case 3:
			if (p.getRoom().getExit(EAST) != null) {
				p.setRoom(p.getRoom().getExit(EAST));
				Trigger.trig(0, null);
			} else
				TextCollector.Add("<font color=white>Пойти куда?<br>\n");
			break;
		case 4:
			if (p.getRoom().getExit(WEST) != null) {
				p.setRoom(p.getRoom().getExit(WEST));
				Trigger.trig(0, null);
			} else
				TextCollector.Add("<font color=white>Пойти куда?<br>\n");
			break;
		case 5:
			TextCollector.Add("<font color=white>Ауч!<br>\n");
			break;
		case 6:
			TextCollector
					.Add("<font color=white>Старик: говорит \"приветствие игрока\"<br>\n");
			break;
		case 7:
			Script.script(1, caller, SudGame.rooms.get(1));
			break;
		default:
			TextCollector.Add("Упс нет такого тригера Trigger number = " + n
					+ "\n");
		}
	}

	// public static void setInput(JTextField input) {
	// Trigger.input = input;
	// }

	public static void setPlayer(Player p) {
		Trigger.p = p;
	}

	public static void setEntities(List<Entity> entities) {
		Trigger.entities = entities;
	}

}
