package utils;

import gameworld.Room;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sud.SudGame;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Editor extends JFrame {
	public static JList<Object> list;
	private DefaultListModel<Object> model;

	public Editor() {
		getContentPane().setLayout(null);
		commands_f = new JTextArea();
		commands_f.setLineWrap(true);
		commands_f.setBounds(383, 252, 140, 143);
		getContentPane().add(commands_f);
		commands_f.setColumns(10);

		scripts_f = new JTextArea();
		scripts_f.setLineWrap(true);
		scripts_f.setBounds(587, 252, 140, 143);
		getContentPane().add(scripts_f);
		scripts_f.setColumns(10);
		model = new DefaultListModel<Object>();
		for (Room r : SudGame.rooms)
			model.addElement(r);
		list = new JList<Object>(model);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setBounds(0, 0, 283, 562);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				Room selected = (Room) list.getSelectedValue();
				name.setText(selected.getName());
				desc.setText(selected.InitText());
				Room n, s, e, w;
				n = selected.getExit(0);
				s = selected.getExit(1);
				e = selected.getExit(2);
				w = selected.getExit(3);
				north.setSelectedItem(n);
				south.setSelectedItem(s);
				east.setSelectedItem(e);
				west.setSelectedItem(w);
				// if (n != null)
				// north.setSelectedItem(n);
				// else
				// north.setSelectedIndex(north.getItemCount() - 1);
				// if (s != null)
				// south.setSelectedItem(s);
				// else
				// south.setSelectedIndex(south.getItemCount() - 1);
				// if (e != null)
				// east.setSelectedItem(e);
				// else
				// east.setSelectedIndex(east.getItemCount() - 1);
				// if (w != null)
				// west.setSelectedItem(w);
				// else
				// west.setSelectedIndex(west.getItemCount() - 1);
				String[] commands = selected.getCommands();
				String[] scripts = selected.getScripts();
				String s_c = "";
				String s_s = "";
				for (int i = 0; i < commands.length; i++) {
					s_c += commands[i];
					if (i != commands.length - 1)
						s_c += ",";
				}
				for (int i = 0; i < scripts.length; i++) {
					s_s += scripts[i];
					if (i != scripts.length - 1)
						s_s += ",";
				}
				commands_f.setText(s_c);
				scripts_f.setText(s_s);
			}
		});
		getContentPane().add(list);

		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(289, 11, 84, 14);
		getContentPane().add(lblName);

		JLabel lblDesciption = new JLabel("Desciption");
		lblDesciption.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDesciption.setBounds(289, 36, 84, 14);
		getContentPane().add(lblDesciption);

		name = new JTextField();
		name.setBounds(383, 11, 391, 20);
		getContentPane().add(name);
		name.setColumns(10);

		desc = new JTextArea();
		desc.setLineWrap(true);
		desc.setWrapStyleWord(true);
		desc.setBounds(383, 36, 391, 143);
		getContentPane().add(desc);

		north = new JComboBox<Object>(SudGame.rooms.toArray());
		north.setBounds(383, 190, 140, 20);
		getContentPane().add(north);

		south = new JComboBox<Object>(SudGame.rooms.toArray());
		south.setBounds(383, 221, 140, 20);
		getContentPane().add(south);

		JLabel label = new JLabel("\u0421\u0435\u0432\u0435\u0440");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(327, 193, 46, 14);
		getContentPane().add(label);

		JLabel label_1 = new JLabel("\u042E\u0433");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(327, 224, 46, 14);
		getContentPane().add(label_1);

		JLabel label_2 = new JLabel("\u0412\u043E\u0441\u0442\u043E\u043A");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(533, 193, 46, 14);
		getContentPane().add(label_2);

		JLabel label_3 = new JLabel("\u0417\u0430\u043F\u0430\u0434");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(533, 224, 46, 14);
		getContentPane().add(label_3);

		east = new JComboBox<Object>(SudGame.rooms.toArray());
		east.setBounds(587, 190, 140, 20);
		getContentPane().add(east);

		west = new JComboBox<Object>(SudGame.rooms.toArray());
		west.setBounds(587, 221, 140, 20);
		getContentPane().add(west);
		setVisible(true);
		setSize(800, 600);
		north.addItem(null);
		south.addItem(null);
		west.addItem(null);
		east.addItem(null);

		JLabel lblScriptName = new JLabel("Script name");
		lblScriptName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblScriptName.setBounds(293, 255, 80, 14);
		getContentPane().add(lblScriptName);
		JLabel lblScriptFile = new JLabel("Script file");
		lblScriptFile.setBounds(533, 255, 46, 14);
		getContentPane().add(lblScriptFile);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Room r = new Room(model.getSize());
				model.addElement(r);
				north.addItem(r);
				south.addItem(r);
				east.addItem(r);
				west.addItem(r);
				SudGame.rooms.add(r);
			}
		});
		btnAdd.setBounds(293, 539, 80, 23);
		getContentPane().add(btnAdd);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Room r = (Room) list.getSelectedValue();
				r.setName(name.getText());
				r.setInitText(desc.getText());
				r.setExit(0, (Room) north.getSelectedItem());
				r.setExit(1, (Room) south.getSelectedItem());
				r.setExit(2, (Room) east.getSelectedItem());
				r.setExit(3, (Room) west.getSelectedItem());
				String commands[];
				String scripts[];
				commands = commands_f.getText().split(",");
				scripts = scripts_f.getText().split(",");
				r.setCommands(commands);
				r.setScripts(scripts);
				list.updateUI();
				// updateExits();
			}
		});
		btnSave.setBounds(383, 406, 89, 23);
		getContentPane().add(btnSave);

		JButton btnNewButton = new JButton("Save and Close");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					BufferedWriter wr = new BufferedWriter(new FileWriter(
							"maps/rooms"));
					wr.write(SudGame.rooms.size() + "\r\n");
					for (int i = 0; i < list.getModel().getSize(); i++) {
						Room r = (Room) list.getModel().getElementAt(i);
						String initText = r.InitText();
						String name = r.getName();
						name = name.trim().replaceAll(" ", "%20");
						name = name.trim().replaceAll("<br>", "%30");
						initText = initText.trim().replaceAll(" ", "%20");
						initText = initText.trim().replaceAll("<br>", "%30");
						String comscripts = "";
						for (int j = 0; j < r.getCommands().length; j++) {
							comscripts += r.getCommands()[j] + " "
									+ r.getScripts()[j];
							if (j != r.getCommands().length - 1)
								comscripts += " ";
						}
						wr.write(r.getId() + " " + name + " " + initText
								+ " " + comscripts + "\r\n");
					}
					for (Room r : SudGame.rooms) { //TODO redo
						wr.write(r.getId() + " ");
						for (int i = 0; i < r.getExits().length; i++) {
							if (r.getExits()[i] != null)
								wr.write(Integer.toString(r.getExits()[i]
										.getId()));
							else
								wr.write("null");
							if (i != r.getExits().length - 1)
								wr.write(" ");
							else
								wr.write("\r\n");
						}
					}
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(533, 479, 241, 72);
		getContentPane().add(btnNewButton);
		list.setSelectedIndex(0);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sel = list.getSelectedIndex();
				list.setSelectedIndex(0);
				model.remove(sel);
			}
		});
		btnRemove.setBounds(383, 539, 89, 23);
		getContentPane().add(btnRemove);
	}

	private static final long serialVersionUID = 8471588457819545558L;
	private JTextField name;
	private JTextArea desc;
	private static JComboBox<Object> north;
	private static JComboBox<Object> south;
	private static JComboBox<Object> east;
	private static JComboBox<Object> west;
	private JTextArea commands_f;
	private JTextArea scripts_f;
}
