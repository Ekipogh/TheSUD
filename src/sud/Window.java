package sud;

import javax.swing.*;
import javax.swing.text.DefaultCaret;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField input;
	public MyTextPane out;
	public JButton enter;
	private JScrollPane scrollPane;
	DefaultCaret caret;
	BoundedRangeModel model;
	JScrollBar scrollBar;

	public Window() {
		
		setResizable(false);
		getContentPane().setBackground(Color.DARK_GRAY);
		setSize(new Dimension(640, 480));
		getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 624, 400);
		getContentPane().add(scrollPane);

		out = new MyTextPane();
		out.setSize(new Dimension(0, 2000));
		out.setCaretPosition(0);
		out.setBorder(null);

		scrollPane.setViewportView(out);
		DefaultCaret caret = (DefaultCaret) out.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		out.setEditable(false);
		out.addString("<p><font color = white>SUD. The Single User Dangeon Game. v 0.3\n</p>");

		input = new JTextField();
		input.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				input.setText("");
			}
		});
		input.setForeground(Color.WHITE);
		input.setBackground(Color.BLACK);
		input.setBounds(0, 411, 450, 20);
		getContentPane().add(input);
		input.setColumns(10);

		enter = new JButton(
				"\u041E\u0442\u043F\u0440\u0430\u0432\u0438\u0442\u044C");
		enter.setSelected(true);

		enter.setBounds(460, 411, 89, 23);
		getContentPane().add(enter);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				SudGame.setRunning(false);
			}
		});
	}
}
