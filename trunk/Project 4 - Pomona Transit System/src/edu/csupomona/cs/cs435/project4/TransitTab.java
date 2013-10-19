package edu.csupomona.cs.cs435.project4;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public abstract class TransitTab extends JPanel implements Constants {
	public abstract void refreshTables();

	public static final int MENU_COMPONENT_SIZE = 75;

	protected JPanel north;
	protected JPanel center;

	protected String tooltip;

	protected JLabel jlblTableDescription;
	protected JScrollPane jspTable;
	protected JTable jtTable;

	public TransitTab(String tooltip) {
		super();

		this.tooltip = tooltip;

		setLayout(new BorderLayout());

		north = createNorthernPanel();
		add(north, BorderLayout.NORTH);

		center = createCenterPanel();
		center.add(jlblTableDescription, BorderLayout.NORTH);
		center.add(jspTable, BorderLayout.CENTER);
		add(center, BorderLayout.CENTER);
	}

	private JPanel createNorthernPanel() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 0));
		p.setPreferredSize(new Dimension(0, MENU_COMPONENT_SIZE));
		p.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		return p;
	}

	private JPanel createCenterPanel() {
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		jlblTableDescription = new JLabel();
		jlblTableDescription.setHorizontalAlignment(SwingConstants.CENTER);
		jlblTableDescription.setFont(jlblTableDescription.getFont().deriveFont(Font.BOLD, 16f));
		jspTable = new JScrollPane();

		return p;
	}

	public void addMenuComponent(JComponent c) {
		c.setPreferredSize(new Dimension(MENU_COMPONENT_SIZE-10, MENU_COMPONENT_SIZE-10));
		north.add(c);
	}

	public void changeTable(JTable table) {
		center.remove(jspTable);
		jtTable = table;
		jspTable = new JScrollPane(jtTable);
		center.add(jspTable, BorderLayout.CENTER);
		revalidate();
	}

	public JTable createTable(String description, Object[][] rowData, Object[] columnNames) {
		jlblTableDescription.setText(description);
		JTable table = new JTable(rowData, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table.setAutoCreateRowSorter(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		return table;
	}

	public JTable getTable() {
		return jtTable;
	}
}
