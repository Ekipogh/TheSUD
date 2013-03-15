package sud;

import javax.swing.JTextPane;

public class MyTextPane extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = -162699115128072464L;

	public MyTextPane() {
		super();
		this.setContentType("text/html");
		this.setText("<html><head></head><body bgColor = black></body></html>");
	}

	public void addString(String str){
		 String temp = this.getText();
		 int breakIndex = temp.lastIndexOf("</body>");
		 temp = temp.substring(0, breakIndex) + str + temp.substring(breakIndex);
		 this.setText(temp);
		}
}
