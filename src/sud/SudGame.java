package sud;

import entities.*;
import gameworld.Room;

import items.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import utils.*;

public class SudGame {

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Item> items = new ArrayList<Item>();
	public static List<Room> rooms = new ArrayList<Room>();
	public static Window w;
	public static Player p;
	private static boolean running = true;
	private static String[] commands;
	private static int[] triggers;

	public static void main(String[] args) {
		w = new Window();

		loadRooms();
		loadPlayer();

		// TODO load save or whatever..
		entities.add(new Ogre(rooms.get(2)));
		entities.add(new Ogre(rooms.get(2)));
		entities.add(new OldMan(rooms.get(0)));
		entities.add(new DangeonDoor(rooms.get(1)));
		items.add(new Sword().setRoom(rooms.get(0)));
		Trigger.trig(0, null);
		commands = new String[4];
		commands[0] = "север";
		commands[1] = "юг";
		commands[2] = "восток";
		commands[3] = "запад";
		triggers = new int[4];
		triggers[0] = 1;
		triggers[1] = 2;
		triggers[2] = 3;
		triggers[3] = 4;

		w.enter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				proceed();
			}
		});

		new Thread(new Runnable() {
			public void run() {
				while (running) {
					System.out.println("Лупаем Ж)");
					HashSet<Entity> toRemove = new HashSet<Entity>();
					for (Iterator<Entity> iter = entities.iterator(); iter
							.hasNext();) {
						Object elem = iter.next();
						if (elem.getClass() == EntityLiving.class)
							if (((EntityLiving) elem).isDead()
									&& !(elem instanceof Player)) {
								toRemove.add((Entity) elem);
							}
					}
					entities.removeAll(toRemove);
					for (Entity e : entities) {
						e.tick();
					}
					// TODO maybe text collector class. Collectronus 2013
					// System.out.println(w.out.getDocument().getLength());
					if (!TextCollector.isEmpty())
						w.out.addString(TextCollector.Get());
					TextCollector.Clear();
					// w.out.setCaretPosition(w.out.getDocument().getLength());
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						System.err.println("Something went wrong *sadface*");
						System.exit(1);
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		}).start();

	}

	private static void proceed() {
		String[] command = w.input.getText().split(" ");
		if (command.length == 1 && !command[0].isEmpty()) {
			if ("смотреть".startsWith(command[0].toLowerCase())
					&& !command[0].toLowerCase().equals("с"))
				Trigger.trig(0, null);
			else if ("хватит".startsWith(command[0].toLowerCase())) {
				p.setAttacking(false);
				p.getEnemies().clear();
			} else if ("инвентарь".startsWith(command[0].toLowerCase())) {
				TextCollector.Add(p.getInventory().toString());
			} else if ("экипировка".startsWith(command[0].toLowerCase())) {
				TextCollector.Add(p.getEquipment().toString());
			} else {
				boolean found = false;
				for (int i = 0; i < commands.length; i++) {
					if (commands[i].startsWith(command[0].toLowerCase())) {

						Trigger.trig(triggers[i], null);
						found = true;
						break;
					}
				}
				if (!found)
					p.getRoom().command(command[0]);
			}
		} else if (command.length == 2) {
			if ("убить".startsWith(command[0].toLowerCase())) {
				if ("себя".startsWith(command[1].toLowerCase())) {
					Script.script(0, p);
				} else {
					boolean found = false;
					for (Entity ent : entities) {
						if (ent instanceof EntityLiving
								&& ent.getName().startsWith(
										command[1].toLowerCase())
								&& ent.getRoom() == p.getRoom()) {
							found = true;
							p.attack((EntityLiving) ent);
							break;
						}
					}
					if (!found)
						TextCollector.Add("<font color=white>Убить кого?<br>");
				}
			} else if ("взять".startsWith(command[0].toLowerCase())) {
				boolean found = false;
				for (Item item : items) {
					if (item.getName().startsWith(command[1].toLowerCase())
							&& item.getRoom() == p.getRoom()) {
						found = true;
						if (p.getInventory().addItem(item)) {
							item.setRoom(null);
							TextCollector.Add("<font color=white>Вы положили "
									+ item.getName() + " в свой рюкзак<br>");
						}
						break;
					}
				}
				if (!found)
					TextCollector.Add("<font color=white>Взять что?<br>");
			} else if ("вооружиться".startsWith(command[0].toLowerCase())) {
				Item toEquip = p.getInventory().getItem(
						command[1].toLowerCase());
				Item Equiped = p.getEquipment().getRighthand();
				if (toEquip != null) {
					Item tmp = toEquip;
					toEquip = Equiped;
					Equiped = tmp;
					p.getEquipment().setRighthand((Weapon) Equiped);
					TextCollector.Add("<font color=white>Вы взяли "
							+ Equiped.Имя() + " в правую руку<br></font>");
				} else
					TextCollector
							.Add("<font color=white>Предмет не найден<br></font>");
			} else {
				boolean found = false;
				for (Entity e1 : entities) {
					if (e1.getName().startsWith(command[1].toLowerCase())
							&& e1.getRoom() == p.getRoom()) {
						found = true;
						e1.command(command[0]);
						break;
					}
				}
				if (!found)
					TextCollector.Add("<font color=white>" + command[0]
							+ " кого?<br>\n");
			}
		}
	}

	static void loadPlayer() {
		// TODO rewrite
		p = new Player(rooms.get(0));
		setUpTrigger();
		entities.add(p);
	}

	private static void setUpTrigger() {
		// Trigger.setInput(w.input);
		Trigger.setPlayer(p);
		Trigger.setEntities(entities);
	}

	static void loadRooms() {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("maps/rooms"));
		} catch (FileNotFoundException e) {
			System.err.println("Файл не найден!");
			e.printStackTrace();
		}
		int n;
		try {
			n = Integer.parseInt(file.readLine());
			for (int i = 0; i < n; i++) {
				int id;
				String name = "";
				String[] comms;
				String inittext = "";
				int[] trigs;
				String[] tmp = file.readLine().split(" ");
				id = Integer.parseInt(tmp[0]);
				{
					String[] nametmp = tmp[1].split("%20");
					for (int j = 0; j < nametmp.length; j++) {
						name += nametmp[j];
						if (j != nametmp.length - 1)
							name += " ";
					}
					String[] initsp = tmp[2].split("%30");
					for (int j = 0; j < initsp.length; j++) {
						String[] initspsp = initsp[j].split("%20");
						for (int k = 0; k < initspsp.length; k++) {
							inittext += initspsp[k] + " ";
						}
						if (j != initsp.length - 1)
							inittext += "<br>";
					}
					int numofcomms = (tmp.length - 3) / 2;
					comms = new String[numofcomms];
					trigs = new int[numofcomms];
					for (int k = 0; k < numofcomms; k++) {
						comms[k] = tmp[k + 3];
						trigs[k] = Integer.parseInt(tmp[k + 3 + numofcomms]);
					}
				}

				rooms.add(new Room(id, name, inittext, comms, trigs));
			}
			for (int j = 0; j < n; j++) {
				String tmp[] = file.readLine().split(" ");
				int id;
				Integer[] dirs = new Integer[4];
				id = Integer.parseInt(tmp[0]);
				for (int d = 1; d < 5; d++) {
					if (!tmp[d].equalsIgnoreCase("null"))
						dirs[d - 1] = Integer.parseInt(tmp[d]);
				}
				rooms.get(id)
						.setExits(
								new Room[] {
										(dirs[0] != null) ? rooms.get(dirs[0])
												: null,
										(dirs[1] != null) ? rooms.get(dirs[1])
												: null,
										(dirs[2] != null) ? rooms.get(dirs[2])
												: null,
										(dirs[3] != null) ? rooms.get(dirs[3])
												: null });
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		SudGame.running = running;
	}
}
