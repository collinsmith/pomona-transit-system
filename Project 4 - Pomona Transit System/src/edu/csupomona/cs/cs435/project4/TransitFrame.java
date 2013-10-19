package edu.csupomona.cs.cs435.project4;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TransitFrame extends JFrame implements Constants {
	public TransitFrame() {
		super(PROGRAM_NAME);

		setContentPane(new TransitPanel());
		setJMenuBar(new TransitMenuBar());

		setSize(1024, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class TransitPanel extends JTabbedPane implements Constants, ChangeListener {
		protected TransitTab ttTripOfferingsTab;
		protected TransitTab ttTripsTab;
		protected TransitTab ttStopsTab;
		protected TransitTab ttDriversTab;
		protected TransitTab ttBusesTab;

		public TransitPanel() {
			super();

			setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

			addTab(LABEL_TAB_TRIPOFFERS, null, ttTripOfferingsTab = new TabTripOfferings(), ttTripOfferingsTab.tooltip);
			addTab(LABEL_TAB_TRIPS, null, ttTripsTab = new TabTrips(), ttTripsTab.tooltip);
			addTab(LABEL_TAB_STOPS, null, ttStopsTab = new TabStops(), ttStopsTab.tooltip);
			addTab(LABEL_TAB_DRIVERS, null, ttDriversTab = new TabDrivers(), ttDriversTab.tooltip);
			addTab(LABEL_TAB_BUSES, null, ttBusesTab = new TabBuses(), ttBusesTab.tooltip);

			addChangeListener(this);

			ttTripOfferingsTab.refreshTables();
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			TransitTab newTab = (TransitTab)getSelectedComponent();
			newTab.refreshTables();
		}
	}

	private class TransitMenuBar extends JMenuBar implements Constants, ActionListener {
		protected JMenu jmFile;
		protected JMenuItem jmiExit;

		protected JMenu jmHelp;
		protected JMenuItem jmiAbout;

		public TransitMenuBar() {
			super();

			jmFile = createFileMenu();
			add(jmFile);

			jmHelp = createHelpMenu();
			add(jmHelp);
		}

		private JMenu createFileMenu() {
			jmiExit = new JMenuItem(MENUITEM_EXIT);
			jmiExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
			jmiExit.addActionListener(this);

			JMenu m = new JMenu(MENU_FILE);
			m.setMnemonic(KeyEvent.VK_F);
			m.add(jmiExit);

			return m;
		}

		private JMenu createHelpMenu() {
			jmiAbout = new JMenuItem(MENUITEM_ABOUT);
			jmiAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK));
			jmiAbout.addActionListener(this);

			JMenu m = new JMenu(MENU_HELP);
			m.setMnemonic(KeyEvent.VK_A);
			m.add(jmiAbout);

			return m;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();
			JFrame parentFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
			if (actionCommand.compareTo(MENUITEM_EXIT) == 0) {
				parentFrame.dispatchEvent(new WindowEvent(parentFrame, WindowEvent.WINDOW_CLOSING));
			} else if (actionCommand.compareTo(MENUITEM_ABOUT) == 0) {
				JOptionPane.showMessageDialog(parentFrame,
									PROGRAM_NAME
										+ "\n"
										+ "\n"
										+ "This program was written as a project by " + PROGRAM_AUTHOR + " for CS435 Database Systems\n"
										+ "at CSU Pomona."
										+ "\n"
										+ "\n"
										+ "Copyright © 2013 " + PROGRAM_AUTHOR + " (" + PROGRAM_EMAIL + "). All rights reserved.\n",
									MENUITEM_ABOUT,
									JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
