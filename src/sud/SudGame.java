package sud;

import entities.*;
import gameworld.Room;
import items.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

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
	private static GameStages gamestage = GameStages.Menu;
	private static int ATTRIBUTES_SCREEN = 0;
	private static int NAME_SCREEN = 1;
	private static int charcreationscreen = NAME_SCREEN;
	private static int namescreenphase = 0;
	private static List<String> commandbuffer = new ArrayList<String>(10);
	private static int commandbuffer_index = 0;

	public static void main(String[] args) {
		w = new Window();
		w.getInputContext().selectInputMethod(new Locale("ru", "RU"));
		w.input.requestFocus();
		loadRooms();
		load_ents();
		
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
		w.input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					proceed();
			}
		});
		w.input.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					w.input.setText(commandbuffer.get(commandbuffer_index));
					if (commandbuffer_index > 0) {
						commandbuffer_index--;
					}

				}
			}
		});
		w.input.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					w.input.setText(commandbuffer.get(commandbuffer_index));
					if (commandbuffer_index < commandbuffer.size() - 1) {
						commandbuffer_index++;
					}

				}
			}
		});

		TextCollector
				.Add("<font color=white>Введите соответствующюю команду:<br>"
						+ "Начать<br>" + "Подолжить</font><br>");
		new Thread(new Runnable() {
			public void run() {
				while (running) {
					System.out.println("Лупаем Ж)");
					if (gamestage == GameStages.Game) {
						HashSet<Entity> toRemove = new HashSet<Entity>();
						for (Iterator<Entity> iter = entities.iterator(); iter
								.hasNext();) {
							Object elem = iter.next();
							if (elem instanceof EntityLiving)
								if (((EntityLiving) elem).isDead()
										&& !(elem instanceof Player)) {
									toRemove.add((Entity) elem);
								}
						}
						entities.removeAll(toRemove);
						for (Entity e : entities) {
							e.tick();
						}
					}
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

	private static void load_ents() {
		try {
			BufferedReader save = new BufferedReader(new FileReader(new File(
					"saves/ents")));
			String line = null;
			while ((line = save.readLine()) != null) {
				Entity e = (Entity) Class.forName(line.split(" ")[0])
						.newInstance();
				if (line.contains("Door")) {
					((Door) e).setRoom(rooms.get(Integer.parseInt(line
							.split(" ")[1])));
					((Door) e).setRoom2(rooms.get(Integer.parseInt(line
							.split(" ")[2])));
					((Door) e).setDir(Integer.parseInt(line.split(" ")[3]));
				} else
					e.setRoom(rooms.get(Integer.parseInt(line.split(" ")[1])));
				entities.add(e);
			}
			save.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void proceed() {
		String[] command = w.input.getText().split(" ");
		if (commandbuffer.size() == 10)
			commandbuffer.remove(0);
		commandbuffer.add(w.input.getText());
		commandbuffer_index = commandbuffer.size() - 1;
		if (gamestage == GameStages.Menu) {
			if ("продолжить".startsWith(command[0].toLowerCase())) {
				loadPlayer();
				gamestage = GameStages.Game;
				Trigger.trig(0, p);
			} else if ("начать".startsWith(command[0].toLowerCase())) {
				gamestage = GameStages.CharCreation;
				p = new Player(rooms.get(0));
				p.setCharpoints(200);
				p.setStrength(10);
				p.setAgility(10);
				p.setHealth(10);
				p.setIntelligence(10);
				TextCollector
						.Add("<font color=white>Для изменения параметра введите соответсвующий символ<br>Когда будете готовы, введите <b>готово</b><br>\n");
				show_nametext();
			}
		} else if (gamestage == GameStages.Game) {
			if (command.length == 1 && !command[0].isEmpty()
					&& !p.isUnconscious()) {
				if ("смотреть".startsWith(command[0].toLowerCase())
						&& !command[0].toLowerCase().equals("с"))
					Trigger.trig(0, null);
				else if ("хватит".startsWith(command[0].toLowerCase())) {
					p.setAttacking(false);
					p.getEnemies().clear();
				} else if ("инвентарь".startsWith(command[0].toLowerCase())
						|| "рюкзак".startsWith(command[0].toLowerCase())) {
					TextCollector.Add(p.getInventory().toString());
				} else if ("экипировка".startsWith(command[0].toLowerCase())) {
					TextCollector.Add(p.getEquipment().toString());
				} else if ("отдохнуть".startsWith(command[0].toLowerCase())) {
					p.setResting(true);
					TextCollector
							.Add("<font color=white>Вы присели отдохнуть</font><br>");
				} else if ("встать".startsWith(command[0].toLowerCase())
						&& !command[0].toLowerCase().equals("в")) {
					p.setResting(false);
					TextCollector
							.Add("<font color=white>Вы перестали отдыхать и встали</font><br>");
				} else if ("конец".startsWith(command[0].toLowerCase())) {
					save_and_exit();
					gamestage = GameStages.Menu;
					entities.remove(p);
					TextCollector
							.Add("<font color=white>Введите соответствующюю команду:<br>"
									+ "Начать<br>" + "Подолжить</font><br>");
				} else if ("умения".startsWith(command[0].toLowerCase())) {
					TextCollector.Add(p.getSkills().toString());
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
							TextCollector
									.Add("<font color=white>Убить кого?<br>");
					}
				} else if ("взять".startsWith(command[0].toLowerCase())) {
					if ("все".startsWith(command[1].toLowerCase())
							|| "всё".startsWith(command[1].toLowerCase())) {
						Collection<Item> toremove = new HashSet<Item>();
						for (Item i : items) {
							if (i.getRoom() == p.getRoom()) {
								if (p.getInventory().addItem(i)) {
									toremove.add(i);
									i.setRoom(null);
									TextCollector
											.Add("<font color=white>Вы положили "
													+ i.getName()
													+ " в свой рюкзак</font><br>");
								} else {
									TextCollector
											.Add("<font color=white>В рюкзаке кончилось место</font><br>");
									break;
								}
							}
						}
						items.removeAll(toremove);
					} else {
						boolean found = false;
						for (Item item : items) {
							if (item.getName().startsWith(
									command[1].toLowerCase())
									&& item.getRoom() == p.getRoom()) {
								found = true;
								if (p.getInventory().addItem(item)) {
									item.setRoom(null);
									items.remove(item);
									TextCollector
											.Add("<font color=white>Вы положили "
													+ item.getName()
													+ " в свой рюкзак<br>");
								}
								break;
							}
						}
						if (!found)
							TextCollector
									.Add("<font color=white>Взять что?<br>");
					}
				} else if ("вооружиться".startsWith(command[0].toLowerCase())) {
					Item toEquip = p.getInventory().getItem(
							command[1].toLowerCase());
					Item Equiped = p.getEquipment().getRighthand();
					if (toEquip != null) {
						if (Equiped.getClass() == Hand.class)
							p.getInventory().setItem(null,
									p.getInventory().getSlot(toEquip));
						else
							p.getInventory().setItem(Equiped,
									p.getInventory().getSlot(toEquip));
						p.getEquipment().setRighthand((Weapon) toEquip);
						TextCollector.Add("<font color=white>Вы взяли "
								+ toEquip.getName()
								+ " в правую руку<br></font>");
					} else
						TextCollector
								.Add("<font color=white>Предмет не найден<br></font>");
				} else if ("экипировать".startsWith(command[0].toLowerCase())) {
					Item toEquip = p.getInventory().getItem(
							command[1].toLowerCase());
					Item Equiped = null;
					if (toEquip != null) {
						if (toEquip instanceof Shield) {
							Equiped = p.getEquipment().getLeftHand();
							p.getEquipment().setLefthand((Shield) toEquip);
						}
						p.getInventory().setItem(Equiped,
								p.getInventory().getSlot(toEquip));
						TextCollector.Add("<font color=white>Вы экипировали "
								+ toEquip.getName() + "<br></font>");
					}
				} else if ("смотреть".startsWith(command[0].toLowerCase())) {
					List<SUDObject> gameobjects = new ArrayList<SUDObject>();
					gameobjects.addAll(entities);
					gameobjects.addAll(items);
					for (int i = 0; i < gameobjects.size(); i++) {
						SUDObject so = gameobjects.get(i);
						if (so.getName().startsWith(command[1].toLowerCase())) {
							TextCollector.Add("<font color=white>"
									+ so.getDescription() + "<font><br>");
							break;
						}
					}
				} else if ("бросить".startsWith(command[0].toLowerCase())) {
					Item founditem = p.getInventory().getItem(
							command[1].toLowerCase());
					if (founditem != null) {
						p.getInventory().setItem(null,
								p.getInventory().getSlot(founditem));
						items.add(founditem.setRoom(p.getRoom()));
						TextCollector.Add("<font color=white>Вы бросили "
								+ founditem.getName() + " на землю</font><br>");
					} else {
						TextCollector
								.Add("<font color=white>Бросить что?</font><br>");
					}
				} else if ("убрать".startsWith(command[0].toLowerCase())) {
					if ("оружие".startsWith(command[1].toLowerCase())) {
						int result = p.getEquipment().itemtoinventory(
								"right_hand", p.getInventory());
						if (result == 0)
							TextCollector
									.Add("<font color=white>Вы убрали свое оружие в рюкзак</font><br>");
						else if (result == 1)
							TextCollector
									.Add("<font color=white>В вашем рюкзаке нет места</font><br>");
						else if (result == 2)
							TextCollector
									.Add("<font color=white>У вас нет экипированного оружия</font><br>");
					}
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
		} else if (gamestage == GameStages.CharCreation) {
			if (charcreationscreen == ATTRIBUTES_SCREEN) {
				switch (command[0]) {
				case "й":
					if (p.getCharpoints() >= 10) {
						p.setStrength(p.getStrength() + 1);
						p.setCharpoints(p.getCharpoints() - 10);
					}
					charcreationattr();
					break;
				case "ц":
					if (p.getStrength() > 1) {
						p.setStrength(p.getStrength() - 1);
						p.setCharpoints(p.getCharpoints() + 10);
					}
					charcreationattr();
					break;
				case "у":
					if (p.getCharpoints() >= 20) {
						p.setAgility(p.getAgility() + 1);
						p.setCharpoints(p.getCharpoints() - 20);
					}
					charcreationattr();
					break;
				case "к":
					if (p.getAgility() > 1) {
						p.setAgility(p.getAgility() - 1);
						p.setCharpoints(p.getCharpoints() + 20);
						charcreationattr();
					}
					charcreationattr();
					break;
				case "е":
					if (p.getCharpoints() >= 10) {
						p.setHealth(p.getHealth() + 1);
						p.setCharpoints(p.getCharpoints() - 10);
					}
					charcreationattr();
					break;
				case "н":
					if (p.getHealth() > 1) {
						p.setHealth(p.getHealth() - 1);
						p.setCharpoints(p.getCharpoints() + 10);
					}
					charcreationattr();
					break;
				case "г":
					if (p.getCharpoints() >= 20) {
						p.setIntelligence(p.getIntelligence() + 1);
						p.setCharpoints(p.getCharpoints() - 20);
					}
					charcreationattr();
					break;
				case "ш":
					if (p.getIntelligence() > 1) {
						p.setIntelligence(p.getIntelligence() - 1);
						p.setCharpoints(p.getCharpoints() + 20);
					}
					charcreationattr();
					break;
				case "готово":
					int ok = JOptionPane.showConfirmDialog(w, "Вы уверены?",
							null, JOptionPane.OK_CANCEL_OPTION);
					if (ok == JOptionPane.OK_OPTION) {
						gamestage = GameStages.Game;
						setUpTrigger();
						p.calculateSkills();
						p.setBasespeed();
						entities.add(0, p);
						Trigger.trig(0, p);
					}
					break;
				}
			} else if (charcreationscreen == NAME_SCREEN) {
				switch (namescreenphase) {
				case 0:
					if (command[0].toLowerCase().equals("м")) {
						namescreenphase = 1;
						p.setSex(0);
					} else if (command[0].toLowerCase().equals("ж")) {
						namescreenphase = 1;
						p.setSex(1);
					}
					show_nametext();
					break;
				case 1:
					p.setName(command[0]);
					charcreationscreen = ATTRIBUTES_SCREEN;
					charcreationattr();
					break;
				}
			}
		}
		w.input.setText("");
	}

	private static void save_and_exit() {
		try {
			BufferedWriter save = new BufferedWriter(new FileWriter(new File(
					"saves/save1.dat")));
			save.write("name " + p.getName() + "\r\n");
			save.write("str " + p.getStrength() + "\r\n");
			save.write("agi " + p.getAgility() + "\r\n");
			save.write("hea " + p.getHealth() + "\r\n");
			save.write("int " + p.getIntelligence() + "\r\n");
			save.write("hpc " + p.getHpcur() + "\r\n");
			save.write("room " + p.getRoom().getId() + "\r\n");
			if (p.getEquipment().getLeftHand() != null)
				save.write("lh "
						+ p.getEquipment().getLeftHand().getClass().getName()
						+ "\r\n");
			for (String skill : p.getSkills().getAll().keySet()) {
				save.write("skill " + skill + " "
						+ p.getSkills().getSkillf(skill) + "\r\n");
			}
			save.write("rh "
					+ p.getEquipment().getRighthand().getClass().getName()
					+ "\r\n");
			for (Item i : p.getInventory().getAll()) {
				String item_class = i.getClass().getName();
				save.write("inv " + item_class + "\r\n");
			}
			save.write("sex " + p.getSex() + "\r\n");
			save.close();
		} catch (IOException e) {
			System.err.println("Could not open save file!");
			e.printStackTrace();
		}
		save_entities();
	}

	private static void save_entities() {
		try {
			BufferedWriter save = new BufferedWriter(new FileWriter(new File(
					"saves/ents")));
			for (Entity e : entities) {
				if (e.getClass() != Player.class)
					if (e.getClass() == Door.class)
						save.write(e.getClass().getName() + " "
								+ e.getRoom().getId() + " "
								+ ((Door) e).getRoom2().getId() + " "
								+ ((Door) e).getDir() + "\r\n");
					else
						save.write("ent " + e.getClass().getName() + " "
								+ e.getRoom().getId() + "\r\n");
			}
			save.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private static void show_nametext() {
		switch (namescreenphase) {
		case 0:
			TextCollector
					.Add("<font color = white>Выберите ваш пол<br>[м] - Мужской<br>[ж] - Женский</font><br>");
			break;
		case 1:
			TextCollector
					.Add("<font color = white>Введите ваше имя</font><br>");
			break;
		}
	}

	static void charcreationattr() {
		TextCollector.Add("<font color=white>Очков осталось:"
				+ p.getCharpoints() + "<br>Cила: " + p.getStrength()
				+ " +[й][ц]-  10 очков<br>" + "Ловкость: " + p.getAgility()
				+ " +[у][к]-  20 очков<br>" + "Здоровье: " + p.getHealth()
				+ " +[е][н]-  10 очков<br>" + "Интелект: "
				+ p.getIntelligence() + " +[г][ш]-  20 очков</font><br>");
	}

	static void loadPlayer() {
		p = new Player(rooms.get(0));
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"saves/save1.dat")));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("name"))
					p.setName(line.split(" ")[1]);
				else if (line.startsWith("str"))
					p.setStrength(Integer.parseInt(line.split(" ")[1]));
				else if (line.startsWith("int"))
					p.setIntelligence(Integer.parseInt(line.split(" ")[1]));
				else if (line.startsWith("agi"))
					p.setAgility(Integer.parseInt(line.split(" ")[1]));
				else if (line.startsWith("hea"))
					p.setHealth(Integer.parseInt(line.split(" ")[1]));
				else if (line.startsWith("room")) {
					p.setRoom(rooms.get(Integer.parseInt(line.split(" ")[1])));
				} else if (line.startsWith("hpc")) {
					p.setHpcur(Integer.parseInt(line.split(" ")[1]));
				} else if (line.startsWith("rh")) {
					try {
						p.getEquipment().setRighthand(
								(Weapon) Class.forName(line.split(" ")[1])
										.newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (line.startsWith("lh")) {
					try {
						p.getEquipment().setLefthand(
								(Shield) Class.forName(line.split(" ")[1])
										.newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (line.startsWith("skill")) {
					p.getSkills().setSkillf(line.split(" ")[1],
							Float.parseFloat(line.split(" ")[2]));
				} else if (line.startsWith("inv")) {
					try {
						p.getInventory().addItem(
								(Item) Class.forName(line.split(" ")[1])
										.newInstance());
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (line.startsWith("sex")) {
					p.setSex(Integer.parseInt(line.split(" ")[1]));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		p.setBasespeed();
		setUpTrigger();
		entities.add(0, p);
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
