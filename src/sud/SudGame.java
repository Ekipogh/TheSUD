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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;
import org.mozilla.javascript.*;

import ai.Behavior;

import utils.*;

public class SudGame {

	public static List<Entity> entities = new ArrayList<Entity>();
	public static List<Item> items = new ArrayList<Item>();
	public static List<Room> rooms = new ArrayList<Room>();
	public static Window w;
	public static Player p;
	private static boolean running = true;
	private static GameStages gamestage = GameStages.Menu;
	private static int ATTRIBUTES_SCREEN = 0;
	private static int NAME_SCREEN = 1;
	private static int charcreationscreen = NAME_SCREEN;
	private static int namescreenphase = 0;
	private static List<String> commandbuffer = new ArrayList<String>(10);
	private static int commandbuffer_index = 0;
	public static Context cx;
	public static ScriptableObject scope;
	public static Map<String, String> scripts = new HashMap<String, String>();
	public static TextCollector text = TextCollector.getInstance();
	public static Thread gamethread;

	public static void main(String[] args) {
		w = new Window();
		w.out.setText("<html>\r\n  <head>\r\n    \r\n  </head>\r\n  <body bgcolor=\"black\">\r\n    <p>\r\n      <font color=\"white\">SUD. The Single User Dangeon Game. v 0.3 </font><br>\r\n<font color=white>\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u043E\u043E\u0442\u0432\u0435\u0442\u0441\u0442\u0432\u0443\u044E\u0449\u044E\u044E \u043A\u043E\u043C\u0430\u043D\u0434\u0443:<br>\u041D\u0430\u0447\u0430\u0442\u044C<br>\u041F\u043E\u0434\u043E\u043B\u0436\u0438\u0442\u044C</font><br>\r\n    </p>\r\n  </body>\r\n</html>\r\n");
		w.getInputContext().selectInputMethod(new Locale("ru", "RU"));
		w.input.requestFocus();
		loadRooms();
		load_ents();
		loadNPCs();
		loadScripts();

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

		gamethread = new Thread(new Runnable() {
			public void run() {
				while (running) {
					System.out.println("������ �)" + p.getTicks());
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
						Time.tick();
					}
					if (!text.isEmpty())
						w.out.addString(text.Get());
					text.Clear();
					// w.out.setCaretPosition(w.out.getDocument().getLength());
					try {
						if (!p.isSleeping())
							Thread.sleep(1000);
					} catch (InterruptedException e1) {
						System.err.println("Something went wrong *sadface*");
						System.exit(1);
						e1.printStackTrace();
					}
				}
				System.exit(0);
			}
		});
	}

	private static void load_ents() {
		try {
			BufferedReader save = new BufferedReader(new FileReader(new File(
					"saves/ents")));
			String line = null;
			while ((line = save.readLine()) != null) {
				Object o = Class.forName(line.split(" ")[0]).newInstance();
				Entity e = (Entity) o;
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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void loadNPCs() {
		try {
			BufferedReader save = new BufferedReader(new FileReader(new File(
					"saves/npcs")));
			String line = null;
			while ((line = save.readLine()) != null) {
				String[] linspl = line.split(" ");
				NPC npc = new NPC();
				npc.setHome(rooms.get(Integer.parseInt(linspl[2])));
				npc.setWorkplace(rooms.get(Integer.parseInt(linspl[3])));
				npc.setName(linspl[0]);
				npc.setSex(Integer.parseInt(linspl[1]));
				npc.setBehavior((Behavior) Class.forName(linspl[4])
						.newInstance());
				entities.add(npc);
			}
			save.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void initJS() {
		cx = Context.enter();
		scope = cx.initStandardObjects();
		Object wrappedOut = Context.javaToJS(p, scope);
		ScriptableObject.putProperty(scope, "player", wrappedOut);
		wrappedOut = Context.javaToJS(entities, scope);
		ScriptableObject.putProperty(scope, "enteties", wrappedOut);
		wrappedOut = Context.javaToJS(rooms, scope);
		ScriptableObject.putProperty(scope, "rooms", wrappedOut);
		wrappedOut = Context.javaToJS(items, scope);
		ScriptableObject.putProperty(scope, "items", wrappedOut);
		wrappedOut = Context.javaToJS(text, scope);
		ScriptableObject.putProperty(scope, "text", wrappedOut);
		wrappedOut = Context.javaToJS(System.out, scope);
		ScriptableObject.putProperty(scope, "sysout", wrappedOut);
	}

	private static void loadScripts() {
		File scriptFolder = new File("scripts");
		File[] scriptsFiles = scriptFolder.listFiles();
		for (File f : scriptsFiles) {
			StringBuffer script = new StringBuffer();
			BufferedReader r;
			try {
				r = new BufferedReader(new FileReader(f));
				String s = null;
				while ((s = r.readLine()) != null)
					script.append(s);
				scripts.put(f.getName(), script.toString());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void proceed() {
		String[] command = w.input.getText().split(" ");
		if (commandbuffer.size() == 10)
			commandbuffer.remove(0);
		commandbuffer.add(w.input.getText());
		commandbuffer_index = commandbuffer.size() - 1;
		if (gamestage == GameStages.Menu) {
			if ("����������".startsWith(command[0].toLowerCase())) {
				loadPlayer();
				gamestage = GameStages.Game;
				initJS();
				updateOut();
				p.getRoom().command("enter");
				gamethread.start();
			} else if ("������".startsWith(command[0].toLowerCase())) {
				gamestage = GameStages.CharCreation;
				p = new Player(rooms.get(0));
				p.setCharpoints(200);
				p.setStrength(10);
				p.setAgility(10);
				p.setHealth(10);
				p.setIntelligence(10);
				text.Add("<font color=white>��� ��������� ��������� ������� �������������� ������<br>����� ������ ������, ������� <b>������</b><br>\n");
				show_nametext();
				gamethread.start();
			}
		} else if (gamestage == GameStages.Game) {
			if (command.length == 1 && !command[0].isEmpty()
					&& !p.isUnconscious()) {
				if ("��������".startsWith(command[0].toLowerCase())
						&& !command[0].toLowerCase().equals("�"))
					updateOut();
				else if ("������".startsWith(command[0].toLowerCase())) {
					p.setAttacking(false);
					p.getEnemies().clear();
				} else if ("���������".startsWith(command[0].toLowerCase())
						|| "������".startsWith(command[0].toLowerCase())) {
					text.Add(p.getInventory().toString());
				} else if ("����������".startsWith(command[0].toLowerCase())) {
					text.Add(p.getEquipment().toString());
				} else if ("���������".startsWith(command[0].toLowerCase())) {
					p.setResting(true);
					text.Add("<font color=white>�� ������� ���������</font><br>");
				} else if ("������".startsWith(command[0].toLowerCase())
						&& !command[0].toLowerCase().equals("�")) {
					p.setResting(false);
					text.Add("<font color=white>�� ��������� �������� � ������</font><br>");
				} else if ("�����".startsWith(command[0].toLowerCase())) {
					save_and_exit();
					gamestage = GameStages.Menu;
					entities.remove(p);
					text.Add("<font color=white>������� ��������������� �������:<br>"
							+ "������<br>" + "���������</font><br>");
				} else if ("������".startsWith(command[0].toLowerCase())) {
					text.Add(p.getSkills().toString());

				} else if ("�������".startsWith(command[0].toLowerCase())) {
					text.Add("<font color = white>��������, ������, ���������, ������, ����������, ���������, ������, �����, �����, �����, �����������, �����������, �������, ������ ������<br></font>");
				} else if ("�����".startsWith(command[0].toLowerCase())
						&& !command[0].equals("�")) {
					text.Add(Time.getTime());
				} else if ("��������".startsWith(command[0].toLowerCase())) {
					new Editor();
				} else if ("�����".startsWith(command[0].toLowerCase())) {
					go(0);
				} else if ("��".startsWith(command[0].toLowerCase())) {
					go(1);
				} else if ("�����".startsWith(command[0].toLowerCase())) {
					go(2);
				} else if ("�����".startsWith(command[0].toLowerCase())) {
					go(3);
				} else
					// �����������
					p.getRoom().command(command[0]);
			} else if (command.length == 2) {
				if ("�����".startsWith(command[0].toLowerCase())) {
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
						text.Add("<font color=white>����� ����?<br>");
				} else if ("�����".startsWith(command[0].toLowerCase())) {
					if ("���".startsWith(command[1].toLowerCase())
							|| "��".startsWith(command[1].toLowerCase())) {
						Collection<Item> toremove = new HashSet<Item>();
						for (Item i : items) {
							if (i.getRoom() == p.getRoom()) {
								if (p.getInventory().addItem(i)) {
									toremove.add(i);
									i.setRoom(null);
									text.Add("<font color=white>�� �������� "
											+ i.getName()
											+ " � ���� ������</font><br>");
								} else {
									text.Add("<font color=white>� ������� ��������� �����</font><br>");
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
									text.Add("<font color=white>�� �������� "
											+ item.getName()
											+ " � ���� ������<br>");
								}
								break;
							}
						}
						if (!found)
							text.Add("<font color=white>����� ���?<br>");
					}
				} else if ("�����������".startsWith(command[0].toLowerCase())) {
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
						text.Add("<font color=white>�� ����� "
								+ toEquip.getName()
								+ " � ������ ����<br></font>");
					} else
						text.Add("<font color=white>������� �� ������<br></font>");
				} else if ("�����������".startsWith(command[0].toLowerCase())) {
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
						text.Add("<font color=white>�� ����������� "
								+ toEquip.getName() + "<br></font>");
					}
				} else if ("���������".startsWith(command[0].toLowerCase())) {
					List<SUDObject> gameobjects = new ArrayList<SUDObject>();
					gameobjects.addAll(entities);
					gameobjects.addAll(items);
					if ("����".startsWith(command[0].toLowerCase())) {
						text.Add(p.getDescription());
					} else {
						for (int i = 0; i < gameobjects.size(); i++) {
							SUDObject so = gameobjects.get(i);
							if (so.getName().startsWith(
									command[1].toLowerCase())) {
								text.Add("<font color=white>"
										+ so.getDescription() + "<font><br>");
								break;
							}
						}
					}
				} else if ("�������".startsWith(command[0].toLowerCase())) {
					Item founditem = p.getInventory().getItem(
							command[1].toLowerCase());
					if (founditem != null) {
						p.getInventory().setItem(null,
								p.getInventory().getSlot(founditem));
						items.add(founditem.setRoom(p.getRoom()));
						text.Add("<font color=white>�� ������� "
								+ founditem.getName() + " �� �����</font><br>");
					} else {
						text.Add("<font color=white>������� ���?</font><br>");
					}
				} else if ("������".startsWith(command[0].toLowerCase())) {
					if ("������".startsWith(command[1].toLowerCase())) {
						int result = p.getEquipment().itemtoinventory(
								"right_hand", p.getInventory());
						if (result == 0)
							text.Add("<font color=white>�� ������ ���� ������ � ������</font><br>");
						else if (result == 1)
							text.Add("<font color=white>� ����� ������� ��� �����</font><br>");
						else if (result == 2)
							text.Add("<font color=white>� ��� ��� �������������� ������</font><br>");
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
						text.Add("<font color=white>" + command[0]
								+ " ����?<br>\n");
				}
			}
		} else if (gamestage == GameStages.CharCreation) {
			if (charcreationscreen == ATTRIBUTES_SCREEN) {
				switch (command[0]) {
				case "�":
					if (p.getCharpoints() >= 10) {
						p.setStrength(p.getStrength() + 1);
						p.setCharpoints(p.getCharpoints() - 10);
					}
					charcreationattr();
					break;
				case "�":
					if (p.getStrength() > 1) {
						p.setStrength(p.getStrength() - 1);
						p.setCharpoints(p.getCharpoints() + 10);
					}
					charcreationattr();
					break;
				case "�":
					if (p.getCharpoints() >= 20) {
						p.setAgility(p.getAgility() + 1);
						p.setCharpoints(p.getCharpoints() - 20);
					}
					charcreationattr();
					break;
				case "�":
					if (p.getAgility() > 1) {
						p.setAgility(p.getAgility() - 1);
						p.setCharpoints(p.getCharpoints() + 20);
						charcreationattr();
					}
					charcreationattr();
					break;
				case "�":
					if (p.getCharpoints() >= 10) {
						p.setHealth(p.getHealth() + 1);
						p.setCharpoints(p.getCharpoints() - 10);
					}
					charcreationattr();
					break;
				case "�":
					if (p.getHealth() > 1) {
						p.setHealth(p.getHealth() - 1);
						p.setCharpoints(p.getCharpoints() + 10);
					}
					charcreationattr();
					break;
				case "�":
					if (p.getCharpoints() >= 20) {
						p.setIntelligence(p.getIntelligence() + 1);
						p.setCharpoints(p.getCharpoints() - 20);
					}
					charcreationattr();
					break;
				case "�":
					if (p.getIntelligence() > 1) {
						p.setIntelligence(p.getIntelligence() - 1);
						p.setCharpoints(p.getCharpoints() + 20);
					}
					charcreationattr();
					break;
				case "������":
					int ok = JOptionPane.showConfirmDialog(w, "�� �������?",
							null, JOptionPane.OK_CANCEL_OPTION);
					if (ok == JOptionPane.OK_OPTION) {
						gamestage = GameStages.Game;
						// setUpTrigger();
						initJS();
						p.calculateSkills();
						p.setBasespeed();
						entities.add(0, p);
						updateOut();
					}
					break;
				}
			} else if (charcreationscreen == NAME_SCREEN) {
				switch (namescreenphase) {
				case 0:
					if (command[0].toLowerCase().equals("�")) {
						namescreenphase = 1;
						p.setSex(0);
					} else if (command[0].toLowerCase().equals("�")) {
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

	private static void go(int d) {
		if (p.getRoom().getExit(d) != null) {
			p.getRoom().command("leave");
			for (Entity e : entities)
				if (e.getRoom().equals(p.getRoom()) && !e.equals(p))
					e.command("leave");
			p.setRoom(p.getRoom().getExit(d));
			updateOut();
			p.getRoom().command("enter");
			for (Entity e : entities)
				if (e.getRoom().equals(p.getRoom()) && !e.equals(p))
					e.command("enter");
		} else
			text.Add("<font color=white>����� ����?<br>\n");
	}

	public static void updateOut() {
		text.Add("<font color=white>" + p.getRoom().getName() + "<br>\n"
				+ p.getRoom().InitText() + "<br>\n");
		text.Add("<font color=white>������: " + "<font color=yellow>"
				+ p.getRoom().exitstext() + "<br>\n");
		text.Add("<font color = white>��������: " + p.getHealthHTML()
				+ "<br>\n");
		for (Entity e : entities) {
			if (e != p && e.getRoom() == p.getRoom())
				text.Add("<font color = red>" + e.getName()
						+ " ����� �����.<br>\n");
		}
		for (Item item : items) {
			if (item.getRoom() == p.getRoom())
				text.Add("<font color = red>" + item.getName()
						+ " ����� �����.<br>\n");
		}
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
			save.write("time " + Time.getDay() + " " + Time.getMonth() + " "
					+ Time.getYear() + " " + Time.getHours() + " "
					+ Time.getMinutes());
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
						save.write(e.getClass().getName() + " "
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
			text.Add("<font color = white>�������� ��� ���<br>[�] - �������<br>[�] - �������</font><br>");
			break;
		case 1:
			text.Add("<font color = white>������� ���� ���</font><br>");
			break;
		}
	}

	static void charcreationattr() {
		text.Add("<font color=white>����� ��������:" + p.getCharpoints()
				+ "<br>C���: " + p.getStrength() + " +[�][�]-  10 �����<br>"
				+ "��������: " + p.getAgility() + " +[�][�]-  20 �����<br>"
				+ "��������: " + p.getHealth() + " +[�][�]-  10 �����<br>"
				+ "��������: " + p.getIntelligence()
				+ " +[�][�]-  20 �����</font><br>");
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
				else if (line.startsWith("time")) {
					String[] splted = line.split(" ");
					Time.setDay(Integer.parseInt(splted[1]));
					Time.setMonth(Integer.parseInt(splted[2]));
					Time.setYear(Integer.parseInt(splted[3]));
					Time.setHours(Integer.parseInt(splted[4]));
					Time.setMinutes(Integer.parseInt(splted[5]));
				} else if (line.startsWith("hea"))
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
		// setUpTrigger();
		entities.add(0, p);
	}

	// private static void setUpTrigger() {
	// // Trigger.setInput(w.input);
	// Trigger.setPlayer(p);
	// Trigger.setEntities(entities);
	// }

	static void loadRooms() {
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader("maps/rooms"));
		} catch (FileNotFoundException e) {
			System.err.println("���� �� ������!");
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
				String[] scripts;
				String[] tmp = file.readLine().split(" ");
				id = Integer.parseInt(tmp[0]);
				{
					String[] nametmp = tmp[1].split("%20"); // TODO:
															// redo!!!!!!!!
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
					scripts = new String[numofcomms];
					for (int k = 0; k < numofcomms; k++) {
						comms[k] = tmp[k * 2 + 3];
						scripts[k] = tmp[k * 2 + 4];
					}
				}
				rooms.add(new Room(id, name, inittext, comms, scripts));
				// rooms.add(new Room(id, name, inittext, comms, trigs));
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

	public static void execute(String scriptName, Object caller) {
		Object wrappedOut = Context.javaToJS(caller, scope);
		ScriptableObject.putProperty(scope, "caller", wrappedOut);
		cx.evaluateString(scope, scripts.get(scriptName), "<cmd>", 1, null);
	}
}
