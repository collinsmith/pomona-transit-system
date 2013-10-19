package edu.csupomona.cs.cs435.project4;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	protected TransitFrame frame;

	public Main() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException|IllegalAccessException|InstantiationException|UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		frame = new TransitFrame();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main();
			}
		});
	}
}
